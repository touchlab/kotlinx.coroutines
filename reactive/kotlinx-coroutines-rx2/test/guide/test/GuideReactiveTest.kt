/*
 * Copyright 2016-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

// This file was automatically generated from coroutines-guide-reactive.md by Knit tool. Do not edit.
package kotlinx.coroutines.rx2.guide.test

import kotlinx.coroutines.guide.test.*
import org.junit.Test

class GuideReactiveTest : ReactiveTestBase() {

    @Test
    fun testExampleReactiveBasic01() {
        test("ExampleReactiveBasic01") { kotlinx.coroutines.rx2.guide.exampleReactiveBasic01.main() }.verifyLines(
            "Elements:",
            "Begin",
            "1",
            "2",
            "3",
            "Again:"
        )
    }

    @Test
    fun testExampleReactiveBasic02() {
        test("ExampleReactiveBasic02") { kotlinx.coroutines.rx2.guide.exampleReactiveBasic02.main() }.verifyLines(
            "Elements:",
            "Begin",
            "1",
            "2",
            "3",
            "Again:",
            "Begin",
            "1",
            "2",
            "3"
        )
    }

    @Test
    fun testExampleReactiveBasic03() {
        test("ExampleReactiveBasic03") { kotlinx.coroutines.rx2.guide.exampleReactiveBasic03.main() }.verifyLines(
            "OnSubscribe",
            "1",
            "2",
            "3",
            "Finally"
        )
    }

    @Test
    fun testExampleReactiveBasic04() {
        test("ExampleReactiveBasic04") { kotlinx.coroutines.rx2.guide.exampleReactiveBasic04.main() }.verifyLines(
            "OnSubscribe",
            "1",
            "2",
            "3",
            "OnComplete",
            "Finally",
            "4",
            "5"
        )
    }

    @Test
    fun testExampleReactiveBasic05() {
        test("ExampleReactiveBasic05") { kotlinx.coroutines.rx2.guide.exampleReactiveBasic05.main() }.verifyLines(
            "Sent 1",
            "Processed 1",
            "Sent 2",
            "Processed 2",
            "Sent 3",
            "Processed 3",
            "Complete"
        )
    }

    @Test
    fun testExampleReactiveBasic06() {
        test("ExampleReactiveBasic06") { kotlinx.coroutines.rx2.guide.exampleReactiveBasic06.main() }.verifyLines(
            "two",
            "three",
            "four"
        )
    }

    @Test
    fun testExampleReactiveBasic07() {
        test("ExampleReactiveBasic07") { kotlinx.coroutines.rx2.guide.exampleReactiveBasic07.main() }.verifyLines(
            "two",
            "three",
            "four"
        )
    }

    @Test
    fun testExampleReactiveBasic08() {
        test("ExampleReactiveBasic08") { kotlinx.coroutines.rx2.guide.exampleReactiveBasic08.main() }.verifyLines(
            "four"
        )
    }

    @Test
    fun testExampleReactiveBasic09() {
        test("ExampleReactiveBasic09") { kotlinx.coroutines.rx2.guide.exampleReactiveBasic09.main() }.verifyLines(
            "four"
        )
    }

    @Test
    fun testExampleReactiveOperators01() {
        test("ExampleReactiveOperators01") { kotlinx.coroutines.rx2.guide.exampleReactiveOperators01.main() }.verifyLines(
            "1",
            "2",
            "3",
            "4",
            "5"
        )
    }

    @Test
    fun testExampleReactiveOperators02() {
        test("ExampleReactiveOperators02") { kotlinx.coroutines.rx2.guide.exampleReactiveOperators02.main() }.verifyLines(
            "2 is even",
            "4 is even"
        )
    }

    @Test
    fun testExampleReactiveOperators03() {
        test("ExampleReactiveOperators03") { kotlinx.coroutines.rx2.guide.exampleReactiveOperators03.main() }.verifyLines(
            "1",
            "2"
        )
    }

    @Test
    fun testExampleReactiveOperators04() {
        test("ExampleReactiveOperators04") { kotlinx.coroutines.rx2.guide.exampleReactiveOperators04.main() }.verifyLines(
            "1",
            "2",
            "11",
            "3",
            "4",
            "12",
            "13"
        )
    }

    @Test
    fun testExampleReactiveContext01() {
        test("ExampleReactiveContext01") { kotlinx.coroutines.rx2.guide.exampleReactiveContext01.main() }.verifyLinesFlexibleThread(
            "1 on thread RxComputationThreadPool-1",
            "2 on thread RxComputationThreadPool-1",
            "3 on thread RxComputationThreadPool-1"
        )
    }

    @Test
    fun testExampleReactiveContext02() {
        test("ExampleReactiveContext02") { kotlinx.coroutines.rx2.guide.exampleReactiveContext02.main() }.verifyLinesStart(
            "1 on thread ForkJoinPool.commonPool-worker-1",
            "2 on thread ForkJoinPool.commonPool-worker-1",
            "3 on thread ForkJoinPool.commonPool-worker-1"
        )
    }

    @Test
    fun testExampleReactiveContext03() {
        test("ExampleReactiveContext03") { kotlinx.coroutines.rx2.guide.exampleReactiveContext03.main() }.verifyLinesFlexibleThread(
            "1 on thread RxComputationThreadPool-1",
            "2 on thread RxComputationThreadPool-1",
            "3 on thread RxComputationThreadPool-1"
        )
    }

    @Test
    fun testExampleReactiveContext04() {
        test("ExampleReactiveContext04") { kotlinx.coroutines.rx2.guide.exampleReactiveContext04.main() }.verifyLinesStart(
            "1 on thread main",
            "2 on thread main",
            "3 on thread main"
        )
    }

    @Test
    fun testExampleReactiveContext05() {
        test("ExampleReactiveContext05") { kotlinx.coroutines.rx2.guide.exampleReactiveContext05.main() }.verifyLinesStart(
            "1 on thread RxComputationThreadPool-1",
            "2 on thread RxComputationThreadPool-1",
            "3 on thread RxComputationThreadPool-1"
        )
    }
}
