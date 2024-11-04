

# 通用延时任务组件

日常开发中，我们经常遇到这种业务场景，如：外卖订单超 30 分钟未支付，则自动取订单；用户注册成功 15 分钟后，发短信息通知用户等等。这就延时任务处理场景。基于该场景，开发一个通用的延时任务组件。
通常web开发是基于springboot环境， 基于此封装一个通用延时任务组件， 便于公司其他产线使用，减少重复代码开发与设计。

## 延时组件策略

1. 基于原生实现 Delayed 接口
    优点： 不依赖任何第三方组件
    缺点： 基于 JVM 内存，如果 JVM 重启了，那所有数据就丢失了
    总结： 适用于数据量较小，且丢失也不影响主业务的场景，就算丢失，也不会有太大影响。

2. Redisson 分布式延迟队列
   Redisson 提供了一个分布式延迟队列RDelayedQueue，他是一种基于 zset 结构实现的延迟队列，其实现类是RedissonDelayedQueue。
   优点： 使用简单，并且其实现类中大量使用 lua 脚本保证其原子性，不会有并发重复问题
   缺点： 需要依赖redis
   总结： Redisson 是 redis 官方推荐的 JAVA 客户端，提供了很多常用的功能，使用简单、高效。

## 使用方式

1. 引入依赖

```xml
        <dependency>
            <groupId>com.ssn.delay</groupId>
            <artifactId>delay-task-spring-boot-starter</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

2. application.yml 配置

```yml

delay:
  type: jdk  # 支持 jdk  redisson
  url: redis://127.0.0.1:6379 # 假设是redisson 的时候需要配置
  name: 处理延时任务的线程名称前缀
  capacity: 使用jdk的时候需要配置，支持的容量， 默认 10000
  delayQueueName: redisson 的延迟队列名称， 默认 delay-task-queue
```


3. 实现 TaskProcessor 接口

编写具体的TaskProcessor实现类，处理延时消息的具体任务

