package com.sum.spirit.core.visiter.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.clazz.pojo.IClass;
import com.sum.spirit.core.common.enums.AttributeEnum;
import com.sum.spirit.core.common.enums.TypeEnum;
import com.sum.spirit.core.element.action.TreeBuilder;
import com.sum.spirit.core.element.pojo.Node;
import com.sum.spirit.core.element.pojo.Statement;
import com.sum.spirit.core.element.pojo.Token;
import com.sum.spirit.core.visiter.pojo.IType;

@Component
public class FastDeducer {

	@Autowired
	public TreeBuilder builder;

	public IType derive(IClass clazz, Statement statement) {
		// 构建树形结构
		for (Node node : builder.buildNodes(statement.tokens)) {
			IType type = getTypeByNode(clazz, node);
			if (type != null) {
				return type;
			}
		}
		throw new RuntimeException("Cannot derive the type!");
	}

	public static IType getTypeByNode(IClass clazz, Node node) {

		Token token = node.token;

		// 如果有类型直接返回
		if (token.attr(AttributeEnum.TYPE) != null) {
			return token.attr(AttributeEnum.TYPE);
		}

		// 如果是逻辑判断，或者类型判断关键字
		if (token.isLogical() || token.isRelation() || token.isInstanceof()) {
			return TypeEnum.boolean_t.value;

		} else if (token.isArithmetic() || token.isBitwise()) {
			// 先取左边的，再取右边的
			if (node.prev != null) {
				return getTypeByNode(clazz, node.prev);
			} else if (node.next != null) {
				return getTypeByNode(clazz, node.next);
			}
		}

		return null;
	}

}
