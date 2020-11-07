package com.sum.spirit.core.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.lexer.TreeBuilder;
import com.sum.spirit.core.type.IType;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Node;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.TypeEnum;

@Component
public class FastDeducer {

	@Autowired
	public TreeBuilder builder;

	public IType derive(IClass clazz, Statement statement) {
		// 构建树形结构
		IType type = null;
		for (Node node : builder.buildNodes(statement.tokens)) {
			type = getTypeByNode(clazz, node);
		}
		// 校验
		Assert.notNull(type, "Type is null!");
		return type;
	}

	public static IType getTypeByNode(IClass clazz, Node node) {

		Token token = node.token;

		// 如果有类型直接返回
		if (token.getAttribute(AttributeEnum.TYPE) != null)
			return token.getAttribute(AttributeEnum.TYPE);

		if (token.isLogical() || token.isRelation() || token.isInstanceof()) { // 如果是逻辑判断，或者类型判断关键字
			return TypeEnum.boolean_t.value;

		} else if (token.isArithmetic() || token.isBitwise()) {// 先取左边的，再取右边的
			if (node.prev != null) {
				return getTypeByNode(clazz, node.prev);

			} else if (node.next != null) {
				return getTypeByNode(clazz, node.next);
			}
		}
		return null;
	}

}
