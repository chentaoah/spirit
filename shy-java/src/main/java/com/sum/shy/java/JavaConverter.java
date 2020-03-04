package com.sum.shy.java;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.doc.Node;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.doc.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.type.api.IType;
import com.sum.shy.lib.Collection;
import com.sum.shy.lib.StringUtils;

public class JavaConverter {

//	public static void convert(IClass clazz, Stmt stmt) {
//
//		for (int i = 0; i < stmt.size(); i++) {
//			Token token = stmt.getToken(i);
//
//			if (token.hasSubStmt())
//				convert(clazz, token.getSubStmt());
//			if (token.isNode())
//				convert(clazz, token.getNode().toStmt());
//
//			if (token.isArrayInit()) {// 数组初始化,是没有子语句的
//				Stmt subStmt = token.getSubStmt();
//				subStmt.tokens.add(0, new Token(Constants.KEYWORD_TOKEN, "new"));
//
//			} else if (token.isTypeInit()) {// 在所有的构造函数前面都加个new
//				Stmt subStmt = token.getSubStmt();
//				subStmt.tokens.add(0, new Token(Constants.KEYWORD_TOKEN, "new"));
//
//			} else if (token.isList()) {// 将所有的array和map都转换成方法调用
//				Stmt subStmt = token.getSubStmt();
//				subStmt.tokens.set(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "Collection.newArrayList("));
//				subStmt.tokens.set(subStmt.size() - 1, new Token(Constants.CUSTOM_SUFFIX_TOKEN, ")"));
//				clazz.addImport(Collection.class.getName());// 添加依赖
//
//			} else if (token.isMap()) {
//				Stmt subStmt = token.getSubStmt();
//				for (Token subToken : subStmt.tokens) {// 将map里面的冒号分隔符,转换成逗号分隔
//					if (subToken.isSeparator() && ":".equals(subToken.toString()))
//						subToken.value = ",";
//				}
//				subStmt.tokens.set(0, new Token(Constants.CUSTOM_PREFIX_TOKEN, "Collection.newHashMap("));
//				subStmt.tokens.set(subStmt.size() - 1, new Token(Constants.CUSTOM_SUFFIX_TOKEN, ")"));
//				clazz.addImport(Collection.class.getName());// 添加依赖
//
//			} else if (token.isNode()) {
//				convertNode(clazz, token);
//
//			}
//
//		}
//
//	}
//
//	public static void convertNode(IClass clazz, Token token) {
//		// 查找符合条件的逻辑判断
//		List<Node> nodes = new ArrayList<>();
//		findEquals(nodes, token.getNode());
//		// 添加依赖
//		if (nodes.size() > 0)
//			clazz.addImport(StringUtils.class.getName());
//		// 遍历所有==节点，转换该节点
//		for (Node node : nodes) {
//			Token someToken = node.token;
//			if (someToken.isEquals()) {// 二元操作符
//				String express = null;
//				if ("==".equals(someToken.toString())) {
//					express = "StringUtils.equals(%s, %s)";
//				} else if ("!=".equals(someToken.toString())) {
//					express = "!StringUtils.equals(%s, %s)";
//				}
//				if (express != null) {
//					express = String.format(express, node.left, node.right);
//					node.left = null;
//					node.right = null;
//					node.token = new Token(Constants.CUSTOM_EXPRESS_TOKEN, express);
//				}
//			} else {// 单独的一个str
//				String express = String.format("StringUtils.isNotEmpty(%s)", node);
//				node.left = null;
//				node.right = null;
//				node.token = new Token(Constants.CUSTOM_EXPRESS_TOKEN, express);
//			}
//
//		}
//
//	}
//
//	public static void findEquals(List<Node> nodes, Node node) {
//		// 将树形结构,转成数组,进行遍历
//		for (Node someNode : node.getNodes()) {
//			Token token = someNode.token;
//			if (token.isLogical()) {// ! or && or ||
//				if (someNode.left != null) {
//					IType type = someNode.left.token.getTypeAtt();
//					if (type != null && type.isStr())
//						nodes.add(someNode.left);
//				}
//				if (someNode.right != null) {
//					IType type = someNode.right.token.getTypeAtt();
//					if (type != null && type.isStr())
//						nodes.add(someNode.right);
//				}
//			} else if (token.isEquals()) {// == or !=
//				if (someNode.left != null && someNode.right != null) {
//					IType type = someNode.left.token.getTypeAtt();
//					if (type != null && type.isStr()) {// 如果左边的类型是str
//						Token rightToken = someNode.right.token;
//						if (!rightToken.isNull()) // 并且右边不是null
//							nodes.add(someNode);
//					}
//				}
//			}
//		}
//	}
//
//	public static void insertBrackets(IClass clazz, Stmt stmt) {
//		// 第一个连续关键字之后，最后的分隔符之前
//		if (stmt.size() >= 2) {// if xxx { //print xxx,xxx //}catch Exception e{
//			int index = findKeyword(stmt);
//			stmt.tokens.add(index + 1, new Token(Constants.SEPARATOR_TOKEN, "("));
//			if ("{".equals(stmt.last())) {
//				stmt.tokens.add(stmt.size() - 1, new Token(Constants.SEPARATOR_TOKEN, ")"));
//			} else {
//				stmt.tokens.add(new Token(Constants.SEPARATOR_TOKEN, ")"));
//			}
//		}
//	}
//
//	public static int findKeyword(Stmt stmt) {
//		int index = -1;
//		for (int i = 0; i < stmt.size(); i++) {
//			Token token = stmt.getToken(i);
//			if (token.isKeyword()) {
//				index = i;
//			} else {
//				if (index == -1) {// 不是关键字的话,则进行下去
//					continue;
//				} else {// 如果不是关键字,但是关键字已经找到,则中断
//					break;
//				}
//			}
//		}
//		return index;
//	}
//
//	public static void addLineEnd(IClass clazz, Stmt stmt) {
//		stmt.tokens.add(new Token(Constants.CUSTOM_SUFFIX_TOKEN, ";"));// 这个添加的后缀,使得后面不会加上空格
//	}

}
