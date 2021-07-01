package com.gitee.spirit.common.function;

public class Function {

    public interface Matcher<T> {
        boolean accept(T t);
    }

    public interface Consumer<T> {
        Object accept(T t);
    }

    public interface Factory<T> {
        Object accept(T t);
    }

    public interface Visitor<T> {
        void accept(int index, T t);
    }

    public interface Scorer<T> {
        Integer accept(T t);
    }

}
