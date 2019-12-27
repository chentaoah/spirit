package com.sum.shy.java;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.analyzer.TreePlanter;
import com.sum.shy.core.clazz.impl.CtClass;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Node;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.type.api.Type;
import com.sum.shy.lib.Collection;
import com.sum.shy.lib.StringUtils;

public class JavaConverter {

	public static void convertCommon(CtClass clazz, Stmt stmt) {

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

	}

	public static Stmt convertEquals(CtClass clazz, Stmt stmt) {
		// 先将子语句替换
		for (Token token : stmt.tokens) {
			if (token.hasSubStmt())
				token.value = convertEquals(clazz, (Stmt) token.value);
		}
		// 查找==节点
		stmt = TreePlanter.grow(stmt);
		// 遍历所有顶点，收集==节点
		List<Node> nodes = new ArrayList<>();
		for (Node node : stmt.findNodes()) {
			findEquals(node, nodes);
		}
		// 遍历所有==节点，转换该节点
		for (Node node : nodes) {
			Token token = node.token;
			if (token.isEqualsOperator()) {
				String express = null;
				if ("==".equals(token.value)) {
					express = "StringUtils.equals(%s, %s)";
				} else if ("!=".equals(token.value)) {
					express = "!StringUtils.equals(%s, %s)";
				}
				if (express != null) {
					express = String.format(express, node.left, node.right);
					node.left = null;
					node.right = null;
					node.token = new Token(Constants.EXPRESS_TOKEN, express, null);
				}
			} else {
				String express = String.format("StringUtils.isNotEmpty(%s)", node);
				node.left = null;
				node.right = null;
				node.token = new Token(Constants.EXPRESS_TOKEN, express, null);
			}

		}
		if (nodes.size() > 0)
			clazz.addImport(StringUtils.class.getName());

		return stmt;

	}

	public static void findEquals(Node node, List<Node> nodes) {
		// 如果当前节点就是
		if (node == null)
			return;

		Token token = node.token;
		if (token.isLogicalOperator()) {// ! or && or ||
			if (node.left != null) {
				Type type = node.left.token.getTypeAtt();
				if (type != null && type.isStr())
					nodes.add(node.left);
			}
			if (node.right != null) {
				Type type = node.right.token.getTypeAtt();
				if (type != null && type.isStr())
					nodes.add(node.right);
			}
		} else if (token.isEqualsOperator()) {// == or !=
			if (node.left != null && node.right != null) {
				Type type = node.left.token.getTypeAtt();
				if (type != null && type.isStr()) {// 如果左边的类型是str
					String value = node.right.token.value.toString();
					if (!"null".equals(value)) {// 并且右边不是null
						nodes.add(node);
					}
				}
			}
		}
		// 查找子节点
		if (node.left != null)
			findEquals(node.left, nodes);
		if (node.right != null)
			findEquals(node.right, nodes);

	}

	public static void insertBrackets(CtClass clazz, Stmt stmt) {
		// 第一个连续关键字之后，最后的分隔符之前
		if (stmt.size() >= 2) {// if xxx { //print xxx,xxx //}catch Exception e{
			int index = findKeyword(stmt);
			stmt.tokens.add(index + 1, new Token(Constants.SEPARATOR_TOKEN, "(", null));
			if ("{".equals(stmt.last())) {
				stmt.tokens.add(stmt.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")", null));
			} else {
				stmt.tokens.add(new Token(Constants.SEPARATOR_TOKEN, ")", null));
			}
		}
	}

	private static int findKeyword(Stmt stmt) {
		int index = -1;
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isKeyword()) {
				index = i;
			} else {
				if (index == -1) {// 不是关键字的话,则进行下去
					continue;
				} else {// 如果不是关键字,但是关键字已经找到,则中断
					break;
				}
			}
		}
		return index;
	}

	public static void addLineEnd(CtClass clazz, Stmt stmt) {
		stmt.tokens.add(new Token(Constants.SUFFIX_TOKEN, ";", null));// 这个添加的后缀,使得后面不会加上空格
	}

}
