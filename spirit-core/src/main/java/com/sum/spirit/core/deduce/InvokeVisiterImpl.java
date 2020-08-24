package com.sum.spirit.core.deduce;

import java.util.ArrayList;
import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.spirit.api.deduce.FastDeducer;
import com.sum.spirit.api.deduce.InvokeVisiter;
import com.sum.spirit.api.link.ClassLinker;
import com.sum.spirit.api.link.TypeFactory;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.exception.NoSuchFieldException;
import com.sum.spirit.pojo.exception.NoSuchMethodException;

public class InvokeVisiterImpl implements InvokeVisiter {

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);
	public static ClassLinker linker = ProxyFactory.get(ClassLinker.class);
	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	public void visit(IClass clazz, Statement statement) {
		try {
			for (int index = 0; index < statement.size(); index++) {

				Token token = statement.getToken(index);

				if (token.canSplit())
					visit(clazz, token.getValue());

				if (token.getTypeAtt() != null)
					continue;

				List<IType> parameterTypes = token.isInvoke() ? getParameterTypes(clazz, token) : null;

				if (token.isType() || token.isArrayInit() || token.isTypeInit() || token.isCast() || token.isValue()) {
					token.setTypeAtt(factory.create(clazz, token));

				} else if (token.isSubexpress()) {
					Statement subStatement = token.getValue();
					token.setTypeAtt(deducer.derive(clazz, subStatement.subStmt("(", ")")));

				} else if (token.isLocalMethod()) {
					IType returnType = linker.visitMethod(clazz.toType().toThis(), token.getMemberName(), parameterTypes);
					token.setTypeAtt(returnType);

				} else if (token.isVisitField()) {
					IType type = statement.getToken(index - 1).getTypeAtt();
					IType returnType = linker.visitField(type, token.getMemberName());
					token.setTypeAtt(returnType);

				} else if (token.isInvokeMethod()) {
					IType type = statement.getToken(index - 1).getTypeAtt();
					IType returnType = linker.visitMethod(type, token.getMemberName(), parameterTypes);
					token.setTypeAtt(returnType);

				} else if (token.isVisitArrayIndex()) {// what like ".str[0]"
					IType type = statement.getToken(index - 1).getTypeAtt();
					IType returnType = linker.visitField(type, token.getMemberName());
					returnType = factory.create(returnType.getTargetName());
					token.setTypeAtt(returnType);
				}
			}
		} catch (NoSuchFieldException | NoSuchMethodException e) {
			throw new RuntimeException("Link failed for class member!", e);
		}
	}

	public List<IType> getParameterTypes(IClass clazz, Token token) {
		List<IType> parameterTypes = new ArrayList<>();
		Statement statement = token.getValue();
		if (statement.size() > 3) {
			List<Statement> subStatements = statement.subStmt(2, statement.size() - 1).split(",");
			for (Statement subStatement : subStatements) {
				IType parameterType = deducer.derive(clazz, subStatement);
				parameterTypes.add(parameterType);
			}
		}
		return parameterTypes;
	}

}
