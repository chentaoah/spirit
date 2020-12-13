package com.sum.spirit.core.visit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Visiter<L, T> {

	public L visit(L listable, Consumer<VisitEvent<T>> consumer) {
		VisitEvent<T> event = new VisitEvent<>();
		return visit(listable, consumer, event);
	}

	public L visitVoid(L listable, java.util.function.Consumer<VisitEvent<T>> consumer) {
		return visit(listable, event -> {
			consumer.accept(event);
			return null;
		});
	}

	public L visit(L listable, Consumer<VisitEvent<T>> consumer, VisitEvent<T> event) {
		if (listable == null) {
			return listable;
		}
		event.listable = listable;
		listable = prevProcess(listable, consumer, event);
		List<T> list = getListable(listable, consumer, event);
		if (list != null && !list.isEmpty()) {
			for (int index = 0; index < list.size(); index++) {
				event.index = index;
				event.item = list.get(index);
				T item = doProcess(consumer, event);
				if (item != null) {
					list.set(index, item);
				}
			}
		}
		listable = postProcess(listable, list);
		return listable;
	}

	public L prevProcess(L listable, Consumer<VisitEvent<T>> consumer, VisitEvent<T> event) {
		return listable;
	}

	public List<T> getListable(L listable, Consumer<VisitEvent<T>> consumer, VisitEvent<T> event) {
		return new ArrayList<T>();
	}

	@SuppressWarnings("unchecked")
	public T doProcess(Consumer<VisitEvent<T>> consumer, VisitEvent<T> event) {
		L listable = getSubListable(event);
		if (listable != null) {
			VisitEvent<T> newEvent = new VisitEvent<>();
			newEvent.context = event.context;
			visit(listable, consumer, newEvent);
		}
		return (T) consumer.accept(event);
	}

	public L getSubListable(VisitEvent<T> event) {
		return null;
	}

	public L postProcess(L listable, List<T> list) {
		return listable;
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

	public static interface Consumer<T> {
		Object accept(T t);
	}

}
