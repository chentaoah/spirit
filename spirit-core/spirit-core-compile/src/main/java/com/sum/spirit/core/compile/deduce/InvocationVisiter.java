package com.sum.spirit.core.compile.deduce;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.api.TypeFactory;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.core.element.utils.StmtVisiter;

@Component
public class InvocationVisiter {

	@Autowired
	public SimpleDeducer deducer;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactory factory;
	@Autowired
	public TypeDerivator derivator;

	public void visit(IClass clazz, Statement statement) {
		StmtVisiter.visit(statement, stmt -> {
			for (int index = 0; index < stmt.size(); index++) {
				try {
					Token token = stmt.get(index);
					if (token.attr(AttributeEnum.TYPE) != null) {
						continue;
					}
					List<IType> parameterTypes = token.isInvoke() ? getParameterTypes(clazz, token) : null;
					if (token.isType() || token.isArrayInit() || token.isTypeInit() || token.isCast() || token.isLiteral()) {
						token.setAttr(AttributeEnum.TYPE, factory.create(clazz, token));

					} else if (token.isSubexpress()) {
						Statement subStatement = token.getValue();
						token.setAttr(AttributeEnum.TYPE, deducer.derive(clazz, subStatement.subStmt("(", ")")));

					} else if (token.isLocalMethod()) {
						String memberName = token.attr(AttributeEnum.MEMBER_NAME);
						IType returnType = linker.visitMethod(derivator.withThisModifiers(clazz.getType()), memberName, parameterTypes);
						token.setAttr(AttributeEnum.TYPE, returnType);

					} else if (token.isVisitField()) {
						IType type = stmt.get(index - 1).attr(AttributeEnum.TYPE);
						String memberName = token.attr(AttributeEnum.MEMBER_NAME);
						IType returnType = linker.visitField(type, memberName);
						token.setAttr(AttributeEnum.TYPE, returnType);

					} else if (token.isVisitMethod()) {
						IType type = stmt.get(index - 1).attr(AttributeEnum.TYPE);
						String memberName = token.attr(AttributeEnum.MEMBER_NAME);
						IType returnType = linker.visitMethod(type, memberName, parameterTypes);
						token.setAttr(AttributeEnum.TYPE, returnType);

					} else if (token.isVisitIndex()) {// what like "[0]"
						IType type = stmt.get(index - 1).attr(AttributeEnum.TYPE);
						type = derivator.toTarget(type);// 转换数组类型为目标类型
						token.setAttr(AttributeEnum.TYPE, type);
					}

				} catch (NoSuchFieldException | NoSuchMethodException e) {
					throw new RuntimeException("Link failed for class member!", e);
				}
			}
		});
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
