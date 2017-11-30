----------
11/28/2017 1:56:05 PM 

----------
### How to Use Docker ###
1. **docker run -p 2181:2181 -d zookeeper:latest**（运行docker 镜像命令）
```
-t 选项让Docker分配一个伪终端（pseudo-tty）并绑定到容器的标准输入上， 
-i 则让容器的标准输入保持打开。
-p 选项指定Container到Host之间的端口映射关系。
-d 让Docker 容器在后台以守护态（Daemonized）形式运行
```
2. **docker ps -a** (查看所有运行过的镜像启动及退出信息)
3. **docker images**（查看Docker中已有的Image） 
4. **docker stop xxx**(容器ID) 停止容器
5. **docker logs -f [ID或者名字]**
### What is Docker ###

### Why We Use Docker ###