package com.gitee.spirit.common.utils;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;

public class ListUtils {

    @SafeVarargs
    public static <T> List<T> asListNonNull(T... items) {
        if (items == null || items.length == 0) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(items.length);
        for (T item : items) {
            if (item != null) {
                list.add(item);
            }
        }
        return list;
    }

    public static <T> int indexOf(List<T> list, int fromIndex, int toIndex, Matcher<T> matcher) {
        int step = toIndex >= fromIndex ? 1 : -1;
        for (int index = fromIndex; index != toIndex; index += step) {
            if (matcher.accept(list.get(index))) {
                return index;
            }
        }
        return -1;
    }

    public static <T> int seekIndexOf(List<T> list, int fromIndex, int toIndex, Matcher<T> matcher) {
        int index = indexOf(list, fromIndex, toIndex, item -> !matcher.accept(item));
        return index == -1 ? toIndex : index;
    }

    public static <T> int indexOf(List<T> list, int fromIndex, Matcher<T> matcher) {
        return indexOf(list, fromIndex, list.size(), matcher);
    }

    public static <T> int indexOf(List<T> list, Matcher<T> matcher) {
        return indexOf(list, 0, list.size(), matcher);
    }

    public static <T> int lastIndexOf(List<T> list, Matcher<T> matcher) {
        return indexOf(list, list.size() - 1, -1, matcher);
    }

    public static <T> T removeOne(List<T> list, Matcher<T> matcher) {
        int index = indexOf(list, matcher);
        return index >= 0 ? list.remove(index) : null;
    }

    public static <T> void removeAllByIndex(List<T> list, int fromIndex, int toIndex) {
        list.subList(fromIndex, toIndex).clear();
    }

    public static <T> T findOneByIndex(List<T> list, int fromIndex, int toIndex, Matcher<T> matcher) {
        int index = indexOf(list, fromIndex, toIndex, matcher);
        return index >= 0 ? list.get(index) : null;
    }

    public static <T> T findOne(Iterable<T> collection, Matcher<T> matcher) {
        return CollUtil.findOne(collection, matcher::accept);
    }

    public static <T> List<T> findAll(List<T> list, Matcher<T> matcher) {
        return CollUtil.filterNew(list, matcher::accept);
    }

    public static <T> List<T> seekAll(List<T> list, Matcher<T> matcher) {
        int index = seekIndexOf(list, 0, list.size(), matcher);
        List<T> view = list.subList(0, index);
        List<T> items = new ArrayList<>(view);
        view.clear();
        return items;
    }

    @SuppressWarnings("unchecked")
    public static <V, T> List<V> seekAll(List<T> list, Matcher<T> matcher, Factory<T> factory) {
        List<T> items = seekAll(list, matcher);
        List<V> list0 = new ArrayList<>();
        items.forEach(item -> list0.add((V) factory.accept(item)));
        return list0;
    }

    public static <T> void visit(List<T> list, Visitor<T> visitor) {
        for (int index = 0; index < list.size(); index++) {
            visitor.accept(index, list.get(index));
        }
    }

    public static <T> T findOneByScore(Iterable<T> collection, Selector<T> selector) {
        Integer maxScore = null;
        T finalItem = null;
        for (T item : collection) {
            Integer score = selector.accept(item);
            if (score != null) {
                Assert.isFalse(maxScore != null && maxScore.intValue() == score.intValue(), "The score cannot be the same!");
                if (maxScore == null || score > maxScore) {
                    maxScore = score;
                    finalItem = item;
                }
            }
        }
        return finalItem;
    }

    @SuppressWarnings("unchecked")
    public static <V, T> V collectOne(List<T> list, Factory<T> factory) {
        for (T item : list) {
            Object object = factory.accept(item);
            if (object != null) {
                return (V) object;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <V, T> V collectOne(List<T> list, Matcher<T> matcher, Factory<T> factory) {
        for (T item : list) {
            if (matcher.accept(item)) {
                return (V) factory.accept(item);
            }
        }
        return null;
    }

    public interface Matcher<T> {
        boolean accept(T t);
    }

    public interface Factory<T> {
        Object accept(T t);
    }

    public interface Visitor<T> {
        void accept(int index, T t);
    }

    public interface Selector<T> {
        Integer accept(T t);
    }

}
