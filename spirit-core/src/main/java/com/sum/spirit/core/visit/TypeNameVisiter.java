package com.sum.spirit.core.visit;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.sum.spirit.pojo.common.IType;

public class TypeNameVisiter extends Visiter<IType, IType> {

	public static final String NAME_KEY = "NAME";

	public String visitName(IType listable, Consumer<VisitEvent<IType>> consumer) {
		VisitEvent<IType> event = new VisitEvent<>();
		visit(listable, consumer, event);
		return event.get(NAME_KEY);
	}

	@Override
	public IType prevProcess(IType listable, Consumer<VisitEvent<IType>> consumer, VisitEvent<IType> event) {
		event.item = listable;
		String finalName = (String) consumer.accept(event);
		if (finalName == null) {
			finalName = listable.toString();
		}
		event.put(NAME_KEY, finalName);
		return listable;
	}

	@Override
	public List<IType> getListable(IType listable, Consumer<VisitEvent<IType>> consumer, VisitEvent<IType> event) {
		if (listable.isGenericType()) {
			String finalName = event.get(NAME_KEY);
			List<String> strs = new ArrayList<>();
			List<IType> genericTypes = listable.getGenericTypes();
			for (int index = 0; index < genericTypes.size(); index++) {
				strs.add(visitName(genericTypes.get(index), consumer));
			}
			finalName = finalName + "<" + Joiner.on(", ").join(strs) + ">";
			event.put(NAME_KEY, finalName);
		}
		return null;
	}

}
