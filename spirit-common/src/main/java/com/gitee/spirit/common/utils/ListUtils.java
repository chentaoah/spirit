package com.gitee.spirit.common.utils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.gitee.spirit.common.function.Function;

public class ListUtils {

    @SafeVarargs
    public static <T> List<T> asListNonNull(T... items) {
        Assert.notNull(items, "The parameter items cannot be null!");
        return Arrays.stream(items).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static <T> int indexOf(List<T> list, int fromIndex, int toIndex, Function.Matcher<T> matcher) {
        if (fromIndex < 0 || fromIndex >= list.size()) {
            return -1;
        }
        Assert.isTrue(toIndex >= -1 && toIndex <= list.size(), "The toIndex must be in range!");
        int step = toIndex >= fromIndex ? 1 : -1;
        for (int index = fromIndex; index != toIndex; index += step) {
            if (matcher.accept(list.get(index))) {
                return index;
            }
        }
        return -1;
    }

    public static <T> int indexOfUnmatched(List<T> list, int fromIndex, int toIndex, Function.Matcher<T> matcher) {
        int index = indexOf(list, fromIndex, toIndex, item -> !matcher.accept(item));
        return index == -1 ? toIndex : index;
    }

    public static <T> int indexOf(List<T> list, int fromIndex, Function.Matcher<T> matcher) {
        return indexOf(list, fromIndex, list.size(), matcher);
    }

    public static <T> int indexOf(List<T> list, Function.Matcher<T> matcher) {
        return indexOf(list, 0, list.size(), matcher);
    }

    public static <T> int lastIndexOf(List<T> list, Function.Matcher<T> matcher) {
        return indexOf(list, list.size() - 1, -1, matcher);
    }

    public static <T> T removeOne(List<T> list, Function.Matcher<T> matcher) {
        int index = indexOf(list, matcher);
        return index >= 0 ? list.remove(index) : null;
    }

    public static <T> void removeAllByIndex(List<T> list, int fromIndex, int toIndex) {
        list.subList(fromIndex, toIndex).clear();
    }

    public static <T> T findOneByIndex(List<T> list, int fromIndex, int toIndex, Function.Matcher<T> matcher) {
        int index = indexOf(list, fromIndex, toIndex, matcher);
        return index >= 0 ? list.get(index) : null;
    }

    public static <T> T findOne(Iterable<T> collection, Function.Matcher<T> matcher) {
        return CollUtil.findOne(collection, matcher::accept);
    }

    public static <T> List<T> findAll(List<T> list, Function.Matcher<T> matcher) {
        return CollUtil.filterNew(list, matcher::accept);
    }

    public static <T> List<T> seekAll(List<T> list, Function.Matcher<T> matcher) {
        int index = indexOfUnmatched(list, 0, list.size(), matcher);
        List<T> view = list.subList(0, index);
        List<T> items = new ArrayList<>(view);
        view.clear();
        return items;
    }

    @SuppressWarnings("unchecked")
    public static <V, T> List<V> seekAll(List<T> list, Function.Matcher<T> matcher, Function.Factory<T> factory) {
        List<T> items = seekAll(list, matcher);
        List<V> list0 = new ArrayList<>();
        items.forEach(item -> list0.add((V) factory.accept(item)));
        return list0;
    }

    public static <T> void visitAll(List<T> list, Function.Visitor<T> visitor) {
        for (int index = 0; index < list.size(); index++) {
            visitor.accept(index, list.get(index));
        }
    }

    public static <T> T findOneByScore(Iterable<T> iterable, Function.Scorer<T> scorer) {
        Integer maxScore = null;
        T finalItem = null;
        for (T item : iterable) {
            Integer score = scorer.accept(item);
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
    public static <V, T> V collectOne(List<T> list, Function.Consumer<T> consumer) {
        for (T item : list) {
            Object object = consumer.accept(item);
            if (object != null) {
                return (V) object;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <V, T> V collectOne(List<T> list, Function.Matcher<T> matcher, Function.Consumer<T> consumer) {
        for (T item : list) {
            if (matcher.accept(item)) {
                return (V) consumer.accept(item);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <V, T> List<V> collectAll(List<T> list, Function.Consumer<T> consumer) {
        List<V> list0 = new ArrayList<>();
        for (T item : list) {
            list0.add((V) consumer.accept(item));
        }
        return list0;
    }

    public static <T> void reverse(List<T> list, Consumer<T> consumer) {
        Collections.reverse(list);
        list.forEach(consumer);
    }

}
