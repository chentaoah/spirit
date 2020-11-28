package com.sum.spirit.core.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.core.ClassVisiter;
import com.sum.spirit.core.type.TypeFactory;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IParameter;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.utils.Assert;

@Component
public class VariableTracker {

	@Autowired
	public ClassVisiter visiter;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactory factory;

	public void track(IClass clazz, MethodContext context, Statement statement) {

		for (Token token : statement.tokens) {

			if (token.canSplit()) {
				track(clazz, context, token.getValue());
			}

			if (token.attr(AttributeEnum.TYPE) != null) {
				continue;
			}

			if (token.isVar()) {
				String name = token.toString();
				IType type = findType(clazz, context, name);
				Assert.notNull(type, "Variable must be declared!name:" + name);
				token.setAttr(AttributeEnum.TYPE, type);

			} else if (token.isArrayIndex()) {// .strs[0]
				String name = token.attr(AttributeEnum.MEMBER_NAME);
				IType type = findType(clazz, context, name);
				Assert.notNull(type, "Variable must be declared!name:" + name);
				// 转换数组类型为目标类型
				type = type.getTargetType();
				token.setAttr(AttributeEnum.TYPE, type);

			} else if (token.isKeyword() && (KeywordEnum.SUPER.value.equals(token.value) || KeywordEnum.THIS.value.equals(token.value))) {
				String name = token.toString();
				IType type = findType(clazz, context, name);
				Assert.notNull(type, "Variable must be declared!name:" + name);
				token.setAttr(AttributeEnum.TYPE, type);
			}
		}
	}

	public IType findType(IClass clazz, MethodContext context, String name) {

		// super
		if (KeywordEnum.SUPER.value.equals(name)) {
			return clazz.getSuperType().toSuper();
		}

		// this
		if (KeywordEnum.THIS.value.equals(name)) {
			return clazz.getType().toThis();
		}

		if (context != null) {
			for (IVariable variable : context.variables) {// 变量
				if (variable.getName().equals(name) && context.getBlockId().startsWith(variable.blockId)) {
					return variable.getType();
				}
			}
			for (IParameter parameter : context.method.parameters) {// 入参
				if (parameter.getName().equals(name)) {
					return parameter.getType();
				}
			}
		}

		// 从本身和父类里面寻找，父类可能是native的
		try {
			return linker.visitField(clazz.getType().toThis(), name);

		} catch (NoSuchFieldException e) {
			return null;
		}
	}

}
