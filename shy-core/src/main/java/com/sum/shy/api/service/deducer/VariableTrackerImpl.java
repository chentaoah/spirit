package com.sum.shy.api.service.deducer;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.MemberVisiter;
import com.sum.shy.api.deducer.MemberLinker;
import com.sum.shy.api.deducer.TypeFactory;
import com.sum.shy.api.deducer.VariableTracker;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.clazz.IParameter;
import com.sum.shy.clazz.IType;
import com.sum.shy.clazz.IVariable;
import com.sum.shy.common.Constants;
import com.sum.shy.common.MethodContext;
import com.sum.shy.element.Statement;
import com.sum.shy.element.Token;
import com.sum.shy.lib.Assert;

public class VariableTrackerImpl implements VariableTracker {

	public static MemberVisiter visiter = ProxyFactory.get(MemberVisiter.class);

	public static MemberLinker linker = ProxyFactory.get(MemberLinker.class);

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
				type = type.getTargetType();// Convert array type to element type
				token.setTypeAtt(type);
			}
		}
	}

	@Override
	public IType findType(IClass clazz, MethodContext context, String name) {

		// super
		if (Constants.SUPER_KEYWORD.equals(name))
			return clazz.getSuperType();

		// this
		if (Constants.THIS_KEYWORD.equals(name))
			return clazz.toType();

		// find in context
		if (context != null) {
			IMethod method = context.method;
			// find in variable
			for (IVariable variable : context.variables) {
				if (variable.name.equals(name) && context.getBlockId().startsWith(variable.blockId))
					return variable.type;
			}
			// find in parameters
			for (IParameter parameter : method.parameters) {
				if (parameter.name.equals(name))
					return parameter.type;
			}
		}

		// find in members
		for (IField field : clazz.fields) {
			if (field.name.equals(name)) {
				if (field.type == null)
					field.type = visiter.visitMember(clazz, field);
				return field.type;
			}
		}

		// Look from the parent class, but note that the parent class may be native
		return linker.visitField(clazz.getSuperType(), name);
	}
}
