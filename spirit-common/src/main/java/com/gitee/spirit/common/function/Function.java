package com.gitee.spirit.common.function;

public interface Function {

    interface Matcher<T> {
        boolean accept(T t);
    }

    interface Consumer<T> {
        Object accept(T t);
    }

    interface Factory<T> {
        Object accept(T t);
    }

    interface Visitor<T> {
        void accept(int index, T t);
    }

    interface Scorer<T> {
        Integer accept(T t);
    }

}
