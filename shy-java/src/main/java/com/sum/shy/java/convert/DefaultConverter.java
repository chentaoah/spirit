package com.sum.shy.java.convert;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.analyzer.AbsSyntaxTree;
import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Node;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.java.api.Converter;
import com.sum.shy.lib.Collection;

public class DefaultConverter implements Converter {

	@Override
	public Stmt convert(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
		return convertStmt(clazz, stmt);
	}

	public static Stmt convertStmt(CtClass clazz, Stmt stmt) {
		// 将语句进行一定的转换
		stmt = convertCommon(clazz, stmt);
		// 这个添加的后缀,使得后面不会加上空格
		stmt.tokens.add(new Token(Constants.SUFFIX_TOKEN, ";", null));

		return stmt;

	}

	public static Stmt convertCommon(CtClass clazz, Stmt stmt) {

		for (int i = 0; i < stmt.size(); i++) {

			Token token = stmt.getToken(i);
			if (token.hasSubStmt())
				convertCommon(clazz, (Stmt) token.value);

			if (token.isArrayInit()) {// 数组初始化,是没有子语句的
				Stmt subStmt = (Stmt) token.value;
				subStmt.tokens.add(0, new Token(Constants.KEYWORD_TOKEN, "new", null));

			} else if (token.isTypeInit()) {// 在所有的构造函数前面都加个new
				Stmt subStmt = (Stmt) token.value;
				subStmt.tokens.add(0, new Token(Constants.KEYWORD_TOKEN, "new", null));

			} else if (token.isList()) {// 将所有的array和map都转换成方法调用
				Stmt subStmt = (Stmt) token.value;
				subStmt.getToken(0).value = "Collection.newArrayList(";
				subStmt.getToken(subStmt.size() - 1).value = ")";
				// 添加依赖
				clazz.addImport(Collection.class.getName());

			} else if (token.isMap()) {
				Stmt subStmt = (Stmt) token.value;
				for (Token subToken : subStmt.tokens) {// 将map里面的冒号分隔符,转换成逗号分隔
					if (subToken.isSeparator() && ":".equals(subToken.value)) {
						subToken.value = ",";
					}
				}
				subStmt.getToken(0).value = "Collection.newHashMap(";
				subStmt.getToken(subStmt.size() - 1).value = ")";
				// 添加依赖
				clazz.addImport(Collection.class.getName());

			}

		}

		return stmt;
	}

	public static Stmt convertEquals(CtClass clazz, Stmt stmt) {
		// 先将子语句替换
		for (Token token : stmt.tokens) {
			if (token.hasSubStmt())
				token.value = convertEquals(clazz, (Stmt) token.value);
		}
		// 查找==节点
		stmt = AbsSyntaxTree.grow(stmt);
		// 遍历所有顶点，收集==节点
		List<Node> nodes = new ArrayList<>();
		for (Node node : stmt.findNodes()) {
			findEquals(node, nodes);
		}
		// 遍历所有==节点，转换该节点
		for (Node node : nodes) {
			String express = String.format("StringUtils.equals(%s, %s)", node.left, node.right);
			node.token = new Token(Constants.EXPRESS_TOKEN, express, null);
			node.left = null;
			node.right = null;
		}
		return stmt;
	}

	public static void findEquals(Node node, List<Node> nodes) {
		// 如果当前节点就是
		Token token = node.token;
		if (token.isOperator() && "==".equals(token.value)) {
			try {
				// 如果两边节点都是str的话
				if (node.left.token.getTypeAtt().isStr()) {
					nodes.add(node);
				}
			} catch (Exception e) {
				// ignore
			}
		}
		// 查找子节点
		if (node.left != null)
			findEquals(node.left, nodes);
		if (node.right != null)
			findEquals(node.right, nodes);

	}

}
