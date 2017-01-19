----------
1/9/2017 10:54:32 AM 

----------
# A Java Performance Toolbox #
1. Operating System Tools and Analysis
	- CPU Usage
	- The CPU Run Queue	
		- unix
			- run queue
		- windows
			- processor queue
			- typeperf -si 1 "\System\Processor Queue Length"
	- Disk Usage
		- iostat -xm 5  -->linux
	- Network Usage
		- nicstat 时间
			- nicstat 5
				- 没5s刷新一次数据
	- Java Monitorint Tools
		- jcmd
		- jconsole