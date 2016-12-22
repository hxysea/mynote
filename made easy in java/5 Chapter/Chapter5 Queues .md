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
8. **How** to implement it?
	- 定义数组存储队列数据
	- 定义队列头引用
	- 定义队列尾引用
	- 定义入队方法
	- 定义出队方法
	- 定义获取队列头部元素的方法
	- 定义获取队列大小的方法
	- 定义队列是否为空的方法 
9. **Code Demo**
	
	A. FixedSizeArrayQueue
	```java
	package com.abuge.chapter5;
	
	/**
	 * Created by AbuGe on 2016/12/22.
	 */
	public class FixedSizeArrayQueue {
	    private int[] queueRep;
	    private int front;
	    private int rear;
	    private int size;
	
	    private static final int CAPACITY = 16;
	
	    public FixedSizeArrayQueue() {
	        queueRep = new int[CAPACITY];
	        front = 0;
	        rear = 0;
	        size = 0;
	    }
	
	    public FixedSizeArrayQueue(int capacity) {
	        queueRep = new int[capacity];
	        front = 0;
	        rear = 0;
	        size = 0;
	    }
	
	    public void enQueue(int data) {
	        int capacity = queueRep.length;
	        if (size == capacity) {
	            throw new IllegalStateException("Queue is full:Overflow");
	        } else {
	            size++;
	            queueRep[rear] = data;
	            rear = (rear + 1) % capacity;
	        }
	    }
	
	    public int deQueue() {
	        int capacity = queueRep.length;
	        if (0 == size) {
	            throw new IllegalStateException("Queue is empty: Underflow");
	        } else {
	            size--;
	            int data = queueRep[front];
	            queueRep[front] = Integer.MIN_VALUE;
	            front = (front + 1) % capacity;
	            return data;
	        }
	    }
	
	    public int size() {
	        return size;
	    }
	
	    public boolean isEmpty() {
	        return 0 == size;
	    }
	
	    public boolean isFull() {
	        int length = queueRep.length;
	
	        return length == size;
	    }
	
	    public String toString() {
	        StringBuilder result = new StringBuilder("[");
	        int length = queueRep.length;
	        for (int i = 0; i < size; i++) {
	            result.append(queueRep[(front + i) % length]);
	            if (i < (size - 1)) {
	                result.append(", ");
	            }
	        }
	        result.append("]");
	        return result.toString();
	    }
	
	}
	
	```