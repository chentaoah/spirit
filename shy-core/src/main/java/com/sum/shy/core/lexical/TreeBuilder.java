package com.sum.shy.core.lexical;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.document.Node;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.document.Tree;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.metadata.Symbol;
import com.sum.shy.core.metadata.SymbolTable;

/**
 * 抽象语法树
 * 
 * @author chentao26275
 *
 */
public class TreeBuilder {

	public static Tree build(Stmt stmt) {
		return new Tree(build(stmt.tokens));
	}

	public static List<Token> build(List<Token> tokens) {
		// 如果只有一个元素
		if (tokens.size() == 1)
			return tokens;
		// 拷贝一份
		tokens = new ArrayList<>(tokens);
		// 先处理子节点
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.canVisit()) {// 如果有子节点,则对子节点进行转换
				token = token.copy();// 拷贝一份
				Stmt subStmt = token.getStmt();
				subStmt.tokens = build(subStmt.tokens);
				tokens.set(i, token); // 替换原来的
			}
		}
		// 通过递归获取节点树
		return getNodeByLoop(tokens);
	}

	public static List<Token> getNodeByLoop(List<Token> tokens) {

		// 1.为每个操作符,或者特殊的关键字,进行优先级分配
		Token finalLastToken = null;
		Token finalCurrToken = null;// 当前优先级最高的操作符
		Token finalNextToken = null;
		int maxPriority = -1;// 优先级
		int finalOperand = Symbol.NONE;// 一元左元,一元右元,二元
		int index = -1;

		// 每个token在一行里面的位置
		for (int i = 0; i < tokens.size(); i++) {

			Token lastToken = i - 1 >= 0 ? tokens.get(i - 1) : null;
			Token currToken = tokens.get(i);
			Token nextToken = i + 1 < tokens.size() ? tokens.get(i + 1) : null;
			int priority = -1;
			int operand = Symbol.NONE;

			if (currToken.isType()) {
				if (nextToken != null && (nextToken.isVar() || nextToken.isLocalMethod())) {
					priority = 55;// 优先级最高
					operand = Symbol.RIGHT;
				}

			} else if (currToken.isFluent()) {
				priority = 50;
				operand = Symbol.LEFT;

			} else if (currToken.isOperator()) {// 如果是操作符
				String value = currToken.toString();
				Symbol symbol = SymbolTable.selectSymbol(value);
				priority = symbol.priority;// 优先级
				if (symbol.isMultiple()) {// 如果有多种可能,则进行进一步判断
					if ("++".equals(value) || "--".equals(value)) {
						if (lastToken != null && (lastToken.isVar() || lastToken.isNode())) {// 左元
							operand = Symbol.LEFT;
						} else if (nextToken != null && (lastToken.isVar() || lastToken.isNode())) {// 右元
							operand = Symbol.RIGHT;
						}
						currToken.setOperand(operand);// 标记一下

					} else if ("-".equals(value)) {// -可能是个符号 100+(-10) var = -1
						if (lastToken != null && (lastToken.isNumber() || lastToken.isVar() || lastToken.isNode())) {
							operand = Symbol.DOUBLE;
						} else {
							operand = Symbol.RIGHT;
						}
						currToken.setOperand(operand);// 标记一下

					}
				} else {
					operand = symbol.operand;
				}

			} else if (currToken.isCast()) {
				priority = 35;// 介于!和 *之间
				operand = Symbol.RIGHT;

			} else if (currToken.isInstanceof()) {// instanceof
				priority = 20;// 相当于一个==
				operand = Symbol.DOUBLE;

			}

			if (priority > maxPriority) {
				finalLastToken = lastToken;
				finalCurrToken = currToken;
				finalNextToken = nextToken;
				maxPriority = priority;
				finalOperand = operand;
				index = i;
			}

		}
		// 校验
		if (finalCurrToken == null)
			return tokens;

		// 构建节点结构
		Node node = getNode(finalCurrToken);
		if (finalOperand == Symbol.LEFT || finalOperand == Symbol.DOUBLE) {
			if (finalLastToken != null)
				node.left = getNode(finalLastToken);
		}
		if (finalOperand == Symbol.RIGHT || finalOperand == Symbol.DOUBLE) {
			if (finalNextToken != null)
				node.right = getNode(finalNextToken);
		}

		// 移除,并添加
		if (finalOperand == Symbol.RIGHT || finalOperand == Symbol.DOUBLE)
			tokens.remove(index + 1);
		tokens.remove(index);
		tokens.add(index, new Token(Constants.NODE_TOKEN, node));
		if (finalOperand == Symbol.LEFT || finalOperand == Symbol.DOUBLE)
			tokens.remove(index - 1);

		// 递归
		return getNodeByLoop(tokens);
	}

	public static Node getNode(Token token) {
		return token.isNode() ? token.getNode() : new Node(token);
	}

}
