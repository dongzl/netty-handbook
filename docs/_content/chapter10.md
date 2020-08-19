## 10.1 基本说明

1. 只有看过 `Netty` 源码，才能说是真的掌握了 `Netty` 框架。
2. 在 `io.netty.example` 包下，有很多 `Netty` 源码案例，可以用来分析。
3. 源码分析章节是针对有 `Java` 项目经验，并且玩过框架源码的人员讲的，否则你听起来会有相当的难度。

## 10.2 Netty 启动过程源码剖析

### 10.2.1 源码剖析目的

用源码分析的方式走一下 `Netty`（服务器）的启动过程，更好的理解 `Netty` 的整体设计和运行机制。

### 10.2.2 源码剖析

说明：

1. 源码需要剖析到 `Netty` 调用 `doBind` 方法，追踪到 `NioServerSocketChannel` 的 `doBind`。
2. 并且要 `Debug` 程序到 `NioEventLoop` 类的 `run` 代码，无限循环，在服务器端运行。

### 10.2.3 源码剖析过程

### 10.2.4 Netty 启动过程梳理

1. 创建 `2` 个 `EventLoopGroup` 线程池数组。数组默认大小 `CPU * 2`，方便 `chooser` 选择线程池时提高性能
2. `BootStrap` 将 `boss` 设置为 `group` 属性，将 `worker` 设置为 `childer` 属性
3. 通过 `bind` 方法启动，内部重要方法为 `initAndRegister` 和 `dobind` 方法
4. `initAndRegister` 方法会反射创建 `NioServerSocketChannel` 及其相关的 `NIO` 的对象，`pipeline`，`unsafe`，同时也为 `pipeline` 初始了 `head` 节点和 `tail` 节点。
5. 在 `register0` 方法成功以后调用在 `dobind` 方法中调用 `doBind0` 方法，该方法会调用 `NioServerSocketChannel` 的 `doBind` 方法对 `JDK` 的 `channel` 和端口进行绑定，完成 `Netty` 服务器的所有启动，并开始监听连接事件。

## 10.3 Netty 接受请求过程源码剖析

### 10.3.1 源码剖析目的

1. 服务器启动后肯定是要接受客户端请求并返回客户端想要的信息的，下面源码分析 `Netty` 在启动之后是如何接受客户端请求的
2. 在 `io.netty.example` 包下

### 10.3.2 源码剖析

说明：

1. 从之前服务器启动的源码中，我们得知，服务器最终注册了一个 `Accept` 事件等待客户端的连接。我们也知道，`NioServerSocketChannel` 将自己注册到了 `boss` 单例线程池（`reactor` 线程）上，也就是 `EventLoop`。
2. 先简单说下 `EventLoop` 的逻辑(后面我们详细讲解 `EventLoop`)

`EventLoop` 的作用是一个死循环，而这个循环中做 3 件事情：

1. 有条件的等待 `NIO` 事件。
2. 处理 `NIO` 事件。
3. 处理消息队列中的任务。
4. 仍用前面的项目来分析：进入到 `NioEventLoop` 源码中后，在 `private void processSelectedKey(SelectionKey k, AbstractNioChannel ch)` 方法开始调试最终我们要分析到 `AbstractNioChannel` 的 `doBeginRead` 方法，当到这个方法时，针对于这个客户端的连接就完成了，接下来就可以监听读事件了
 
源码分析过程

### 10.3.3 Netty 接受请求过程梳理

总体流程：接受连接 --> 创建一个新的 `NioSocketChannel` --> 注册到一个 `workerEventLoop` 上 --> 注册 `selecotRead` 事件。

1. 服务器轮询 `Accept` 事件，获取事件后调用 `unsafe` 的 `read` 方法，这个 `unsafe` 是 `ServerSocket` 的内部类，该方法内部由 `2` 部分组成
2. `doReadMessages` 用于创建 `NioSocketChannel` 对象，该对象包装 `JDK` 的 `NioChannel` 客户端。该方法会像创建 `ServerSocketChanel` 类似创建相关的 `pipeline`，`unsafe`，`config`
3. 随后执行执行 `pipeline.fireChannelRead` 方法，并将自己绑定到一个 `chooser` 选择器选择的 `workerGroup` 中的一个 `EventLoop`。并且注册一个 `0`，表示注册成功，但并没有注册读（1）事件

