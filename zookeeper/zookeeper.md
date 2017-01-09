在 CentOS7 上安装 zookeeper-3.4.9 服务
 
1、创建 /usr/local/services/zookeeper 文件夹：
    mkdir -p /usr/local/services/zookeeper
 
2、进入到 /usr/local/services/zookeeper 目录中：
    cd /usr/local/services/zookeeper
 
3、下载 zookeeper-3.4.9.tar.gz：
    wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/zookeeper-3.4.9/zookeeper-3.4.9.tar.gz
 
4、解压缩 zookeeper-3.4.9.tar.gz：
    tar -zxvf zookeeper-3.4.9.tar.gz
 
5、进入到 /usr/local/services/zookeeper/zookeeper-3.4.9/conf 目录中：
    cd zookeeper-3.4.9/conf/
 
6、复制 zoo_sample.cfg 文件的并命名为为 zoo.cfg：
    cp zoo_sample.cfg zoo.cfg
 
7、用 vim 打开 zoo.cfg 文件并修改其内容为如下：
    # The number of milliseconds of each tick
 
    # zookeeper 定义的基准时间间隔，单位：毫秒
    tickTime=2000
 
    # The number of ticks that the initial
    # synchronization phase can take
    initLimit=10
    # The number of ticks that can pass between
    # sending a request and getting an acknowledgement
    syncLimit=5
    # the directory where the snapshot is stored.
    # do not use /tmp for storage, /tmp here is just
    # example sakes.
    # dataDir=/tmp/zookeeper
 
    # 数据文件夹
    dataDir=/usr/local/services/zookeeper/zookeeper-3.4.9/data
 
    # 日志文件夹
    dataLogDir=/usr/local/services/zookeeper/zookeeper-3.4.9/logs
 
    # the port at which the clients will connect
    # 客户端访问 zookeeper 的端口号
    clientPort=2181
 
    # the maximum number of client connections.
    # increase this if you need to handle more clients
    #maxClientCnxns=60
    #
    # Be sure to read the maintenance section of the
    # administrator guide before turning on autopurge.
    #
    # http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
    #
    # The number of snapshots to retain in dataDir
    #autopurge.snapRetainCount=3
    # Purge task interval in hours
    # Set to "0" to disable auto purge feature
    #autopurge.purgeInterval=1
 
8、保存并关闭 zoo.cfg 文件:
     
9、进入到 /usr/local/services/zookeeper/zookeeper-3.4.9/bin 目录中：
    cd ../bin/
 
10、用 vim 打开 /etc/ 目录下的配置文件 profile：
    vim /etc/profile
    并在其尾部追加如下内容：
 
    # idea - zookeeper-3.4.9 config start - 2016-09-08
 
    export ZOOKEEPER_HOME=/usr/local/services/zookeeper/zookeeper-3.4.9/
    export PATH=$ZOOKEEPER_HOME/bin:$PATH
    export PATH
 
    # idea - zookeeper-3.4.9 config start - 2016-09-08
 
11、使 /etc/ 目录下的 profile 文件即可生效：
    source /etc/profile
 
12、启动 zookeeper 服务：
    zkServer.sh start
    如打印如下信息则表明启动成功：
    ZooKeeper JMX enabled by default
    Using config: /usr/local/services/zookeeper/zookeeper-3.4.9/bin/../conf/zoo.cfg
    Starting zookeeper ... STARTED
 
13、查询 zookeeper 状态：
    zkServer.sh status
 
14、关闭 zookeeper 服务：
    zkServer.sh stop
    如打印如下信息则表明成功关闭：
    ZooKeeper JMX enabled by default
    Using config: /usr/local/services/zookeeper/zookeeper-3.4.9/bin/../conf/zoo.cfg
    Stopping zookeeper ... STOPPED
 
15、重启 zookeeper 服务：
    zkServer.sh restart
    如打印如下信息则表明重启成功：
    ZooKeeper JMX enabled by default
    Using config: /usr/local/services/zookeeper/zookeeper-3.4.9/bin/../conf/zoo.cfg
    ZooKeeper JMX enabled by default
    Using config: /usr/local/services/zookeeper/zookeeper-3.4.9/bin/../conf/zoo.cfg
    Stopping zookeeper ... STOPPED
    ZooKeeper JMX enabled by default
    Using config: /usr/local/services/zookeeper/zookeeper-3.4.9/bin/../conf/zoo.cfg
    Starting zookeeper ... STARTED