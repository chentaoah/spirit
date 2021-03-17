package com.sum.spirit.core.element.utils;

import java.util.List;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.common.enums.SymbolEnum.OperandEnum;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;

public class StmtFormat {

	public static List<Token> format(Statement statement) {

		List<Token> tokens = statement.copyTokens();

		for (int i = tokens.size() - 1; i >= 1; i--) {
			tokens.add(i, new Token(TokenTypeEnum.SEPARATOR, " "));
		}

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.isSeparator() && " ".equals(token.toString())) {
				Token lastToken = tokens.get(i - 1);
				Token nextToken = tokens.get(i + 1);
				if (lastToken.isOperator()) {
					if ("!".equals(lastToken.toString())) {
						tokens.remove(i);
						continue;

					} else if ("++".equals(lastToken.toString()) || "--".equals(lastToken.toString())) {
						OperandEnum operandEnum = lastToken.attr(AttributeEnum.OPERAND);
						if (operandEnum == OperandEnum.RIGHT) {
							tokens.remove(i);
							continue;
						}

					} else if ("-".equals(lastToken.toString())) {
						OperandEnum operandEnum = lastToken.attr(AttributeEnum.OPERAND);
						if (operandEnum == OperandEnum.RIGHT) {
							tokens.remove(i);
							continue;
						}
					}

				} else if (lastToken.isSeparator()) {
					if ("[".equals(lastToken.toString()) || "(".equals(lastToken.toString()) || "<".equals(lastToken.toString())) {
						tokens.remove(i);
						continue;
					}

				} else if (lastToken.isCustomPrefix()) {
					tokens.remove(i);
					continue;
				}

				if (nextToken.isOperator()) {
					if ("++".equals(nextToken.toString()) || "--".equals(nextToken.toString())) {
						OperandEnum operandEnum = nextToken.attr(AttributeEnum.OPERAND);
						if (operandEnum == OperandEnum.LEFT) {
							tokens.remove(i);
							continue;
						}
					}

				} else if (nextToken.isSeparator()) {
					if ("[".equals(nextToken.toString()) || "(".equals(nextToken.toString()) || "<".equals(nextToken.toString())
							|| "]".equals(nextToken.toString()) || ")".equals(nextToken.toString()) || ">".equals(nextToken.toString())
							|| ",".equals(nextToken.toString()) || ";".equals(nextToken.toString())) {

						if (lastToken.isKeyword() && "(".equals(nextToken.toString())) {
							continue;// if (express) {

						} else {
							tokens.remove(i);
							continue;
						}
					}

				} else if (nextToken.isVisit()) {
					tokens.remove(i);
					continue;

				} else if (nextToken.isCustomSuffix()) {
					tokens.remove(i);
					continue;
				}
			}
		}

		return tokens;
	}

}