## 10.4 Pipeline Handler HandlerContext 创建源码剖析

### 10.4.1 源码剖析目的

`Netty` 中的 `ChannelPipeline`、`ChannelHandler` 和 `ChannelHandlerContext` 是非常核心的组件，我们从源码来分析 `Netty` 是如何设计这三个核心组件的，并分析是如何创建和协调工作的.

### 10.4.2 源码剖析说明

说明
分析过程中，有很多的图形，所以我们准备了一个文档，在文档的基础上来做源码剖析

### 10.4.3 源码剖析

### 10.4.4 Pipeline Handler HandlerContext 创建过程梳理

1. 每当创建 `ChannelSocket` 的时候都会创建一个绑定的 `pipeline`，一对一的关系，创建 `pipeline` 的时候也会创建 `tail` 节点和 `head` 节点，形成最初的链表。
2. 在调用 `pipeline` 的 `addLast` 方法的时候，会根据给定的 `handler` 创建一个 `Context`，然后，将这个 `Context` 插入到链表的尾端（`tail` 前面）。
3. `Context` 包装 `handler`，多个 `Context` 在 `pipeline` 中形成了双向链表
4. 入站方向叫 `inbound`，由 `head` 节点开始，出站方法叫 `outbound`，由 `tail` 节点开始

## 10.5 ChannelPipeline 调度 handler 的源码剖析

### 10.5.1 源码剖析目的

1. 当一个请求进来的时候，`ChannelPipeline` 是如何调用内部的这些 `handler` 的呢？我们一起来分析下。
2. 首先，当一个请求进来的时候，会第一个调用 `pipeline` 的相关方法，如果是入站事件，这些方法由 `fire` 开头，表示开始管道的流动。让后面的 `handler` 继续处理
 
### 10.5.2 源码剖析

说明

当浏览器输入 `http://localhost:8007` 。可以看到会执行 `handler`
在 `Debug` 时，可以将断点下在 `DefaultChannelPipeline` 类的

### 10.5.3 ChannelPipeline 调度 handler 梳理

1. `Context` 包装 `handler`，多个 `Context` 在 `pipeline` 中形成了双向链表，入站方向叫 `inbound`，由 `head` 节点开始，出站方法叫 `outbound`，由 `tail` 节点开始。
2. 而节点中间的传递通过 `Abstract ChannelHandlerContext` 类内部的 `fire` 系列方法，找到当前节点的下一个节点不断的循环传播。是一个过滤器形式完成对 `handler` 的调度

## 10.6 Netty 心跳(heartbeat)服务源码剖析

### 10.6.1 源码剖析目的

`Netty` 作为一个网络框架，提供了诸多功能，比如编码解码等，`Netty` 还提供了非常重要的一个服务 -- 心跳机制 `heartbeat`。通过心跳检查对方是否有效，这是 `RPC` 框架中是必不可少的功能。下面我们分析一下 `Netty` 内部心跳服务源码实现。

### 10.6.2 源码剖析

说明

`Netty` 提供了 `IdleStateHandler`，`ReadTimeoutHandler`，`WriteTimeoutHandler` 三个 `Handler` 检测连接的有效性，重点分析 `IdleStateHandler`。

如图

## 10.7 Netty 核心组件 EventLoop 源码剖析

### 10.7.1 源码剖析目的

`Echo` 第一行代码就是：`EventLoopGroup bossGroup = new NioEventLoopGroup(1)`; 下面分析其最核心的组件 `EventLoop`。

### 10.7.2 源码剖析

源码剖析

## 10.8 handler 中加入线程池和 Context 中添加线程池的源码剖析

### 10.8.1 源码剖析目的

1. 在 `Netty` 中做耗时的，不可预料的操作，比如数据库，网络请求，会严重影响 `Netty` 对 `Socket` 的处理速度。
2. 而解决方法就是将耗时任务添加到异步线程池中。但就添加线程池这步操作来讲，可以有 `2` 种方式，而且这 `2` 种方式实现的区别也蛮大的。
3. 处理耗时业务的第一种方式 -- `handler` 中加入线程池
4. 处理耗时业务的第二种方式 -- `Context` 中添加线程池
5. 我们就来分析下两种方式

### 10.8.2 源码剖析

说明 演示两种方式的实现，以及从源码来追踪两种方式执行流程

