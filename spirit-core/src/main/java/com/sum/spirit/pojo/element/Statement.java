package com.sum.spirit.pojo.element;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.sum.spirit.pojo.enums.SymbolEnum.OperandEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;

public class Statement extends TokenBox {

	public List<Token> tokens;

	public Statement(List<Token> tokens) {
		this.tokens = tokens;
	}

	public Statement copy() {
		return new Statement(copyTokens());
	}

	public Statement subStmt(int start, int end) {
		return new Statement(subTokens(start, end));
	}

	public Statement subStmt(String left, String right) {
		return subStmt(indexOf(left) + 1, lastIndexOf(right));
	}

	public List<Statement> split(String separator) {
		List<Statement> statements = new ArrayList<>();
		List<List<Token>> tokensList = splitTokens(separator);
		for (List<Token> tokens : tokensList)
			statements.add(new Statement(tokens));
		return statements;
	}

	@Override
	public List<Token> getTokens() {
		return tokens;
	}

	@Override
	public String toString() {
		return Joiner.on("").join(format());
	}

	public List<Token> format() {

		List<Token> tokens = copyTokens();

		for (int i = tokens.size() - 1; i >= 1; i--)
			tokens.add(i, new Token(TokenTypeEnum.SEPARATOR, " "));

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
						if (lastToken.getOperand() == OperandEnum.RIGHT) {
							tokens.remove(i);
							continue;
						}

					} else if ("-".equals(lastToken.toString())) {
						if (lastToken.getOperand() == OperandEnum.RIGHT) {
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
						if (nextToken.getOperand() == OperandEnum.LEFT) {
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

				} else if (nextToken.isFluent()) {
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

	public String debug() {
		StringBuilder sb = new StringBuilder();
		for (Token token : getTokens())
			sb.append(token.debug() + " ");
		return sb.toString().trim();
	}

}
