package com.gitee.spirit.core.element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.DefaultValue;
import com.gitee.spirit.common.utils.ConfigUtils;
import com.gitee.spirit.common.utils.LineUtils;
import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.api.DocumentReader;
import com.gitee.spirit.core.api.ElementBuilder;
import com.gitee.spirit.core.element.entity.Document;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Line;
import com.gitee.spirit.core.element.entity.Statement;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

@Component
public class DefaultDocumentReader implements DocumentReader {

    @Autowired
    public ElementBuilder builder;

    @Override
    public Document read(String fileName, InputStream input) {
        List<String> lines = IoUtil.readLines(input, DefaultValue.CHARSET, new ArrayList<>());
        Document document = doReadLines(new Document(fileName), lines);
        if (ConfigUtils.isDebug()) {
            document.debug();
        }
        return document;
    }

    public Document doReadLines(Document document, List<String> lines) {
        Stack<List<Element>> stack = new Stack<>();
        stack.push(document);
        for (int index = 0; index < lines.size(); index++) {
            String text = lines.get(index);
            Line line = new Line(index + 1, text);
            if (line.isIgnore()) {
                continue;
            }
            Element element = builder.build(line);
            // what like "var = {"
            if (mergeLinesIfPossible(document, lines, index, element)) {
                index--;
                continue;
            }
            // what like "if xxx : xxx : xxx"
            List<String> subLines = splitLineIfPossible(element);
            if (subLines != null && !subLines.isEmpty()) {
                lines.remove(index);
                lines.addAll(index, subLines);
                index--;
                continue;
            }
            //do read line
            if (line.isEnding()) {
                stack.pop();
            }
            stack.peek().add(element);
            if (line.hasChild()) {
                stack.push(element.children);
            }
        }
        return document;
    }

    public boolean mergeLinesIfPossible(Document document, List<String> lines, int startIndex, Element element) {
        if (element.isStructAssign()) {
            StringBuilder builder = new StringBuilder(lines.get(startIndex));
            for (int index = startIndex + 1; index < lines.size(); index++) {
                builder.append(StrUtil.removeAny(lines.get(index), "\t").trim());
                int end = LineUtils.findEndIndex(builder, 0, '{', '}');
                if (end != -1) {
                    ListUtils.removeAllByIndex(lines, startIndex, index + 1);
                    lines.add(startIndex, builder.toString());
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> splitLineIfPossible(Element element) {
        if (element.isIf() || element.isFor() || element.isForIn() || element.isWhile()) {
            if (element.contains(":")) {
                String indent = element.getIndent();
                List<String> subLines = new ArrayList<>();
                List<Statement> statements = element.splitStmt(":");
                subLines.add(indent + statements.get(0).toString() + " {");
                for (int index = 1; index < statements.size(); index++) {
                    subLines.add(indent + "\t" + statements.get(index).toString());
                }
                subLines.add(indent + "}");
                return subLines;
            }
        }
        return null;
    }
}
