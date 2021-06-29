package com.gitee.spirit.core.compile.derivator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.api.StatementDeducer;
import com.gitee.spirit.core.api.TreeBuilder;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.utils.CommonTypes;
import com.gitee.spirit.core.element.entity.Node;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;
import com.gitee.spirit.core.element.utils.NodeVisiter;

import cn.hutool.core.lang.Assert;

@Component
public class DefaultStatementDeducer implements StatementDeducer {

    @Autowired
    public TreeBuilder builder;

    @Override
    public IType derive(Statement statement) {
        List<Node> nodes = builder.buildNodes(statement);
        IType type = (IType) NodeVisiter.forEachNode(nodes, node -> {
            Token token = node.token;
            if (token.attr(Attribute.TYPE) != null) {
                return token.attr(Attribute.TYPE);
            }
            if (token.isLogical() || token.isRelation() || token.isInstanceof()) {
                return CommonTypes.BOOLEAN;

            } else if (token.isArithmetic() || token.isBitwise()) {
                return ListUtils.asListNonNull(node.prev, node.next);
            }
            return null;
        });
        Assert.notNull(type, "Type cannot be null!");
        return type;
    }

}
