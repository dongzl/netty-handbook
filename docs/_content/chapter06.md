## 6.1 Bootstrap、ServerBootstrap

1. `Bootstrap` 意思是引导，一个 `Netty` 应用通常由一个 `Bootstrap` 开始，主要作用是配置整个 `Netty` 程序，串联各个组件，`Netty` 中 `Bootstrap` 类是客户端程序的启动引导类，`ServerBootstrap` 是服务端启动引导类。
2. 常见的方法有
   - public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup)，该方法用于服务器端，用来设置两个EventLoop
   - public B group(EventLoopGroup group)，该方法用于客户端，用来设置一个 EventLoop
   - public B channel(Class<? extends C> channelClass)，该方法用来设置一个服务器端的通道实现
   - public <T> B option(ChannelOption<T> option, T value)，用来给 ServerChannel 添加配置
   - public <T> ServerBootstrap childOption(ChannelOption<T> childOption, T value)，用来给接收到的通道添加配置
   - public ServerBootstrap childHandler(ChannelHandler childHandler)，该方法用来设置业务处理类（自定义的handler）
   - public ChannelFuture bind(int inetPort)，该方法用于服务器端，用来设置占用的端口号
   - public ChannelFuture connect(String inetHost, int inetPort)，该方法用于客户端，用来连接服务器端

## 6.2 Future、ChannelFuture

Netty 中所有的 IO 操作都是异步的，不能立刻得知消息是否被正确处理。但是可以过一会等它执行完成或者直接注册一个监听，具体的实现就是通过 Future 和 ChannelFutures，他们可以注册一个监听，当操作执行成功或失败时监听会自动触发注册的监听事件

常见的方法有
- Channel channel()，返回当前正在进行 IO 操作的通道
- ChannelFuture sync()，等待异步操作执行完毕

## 6.3 Channel

1. Netty 网络通信的组件，能够用于执行网络 I/O 操作。
2. 通过 Channel 可获得当前网络连接的通道的状态
3. 通过 Channel 可获得网络连接的配置参数（例如接收缓冲区大小）
4. Channel 提供异步的网络 I/O 操作(如建立连接，读写，绑定端口)，异步调用意味着任何 I/O 调用都将立即返回，并且不保证在调用结束时所请求的 I/O 操作已完成
5. 调用立即返回一个 ChannelFuture 实例，通过注册监听器到 ChannelFuture 上，可以 I/O 操作成功、失败或取消时回调通知调用方
6. 支持关联 I/O 操作与对应的处理程序
7. 不同协议、不同的阻塞类型的连接都有不同的 Channel 类型与之对应，常用的 Channel 类型：
   - NioSocketChannel，异步的客户端 TCP Socket连接。
   - NioServerSocketChannel，异步的服务器端 TCP Socket 连接。
   - NioDatagramChannel，异步的 UDP 连接。
   - NioSctpChannel，异步的客户端 Sctp 连接。
   - NioSctpServerChannel，异步的 Sctp 服务器端连接，这些通道涵盖了 UDP 和 TCP 网络 IO 以及文件 IO。

## 6.4 Selector

1. Netty 基于 Selector 对象实现 I/O 多路复用，通过 Selector 一个线程可以监听多个连接的 Channel 事件。
2. 当向一个 Selector 中注册 Channel 后，Selector 内部的机制就可以自动不断地查询（Select）这些注册的 Channel 是否有已就绪的 I/O 事件（例如可读，可写，网络连接完成等），这样程序就可以很简单地使用一个线程高效地管理多个 Channel

## 6.5 ChannelHandler 及其实现类

1. ChannelHandler 是一个接口，处理 I/O 事件或拦截 I/O 操作，并将其转发到其 ChannelPipeline（业务处理链）中的下一个处理程序。
2. ChannelHandler 本身并没有提供很多方法，因为这个接口有许多的方法需要实现，方便使用期间，可以继承它的子类
3. ChannelHandler 及其实现类一览图（后）

![](../_media/chapter06/chapter06_01.png)

4. 我们经常需要自定义一个 Handler 类去继承 ChannelInboundHandlerAdapter，然后通过重写相应方法实现业务逻辑，我们接下来看看一般都需要重写哪些方法

![](../_media/chapter06/chapter06_02.png)

## 6.6 Pipeline 和 ChannelPipeline

ChannelPipeline 是一个重点：

