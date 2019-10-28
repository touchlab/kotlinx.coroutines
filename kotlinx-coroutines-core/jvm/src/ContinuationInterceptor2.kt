/*
 * Copyright 2016-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.coroutines

import kotlin.coroutines.*

/**
 * Copy-paste of [ContinuationInterceptor] with adjusted get/minusKey
 */
interface ContinuationInterceptor2 : CoroutineContext.Element {

    companion object Key : CoroutineContext.Key<ContinuationInterceptor2>

    override val key: CoroutineContext.Key<*>
        get() = Key

    public override operator fun <E : CoroutineContext.Element> get(key: CoroutineContext.Key<E>): E? {
        if (key is AsymmetricKey<*, *> && key.isSubKey(this.key)) {
            return key.tryCast(this) as? E
        }

        return if (key === ContinuationInterceptor2) this as E else null
    }

    public override fun minusKey(key: CoroutineContext.Key<*>): CoroutineContext {
        if (key is AsymmetricKey<*, *>) {
            return if (key.isSubKey(this.key)) EmptyCoroutineContext else this
        }
        return if (key === ContinuationInterceptor2) EmptyCoroutineContext else this
    }
}

abstract class AsymmetricKey<Base : CoroutineContext.Element, Element : Base>(
    baseKey: CoroutineContext.Key<Base>,
    private val cast: (CoroutineContext.Element) -> Element?
) : CoroutineContext.Key<Element> {

    // To allow subclasses in type parameters, e.g. Key<Dispatcher, ExecutorDispatcher> instead of Key<ContinuationInterceptor, ExecutorDispatcher>
    private val topMostKey: CoroutineContext.Key<*> =
        if (baseKey is AsymmetricKey<*, *>) baseKey.topMostKey else baseKey

    fun tryCast(element: CoroutineContext.Element): Element? = cast(element)
    fun isSubKey(key: CoroutineContext.Key<*>): Boolean = key === this || topMostKey === key
}

abstract class Dispatcher : ContinuationInterceptor2 {
    companion object Key :
        AsymmetricKey<ContinuationInterceptor2, Dispatcher>(ContinuationInterceptor2, { it as? Dispatcher })

    fun dispatcher(): Dispatcher = this
}

class EventLoop2 : Dispatcher()

class ExecutorDispatcher : Dispatcher() {
    companion object Key :
        AsymmetricKey<Dispatcher, ExecutorDispatcher>(Dispatcher, { it as? ExecutorDispatcher })

    fun executor(): ExecutorDispatcher = this
}

class CustomInterceptor : ContinuationInterceptor2 {
    override val key: CoroutineContext.Key<*>
        get() = ContinuationInterceptor2
}


fun main() {
    sample1()
    sample2()
    sample3()
}

fun sample1() {
    val ctx = CoroutineId(1) + EventLoop2()
    println(ctx[ContinuationInterceptor2]) // EL
    println(ctx[Dispatcher]?.dispatcher()) // EL
    println(ctx[ExecutorDispatcher]?.executor()) // null

    // Validation
    require(ctx.size == 2)
    require(ctx.minusKey(Dispatcher).size == 1)
    require(ctx.minusKey(ContinuationInterceptor2).size == 1)
    require(ctx.minusKey(ExecutorDispatcher).size == 2)
    require((ctx + EventLoop2()).size == 2)
    require((ctx + CustomInterceptor()).size == 2)
    require((ctx + ExecutorDispatcher()).size == 2)
}

fun sample2() {
    println()
    val ctx = CoroutineId(1) + ExecutorDispatcher()
    println(ctx[ContinuationInterceptor2]) // ED
    println(ctx[Dispatcher]?.dispatcher()) // ED
    println(ctx[ExecutorDispatcher]?.executor()) // ED

    // Validation
    require(ctx.size == 2)
    require(ctx.minusKey(Dispatcher).size == 1)
    require(ctx.minusKey(ContinuationInterceptor2).size == 1)
    require(ctx.minusKey(ExecutorDispatcher).size == 1)
    require((ctx + EventLoop2()).size == 2)
    require((ctx + CustomInterceptor()).size == 2)
    require((ctx + ExecutorDispatcher()).size == 2)
}


fun sample3() {
    println()
    val ctx = CoroutineId(1) + CustomInterceptor()
    println(ctx[ContinuationInterceptor2]) // CI
    println(ctx[Dispatcher]?.dispatcher()) // null
    println(ctx[ExecutorDispatcher]?.executor()) // null

    // Validation
    require(ctx.size == 2)
    require(ctx.minusKey(Dispatcher).size == 2)
    require(ctx.minusKey(ContinuationInterceptor2).size == 1)
    require(ctx.minusKey(ExecutorDispatcher).size == 2)
    require((ctx + EventLoop2()).size == 2)
    require((ctx + CustomInterceptor()).size == 2)
    require((ctx + ExecutorDispatcher()).size == 2)
}

val CoroutineContext.size get() = fold(0) { acc, _ -> acc + 1 }
