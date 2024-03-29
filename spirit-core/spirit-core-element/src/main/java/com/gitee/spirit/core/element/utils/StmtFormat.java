package com.gitee.spirit.core.element.utils;

import java.util.List;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.OperatorEnum.OperandEnum;
import com.gitee.spirit.common.enums.TokenTypeEnum;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;

public class StmtFormat {

	public static List<Token> format(Statement statement) {

		List<Token> tokens = statement.copyTokens();

		for (int index = tokens.size() - 1; index >= 1; index--) {
			tokens.add(index, new Token(TokenTypeEnum.SEPARATOR, " "));
		}

		for (int index = 0; index < tokens.size(); index++) {
			Token token = tokens.get(index);
			if (token.isSeparator() && " ".equals(token.toString())) {
				Token lastToken = tokens.get(index - 1);
				Token nextToken = tokens.get(index + 1);
				// 处理上一节点
				boolean isFinish = dealLastToken(tokens, index, lastToken, nextToken);
				if (isFinish) {
					continue;
				}
				// 处理下一节点
				dealNextToken(tokens, index, lastToken, nextToken);
			}
		}

		return tokens;
	}

	public static boolean dealLastToken(List<Token> tokens, int index, Token lastToken, Token nextToken) {
		if (lastToken.isOperator()) {
			if ("!".equals(lastToken.toString())) {
				tokens.remove(index);
				return true;

			} else if ("-".equals(lastToken.toString())) {
				OperandEnum operandEnum = lastToken.attr(Attribute.OPERAND);
				if (operandEnum == OperandEnum.RIGHT) {
					tokens.remove(index);
					return true;
				}
			}

		} else if (lastToken.isSeparator()) {
			if ("[".equals(lastToken.toString()) || "(".equals(lastToken.toString()) || "<".equals(lastToken.toString())) {
				tokens.remove(index);
				return true;
			}

		} else if (lastToken.isCustomPrefix()) {
			tokens.remove(index);
			return true;
		}

		return false;
	}

	public static void dealNextToken(List<Token> tokens, int index, Token lastToken, Token nextToken) {
		if (nextToken.isOperator()) {
			if ("++".equals(nextToken.toString()) || "--".equals(nextToken.toString())) {
				OperandEnum operandEnum = nextToken.attr(Attribute.OPERAND);
				if (operandEnum == OperandEnum.LEFT) {
					tokens.remove(index);
					return;
				}
			}

		} else if (nextToken.isSeparator()) {
			if ("[".equals(nextToken.toString()) || "]".equals(nextToken.toString()) || //
					"(".equals(nextToken.toString()) || ")".equals(nextToken.toString()) || //
					"<".equals(nextToken.toString()) || ">".equals(nextToken.toString()) || //
					",".equals(nextToken.toString()) || ";".equals(nextToken.toString())) {
				tokens.remove(index);
				return;
			}

		} else if (nextToken.isVisit()) {
			tokens.remove(index);
			return;

		} else if (nextToken.isCustomSuffix()) {
			tokens.remove(index);
			return;
		}
	}

}
