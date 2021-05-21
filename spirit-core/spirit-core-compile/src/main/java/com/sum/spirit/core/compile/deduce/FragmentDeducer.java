package com.sum.spirit.core.compile.deduce;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.constants.Attribute;
import com.sum.spirit.core.api.TreeBuilder;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.utils.StaticTypes;
import com.sum.spirit.core.element.entity.Node;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;

@Component
public class FragmentDeducer {

	@Autowired
	public TreeBuilder builder;

	public IType derive(IClass clazz, Statement statement) {
		List<Node> nodes = builder.buildNodes(statement);
		for (Node node : nodes) {
			IType type = getTypeByNode(clazz, node);
			if (type != null) {
				return type;
			}
		}
		throw new RuntimeException("Unhandled branch!");
	}

	public static IType getTypeByNode(IClass clazz, Node node) {
		Token token = node.token;

		// 如果有类型直接返回
		if (token.attr(Attribute.TYPE) != null) {
			return token.attr(Attribute.TYPE);
		}

		// 如果是逻辑判断，或者类型判断关键字
		if (token.isLogical() || token.isRelation() || token.isInstanceof()) {
			return StaticTypes.BOOLEAN;

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
