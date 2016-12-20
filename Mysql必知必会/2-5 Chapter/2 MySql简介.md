
----------
12/19/2016 11:18:15 AM 

----------
# MySql 简介 #
1. 什么是MySql？
	- 一种DBMS（数据库管理系统），即Mysql是一种数据库软件
	- 成本--开源
	- 性能--很快
	- 可信赖
	- 简单
2. DBMS
	- 基于共享软件系统的DBMS
		- Microsoft Access和FileMaker
	- 基于客户端-服务器的DBMS
		- MySql、Oracle、Microsoft SQL Server  

# 检索数据 #
 
1. 检索单个列
	- select column_name from tbl_name; 
	- select * from tbl_name;
	- select distinct column_name from tbl_name;
	- select column_name from tbl_name limit num;
	- select column_name from tbl_name limit start_index, num;//第一行为0
	- select column_name from tbl_name limit num offset index;
	- select tbl_name.column_name from db_name.tbl_name;//全限定名查询


# 排序检索数据 #