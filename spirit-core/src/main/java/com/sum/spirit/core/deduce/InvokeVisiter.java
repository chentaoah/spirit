package com.sum.spirit.core.deduce;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.core.link.TypeFactory;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.exception.NoSuchFieldException;
import com.sum.spirit.pojo.exception.NoSuchMethodException;

@Component
public class InvokeVisiter {

	@Autowired
	public FastDeducer deducer;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactory factory;

	public void visit(IClass clazz, Statement statement) {
		try {
			for (int index = 0; index < statement.size(); index++) {

				Token token = statement.getToken(index);

				if (token.canSplit())
					visit(clazz, token.getValue());

				if (token.getAttribute(AttributeEnum.TYPE) != null)
					continue;

				List<IType> parameterTypes = token.isInvoke() ? getParameterTypes(clazz, token) : null;

				if (token.isType() || token.isArrayInit() || token.isTypeInit() || token.isCast() || token.isValue()) {
					token.setAttribute(AttributeEnum.TYPE, factory.create(clazz, token));

				} else if (token.isSubexpress()) {
					Statement subStatement = token.getValue();
					token.setAttribute(AttributeEnum.TYPE, deducer.derive(clazz, subStatement.subStmt("(", ")")));

				} else if (token.isLocalMethod()) {
					String memberName = token.getAttribute(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitMethod(clazz.toType().toThis(), memberName, parameterTypes);
					token.setAttribute(AttributeEnum.TYPE, returnType);

				} else if (token.isVisitField()) {
					IType type = statement.getToken(index - 1).getAttribute(AttributeEnum.TYPE);
					String memberName = token.getAttribute(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitField(type, memberName);
					token.setAttribute(AttributeEnum.TYPE, returnType);

				} else if (token.isInvokeMethod()) {
					IType type = statement.getToken(index - 1).getAttribute(AttributeEnum.TYPE);
					String memberName = token.getAttribute(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitMethod(type, memberName, parameterTypes);
					token.setAttribute(AttributeEnum.TYPE, returnType);

				} else if (token.isVisitArrayIndex()) {// what like ".str[0]"
					IType type = statement.getToken(index - 1).getAttribute(AttributeEnum.TYPE);
					String memberName = token.getAttribute(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitField(type, memberName);
					returnType = factory.create(returnType.getTargetName());
					token.setAttribute(AttributeEnum.TYPE, returnType);
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
			List<Statement> subStatements = statement.subStmt(2, statement.size() - 1).splitStmt(",");
			for (Statement subStatement : subStatements) {
				IType parameterType = deducer.derive(clazz, subStatement);
				parameterTypes.add(parameterType);
			}
		}
		return parameterTypes;
	}

}
