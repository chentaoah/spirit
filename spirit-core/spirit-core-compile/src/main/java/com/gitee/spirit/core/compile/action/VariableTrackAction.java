package com.gitee.spirit.core.compile.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.core.api.VariableTracker;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.utils.StmtVisiter;

import cn.hutool.core.lang.Assert;

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
				if (token.isVariable() || (token.isKeyword() && KeywordEnum.isKeywordVariable(token.getValue()))) {
					String variableName = token.toString();
					IType type = tracker.findVariableType(context, variableName);
					Assert.notNull(type, "Variable must be declared!variableName:" + variableName);
					token.setAttr(Attribute.TYPE, type);
				}
			});
		});
	}

}
