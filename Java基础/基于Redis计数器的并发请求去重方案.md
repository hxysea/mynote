----------
7/21/2017 6:07:57 PM 

----------
1. **缘起**
业务逻辑为M服务器向中央服务器C上报数据，每条数据使用uid标识，即M服务器-->uid数据-->C服务器。基于超时重发机制，M服务器可能会向C服务器发送重复数据，故C服务器需要做去重处理。
2. **问题分析**
脱离业务场景，这就是一个并发访问的问题，这也是解决此问题的出发点。解决并发问题的核心思想就是将异步控制为同步，想要对代码逻辑做同步控制就要使用同步机制，通常使用的就是锁机制了。而锁机制的本质就是保证代码逻辑执行的原子性，即对于同一条数据的入库请求判断，要做重复判断，进而解决并发请求的问题。具体到此业务，对于同一个uid标识的数据，只入库一条。故问题转换过程为：
**重复数据上传 --> 并发访问去重 --> 同步机制控制代码访问逻辑 --> 锁机制/同步块/redis计数器**
3. 问题解决
- 基于redis计数器并发控制
	- 利用rdis的原子性操作，就是当一个请求在操作某个key时，其它操作同一个key的请求必须等待，即利用原子性实现了代码控制
	- 每次请求进来都会在对应kye上加1，只有第一次请求是1，故一分钟以内的其它重复请求，不做业务处理
```java
public boolean filterRepeatMessage(MqttMessage message) {
	boolean needContinueHandle = false;
	String key = "MSG_" + message.getData().getString("uid");
	long count = redisTemplate.opsForValue().increment(key, 1);
	if (count == 1) {
		redisTemplate.expire(key, 60, TimeUnit.SECONDS);
		needContinueHandle = true;
	}
	//日志记录
	return needContinueHandle;

}
```

- 基于同步方法的并发控制
	- 所有请求同步，效率低
```java
public synchronized boolean filterRepeatMessage(MqttMessage message) {
	boolean needContinueHandle = false;
	//查询数据库中是否有重复记录
	if (has) {
		needContinueHandle = true;
	}
	//日志记录
	return needContinueHandle;

}
```

- 基于uid分段锁的并发控制
	- 所有请求同步，效率低
```java

private static ConcurrentHashMap<Long, Byte[]> lockerStore = new ConcurrentHashMap<Long, Byte[]>();

private static Object getLock(String uid) {
        lockerStore.putIfAbsent(uid, new Byte[]{});
        Byte[] ret = lockerStore.get(phone);
        return ret;
}
public boolean filterRepeatMessage(MqttMessage message) {
	boolean needContinueHandle = false;
	synchronized(getLock( message.getData().getString("uid"))){
		//查询数据库中是否有重复记录
		if (has) {
			needContinueHandlae = true;
		}
	}
	//日志记录
	return needContinueHandle;

}
```
**PS：**
实际中，可根据业务需求使用缓存来替代数据库查询。综合比对，使用redis计数器解决并发请求去重的方案效率更高，使用更方便，推荐使用。

