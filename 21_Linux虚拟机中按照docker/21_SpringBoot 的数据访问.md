### 21_SpringBoot 的数据访问
* Spring Data 项目是 Spring 用来解决数据访问问题的一整套解决方案。
* Spring Data 是一个伞形项目，包含了大量关系型数据库及非关系型数据库的数据访问解决方案。
* Spring Data 使我们可以快速简单的使用普通的数据访问技术及新的数据访问技术。
* Spring Data Commons 是所有Spring Data项目的依赖，可以让我们在使用关系型/非关系型/数据访问技术时，都可以使用基于Spring 的统一标准，该标准包含增删改查，排序和分页的相关操作

### 安装Linux虚拟机
### 在Linux上安装yum（就类似于maven）
### 在Linux上安装docker
### 使用docker安装redis
镜像与容器：[点击查看](https://www.cnblogs.com/bethal/p/5942369.html)
#### 1，容器的基本操作
运行镜像为容器的命令：`docker run --name 容器名 -d 镜像名`
* --name 为容器取得名称
* -d 表示detached，表示执行完这句命令后，控制台将不会被阻碍
* 将redis镜像启动为容器

#### 2，查看运行中的容器列表
`docker ps`
![](https://ws4.sinaimg.cn/large/006tNbRwgy1fp9rpsovxxj31f803gt93.jpg)

* CONTINER ID ：启动时候生成的ID
* IMAGE ：该容器使用的镜像
* COMMAND ：容器启动时调用的命令
* CREATED：容器创建时间
* STATUS ：当前容器的状态
* PORTS ：容器系统所使用的端口号
* Redis 默认使用6379
* NAMES ：在执行运行容器时为容器定义的名字


#### 3，查看运行状态和停止状态的容器
`docker ps -a`

#### 4，停止和启动容器
**停止容器**
* `docker stop 容器名/id`
* 停止刚刚将镜像运行为容器时开启的redis容器 ：`docker stop test-redis`

**启动容器**
* `docker start 容器名/容器id`
* 将刚刚停止的容器，启动 ：`docker start test-redis`

**端口映射**
Docker 容器中运行的软件所使用的窗口，在本机和本机的局域网是不能访问的，所以我们需要将Docker容器中的端口映射到当前主机端口上，这样我们在本机和本机所在的局域网就能够访问该软件了。

Docker 的端口映射是通过一个 -p 参数来实现的，我们以刚才的 Redis 为例，映射容器的6379端口，命令如下：
`docker run -d -p 6378:6379 --name port-redis redis`

**删除容器**
`docker rm 容器id`
`docker rm $(docker ps -a -q)` 删除所有容器

**查看当前容器日志**
`docker logs 容器名/容器id`

**登录容器**
运行中的容器其实是一个功能完备的Linux操作系统，所以我们可以像常规的系统一样登录并访问容器。

* 登录当前容器：`docker exec -it 容器id/容器名 bash`
* 退出登录：`exit`

![](https://ws2.sinaimg.cn/large/006tNbRwgy1fp9slemtqhj30lw07w0tf.jpgroot)

