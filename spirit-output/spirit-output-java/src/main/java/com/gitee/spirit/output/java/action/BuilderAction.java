package com.gitee.spirit.output.java.action;

import cn.hutool.core.lang.Assert;
import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.TokenTypeEnum;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;
import com.gitee.spirit.core.element.utils.StmtVisitor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(-120)
public class BuilderAction extends AbstractExtElementAction {

    @Override
    public void visitElement(VisitContext context, Element element) {
        StmtVisitor.forEachToken(element, token -> {
            if (token.isTypeBuilder()) {//User{name="chen",age=18} => User.builder().name("chen").age(18).build()
                //append type name
                StringBuilder builder = new StringBuilder();
                builder.append((String) token.attr(Attribute.SIMPLE_NAME));
                //append .builder()
                builder.append(".builder()");
                //append .property()
                Statement statement = token.getValue();
                Statement subStatement = statement.subStmt("{", "}");
                List<Statement> subStatements = subStatement.splitStmt(",");
                for (Statement eachStmt : subStatements) {
                    if (eachStmt.contains("=")) {
                        List<Statement> eachStmts = eachStmt.splitStmt("=");
                        Assert.isTrue(eachStmts.size() == 2, "The size must be 2!");
                        String property = eachStmts.get(0).toString();
                        String value = eachStmts.get(1).toString();
                        builder.append(".").append(property).append("(").append(value).append(")");
                    }
                }
                //append .build()
                builder.append(".build()");
                statement.clear();
                statement.add(new Token(TokenTypeEnum.CUSTOM_EXPRESS, builder.toString()));
            }
        });
    }

}
