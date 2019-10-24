/*
 * Copyright 2016-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.coroutines

import kotlin.coroutines.*
import kotlin.reflect.*

/**
 * Copy-paste of [ContinuationInterceptor] with adjusted get/minusKey
 */
interface AsymmetricInterceptor : CoroutineContext.Element {

    companion object Key : CoroutineContext.Key<AsymmetricInterceptor>

    override val key: CoroutineContext.Key<*>
        get() = Key

    public override operator fun <E : CoroutineContext.Element> get(key: CoroutineContext.Key<E>): E? {
        if (key is AsymmetricKey<*, *> && key.isSubKey(this.key)) {
            return key.tryCast(this) as? E
        }

        return if (key === AsymmetricInterceptor) this as E else null
    }

    public override fun minusKey(key: CoroutineContext.Key<*>): CoroutineContext {
        if (key is AsymmetricKey<*, *>) {
            return if (key.isSubKey(this.key)) EmptyCoroutineContext else this
        }
        return if (key === AsymmetricInterceptor) EmptyCoroutineContext else this
    }
}

abstract class AsymmetricKey<Base : CoroutineContext.Element, Element : Base>(
    private val baseKey: CoroutineContext.Key<Base>,
    private val elementClass: KClass<Element>
) : CoroutineContext.Key<Element> {

    fun tryCast(element: CoroutineContext.Element): Element? {
        // NB: can be implemented in MPP manner without reflection
        if (elementClass.java.isInstance(element)) {
            return element as Element
        }
        return null
    }

    fun isSubKey(key: CoroutineContext.Key<*>): Boolean {
        if (key === this) return true
        return baseKey === key
    }
}


abstract class Dispatcher : AsymmetricInterceptor {
    companion object Key : AsymmetricKey<AsymmetricInterceptor, Dispatcher>(AsymmetricInterceptor, Dispatcher::class)
}

class EventLoop2 : Dispatcher()

class ExecutorDispatcher : Dispatcher() {
    companion object Key : AsymmetricKey<AsymmetricInterceptor, ExecutorDispatcher>(AsymmetricInterceptor, ExecutorDispatcher::class)
}

class CustomInterceptor : AsymmetricInterceptor {
    override val key: CoroutineContext.Key<*>
        get() = AsymmetricInterceptor
}


fun main() {
    sample1()
    sample2()
    sample3()
}

fun sample1() {
    val ctx = CoroutineId(1) + EventLoop2()
    println(ctx[AsymmetricInterceptor]) // EL
    println(ctx[Dispatcher]) // EL
    println(ctx[ExecutorDispatcher]) // null

    // Validation
    require(ctx.size == 2)
    require(ctx.minusKey(Dispatcher).size == 1)
    require(ctx.minusKey(AsymmetricInterceptor).size == 1)
    require(ctx.minusKey(ExecutorDispatcher).size == 2)
    require((ctx + EventLoop2()).size == 2)
    require((ctx + CustomInterceptor()).size == 2)
    require((ctx + ExecutorDispatcher()).size == 2)
}

fun sample2() {
    println()
    val ctx = CoroutineId(1) + ExecutorDispatcher()
    println(ctx[AsymmetricInterceptor]) // ED
    println(ctx[Dispatcher]) // ED
    println(ctx[ExecutorDispatcher]) // ED

    // Validation
    require(ctx.size == 2)
    require(ctx.minusKey(Dispatcher).size == 1)
    require(ctx.minusKey(AsymmetricInterceptor).size == 1)
    require(ctx.minusKey(ExecutorDispatcher).size == 1)
    require((ctx + EventLoop2()).size == 2)
    require((ctx + CustomInterceptor()).size == 2)
    require((ctx + ExecutorDispatcher()).size == 2)
}


fun sample3() {
    println()
    val ctx = CoroutineId(1) + CustomInterceptor()
    println(ctx[AsymmetricInterceptor]) // CI
    println(ctx[Dispatcher]) // null
    println(ctx[ExecutorDispatcher]) // null

    // Validation
    require(ctx.size == 2)
    require(ctx.minusKey(Dispatcher).size == 2)
    require(ctx.minusKey(AsymmetricInterceptor).size == 1)
    require(ctx.minusKey(ExecutorDispatcher).size == 2)
    require((ctx + EventLoop2()).size == 2)
    require((ctx + CustomInterceptor()).size == 2)
    require((ctx + ExecutorDispatcher()).size == 2)
}

val CoroutineContext.size get() = fold(0) { acc, _ -> acc + 1 }
