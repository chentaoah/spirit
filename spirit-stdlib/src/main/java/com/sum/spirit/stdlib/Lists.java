package com.sum.spirit.stdlib;

import java.util.ArrayList;
import java.util.List;

public class Lists {

	public static <E> List<E> newArrayList() {
		return new ArrayList<>();
	}

	public static <E> List<E> of() {
		return newArrayList();
	}

	public static <E> List<E> of(E e1) {
		List<E> list = of();
		list.add(e1);
		return list;
	}

	public static <E> List<E> of(E e1, E e2) {
		List<E> list = of();
		list.add(e1);
		list.add(e2);
		return list;
	}

	public static <E> List<E> of(E e1, E e2, E e3) {
		List<E> list = of();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		return list;
	}

	public static <E> List<E> of(E e1, E e2, E e3, E e4) {
		List<E> list = of();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		return list;
	}

	public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5) {
		List<E> list = of();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		return list;
	}

	public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
		List<E> list = of();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		list.add(e6);
		return list;
	}

	public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
		List<E> list = of();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		list.add(e6);
		list.add(e7);
		return list;
	}

	public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
		List<E> list = of();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		list.add(e6);
		list.add(e7);
		list.add(e8);
		return list;
	}

}
