/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */
package org.jetbrains.dokka.gradle.internal

import kotlinx.serialization.json.JsonArrayBuilder
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import java.io.File


@JvmName("addAllFiles")
internal fun JsonArrayBuilder.addAll(files: Iterable<File>) {
    files
        .map { it.canonicalFile.invariantSeparatorsPath }
        .forEach { path -> add(path) }
}

@JvmName("addAllStrings")
internal fun JsonArrayBuilder.addAll(values: Iterable<String>) {
    values.forEach { add(it) }
}

internal fun JsonArrayBuilder.addAllIfNotNull(values: Iterable<String>?) {
    if (values != null) addAll(values)
}

internal fun JsonObjectBuilder.putIfNotNull(key: String, value: Boolean?) {
    if (value != null) put(key, JsonPrimitive(value))
}

internal fun JsonObjectBuilder.putIfNotNull(key: String, value: String?) {
    if (value != null) put(key, JsonPrimitive(value))
}

internal fun JsonObjectBuilder.putIfNotNull(key: String, value: File?) {
    if (value != null) put(key, JsonPrimitive(value.canonicalFile.invariantSeparatorsPath))
}
