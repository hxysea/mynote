
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
1. 排序数据
	- select * from tbl_name order by column_name;
	- select * from tbl_name order by column_name1, column_name2;
	- select * from tbl_name order by column_name asc/desc;
	- select * from tbl_name order by column_name asc/desc limit num;
# 过滤数据 #
1. 使用where子句
	- select * from tbl_name where column_name = value order by column_name desc;
	- =
	- <>
	- !=
	- <
	- <=
	- >
	- >=
	- BETWEEN ... AND ...
	- NULL
	
# 数据过滤 #
1. 组合where子句
	- **select** * **from** tbl_name **where** colunm_name >= value **and** column_name <= value2;
	- or
	- in
		- select * from tbl_name where column_name in(value, value2...) order by column_name;
	- not in
# 使用通配符进行过滤 #
1. like操作符
	- select * from tbl_name where column_name like '%xxx%'
	- select * from tbl_name where column_name like '_xxx'; _只匹配单个字符

# 用正则表达式进行搜索 #
1. 正则表达式是用来匹配文本的特殊的串（字符集合）
	```sql
	- select * from tbl_name where column_name REGXP 'xxx' order by column_name;
	- select * from tbl_name where column_name REGXP '.xxx' order by column_name;
	- select * from tbl_name where column_name REGXP '.xxx | .yyy' order by column_name;
	- select * from tbl_name where column_name REGXP '[123]xxx' order by column_name;
	- select * from tbl_name where column_name REGXP '[^123]xxx' order by column_name;
	- select * from tbl_name where column_name REGXP '[a-z]xxx' order by column_name;
	- select * from tbl_name where column_name REGXP ' \\.' order by column_name;
	- ^ 文本的开始
	- $ 文本的结尾
	- [[:<:]] 词的开始
	- [[:>:]] 词的结尾
	``` 
