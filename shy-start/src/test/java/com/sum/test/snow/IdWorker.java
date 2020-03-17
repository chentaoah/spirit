package com.sum.test.snow;

public class IdWorker {

	public long twepoch = 1420041600000L;

	public long workerIdBits = 5L;

	public long datacenterIdBits = 5L;

	public long maxWorkerId = - 1L ^ (- 1L << workerIdBits);

	public long maxDatacenterId = - 1L ^ (- 1L << datacenterIdBits);

	public long sequenceBits = 12L;

	public long workerIdShift = sequenceBits;

	public long datacenterIdShift = sequenceBits + workerIdBits;

	public long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

	public long sequenceMask = - 1L ^ (- 1L << sequenceBits);

	public long workerId;

	public long datacenterId;

	public long sequence = 0L;

	public long lastTimestamp = - 1L;

	public IdWorker(long workerId, long datacenterId) {
		if(workerId > maxWorkerId || workerId < 0) {
			String message = String.format("worker id can't be greater than %d or less than 0", maxWorkerId);
			throw new IllegalArgumentException(message);
		}
		if(datacenterId > maxDatacenterId || datacenterId < 0) {
			String message = String.format("datacenter id can't be greater than %d or less than 0", maxDatacenterId);
			throw new IllegalArgumentException(message);
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public synchronized long nextId() {
		long timestamp = timeGen();
		if(timestamp < lastTimestamp) {
			String message = String.format("Clock moved backwards.refusing to generate id for %d milliseconds", lastTimestamp - timestamp);
			throw new RuntimeException(message);
		}
		if(timestamp == lastTimestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if(sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}
		lastTimestamp = timestamp;
		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
	}

	public long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while(timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	public long timeGen() {
		return System.currentTimeMillis();
	}

}
