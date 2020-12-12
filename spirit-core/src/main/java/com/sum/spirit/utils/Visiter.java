package com.sum.spirit.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Visiter<L, T> {

	public void visit(L listable, Consumer<VisitEvent<T>> consumer) {
		VisitEvent<T> event = new VisitEvent<>();
		visit(listable, consumer, event);
	}

	public void visit(L listable, Consumer<VisitEvent<T>> consumer, VisitEvent<T> event) {
		prevProcess(listable, event.context);
		List<T> list = getListable(listable, event.context);
		event.listable = listable;
		for (int index = 0; index < list.size(); index++) {
			event.index = index;
			event.item = list.get(index);
			doProcess(consumer, event);
		}
	}

	public void prevProcess(L listable, Map<String, Object> context) {
		// ignore
	}

	public List<T> getListable(L listable, Map<String, Object> context) {
		return new ArrayList<T>();
	}

	public void doProcess(Consumer<VisitEvent<T>> consumer, VisitEvent<T> event) {
		L listable = getSubListable(event);
		if (listable != null) {
			VisitEvent<T> newEvent = new VisitEvent<>();
			newEvent.context = event.context;
			visit(listable, consumer, newEvent);
		}
		consumer.accept(event);
	}

	public L getSubListable(VisitEvent<T> event) {
		return null;
	}

	public static class VisitEvent<T> {
		public Map<String, Object> context = new HashMap<>();
		public Object listable;
		public int index = -1;
		public T item;

		public void put(String key, Object value) {
			context.put(key, value);
		}

		@SuppressWarnings("unchecked")
		public <V> V get(String key) {
			return (V) context.get(key);
		}
	}

}
