----------
12/19/2016 9:50:10 PM 

----------
# Queues #

1. **What** is a Queue?
	-  一种数据结构
	-  FIFO/LILO
	-  EnQueue/DeQueue
	-  underflow
	-  overflow

2. **How** are Queues Used?
	- 排队买票：一个买完，下一个跟上。
	- 适用于根据数据到来的先后顺序进行数据处理的场景
3. **Queue ADT**   
	- enQueue(Object data) 添加数据到队列尾部
	- deQueue() 删除队列头部数据
	- Object Front() 获取队列头部元素
	- int QueueSize() 队列长度
	- boolean IsEmptyQueue() 判断队列是否为空  
4. **Exceptions**
	- Empty Queue Exception
	- Full Queue Exception
5. **Applications**
	- Direct Applications
		- 操作系统根据任务到来的顺序进行任务调度（同等优先级），如打印队列
		- 售票窗口排队
		- MultiProgramming
		- 异步数据传输（文件IO，管道，套接字）（数据队列）
		- 确定呼叫中心的顾客等待时间（根据顾客排队长度）
		- 确定超市收银员的个数（根据顾客排队长度）
	- Indirect Applications
		- 算法的辅助数据结构
		- 其他数据结构的组成元素
6. **Implementations**
	- 基于简单循环数组的实现
	- 基于动态循环数组的实现
	- 基于链表的实现
7. **Why** Circular Arrays?
	- 简单数组空间利用率不高
		- 经过多次插入删除后，数组头部为空，空间浪费。使用循环数组可以利用这部分被浪费的空间

8. **Code Demo**

A. FixedSizeArrayQueue
```java

```