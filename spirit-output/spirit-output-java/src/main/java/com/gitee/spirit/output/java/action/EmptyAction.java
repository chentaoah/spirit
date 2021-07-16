package com.gitee.spirit.output.java.action;

import com.gitee.spirit.output.api.ImportManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.utils.StmtVisitor;
import com.gitee.spirit.stdlib.Emptys;

@Component
@Order(-100)
public class EmptyAction extends AbstractExtElementAction {

    @Autowired
    public ImportManager manager;

    @Override
    public void visitElement(VisitContext context, Element element) {
        StmtVisitor.forEachToken(element, token -> {
            if (token.isLocalMethod()) {// empty(str)
                if (KeywordEnum.EMPTY.value.equals(token.attr(Attribute.MEMBER_NAME))) {
                    manager.addStaticImport(context.clazz, Emptys.class.getName() + ".empty");
                }
            }
        });
    }

}
