package com.sum.spirit.core.deduce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.MemberVisiter;
import com.sum.spirit.api.deduce.VariableTracker;
import com.sum.spirit.api.link.ClassLinker;
import com.sum.spirit.api.link.TypeFactory;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IParameter;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.exception.NoSuchFieldException;

@Component
public class VariableTrackerImpl implements VariableTracker {

	@Autowired
	public MemberVisiter visiter;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactory factory;

	@Override
	public void track(IClass clazz, MethodContext context, Statement statement) {

		for (Token token : statement.tokens) {

			if (token.canSplit())
				track(clazz, context, token.getValue());

			if (token.getTypeAtt() != null)
				continue;

			if (token.isVar()) {
				String name = token.toString();
				IType type = findType(clazz, context, name);
				Assert.notNull(type, "Variable must be declared!name:" + name);
				token.setTypeAtt(type);

			} else if (token.isArrayIndex()) {
				String name = token.getMemberName();
				IType type = findType(clazz, context, name);
				Assert.notNull(type, "Variable must be declared!name:" + name);
				// Convert array type to element type
				type = type.getTargetType();
				token.setTypeAtt(type);
			}
		}
	}

	@Override
	public IType findType(IClass clazz, MethodContext context, String name) {

		// super
		if (Constants.SUPER_KEYWORD.equals(name))
			return clazz.getSuperType().toSuper();

		// this
		if (Constants.THIS_KEYWORD.equals(name))
			return clazz.toType().toThis();

		// find in context
		if (context != null) {
			// find in variable
			for (IVariable variable : context.variables) {
				if (variable.name.equals(name) && context.getBlockId().startsWith(variable.blockId))
					return variable.type;
			}
			// find in parameters
			for (IParameter parameter : context.method.parameters) {
				if (parameter.name.equals(name))
					return parameter.type;
			}
		}

		// Look from the parent class,
		// but note that the parent class may be native
		try {
			return linker.visitField(clazz.toType().toThis(), name);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

}
