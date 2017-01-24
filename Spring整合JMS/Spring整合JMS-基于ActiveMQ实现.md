----------
1/18/2017 9:28:09 AM 

----------
## Spring整合JMS——基于ActiveMQ实现 ##
1. JMS (Message Oriented Middleware，MOM)
	- 全称：Java Message Service
	- 消息传递方式
		- 点对点 e.g.,**ActiveMQQueue**
			- 一个生产者一个消费者一一对应
		- 发布/订阅 e.g.,**ActiveMQTopic**
			- 一个生产者产生消息并发送后，多个消费者进行接收
2. Spring整合JMS（以ActiveMQ为例）
	- ConnectionFactory
		- SingleConnectionFactory
			- 一直返回同一个链接
		- CachingConnectionFactory
			- 继承SingleConnectionFactory
			- 缓存Session、MessageProducer、MessageConsumer
	- failover transport是一种重新连接的机制
		- 建立可靠的传输，允许定义多个符合的URI
		- 自动选择其中的一个URI来尝试建立连接，若不成功，则选择其它的URI建立一个新的连接
		- e.g., failover:(tcp://primary:61616,tcp://secondary:61616)?randomize=false
	- JmsTemplate 同步收发消息
	- MessageListenerContainer 异步收发消息
		- DefaultMessageListenerContainer
		- SimpleMessageListenerContainer
	- MessageListener
	- SessionAwareMessageListener
	- MessageListenerAdapter