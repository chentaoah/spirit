package com.sum.spirit.api.lexer;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.element.Line;

@Service(value = "syntax_checker", mockIfMissing = true)
public interface SyntaxChecker {

	void check(Line line);

}
