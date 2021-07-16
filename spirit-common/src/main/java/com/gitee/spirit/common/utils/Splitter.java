package com.gitee.spirit.common.utils;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.gitee.spirit.common.function.Function;

public class Splitter {

    public static <T> List<List<T>> split(List<T> list, Function.Matcher<T> matcher) {
        int[] indexes = CollUtil.indexOfAll(list, matcher::accept);
        List<List<T>> subLists = new ArrayList<>();
        int lastIndex = 0;
        for (int index : indexes) {
            if (index > lastIndex) {
                subLists.add(new ArrayList<>(list.subList(lastIndex, index)));
            }
            lastIndex = index + 1;
        }
        if (lastIndex != list.size()) {
            subLists.add(new ArrayList<>(list.subList(lastIndex, list.size())));
        }
        return subLists;
    }

    @SuppressWarnings("unchecked")
    public static <V, T> List<V> split(List<T> list, Function.Matcher<T> matcher, Function.Factory<List<T>> factory) {
        List<List<T>> subLists = split(list, matcher);
        List<V> anotherList = new ArrayList<>();
        subLists.forEach(subList -> anotherList.add((V) factory.accept(subList)));
        return anotherList;
    }

}
