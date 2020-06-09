package com.sum.shy.processor;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.TreeBuilder;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.clazz.IVariable;
import com.sum.shy.common.StaticType;
import com.sum.shy.element.Element;
import com.sum.shy.element.Node;
import com.sum.shy.element.Stmt;
import com.sum.shy.element.Token;
import com.sum.shy.lib.Assert;

public class FastDeducer {

	public static TreeBuilder builder = ProxyFactory.get(TreeBuilder.class);

	/**
	 * 根据语法，返回语句中定义的变量，
	 * 
	 * @param clazz
	 * @param element
	 * @return
	 */
	public static IVariable derive(IClass clazz, Element element) {

		if (element.isDeclare() || element.isDeclareAssign()) {
			Token varToken = element.getToken(1);
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isCatch()) {
			Token varToken = element.getToken(3);
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isAssign()) {
			Token varToken = element.getToken(0);
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isForIn()) {
			Token varToken = element.getToken(1);
			return new IVariable(varToken.getTypeAtt(), varToken.toString());

		} else if (element.isReturn()) {
			Stmt subStmt = element.subStmt(1, element.size());
			return new IVariable(deriveStmt(clazz, subStmt), null);
		}
		return null;
	}

	/**
	 * 推导表达式的返回值类型，这里的表达式只是一个语句的一部分
	 * 
	 * @param clazz
	 * @param stmt
	 * @return
	 */
	public static IType deriveStmt(IClass clazz, Stmt stmt) {
		// 构建树形结构
		List<Token> tokens = builder.build(stmt.tokens);
		for (Token token : tokens) {
			if (token.getTypeAtt() != null) {// 如果有类型直接返回
				return token.getTypeAtt();

			} else if (token.isNode()) {// 如果是节点，则推导节点的类型
				return getType(clazz, token.getNode());
			}
		}
		throw new RuntimeException("Cannot deduce type!stmt:" + stmt.toString());
	}

	public static IType getType(IClass clazz, Node node) {

		if (node == null)
			return null;

		Token token = node.token;
		// 如果是逻辑判断，或者类型判断关键字
		if (token.isLogical() || token.isRelation() || token.isInstanceof()) {
			return StaticType.BOOLEAN_TYPE;

		} else if (token.isArithmetic() || token.isBitwise()) {
			if (node.left != null) {// 先取左边的，再取右边的
				return getType(clazz, node.left);

			} else if (node.right != null) {
				return getType(clazz, node.right);
			}
		}

		Assert.notNull(token.getTypeAtt(), "Type is null!");
		return token.getTypeAtt();

	}

}
