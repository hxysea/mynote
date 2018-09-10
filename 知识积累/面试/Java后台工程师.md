1. Java中的集合类有哪些？

   - 数组
     - 可以存储对象和基本数据类型
     - 长度固定
   - 集合
     - 只能存储对象
     - 长度可变
   - 层次关系
     - Collection
       - List
         - 有序集合
         - 可以包含重复元素
         - 提供索引方式访问
         - 查询快，插入/删除慢
       - Set
         - 无序
         - 不能包含重复元素
     - Map
       - 不能包含重复的key
       - 可以包含相同的value
     - ArrayList和LinkedList
       - 用法无区别
       - LinkedList适用于增删多而查询少的情况
       - ArrayList适用于查询多而增删少的情况
     - Map集合
       - HashMap
         - 最常用
         - 只允许一条key为null的记录
       - Hashtable
         - HashMap线程安全版，支持线程同步
         - 不允许key和value为null，效率较低
         - 继承Diactionary，实现Map接口
       - ConcurrentHashMap
         - 线程安全
         - 使用分段锁，每个段就是一个小的hash table，不同段操作可并发执行
       - LinkedHashMap
         - 保存记录的插入顺序
         - 使用Iterator遍历时，先插入的先遍历到
         - 遍历比HashMap慢
       - TreeMap
         - 根据key排序
         - 不允许key为空
         - 非同步
   - 区别
     - Vectotr和ArrayList
       - Vector线程安全，ArrayList非线程安全
       - Vector增长率为100%，ArrayList为50%
       - 基于数组，插入慢，查询快
     - ArrayList和LinkedList
       - 大批量add或remove数据，LinkedList占优，因为ArrayList需要移动前后数据，LinedList只需移动指针
       - ArrayList基于动态数组，LinedList基于链表
     - HashMap与TreeMap
       - HashMap根据hashcode快速查找，TreeMap保持某种固定顺序，返回有序结果
       - HashMap适合查找、删除、设置值等，TreeMap适合有序返回

2. Object方法

   - getClass
   - hashCode
   - equals
   - clone
   - toString
   - notify
   - notifyAll
   - wait
   - finalize

3. 　现在来看看MySQL数据库为我们提供的四种隔离级别：

   　　① Serializable (串行化)：可避免脏读、不可重复读、幻读的发生。

   　　② Repeatable read (可重复读)：可避免脏读、不可重复读的发生。

   　　③ Read committed (读已提交)：可避免脏读的发生。

   　　④ Read uncommitted (读未提交)：最低级别，任何情况都无法保证