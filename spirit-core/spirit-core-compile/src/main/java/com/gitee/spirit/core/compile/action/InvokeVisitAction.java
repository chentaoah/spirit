package com.gitee.spirit.core.compile.action;

import java.util.ArrayList;
import java.util.List;

import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.clazz.utils.CommonTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.api.StatementDeducer;
import com.gitee.spirit.core.api.TypeFactory;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;
import com.gitee.spirit.core.element.utils.StmtVisitor;

@Component
@Order(-40)
public class InvokeVisitAction extends AbstractAppElementAction {

    @Autowired
    public TypeFactory factory;
    @Autowired
    public StatementDeducer deducer;
    @Autowired
    public ClassLinker linker;

    @Override
    public void visitElement(VisitContext context, Element element) {
        StmtVisitor.forEachStmt(element, statement -> {
            for (int index = 0; index < statement.size(); index++) {
                try {
                    Token token = statement.get(index);
                    if (token.attr(Attribute.TYPE) != null) {
                        continue;
                    }

                    List<IType> parameterTypes = token.isInvoke() ? getParameterTypes(token) : null;

                    if (token.isTypeSmartBuilder()) {
                        token.setAttr(Attribute.TYPE, CommonTypes.NULL);

                    } else if (token.isType() || token.isArrayInit() || token.isTypeInit()
                            || token.isTypeBuilder() || token.isCast() || token.isLiteral()) {
                        token.setAttr(Attribute.TYPE, factory.create(context.clazz, token));

                    } else if (token.isSubexpress()) {
                        Statement subStatement = token.getValue();
                        token.setAttr(Attribute.TYPE, deducer.derive(subStatement.subStmt("(", ")")));

                    } else if (token.isVisitField()) {
                        IType type = statement.get(index - 1).attr(Attribute.TYPE);
                        String memberName = token.attr(Attribute.MEMBER_NAME);
                        IType returnType = linker.visitField(type, memberName);
                        token.setAttr(Attribute.TYPE, returnType);

                    } else if (token.isLocalMethod()) {
                        String memberName = token.attr(Attribute.MEMBER_NAME);
                        IType type = context.clazz.getType().withPrivate();
                        IType returnType = linker.visitMethod(type, memberName, parameterTypes);
                        token.setAttr(Attribute.TYPE, returnType);
                        //for smart builder
                        parameterTypes = linker.getParameterTypes(type, memberName, parameterTypes);
                        setTypeForSmartBuilder(token, parameterTypes);

                    } else if (token.isVisitMethod()) {
                        IType type = statement.get(index - 1).attr(Attribute.TYPE);
                        String memberName = token.attr(Attribute.MEMBER_NAME);
                        IType returnType = linker.visitMethod(type, memberName, parameterTypes);
                        token.setAttr(Attribute.TYPE, returnType);
                        //for smart builder
                        parameterTypes = linker.getParameterTypes(type, memberName, parameterTypes);
                        setTypeForSmartBuilder(token, parameterTypes);

                    } else if (token.isVisitIndex()) {// what like "[0]"
                        IType type = statement.get(index - 1).attr(Attribute.TYPE);
                        type = type.toTarget();// 转换数组类型为目标类型
                        token.setAttr(Attribute.TYPE, type);
                    }

                } catch (NoSuchFieldException | NoSuchMethodException e) {
                    throw new RuntimeException("Link failed for class member!", e);
                }
            }
        });
    }

    public List<IType> getParameterTypes(Token token) {
        List<IType> parameterTypes = new ArrayList<>();
        Statement statement = token.getValue();
        if (statement.size() > 3) {
            List<Statement> subStatements = statement.subStmt(2, statement.size() - 1).splitStmt(",");
            for (Statement subStatement : subStatements) {
                IType parameterType = deducer.derive(subStatement);
                parameterTypes.add(parameterType);
            }
        }
        return parameterTypes;
    }

    public void setTypeForSmartBuilder(Token token, List<IType> parameterTypes) {
        if (!parameterTypes.isEmpty()) {
            if (ListUtils.findOne(parameterTypes, parameterType -> parameterType == CommonTypes.NULL) != null) {
                Statement statement = token.getValue();
                List<Statement> subStatements = statement.subStmt(2, statement.size() - 1).splitStmt(",");
                //TODO 这里添加逻辑
            }
        }
    }

}
