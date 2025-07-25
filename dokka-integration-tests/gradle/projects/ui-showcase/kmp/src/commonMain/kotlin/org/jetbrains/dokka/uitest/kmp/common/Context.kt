@file:OptIn(kotlin.contracts.ExperimentalContracts::class)
package org.jetbrains.dokka.uitest.kmp.common

import kotlinx.coroutines.CoroutineScope
/*
 * Copyright 2014-2025 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */


/**
 * Runs the specified [block] with the given value in context scope.
 *
 * As opposed to [with], [context] only makes the value available for
 * context parameter resolution, but not as implicit receiver.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <T, R> context(with: T, block: context(T) () -> R): R {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(with)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, Result> context(a: A, b: B, block: context(A, B) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, Result> context(a: A, b: B, c: C, block: context(A, B, C) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, Result> context(a: A, b: B, c: C, d: D, block: context(A, B, C, D) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, Result> context(a: A, b: B, c: C, d: D, e: E, block: context(A, B, C, D, E) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, block: context(A, B, C, D, E, F) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, block: context(A, B, C, D, E, F, G) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, block: context(A, B, C, D, E, F, G, H) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, block: context(A, B, C, D, E, F, G, H, I) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, block: context(A, B, C, D, E, F, G, H, I, J) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, block: context(A, B, C, D, E, F, G, H, I, J, K) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, L, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, block: context(A, B, C, D, E, F, G, H, I, J, K, L) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k, l)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, L, M, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, block: context(A, B, C, D, E, F, G, H, I, J, K, L, M) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k, l, m)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, block: context(A, B, C, D, E, F, G, H, I, J, K, L, M, N) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k, l, m, n)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, block: context(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, block: context(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, block: context(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, block: context(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, block: context(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, block: context(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, block: context(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)
}


/**
 * Runs the specified [block] with the given values in context scope.
 *
 * As opposed to [with], [context] only makes the values available for
 * context parameter resolution, but not as implicit receivers.
 *
 * @sample samples.misc.ContextParameters.useContext
 */
@SinceKotlin("2.2")
public inline fun <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, Result> context(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V, block: context(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V) () -> Result): Result {
    kotlin.contracts.contract {
        callsInPlace(block, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return block(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)
}
