package com.sum.spirit.common.entity;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriorityNode<T> {

	public int priority;
	public T item;

	public static class PriorityComparator<T> implements Comparator<PriorityNode<T>> {
		@Override
		public int compare(PriorityNode<T> o1, PriorityNode<T> o2) {
			if (o2.priority == o1.priority) {
				return Convert.toInt(o1.item) - Convert.toInt(o2.item);
			} else {
				return Math.abs(o2.priority) - Math.abs(o1.priority);
			}
		}
	}

	public static void main(String[] args) {
		Queue<PriorityNode<Integer>> queue = new PriorityQueue<>(16, new PriorityNode.PriorityComparator<Integer>());
		queue.add(new PriorityNode<Integer>(55, 3));
		queue.add(new PriorityNode<Integer>(5, 1));
		queue.add(new PriorityNode<Integer>(55, 4));
		queue.add(new PriorityNode<Integer>(55, 2));
		while (!queue.isEmpty()) {
			PriorityNode<Integer> node = queue.poll();
			System.out.println(node);
		}
	}
}
