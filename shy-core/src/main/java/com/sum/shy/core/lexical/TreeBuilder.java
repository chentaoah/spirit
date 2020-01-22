package com.sum.shy.core.lexical;

import java.util.List;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Node;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.metadata.Symbol;
import com.sum.shy.metadata.SymbolTable;

/**
 * 抽象语法树
 * 
 * @author chentao26275
 *
 */
public class TreeBuilder {

	public static List<Token> build(List<Token> tokens) {
		// 如果只有一个元素
		if (tokens.size() == 1)
			return tokens;
		// 先处理子节点
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.hasSubStmt()) {// 如果有子节点,则对子节点进行转换
				Stmt subStmt = token.getSubStmt();
				build(subStmt.tokens);
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
		int finalCategory = Symbol.NONE;// 一元左元,一元右元,二元
		int index = -1;

		// 每个token在一行里面的位置
		for (int i = 0; i < tokens.size(); i++) {

			Token lastToken = i - 1 >= 0 ? tokens.get(i - 1) : null;
			Token currToken = tokens.get(i);
			Token nextToken = i + 1 < tokens.size() ? tokens.get(i + 1) : null;
			int priority = -1;
			int category = Symbol.NONE;

			if (currToken.isFluent()) {
				// 优先级最高,但是左边不能是?号
				if (lastToken != null && !"?".equals(lastToken.toString())) {
					priority = 50;// 优先级最高
					category = Symbol.LEFT;
				}
			} else if (currToken.isOperator()) {// 如果是操作符
				String value = currToken.toString();
				Symbol symbol = SymbolTable.selectSymbol(value);
				priority = symbol.priority;// 优先级
				if (symbol.isMultiple()) {// 如果有多种可能,则进行进一步判断
					if ("++".equals(value) || "--".equals(value)) {
						if (lastToken != null && lastToken.isVar()) {// 左元
							category = Symbol.LEFT;
						} else if (nextToken != null && nextToken.isVar()) {// 右元
							category = Symbol.RIGHT;
						}
					} else if ("-".equals(value)) {// -可能是个符号 100+(-10) var = -1
						category = (lastToken == null || lastToken.isOperator()) && nextToken != null ? Symbol.RIGHT
								: Symbol.DOUBLE;
					}
				} else {
					category = symbol.category;
				}

			} else if (currToken.isCast()) {
				priority = 35;// 介于!和 *之间
				category = Symbol.RIGHT;

			} else if (currToken.isInstanceof()) {// instanceof
				priority = 20;// 相当于一个==
				category = Symbol.DOUBLE;

			}

			if (priority > maxPriority) {
				finalLastToken = lastToken;
				finalCurrToken = currToken;
				finalNextToken = nextToken;
				maxPriority = priority;
				finalCategory = category;
				index = i;
			}

		}
		// 校验
		if (finalCurrToken == null)
			return tokens;

		// 构建节点结构
		Node node = getNode(finalCurrToken);
		if (finalCategory == Symbol.LEFT || finalCategory == Symbol.DOUBLE) {
			if (finalLastToken != null)
				node.left = getNode(finalLastToken);
		}
		if (finalCategory == Symbol.RIGHT || finalCategory == Symbol.DOUBLE) {
			if (finalNextToken != null)
				node.right = getNode(finalNextToken);
		}

		// 移除,并添加
		if (finalCategory == Symbol.RIGHT || finalCategory == Symbol.DOUBLE)
			tokens.remove(index + 1);
		tokens.remove(index);
		tokens.add(index, new Token(Constants.NODE_TOKEN, node));
		if (finalCategory == Symbol.LEFT || finalCategory == Symbol.DOUBLE)
			tokens.remove(index - 1);

		// 递归
		return build(tokens);
	}

	public static Node getNode(Token token) {
		return token.isNode() ? token.getNode() : new Node(token);
	}

}
