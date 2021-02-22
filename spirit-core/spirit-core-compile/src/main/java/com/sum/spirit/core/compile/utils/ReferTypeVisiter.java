package com.sum.spirit.core.compile.utils;

import com.sum.spirit.core.clazz.entity.IType;

public class ReferTypeVisiter extends TypeVisiter {

	public static final String REFER_KEY = "REFER";

	public IType visit(IType listable, IType referType, Consumer<VisitEvent<IType>> consumer) {
		VisitEvent<IType> event = new VisitEvent<>();
		event.put(REFER_KEY, referType);
		return visit(listable, consumer, event);
	}

	@Override
	public IType doProcess(Consumer<VisitEvent<IType>> consumer, VisitEvent<IType> event) {
		VisitEvent<IType> newEvent = new VisitEvent<>();
		newEvent.context.putAll(event.context);
		IType referType = newEvent.get(REFER_KEY);
		newEvent.put(REFER_KEY, referType.getGenericTypes().get(event.index));
		return visit(event.item, consumer, newEvent);
	}

}
