package com.sum.jass.core.deduce;

import com.sum.pisces.core.ProxyFactory;
import com.sum.jass.api.MemberVisiter;
import com.sum.jass.api.deduce.VariableTracker;
import com.sum.jass.api.link.ClassLinker;
import com.sum.jass.api.link.TypeFactory;
import com.sum.jass.lib.Assert;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.clazz.IParameter;
import com.sum.jass.pojo.clazz.IType;
import com.sum.jass.pojo.clazz.IVariable;
import com.sum.jass.pojo.common.Constants;
import com.sum.jass.pojo.common.MethodContext;
import com.sum.jass.pojo.element.Statement;
import com.sum.jass.pojo.element.Token;
import com.sum.jass.pojo.exception.NoSuchFieldException;

public class VariableTrackerImpl implements VariableTracker {

	public static MemberVisiter visiter = ProxyFactory.get(MemberVisiter.class);

	public static ClassLinker linker = ProxyFactory.get(ClassLinker.class);

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	public void track(IClass clazz, MethodContext context, Statement stmt) {

		for (Token token : stmt.tokens) {

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
