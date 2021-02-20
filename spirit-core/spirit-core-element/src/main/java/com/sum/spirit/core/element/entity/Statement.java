package com.sum.spirit.core.element.entity;

import java.util.List;

import com.google.common.base.Joiner;
import com.sum.spirit.common.utils.Splitter;
import com.sum.spirit.core.element.frame.KeywordTokenBox;
import com.sum.spirit.core.element.utils.StmtFormat;

@SuppressWarnings("serial")
public class Statement extends KeywordTokenBox {

	public Statement(List<Token> tokens) {
		super(tokens);
	}

	public Statement subStmt(int start, int end) {
		return new Statement(subTokens(start, end));
	}

	public Statement subStmt(String left, String right) {
		return subStmt(indexOf(left) + 1, lastIndexOf(right));
	}

	public List<Statement> splitStmt(String separator) {
		return Splitter.splitByMatcherTrim(this, token -> isSymbol(token) && separator.equals(token.toString()), list -> new Statement(list));
	}

	@Override
	public String toString() {
		return Joiner.on("").join(StmtFormat.format(this));
	}

	public String debug() {
		StringBuilder builder = new StringBuilder();
		for (Token token : this) {
			builder.append(token.debug() + " ");
		}
		return builder.toString().trim();
	}

}
