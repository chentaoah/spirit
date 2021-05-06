package com.sum.spirit.core.api;

import java.util.List;

import com.sum.spirit.common.enums.SyntaxEnum;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.SyntaxResult;
import com.sum.spirit.core.element.entity.SyntaxTree;
import com.sum.spirit.core.element.entity.Token;

public interface SyntaxParser {

	SyntaxResult parseSyntax(List<Token> tokens, Statement statement);

	SyntaxEnum getSyntaxWithoutTree(List<Token> tokens);

	SyntaxEnum getSyntaxWithTree(List<Token> tokens);

	SyntaxEnum getSyntaxByTree(SyntaxTree syntaxTree);

}
