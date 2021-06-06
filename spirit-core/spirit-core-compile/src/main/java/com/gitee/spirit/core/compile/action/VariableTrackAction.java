package com.gitee.spirit.core.compile.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.compile.deduce.VariableTracker;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.utils.StmtVisiter;

@Component
@Order(-60)
public class VariableTrackAction extends AbstractAppElementAction {

	@Autowired
	public VariableTracker tracker;

	@Override
	public void visitElement(VisitContext context, Element element) {
		StmtVisiter.visit(element, stmt -> {
			stmt.forEach(token -> {
				if (token.attr(Attribute.TYPE) != null) {
					return;
				}
				if (token.isVariable()) {// variable
					String variableName = token.toString();
					IType type = tracker.getVariableType(context.clazz, context, variableName);
					token.setAttr(Attribute.TYPE, type);

				} else if (token.isKeyword() && KeywordEnum.isKeywordVariable(token.getValue())) {
					String variableName = token.toString();
					IType type = tracker.findTypeByKeyword(context.clazz, variableName);
					token.setAttr(Attribute.TYPE, type);
				}
			});
		});
	}

}
