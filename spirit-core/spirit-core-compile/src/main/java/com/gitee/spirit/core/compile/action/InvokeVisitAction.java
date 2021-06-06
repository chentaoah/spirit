package com.gitee.spirit.core.compile.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.api.TypeFactory;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.compile.FragmentDeducer;
import com.gitee.spirit.core.compile.AppTypeDerivator;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;
import com.gitee.spirit.core.element.utils.StmtVisiter;

@Component
@Order(-40)
public class InvokeVisitAction extends AbstractAppElementAction {

	@Autowired
	public TypeFactory factory;
	@Autowired
	public FragmentDeducer deducer;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public AppTypeDerivator derivator;

	@Override
	public void visitElement(VisitContext context, Element element) {
		IClass clazz = context.clazz;
		StmtVisiter.visit(element, stmt -> {
			for (int index = 0; index < stmt.size(); index++) {
				try {
					Token token = stmt.get(index);
					if (token.attr(Attribute.TYPE) != null) {
						continue;
					}
					List<IType> parameterTypes = token.isInvoke() ? getParameterTypes(clazz, token) : null;
					if (token.isType() || token.isArrayInit() || token.isTypeInit() || token.isCast()
							|| token.isLiteral()) {
						token.setAttr(Attribute.TYPE, factory.create(clazz, token));

					} else if (token.isSubexpress()) {
						Statement subStatement = token.getValue();
						token.setAttr(Attribute.TYPE, deducer.derive(clazz, subStatement.subStmt("(", ")")));

					} else if (token.isLocalMethod()) {
						String memberName = token.attr(Attribute.MEMBER_NAME);
						IType returnType = linker.visitMethod(derivator.withThisModifiers(clazz.getType()), memberName,
								parameterTypes);
						token.setAttr(Attribute.TYPE, returnType);

					} else if (token.isVisitField()) {
						IType type = stmt.get(index - 1).attr(Attribute.TYPE);
						String memberName = token.attr(Attribute.MEMBER_NAME);
						IType returnType = linker.visitField(type, memberName);
						token.setAttr(Attribute.TYPE, returnType);

					} else if (token.isVisitMethod()) {
						IType type = stmt.get(index - 1).attr(Attribute.TYPE);
						String memberName = token.attr(Attribute.MEMBER_NAME);
						IType returnType = linker.visitMethod(type, memberName, parameterTypes);
						token.setAttr(Attribute.TYPE, returnType);

					} else if (token.isVisitIndex()) {// what like "[0]"
						IType type = stmt.get(index - 1).attr(Attribute.TYPE);
						type = derivator.toTarget(type);// 转换数组类型为目标类型
						token.setAttr(Attribute.TYPE, type);
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
