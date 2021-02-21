package com.sum.spirit.core.visiter.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.utils.TypeBuilder;

public class TypeVisiter extends Visiter<IType, IType> {

	@Override
	public IType prevProcess(IType listable, Consumer<VisitEvent<IType>> consumer, VisitEvent<IType> event) {
		listable = TypeBuilder.copy(listable);// 拷贝一份
		event.item = listable;
		IType returnType = (IType) consumer.accept(event);// 触发一次
		return returnType != null ? returnType : listable;
	}

	@Override
	public List<IType> getListable(IType listable, Consumer<VisitEvent<IType>> consumer, VisitEvent<IType> event) {
		if (listable.isGenericType()) {
			return new ArrayList<>(listable.getGenericTypes());// 注意，泛型集合是不可修改的
		}
		return new ArrayList<>();
	}

	@Override
	public IType doProcess(Consumer<VisitEvent<IType>> consumer, VisitEvent<IType> event) {
		VisitEvent<IType> newEvent = new VisitEvent<>();
		newEvent.context = event.context;
		return visit(event.item, consumer, newEvent);
	}

	@Override
	public IType postProcess(IType listable, List<IType> list) {
		if (listable.isGenericType()) {
			listable.setGenericTypes(Collections.unmodifiableList(list));
		}
		return listable;
	}

}
