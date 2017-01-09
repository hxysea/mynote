----------
1/3/2017 9:25:02 AM 

----------

# Introduction #
1. 调优什么？
	- the performance of the jvm
	- the performace of the platform
2. A Brief Outline
	- methodology for testing Java applications 方法论
		- 包括pitfalls of java benchmarking
	- overview of some tools avaliable to monitor java applications
	- just in time compilation
	- garbage collection
	- java heap
	- native memory use
	- thread performance
	- java enterprise edition apis
	- jpa and jdbc
	- some general java se api tips
3. JVM Tuning Flags
	- flags that require parameter
		- **-XX:FlagName=something**
	- boolean flags 
		- **-XX:+FlagName** 开启标志
		- **-XX:-FlagName** 关闭标志
4. How to improve java performance
	- write better algorithms
	- write less code

5. 性能瓶颈
	- 数据库
	- 代码
	- other后台系统
	- jvm性能 