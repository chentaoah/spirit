package com.gitee.spirit.output.java.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.compile.action.AbstractElementAction;
import com.gitee.spirit.core.compile.deduce.FragmentDeducer;
import com.gitee.spirit.core.compile.entity.ElementEvent;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;
import com.gitee.spirit.core.element.utils.StmtVisiter;
import com.gitee.spirit.output.java.utils.TreeUtils;

public abstract class AbstractTreeElementAction extends AbstractElementAction {

	public static final String START = "START";
	public static final String PREV_STATEMENT = "PREV_STATEMENT";
	public static final String PREV_TYPE = "PREV_TYPE";
	public static final String END = "END";
	public static final String NEXT_STATEMENT = "NEXT_STATEMENT";
	public static final String NEXT_TYPE = "NEXT_TYPE";

	@Autowired
	public FragmentDeducer deducer;

	@Override
	public void handle(ElementEvent event) {
		IClass clazz = event.clazz;
		Statement statement = event.element;
		StmtVisiter.visit(statement, stmt -> {
			for (int index = 0; index < stmt.size(); index++) {
				Token token = stmt.get(index);
				if (isTrigger(token)) {
					visit(clazz, stmt, index, token);
				}
			}
		});
	}

	public void visit(IClass clazz, Statement statement, int index, Token token) {
		Map<String, Object> context = new HashMap<>();
		visitPrev(clazz, statement, index, token, context);
		visitNext(clazz, statement, index, token, context);
	}

	public void visitPrev(IClass clazz, Statement statement, int index, Token token, Map<String, Object> context) {
		int start = TreeUtils.findStart(statement, index);
		Statement prevStatement = statement.subStmt(start, index);
		IType prevType = deducer.derive(clazz, prevStatement);
		context.put(START, start);
		context.put(PREV_STATEMENT, prevStatement);
		context.put(PREV_TYPE, prevType);
		doVisitPrev(clazz, statement, index, token, context);
	}

	public void visitNext(IClass clazz, Statement statement, int index, Token token, Map<String, Object> context) {
		int end = TreeUtils.findEnd(statement, index);
		Statement nextStatement = statement.subStmt(index + 1, end);
		IType nextType = deducer.derive(clazz, nextStatement);
		context.put(END, end);
		context.put(NEXT_STATEMENT, nextStatement);
		context.put(NEXT_TYPE, nextType);
		doVisitNext(clazz, statement, index, token, context);
	}

	public void doVisitPrev(IClass clazz, Statement statement, int index, Token token, Map<String, Object> context) {
		// ignore
	}

	public void doVisitNext(IClass clazz, Statement statement, int index, Token token, Map<String, Object> context) {
		// ignore
	}

	public abstract boolean isTrigger(Token currentToken);

}
