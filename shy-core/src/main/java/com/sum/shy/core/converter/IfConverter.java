package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.analyzer.VariableChecker;
import com.sum.shy.core.api.Converter;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.utils.LineUtils;

public class IfConverter implements Converter {

	@Override
	public int convert(StringBuilder sb, String block, String indent, Clazz clazz, Method method, List<String> lines,
			int index, String line, Stmt stmt) {

		VariableChecker.check(clazz, method, block, stmt);

		// 确认结束的行
		int end = 0;
		for (int i = index + 1; i < lines.size(); i++) {
			String nextLine = lines.get(i);
			// 去掉空格
			nextLine = LineUtils.removeSpace(nextLine);
			if (nextLine.startsWith("}") && !nextLine.startsWith("} else if") && !nextLine.startsWith("}else if")) {
				end = i;
			}
		}

		for (int i = index, count = 0; i <= end; i++) {
			String currLine = lines.get(i);
			Stmt currStmt = Stmt.create(currLine);
			// 如果是if,则添加括号
			if ("if".equals(currStmt.syntax)) {
				currStmt.tokens.add(1, new Token("separator", "(", null));
				currStmt.tokens.add(currStmt.tokens.size() - 1, new Token("separator", ")", null));
				sb.append(indent + currStmt + "\n");
				count++;
			} else if ("elseif".equals(currStmt.syntax)) {
				currStmt.tokens.add(3, new Token("separator", "(", null));
				currStmt.tokens.add(currStmt.tokens.size() - 1, new Token("separator", ")", null));
				sb.append(indent + currStmt + "\n");
				count++;
			} else if ("else".equals(currStmt.syntax)) {
				sb.append(indent + currStmt + "\n");
				count++;
			} else if ("end".equals(currStmt.syntax)) {
				sb.append(indent + currStmt + "\n");
			} else {
				Converter converter = Converter.get(currStmt.syntax);
				converter.convert(sb, block + count, indent + "\t", clazz, method, lines, index, currLine, currStmt);
			}

		}
		return end - index;
	}

}
