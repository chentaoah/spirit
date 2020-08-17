package com.sum.shy.core.deduce;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.deduce.FastDeducer;
import com.sum.shy.api.lexer.TreeBuilder;
import com.sum.shy.lib.Assert;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.common.TypeTable;
import com.sum.shy.pojo.element.Node;
import com.sum.shy.pojo.element.Statement;
import com.sum.shy.pojo.element.Token;

public class FastDeducerImpl implements FastDeducer {

	public static TreeBuilder builder = ProxyFactory.get(TreeBuilder.class);

	@Override
	public IType derive(IClass clazz, Statement stmt) {
		// 构建树形结构
		List<Token> tokens = builder.build(stmt.tokens);
		for (Token token : tokens) {
			if (token.getTypeAtt() != null) {// 如果有类型直接返回
				return token.getTypeAtt();

			} else if (token.isNode()) {// 如果是节点，则推导节点的类型
				return getType(clazz, token.getValue());
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
			return TypeTable.BOOLEAN_TYPE;

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
