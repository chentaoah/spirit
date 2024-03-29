
//雪花分片实现
class IdWorker {
	//开始时间截 (2015-01-01)
	twepoch = 1420041600000L
   	//机器id所占的位数
 	workerIdBits = 5L
   	//数据标识id所占的位数
 	datacenterIdBits = 5L
   	//支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
 	maxWorkerId = -1L ^ (-1L << workerIdBits)
   	//支持的最大数据标识id，结果是31
 	maxDatacenterId = -1L ^ (-1L << datacenterIdBits)
   	//序列在id中占的位数
 	sequenceBits = 12L
   	//机器ID向左移12位
 	workerIdShift = sequenceBits
   	//数据标识id向左移17位(12+5)
 	datacenterIdShift = sequenceBits + workerIdBits
   	//时间截向左移22位(5+5+12)
 	timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits
   	//生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
 	sequenceMask = -1L ^ (-1L << sequenceBits)
   	//工作机器ID(0~31)
 	long workerId
	//数据中心ID(0~31)
 	long datacenterId
	//毫秒内序列(0~4095)
 	sequence = 0L
	//上次生成ID的时间截
 	lastTimestamp = -1L

	func IdWorker(long workerId, long datacenterId){
		if workerId > maxWorkerId || workerId < 0 {
			message = String.format("worker id can't be greater than %d or less than 0", maxWorkerId)
			throw IllegalArgumentException(message)
		}
		if datacenterId > maxDatacenterId || datacenterId < 0 {
			message = String.format("datacenter id can't be greater than %d or less than 0", maxDatacenterId)
			throw IllegalArgumentException(message)
		}	
		this.workerId = workerId
		this.datacenterId = datacenterId
	}

	synch func nextId(){
		timestamp = timeGen()
		if timestamp < lastTimestamp {
			message = String.format("Clock moved backwards.Refusing to generate id for %d milliseconds", lastTimestamp - timestamp)
			throw RuntimeException(message)
		}
		if timestamp == lastTimestamp {
			sequence = (sequence + 1) & sequenceMask
			if sequence == 0 : timestamp = tilNextMillis(lastTimestamp)
		} else {
			sequence = 0L
		}
		lastTimestamp = timestamp
		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence
	}

	func tilNextMillis(long lastTimestamp){
		timestamp = timeGen()
		while timestamp <= lastTimestamp : timestamp = timeGen()
		return timestamp
	}

	func timeGen(){
		return System.currentTimeMillis()
	}

}
