package com.sum.spirit.utils;

public class Holder<T> {

	public T obj;

	public T get() {
		return obj;
	}

	public void set(T obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		throw new RuntimeException("Holder can`t get string!");
	}

}
