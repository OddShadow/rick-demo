# JavaNIO

## Overview

1. NIO 指 new IO，而不是 Non-blocking IO，因为部分 API 实际上是阻塞的
2. 最重要的三个概念是 `Channel`   `Buffer`  `Selector`
3. 读过程是数据从 Channel 流向 Buffer
4. 写过程是数据从 Buffer 流向 Channel
5. Selector 是单个线程管理多个 Channel 的工具

## Channel

`docker run -d --name jenkins -p 8080:8080-v /data/jenkins_home:/var/jenkins_home jenkins/jenkins:lts`

## Buffer & Selector

Channel 单向，异步，可读可写，联系 Buffer 和数据源的通道

Buffer 本质是分配一块物理内存，Buffer 类是其抽象，Buffer 类中的方法封装了对物理内存的操作

![](https://raw.githubusercontent.com/OddShadow/images/main/rick-demo/202307261825778.png)

mark <= position <= limit <= capacity

- capacity 指 buffer 最大容量
- limit 写模式下等于 capacity，切换读模式 `limit=position`
- position 写模式下初始值为 0，每写一个加一，切换读模式 `position=0`

## Buffer 用法

直接实现 `Buffer` 的抽象类

- `ByteBuffer`
  - `MappedByteBuffer`
- `ShortBuffer`
- `IntBuffer`
- `LongBuffer`
- `FloatBuffer`
- `DoubleBuffer`
- `CharBuffer`

`ByteBuffer` 为例展示常用方法

1. `ByteBuffer allocate(int capacity)` 创建指定 `capacity` 的一个 `ByteBuffer` 实际返回了 `HeapByteBuffer` 实现类
2. `byte get()`
3. `ByteBuffer put(byte b)`
4. `Buffer flip()`
5. `Buffer rewind()`
6. `Buffer clear()`
7. `ByteBuffer compact()`
8. `Buffer mark()`
9. `Buffer reset()`

## Scattering Reads & Gathering Writes

- Scattering Reads 指从多个 Buffer 中同时读取数据到同一个 Channel
  使用 `ScatteringByteChannel` 方法 `long read(ByteBuffer[] dsts)`
- Gathering Writes 指从同一个 Channel 写道多个 Buffer 中
  使用 `GatheringByteChannel` 方法 `long write(ByteBuffer[] srcs)`

## Channel To Channel

支持 `Channel` 间传输

再 `FileChannel` 中可以用

`long transferTo(long position, long count,WritableByteChannel target)`

`long transferFrom(ReadableByteChannel src, long position, long count)`

## Selector 用法

创建 `Selector.open()`