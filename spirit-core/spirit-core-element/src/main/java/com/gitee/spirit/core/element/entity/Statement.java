package com.gitee.spirit.core.element.entity;

import java.util.List;

import com.gitee.spirit.common.utils.Splitter;
import com.gitee.spirit.core.element.frame.KeywordTokenBox;
import com.gitee.spirit.core.element.utils.StmtFormat;
import com.google.common.base.Joiner;

public class Statement extends KeywordTokenBox {

	public Statement(List<Token> tokens) {
		super(tokens);
	}

	public Statement subStmt(int fromIndex, int toIndex) {
		return new Statement(subTokens(fromIndex, toIndex));
	}

	public Statement subStmt(String fromStr, String toStr) {
		return subStmt(indexOf(fromStr) + 1, lastIndexOf(toStr));
	}

	public List<Statement> splitStmt(String separator) {
		return Splitter.splitByMatcherTrim(this, token -> isSymbol(token) && separator.equals(token.toString()), list -> new Statement(list));
	}

	@Override
	public String toString() {
		return Joiner.on("").join(StmtFormat.format(this));
	}

	public String debugTokens() {
		StringBuilder builder = new StringBuilder();
		for (Token token : this) {
			builder.append(token.debug() + " ");
		}
		return builder.toString().trim();
	}

}
