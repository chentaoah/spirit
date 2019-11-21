package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.analyzer.VariableTracker;
import com.sum.shy.core.api.Converter;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.utils.LineUtils;

public class IfConverter extends AbstractConverter {

	@Override
	public int convert(StringBuilder sb, String block, String indent, CtClass clazz, CtMethod method, List<Line> lines,
			int index, Line line, Stmt stmt) {

		// 这里的算法是能够截取到所有的块的
		List<Line> blockLines = LineUtils.getAllLines(lines, index);
		for (int i = 0, count = -1; i < blockLines.size(); i++) {
			Line currLine = blockLines.get(i);
			if (currLine.isIgnore())
				continue;

			Stmt currStmt = Stmt.create(currLine);
			// 变量追踪
			VariableTracker.track(clazz, method, block, currLine, currStmt);
			// 如果是if,则添加括号
			if (currStmt.isIf()) {
				currStmt.tokens.add(1, new Token(Constants.SEPARATOR_TOKEN, "(", null));
				currStmt.tokens.add(currStmt.tokens.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")", null));
				sb.append(indent + convertJudgeStmt(clazz, currStmt) + "\n");
				count++;
			} else if (currStmt.isElseIf()) {
				currStmt.tokens.add(3, new Token(Constants.SEPARATOR_TOKEN, "(", null));
				currStmt.tokens.add(currStmt.tokens.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")", null));
				sb.append(indent + convertJudgeStmt(clazz, currStmt) + "\n");
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

	private String convertJudgeStmt(CtClass clazz, Stmt stmt) {
		for (int i = 0; i < stmt.tokens.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isVar()) {
				// 如果是str类型
//				if (token.getTypeAtt().isStr()) {
//					// 添加依赖
//					clazz.addImport(StringUtils.class.getName());
//					try {
//						Token nextToken = stmt.getToken(i + 1);
//						if (nextToken.isOperator() && "==".equals(nextToken.value)) {
//							Token paramToken = stmt.getToken(i + 2);
//							stmt.tokens.set(i, new Token(Constants.EXPRESS_TOKEN,
//									"StringUtils.equals(" + token.value + "," + paramToken.value + ")", null));
//							stmt.tokens.remove(i + 2);
//							stmt.tokens.remove(i + 1);
//						} else if (nextToken.isOperator() && "!=".equals(nextToken.value)) {
//							Token paramToken = stmt.getToken(i + 2);
//							stmt.tokens.set(i, new Token(Constants.EXPRESS_TOKEN,
//									"!StringUtils.equals(" + token.value + "," + paramToken.value + ")", null));
//							stmt.tokens.remove(i + 2);
//							stmt.tokens.remove(i + 1);
//						} else {
//							stmt.tokens.set(i, new Token(Constants.EXPRESS_TOKEN,
//									"StringUtils.isNotEmpty(" + token.value + ")", null));
//						}
//					} catch (Exception e) {
//						// ignore
//					}
//				}
			}
		}
		return stmt.toString();
	}

}
