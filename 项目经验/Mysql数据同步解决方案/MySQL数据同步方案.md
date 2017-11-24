----------
11/24/2017 4:50:51 PM 

----------

# MySQL数据同步方案实战 #

## 准备 ##
- MySQL 5.6
- Canal 1.0.24
- Kafka 1.0.0
- Zookeeper 3.4.11
- Spring Boot
- JDK 8
- IntelliJ Idea 14.1.7

## 背景 ##

在实际工作中，多个系统使用相同的用户等业务数据是常有的事情。随着需求的迭代式开发，系统的增加。系统的业务数据变更通知逐渐成为痛点，将数据变更逐一通知到其他系统在系统少的时候勉强可以接受，但是系统变多，需要通知的业务数据增多再使用此类的方式无疑是得不偿失的，且极不易维护，耦合性太强，牵一发而动全身。此时，引入中间件无疑是一种明智的选择。将系统从繁杂的通知中解脱出来，把通知这件事交给中间件来做。而这是基于业务操作最终会落实到数据库中，这也是本文方案可行的前提。

## 架构图 ##

![](https://i.imgur.com/3wALRVW.png)

## 实现 ##

### Canal Server搭建 ###

#### MySQL配置 ####
- 进入C:\Program Files\MySQL\MySQL Server 5.6目录
- 编辑**my.ini**文件（按需修改），打开mysql bin log功能，如下
```ini
[mysqld]
# Remove leading # and set to the amount of RAM for the most important data
# cache in MySQL. Start at 70% of total RAM for dedicated server, else 10%.
# innodb_buffer_pool_size = 128M

# Remove leading # to turn on a very important data integrity option: logging
# changes to the binary log between backups.
# log_bin

# These are commonly set, remove the # and set as required.
basedir = C:\Program Files\MySQL\MySQL Server 5.6
datadir = C:\Program Files\MySQL\MySQL Server 5.6\data
# port = .....
# server_id = .....

log-bin=mysql-bin  #添加这一行即可
#选择row模式
binlog-format=ROW
#配置mysql replaction需要定义，不能和canal的slaveId重复
server_id=1 
# Remove leading # to set options mainly useful for reporting servers.
# The server defaults are faster for transactions and fast SELECTs.
# Adjust sizes as needed, experiment to find the optimal values.
# join_buffer_size = 128M
# sort_buffer_size = 2M
# read_rnd_buffer_size = 2M 

sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES 

```
- canal的原理是模拟自己为mysql slave，所以这里一定需要拥有做为mysql slave的相关权限（canal server配置需要用到此用户）
```sql
CREATE USER canal IDENTIFIED BY 'canal';  
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
-- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;
FLUSH PRIVILEGES;
```
- 重启mysql server
- 查看bin log状态

```sql
show binlog events;
```
![](https://i.imgur.com/e8T93sy.png)

- 获取canalserver（[https://github.com/alibaba/canal/releases/download/canal-1.0.24/canal.deployer-1.0.24.tar.gz](https://github.com/alibaba/canal/releases/download/canal-1.0.24/canal.deployer-1.0.24.tar.gz)） 
- 修改canal server配置
	- 进入D:\MySQL_Sync_Component\canal.deployer-1.0.24\conf\example目录
	- 修改**instance.properties**文件

```properties
#################################################
## mysql serverId
canal.instance.mysql.slaveId = 1234

# position info,需要改成自己的数据库信息
canal.instance.master.address = 127.0.0.1:3306
canal.instance.master.journal.name = 
canal.instance.master.position = 
canal.instance.master.timestamp = 

#canal.instance.standby.address = 
#canal.instance.standby.journal.name =
#canal.instance.standby.position = 
#canal.instance.standby.timestamp = 

# username/password,需要改成自己的数据库信息
canal.instance.dbUsername = canal
canal.instance.dbPassword = canal
canal.instance.defaultDatabaseName =
canal.instance.connectionCharset = UTF-8

# table regex
canal.instance.filter.regex = .*\\..*
# table black regex
canal.instance.filter.black.regex =  

#################################################
```

- 启动canal server
	- 进入D:\MySQL_Sync_Component\canal.deployer-1.0.24\bin目录
	- 运行startup.bat

- 查看canal server日志
	- D:\MySQL_Sync_Component\canal.deployer-1.0.24\logs\canal\canal.log
	- D:\MySQL_Sync_Component\canal.deployer-1.0.24\logs\example\example.log

```log
canal.log

2017-11-24 17:55:32.550 [main] INFO  com.alibaba.otter.canal.deployer.CanalLauncher - ## start the canal server.
2017-11-24 17:55:32.770 [main] INFO  com.alibaba.otter.canal.deployer.CanalController - ## start the canal server[192.168.191.1:11111]
2017-11-24 17:55:34.503 [main] INFO  com.alibaba.otter.canal.deployer.CanalLauncher - ## the canal server is running now ......
```

```log
example.log

2017-11-24 17:55:33.202 [main] INFO  c.a.o.c.i.spring.support.PropertyPlaceholderConfigurer - Loading properties file from class path resource [canal.properties]
2017-11-24 17:55:33.209 [main] INFO  c.a.o.c.i.spring.support.PropertyPlaceholderConfigurer - Loading properties file from class path resource [example/instance.properties]
2017-11-24 17:55:33.343 [main] WARN  org.springframework.beans.TypeConverterDelegate - PropertyEditor [com.sun.beans.editors.EnumEditor] found through deprecated global PropertyEditorManager fallback - consider using a more isolated form of registration, e.g. on the BeanWrapper/BeanFactory!
2017-11-24 17:55:33.471 [main] INFO  c.a.otter.canal.instance.spring.CanalInstanceWithSpring - start CannalInstance for 1-example 
2017-11-24 17:55:33.647 [main] INFO  c.a.otter.canal.instance.core.AbstractCanalInstance - start successful....
2017-11-24 17:55:33.896 [destination = example , address = /127.0.0.1:3306 , EventParser] WARN  c.a.otter.canal.parse.inbound.mysql.MysqlEventParser - prepare to find start position just show master status

``` 


## 参考文档 ##

[https://github.com/alibaba/canal/wiki](https://github.com/alibaba/canal/wiki)

[http://kafka.apache.org/](http://kafka.apache.org/)

