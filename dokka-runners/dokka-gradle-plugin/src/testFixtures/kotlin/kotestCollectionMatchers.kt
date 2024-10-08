/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */
package org.jetbrains.dokka.gradle.utils

import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.maps.shouldContainAll
import io.kotest.matchers.maps.shouldContainExactly

/** @see io.kotest.matchers.maps.shouldContainAll */
fun <K, V> Map<K, V>.shouldContainAll(
    vararg expected: Pair<K, V>
): Unit = shouldContainAll(expected.toMap())

/** @see io.kotest.matchers.maps.shouldContainExactly */
fun <K, V> Map<K, V>.shouldContainExactly(
    vararg expected: Pair<K, V>
): Unit = shouldContainExactly(expected.toMap())

/** Verify the sequence contains a single element, matching [match]. */
fun <T> Sequence<T>.shouldBeSingleton(match: (T) -> Unit) {
    toList().shouldBeSingleton(match)
}
