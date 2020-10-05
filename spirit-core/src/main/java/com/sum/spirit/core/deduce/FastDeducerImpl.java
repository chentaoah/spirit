package com.sum.spirit.core.deduce;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.deduce.FastDeducer;
import com.sum.spirit.api.lexer.TreeBuilder;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.element.Node;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.TypeEnum;

@Component
public class FastDeducerImpl implements FastDeducer {

	@Autowired
	public TreeBuilder builder;

	@Override
	public IType derive(IClass clazz, Statement statement) {
		// 构建树形结构
		List<Token> tokens = builder.build(statement.tokens);
		for (Token token : tokens) {
			if (token.getTypeAtt() != null) {// 如果有类型直接返回
				return token.getTypeAtt();

			} else if (token.isNode()) {// 如果是节点，则推导节点的类型
				return getType(clazz, token.getValue());
			}
		}
		throw new RuntimeException("Cannot deduce type!statement:" + statement.toString());
	}

	public static IType getType(IClass clazz, Node node) {

		if (node == null)
			return null;

		Token token = node.token;
		// 如果是逻辑判断，或者类型判断关键字
		if (token.isLogical() || token.isRelation() || token.isInstanceof()) {
			return TypeEnum.BOOLEAN.value;

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
