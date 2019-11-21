package com.sum.shy.core.converter;

import java.util.List;

import com.sum.shy.core.analyzer.FastDerivator;
import com.sum.shy.core.analyzer.VariableTracker;
import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Variable;

public class AssignConverter extends AbsConverter {

	@Override
	public int convert(StringBuilder sb, String block, String indent, CtClass clazz, CtMethod method, List<Line> lines,
			int index, Line line, Stmt stmt) {
		// 直接校验
		VariableTracker.track(clazz, method, block, line, stmt);

		// 如果是单纯的变量,而不是成员变量,则需要进行类型声明
		Token token = stmt.getToken(0);
		if (token.isVar() && token.getTypeAtt() == null) {
			// 如果没有,则在最前面追加类型
			Type type = FastDerivator.getType(clazz, stmt);
			token.setTypeAtt(type);
			method.addVariable(new Variable(block, type, (String) token.value));
			stmt.tokens.add(0, new Token(Constants.TYPE_TOKEN, type.toString(), null));
		}

		// 将语句进行一定的转换
		sb.append(indent + convertStmt(clazz, stmt) + ";\n");
		return 0;

	}

}
