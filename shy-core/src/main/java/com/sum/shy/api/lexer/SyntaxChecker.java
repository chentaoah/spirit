package com.sum.shy.api.lexer;

import com.sum.pisces.api.annotation.Service;
import com.sum.shy.pojo.element.Line;

@Service(value = "syntax_checker", mockIfMissing = true)
public interface SyntaxChecker {

	void check(Line line);

}
