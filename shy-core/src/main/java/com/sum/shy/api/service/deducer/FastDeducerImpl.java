package com.sum.shy.api.service.deducer;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.FastDeducer;
import com.sum.shy.api.TreeBuilder;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.common.StaticType;
import com.sum.shy.element.Node;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;
import com.sum.shy.lib.Assert;

public class FastDeducerImpl implements FastDeducer {

	public static TreeBuilder builder = ProxyFactory.get(TreeBuilder.class);

	@Override
	public IType deriveStmt(IClass clazz, Statement stmt) {
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
