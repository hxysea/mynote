----------
5/11/2017 9:01:27 AM 

----------
## 12 汇总数据 ##
1. 聚集函数
	- mysql封装好的对表中数据进行统计的一组函数
	- 运行在行祖上，计算和返回单个值（针对某列计算）的函数
2. 常用聚集函数介绍
	- AVG() 返回某列的平均值 
		- 忽略值为NULL的行
	- COUNT() 返回某列的行数
		- COUNT(*) 统计的列中可能包括空值，但是有某列不为空
		- COUNT(column) 则不统计NULL列
	- MAX() 返回某列的最大值 
		- 忽略列值为NULL的行
	- MIN() 返回某列的最小值
		- 忽略列值为NULL的行
	- SUM() 返回某列值之和  

3. 示例
```sql
select
	count(*) as num_items,
	min(prod_price) as price_min,
	max(prod_price) as price_max,
	avg(prod_price) as price_avg
from
	products
where
	prod_price >= 10;

SELECT
	u.username
FROM
	1344_tcparkingspace AS p
LEFT JOIN 1344_tcuser AS u ON p.userid = u.userid
WHERE
	p.parkingspacecode LIKE concat('%', '0', '%')
AND u.username IS NOT NULL -- 对应列不为空
group by u.username
ORDER BY
	p.parkingspacecode ASC
``` 

## 13 分组数据 ##
### 写在前面的话 ###
我们可以使用聚集函数实现汇总数据的功能，此种汇总是针对一个类别的数据，换句话说就是汇总的分类只有一个。那么问题来了，如果想要对一个数据集进行多个类别的统计又该怎么办呢？举个例子，现在有一个班30个人，分成5组，每组6人，分别计算6组的平均成绩。如果只是使用单纯的AVG函数无法实现分组统计的功能，此时数据库中的分组语句group by登场了。

本质上，分组语句是将分组统计的逻辑放在了数据库层面实现，及针对某列进行数据汇总统计。
### 常用语 ###
1. 创建分组
	- 分组语句通常与聚集函数联合使用，实现分组汇总数据的功能
	- group by语句必须在where语句之后，order by语句之前
	- group by必须包含select查询的from后表的所有字段，可不包括left/right join字段


	```sql
	-- 分组统计各组同学成绩
	select 
		group_id, avg(score) as score
	
	from
		 student
	
	group by group_id；
	
	```
2. 过滤分组
	- 对分组进行过滤，where是分组钱过滤数据，having是分组后过滤数据

	```sql
	-- 分组统计各组同学成绩且只统计出平均分大于等于80的
	select 

		group_id, avg(score) as score
	
	from
		 student
	
	group by group_id

	having avg(score) >= 80
	```
 