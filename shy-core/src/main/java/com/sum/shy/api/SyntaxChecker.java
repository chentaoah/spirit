package com.sum.shy.api;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Line;

@Service(value = "syntax_checker", mockIfMissing = true)
public interface SyntaxChecker {

	void check(Line line);

}
