package com.sum.spirit.core.visiter.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.api.StatementAction;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.core.visiter.entity.ElementEvent;
import com.sum.spirit.core.visiter.entity.IType;
import com.sum.spirit.core.visiter.entity.StatementEvent;
import com.sum.spirit.core.visiter.linker.TypeFactory;
import com.sum.spirit.core.visiter.utils.StmtVisiter;

@Component
@Order(-40)
public class InvocationVisiter extends AbstractElementAction implements StatementAction {

	@Autowired
	public FastDeducer deducer;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactory factory;

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.element;
		doVisit(clazz, statement);
	}

	@Override
	public void visit(StatementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.statement;
		doVisit(clazz, statement);
	}

	public void doVisit(IClass clazz, Statement statement) {
		new StmtVisiter().visitVoid(statement, event -> {
			try {
				Statement stmt = (Statement) event.listable;
				int index = event.index;
				Token token = event.item;
				if (token.attr(AttributeEnum.TYPE) != null) {
					return;
				}
				List<IType> parameterTypes = token.isInvoke() ? getParameterTypes(clazz, token) : null;
				if (token.isType() || token.isArrayInit() || token.isTypeInit() || //
				token.isCast() || token.isValue()) {
					token.setAttr(AttributeEnum.TYPE, factory.create(clazz, token));

				} else if (token.isSubexpress()) {
					Statement subStatement = token.getValue();
					token.setAttr(AttributeEnum.TYPE, deducer.derive(clazz, subStatement.subStmt("(", ")")));

				} else if (token.isLocalMethod()) {
					String memberName = token.attr(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitMethod(clazz.getType().toThis(), memberName, parameterTypes);
					token.setAttr(AttributeEnum.TYPE, returnType);

				} else if (token.isVisitField()) {
					IType type = stmt.get(index - 1).attr(AttributeEnum.TYPE);
					String memberName = token.attr(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitField(type, memberName);
					token.setAttr(AttributeEnum.TYPE, returnType);

				} else if (token.isInvokeMethod()) {
					IType type = stmt.get(index - 1).attr(AttributeEnum.TYPE);
					String memberName = token.attr(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitMethod(type, memberName, parameterTypes);
					token.setAttr(AttributeEnum.TYPE, returnType);

				} else if (token.isVisitArrayIndex()) {// what like ".str[0]"
					IType type = stmt.get(index - 1).attr(AttributeEnum.TYPE);
					String memberName = token.attr(AttributeEnum.MEMBER_NAME);
					IType returnType = linker.visitField(type, memberName);
					returnType = factory.create(returnType.getTargetName());
					token.setAttr(AttributeEnum.TYPE, returnType);
				}

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
