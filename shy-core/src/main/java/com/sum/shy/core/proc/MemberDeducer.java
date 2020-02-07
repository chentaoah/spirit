package com.sum.shy.core.proc;

import java.util.Map;

import com.sum.shy.core.clazz.AbsMember;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.type.api.IType;

public class MemberDeducer {

	public static void derive(Map<String, IClass> allClasses) {
		for (IClass clazz : allClasses.values()) {
			for (AbsMember member : clazz.getAllMembers())
				member.setType(visitMember(clazz, member));
		}
	}

	public static IType visitMember(IClass clazz, AbsMember member) {
		// 上锁
//		member.lock();
//		IType type = member.getType();
//		if (type == null) {
//			if (member instanceof IField) {
//				Stmt stmt = ((IField) member).stmt;
//				if (stmt != null && stmt.isAssign()) {
//					VariableTracker.trackStmt(clazz, null, null, stmt.line, stmt.subStmt(2, stmt.size()));
//					InvokeVisiter.visitStmt(clazz, stmt);
//					type = FastDerivator.deriveStmt(clazz, stmt);
//				}
//			} else if (member instanceof IMethod) {// 如果是方法
//
//				Holder<IType> holder = new Holder<>(new CodeType(clazz, Constants.VOID_TYPE));
//				MethodResolver.resolve(clazz, (IMethod) member, new Handler() {
//					@Override
//					public Object handle(IClass clazz, IMethod method, String indent, String block, Line line,
//							Stmt stmt) {
//						// 有效返回，才是返回
//						if (stmt.isReturn()) {
//							IType returnType = FastDerivator.deriveStmt(clazz, stmt.subStmt(1, stmt.size()));
//							if (holder.obj.isVoid() || holder.obj.isObj()) {
//								if (returnType != null)
//									holder.obj = returnType;
//							}
//						}
//						return null;
//					}
//				});
//				type = holder.obj;
//
//			}
//
//		}
		// 解锁
		member.unLock();
		return null;
	}

}
