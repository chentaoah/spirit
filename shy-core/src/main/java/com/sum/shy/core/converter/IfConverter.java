package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.analyzer.VariableTracker;
import com.sum.shy.core.api.Converter;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.utils.LineUtils;

public class IfConverter implements Converter {

	@Override
	public int convert(StringBuilder sb, String block, String indent, Clazz clazz, Method method, List<Line> lines,
			int index, Line line, Stmt stmt) {

		VariableTracker.check(clazz, method, block, line, stmt);

		// 这里的算法是能够截取到所有的块的
		List<Line> blockLines = LineUtils.getAllLines(lines, index);

		for (int i = 0, count = 0; i < blockLines.size(); i++) {
			Line currLine = blockLines.get(i);
			if (currLine.isIgnore())
				continue;
			
			Stmt currStmt = Stmt.create(currLine);
			// 如果是if,则添加括号
			if (currStmt.isIf()) {
				currStmt.tokens.add(1, new Token(Constants.SEPARATOR_TOKEN, "(", null));
				currStmt.tokens.add(currStmt.tokens.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")", null));
				sb.append(indent + currStmt + "\n");
				count++;
			} else if (currStmt.isElseIf()) {
				currStmt.tokens.add(3, new Token(Constants.SEPARATOR_TOKEN, "(", null));
				currStmt.tokens.add(currStmt.tokens.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")", null));
				sb.append(indent + currStmt + "\n");
				count++;
			} else if (currStmt.isElse()) {
				sb.append(indent + currStmt + "\n");
				count++;
			} else if (currStmt.isEnd()) {
				sb.append(indent + currStmt + "\n");
			} else {
				Converter converter = Converter.get(currStmt.syntax);
				converter.convert(sb, block + count, indent + "\t", clazz, method, lines, index, currLine, currStmt);
			}
		}

		return blockLines.size() - 1;
	}

}
