package com.gitee.spirit.output.java.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gitee.spirit.core.api.StatementDeducer;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;
import com.gitee.spirit.core.element.utils.StmtVisitor;
import com.gitee.spirit.output.java.utils.TreeUtils;

public abstract class AbstractTreeElementAction extends AbstractExtElementAction {

	public static final String START = "START";
	public static final String PREV_STATEMENT = "PREV_STATEMENT";
	public static final String PREV_TYPE = "PREV_TYPE";
	public static final String END = "END";
	public static final String NEXT_STATEMENT = "NEXT_STATEMENT";
	public static final String NEXT_TYPE = "NEXT_TYPE";

	@Autowired
	public StatementDeducer deducer;

	@Override
	public void visitElement(VisitContext context, Element element) {
		StmtVisitor.forEachStmt(element, statement -> {
			for (int index = 0; index < statement.size(); index++) {
				Token token = statement.get(index);
				if (isTrigger(token)) {
					visit(context.clazz, statement, index, token);
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
		IType prevType = deducer.derive(prevStatement);
		context.put(START, start);
		context.put(PREV_STATEMENT, prevStatement);
		context.put(PREV_TYPE, prevType);
		doVisitPrev(clazz, statement, index, token, context);
	}

	public void visitNext(IClass clazz, Statement statement, int index, Token token, Map<String, Object> context) {
		int end = TreeUtils.findEnd(statement, index);
		Statement nextStatement = statement.subStmt(index + 1, end);
		IType nextType = deducer.derive(nextStatement);
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
