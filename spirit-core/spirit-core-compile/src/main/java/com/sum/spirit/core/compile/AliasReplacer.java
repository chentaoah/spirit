package com.sum.spirit.core.compile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.Import;
import com.sum.spirit.core.lexer.AliasLexer;

@Component
public class AliasReplacer {

	@Autowired
	public AliasLexer lexer;

	public String replace(IClass clazz, String code) {
		for (Import imp : clazz.getAliasImports()) {
			code = lexer.replace(code, imp.getAlias(), imp.getClassName());
		}
		return code;
	}

}
