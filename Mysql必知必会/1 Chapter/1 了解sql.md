----------
12/19/2016 11:16:38 AM 

----------
# 了解Sql #
1. 什么是数据库？
	- 数据库与数据结构有异曲同工之妙，都包含两个层面的意思：数据+组织方式。数据库是一个保存有组织的数据的容器（通常是一个文件或一组文件），数据结构是以某种组织的方式存储的数据集合（存储于内存中）。
	- 数据库可以比拟为一个文件柜，其是一个存储数据的物理位置
2. 表 
	- 存储某种特定类型的结构化文件
	- 名字唯一
	- 定义数据在表中如何存储（数据类型、字段命名，数据如何分解等）
	- 模式：关于数据库和表的布局及特性的信息

3. 列和数据类型
	- 列是表中一个字段
	- 数据类型是所允许的一类数据的类型
		- 数据类型可以限制列中的数据种类
		- 帮助正确的排序数据
		- 优化磁盘使用
4. 行
	- 表中的一个记录

5. 主键
	- 唯一标识表中每行数据的某列（或某组列）
		- 任意两行不能有相同的主键值
		- 主键值不允许为NULL值
	- 最佳实践
		- 不更新主键列中的值
		- 不重用主键列中的值
		- 不在主键列中使用可能会更改的值