1. ChannelPipeline 是一个 Handler 的集合，它负责处理和拦截 inbound 或者 outbound 的事件和操作，相当于一个贯穿 Netty的链。（也可以这样理解：ChannelPipeline 是保存 ChannelHandler 的 List，用于处理或拦截 Channel 的入站事件和出站操作）
2. ChannelPipeline 实现了一种高级形式的拦截过滤器模式，使用户可以完全控制事件的处理方式，以及 Channel 中各个的 ChannelHandler 如何相互交互
3. 在 Netty 中每个 Channel 都有且仅有一个 ChannelPipeline 与之对应，它们的组成关系如下

![](../_media/chapter06/chapter06_03.png)

![](../_media/chapter06/chapter06_04.png)

4. 常用方法
   ChannelPipeline addFirst(ChannelHandler... handlers)，把一个业务处理类（handler）添加到链中的第一个位置ChannelPipeline addLast(ChannelHandler... handlers)，把一个业务处理类（handler）添加到链中的最后一个位置

## 6.7 ChannelHandlerContext

1. 保存 Channel 相关的所有上下文信息，同时关联一个 ChannelHandler 对象
2. 即 ChannelHandlerContext 中包含一个具体的事件处理器 ChannelHandler，同时 ChannelHandlerContext 中也绑定了对应的 pipeline 和 Channel 的信息，方便对 ChannelHandler 进行调用。
3. 常用方法
   - ChannelFuture close()，关闭通道
   - ChannelOutboundInvoker flush()，刷新
   - ChannelFuture writeAndFlush(Object msg)，将数据写到 
   - ChannelPipeline 中当前 ChannelHandler 的下一个 ChannelHandler 开始处理（出站）

![](../_media/chapter06/chapter06_05.png)

## 6.8 ChannelOption

1. Netty 在创建 Channel 实例后，一般都需要设置 ChannelOption 参数。
2. ChannelOption 参数如下：

![](../_media/chapter06/chapter06_06.png)

## 6.9 EventLoopGroup 和其实现类 NioEventLoopGroup

1. EventLoopGroup 是一组 EventLoop 的抽象，Netty 为了更好的利用多核 CPU 资源，一般会有多个 EventLoop 同时工作，每个 EventLoop 维护着一个 Selector 实例。
2. EventLoopGroup 提供 next 接口，可以从组里面按照一定规则获取其中一个 EventLoop 来处理任务。在 Netty 服务器端编程中，我们一般都需要提供两个 EventLoopGroup，例如：BossEventLoopGroup 和 WorkerEventLoopGroup。
3. 通常一个服务端口即一个 ServerSocketChannel 对应一个 Selector 和一个 EventLoop 线程。BossEventLoop 负责接收客户端的连接并将 SocketChannel 交给 WorkerEventLoopGroup 来进行 IO 处理，如下图所示

![](../_media/chapter06/chapter06_07.png)

4. 常用方法
   public NioEventLoopGroup()，构造方法
   public Future<?> shutdownGracefully()，断开连接，关闭线程

## 6.10 Unpooled 类

1. Netty 提供一个专门用来操作缓冲区（即 Netty 的数据容器）的工具类
2. 常用方法如下所示

![](../_media/chapter06/chapter06_08.png)

3. 举例说明 Unpooled 获取 Netty 的数据容器 ByteBuf 的基本使用【案例演示】

![](../_media/chapter06/chapter06_09.png)

案例 1

案例 2

## 6.11 Netty 应用实例-群聊系统

实例要求：

1. 编写一个 Netty 群聊系统，实现服务器端和客户端之间的数据简单通讯（非阻塞）
2. 实现多人群聊
3. 服务器端：可以监测用户上线，离线，并实现消息转发功能
4. 客户端：通过 channel 可以无阻塞发送消息给其它所有用户，同时可以接受其它用户发送的消息（有服务器转发得到）
5. 目的：进一步理解 Netty 非阻塞网络编程机制
6. 看老师代码演示

## 6.12 Netty 心跳检测机制案例

实例要求：

1. 编写一个 Netty 心跳检测机制案例,当服务器超过 3 秒没有读时，就提示读空闲
2. 当服务器超过 5 秒没有写操作时，就提示写空闲
3. 实现当服务器超过 7 秒没有读或者写操作时，就提示读写空闲
4. 代码如下：

## 6.13 Netty 通过 WebSocket 编程实现服务器和客户端长连接

实例要求：

1. Http 协议是无状态的，浏览器和服务器间的请求响应一次，下一次会重新创建连接。
2. 要求：实现基于 WebSocket 的长连接的全双工的交互
3. 改变 Http 协议多次请求的约束，实现长连接了，服务器可以发送消息给浏览器
4. 客户端浏览器和服务器端会相互感知，比如服务器关闭了，浏览器会感知，同样浏览器关闭了，服务器会感知
5. 运行界面
