# Java 并发进阶

## 1. CompletableFuture

### 常用 API

1. 获得结果和触发计算

   1. `completableFuture.get()`

      调用线程阻塞直到拿到返回值

   2. `completableFuture.get(2L, TimeUnit.SECONDS)`

      调用线程阻塞直到拿到返回值，如果超时抛出 `TimeoutException`

   3. `completableFuture.join()`

      调用线程阻塞直到拿到返回值，封装异常，不用手动处理

   4. `completableFuture.getNow("defaultValue")`

      立刻拿到返回值，如果任务没有完成则返回传参值

   5. `completableFuture.complete("defaultValue")`

      立刻拿到返回值，如果任务没有完成则中断任务，将参数作为最终结果

2. 对计算结果进行处理

   1. `thenApply(f -> f)` 

      线程的计算结果存在依赖关系，线程串行化执行，上一步抛出异常，当前步骤不执行并且终止线程操作

   2. `handle((f, e) -> f)`

      线程的计算结果存在依赖关系，线程串行化执行，并且可以即使上一步抛出异常，依然可以继续执行当前步骤

3. 对计算结构进行消费

4. 对计算速度进行选用

5. 对计算结果进行合并

