package com.sum.shy.core.entity;

import java.util.List;

import com.google.common.base.Joiner;

public class Stmt {

	// 一行
	public String line;
	// 语法
	public String syntax;
	// 语义
	public List<Token> tokens;

	/**
	 * 构造方法
	 * 
	 * @param line
	 * @param syntax
	 * @param tokens
	 */
	public Stmt(String line, String syntax, List<Token> tokens) {
		this.line = line;
		this.syntax = syntax;
		this.tokens = tokens;
	}

	@Override
	public String toString() {
		return Joiner.on(" ").join(tokens);
	}

}
