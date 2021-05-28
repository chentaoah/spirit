package com.gitee.spirit.core.api;

import java.util.List;

import com.gitee.spirit.common.enums.SyntaxEnum;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.SyntaxResult;
import com.gitee.spirit.core.element.entity.SyntaxTree;
import com.gitee.spirit.core.element.entity.Token;

public interface SyntaxParser {

	SyntaxResult parseSyntax(List<Token> tokens, Statement statement);

	SyntaxEnum getSyntaxWithoutTree(List<Token> tokens);

	SyntaxEnum getSyntaxWithTree(List<Token> tokens);

	SyntaxEnum getSyntaxByTree(SyntaxTree syntaxTree);

}
