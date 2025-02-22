[TOC]



# 轻点评项目

## 前置

### SQL数据库表

tb_user:用户表

tb_user info:用户详情表

tb shop:商户信息表

tb_shop type:商户类型表

tb blog:用户日记表（达人探店日记）

tb follow:用户关注表

tb voucher:优惠券表

tb voucher order:优惠券的订单表



## 短信登陆

![image-20250210144314967](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250210144314967.png)

![image-20250210152523279](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250210152523279.png)



## 商品查询缓存

### 添加缓存

![image-20250211141214192](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250211141214192.png)



### 缓存更新策略

![image-20250213130830011](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250213130830011.png)



### 缓存穿透

![image-20250213154454015](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250213154454015.png)

![image-20250213155158517](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250213155158517.png)



### 缓存雪崩

![image-20250213155758200](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250213155758200.png)



### 缓存击穿

![image-20250213161509181](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250213161509181.png)

![image-20250213161528280](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250213161528280.png)

![image-20250213161536898](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250213161536898.png)



## 优惠券秒杀

### 全局唯一ID

![image-20250216152606152](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250216152606152.png)



### 超卖问题

![image-20250218221046391](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250218221046391.png)

![image-20250218221950293](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250218221950293.png)

![image-20250218222208224](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250218222208224.png)

![image-20250218223218137](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250218223218137.png)



### 一人一单（单机器）

同一个优惠券，一个用户只能下一单

判断是否存在：无法使用乐观锁，需要使用悲观锁（根据用户ID加锁，减小粒度）

加锁与事务的顺序？代理对象？



### 一人一单的并发安全问题

![image-20250218225430176](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250218225430176.png)

![image-20250218230223681](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250218230223681.png)

jvm无法提供/实现跨进程的锁

synchronized只能保证JVM内部的多个线程之间的互斥，而没有办法让我们在集群下的多个JVM之间的进程互斥



## 分布式锁

在集群模式下，多个JVM会有多个锁监视器，也就会有多个线程获取到锁，也就无法实现多JVM下的互斥。

分布式锁：满足分布式系统或集群模式下多进程可见并且互斥的锁

![image-20250219181136761](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250219181136761.png)

![image-20250219181948411](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250219181948411.png)



### 误删问题1

![image-20250221150116160](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250221150116160.png)

关键在于释放锁的时候判断是否是自己的锁



### 误删问题2

判断锁标识和释放锁不是一起执行（非原子）

![image-20250221153325124](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250221153325124.png)



解决方式：Lua脚本

![image-20250221162808012](C:\Users\Makehan\AppData\Roaming\Typora\typora-user-images\image-20250221162808012.png)
#   l i g h t - d i a n p i n g  
 