package com.sum.spirit.core.visit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.api.StatementAction;
import com.sum.spirit.core.FastDeducer;
import com.sum.spirit.core.link.TypeFactory;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.ElementEvent;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.common.StatementEvent;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.utils.StmtVisiter;

@Component
@Order(-40)
public class InvokeVisiter extends AbsElementAction implements StatementAction {

	@Autowired
	public FastDeducer deducer;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactory factory;

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.element.statement;
		doVisit(clazz, statement);
	}

	@Override
	public void visit(StatementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.statement;
		doVisit(clazz, statement);
	}

	public void doVisit(IClass clazz, Statement statement) {
		new StmtVisiter().visit(statement, (stmt, index, currentToken) -> {
			try {
				if (currentToken.attr(AttributeEnum.TYPE) != null) {
					return null;
				}
				List<IType> parameterTypes = currentToken.isInvoke() ? getParameterTypes(clazz, currentToken) : null;
				if (currentToken.isType() || currentToken.isArrayInit() || currentToken.isTypeInit() || //
				currentToken.isCast() || currentToken.isValue()) {
					currentToken.setAttr(AttributeEnum.TYPE, factory.create(clazz, currentToken));

				} else if (currentToken.isSubexpress()) {
					Statement subStatement = currentToken.getValue();
					currentToken.setAttr(AttributeEnum.TYPE, deducer.derive(clazz, subStatement.subStmt("(", ")")));

				} else if (currentToken.isLocalMethod()) {
					String memberName = currentToken.attr(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitMethod(clazz.getType().toThis(), memberName, parameterTypes);
					currentToken.setAttr(AttributeEnum.TYPE, returnType);

				} else if (currentToken.isVisitField()) {
					IType type = stmt.getToken(index - 1).attr(AttributeEnum.TYPE);
					String memberName = currentToken.attr(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitField(type, memberName);
					currentToken.setAttr(AttributeEnum.TYPE, returnType);

				} else if (currentToken.isInvokeMethod()) {
					IType type = stmt.getToken(index - 1).attr(AttributeEnum.TYPE);
					String memberName = currentToken.attr(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitMethod(type, memberName, parameterTypes);
					currentToken.setAttr(AttributeEnum.TYPE, returnType);

				} else if (currentToken.isVisitArrayIndex()) {// what like ".str[0]"
					IType type = stmt.getToken(index - 1).attr(AttributeEnum.TYPE);
					String memberName = currentToken.attr(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitField(type, memberName);
					returnType = factory.create(returnType.getTargetName());
					currentToken.setAttr(AttributeEnum.TYPE, returnType);
				}
				return null;

			} catch (NoSuchFieldException | NoSuchMethodException e) {
				throw new RuntimeException("Link failed for class member!", e);
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
