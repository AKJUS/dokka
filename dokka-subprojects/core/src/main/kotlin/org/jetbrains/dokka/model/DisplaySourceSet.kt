/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package org.jetbrains.dokka.model

import org.jetbrains.dokka.*
import org.jetbrains.dokka.pages.*
import org.jetbrains.dokka.DokkaConfiguration.DokkaSourceSet
import java.util.concurrent.ConcurrentHashMap

/**
 * Represents a final user-visible source set in the documentable model that is
 * used to specify under which source sets/targets current signatures are available,
 * can be used to filter in and out all available signatures under the specified source set,
 * and, depending on the format, are rendered as "platform" selectors.
 *
 * E.g. HTML format renders display source sets as "bubbles" that later are used for filtering
 * and informational purposes.
 *
 * [DisplaySourceSet]s typically have a one-to-one correspondence to the build system source sets,
 * are created by the base plugin from [DokkaSourceSet] and never tweaked manually.
 * [DisplaySourceSet] is uniquely identified by the corresponding [CompositeSourceSetID].
 *
 * @property sourceSetIDs unique stable id of the display source set.
 *  It is composite by definition, as it uniquely defines the source set and all nested source sets.
 *  Apart from names, it also contains a substitute to a full source set path in order to differentiate
 *  source sets with the same name in a stable manner.
 * @property name corresponds to the name of the original [DokkaSourceSet]
 * @property platform the platform of the source set. If the source set is a mix of multiple source sets
 *  that correspond to multiple KMP platforms, then it is [Platform.common]
 */
public data class DisplaySourceSet(
    val sourceSetIDs: CompositeSourceSetID,
    val name: String,
    val platform: Platform
) {
    public constructor(sourceSet: DokkaSourceSet) : this(
        sourceSetIDs = CompositeSourceSetID(sourceSet.sourceSetID),
        name = sourceSet.displayName,
        platform = sourceSet.analysisPlatform
    )
}

/**
 * Transforms the current [DokkaSourceSet] into [DisplaySourceSet],
 * matching the corresponding subset of its properties to [DisplaySourceSet] properties.
 */
public fun DokkaSourceSet.toDisplaySourceSet(): DisplaySourceSet {
    return DisplaySourceSetCaches.displaySourceSet(this)
}

/**
 * Transforms all the given [DokkaSourceSet]s into [DisplaySourceSet]s.
 */
public fun Iterable<DokkaSourceSet>.toDisplaySourceSets(): Set<DisplaySourceSet> {
    return DisplaySourceSetCaches.displaySourceSets(this)
}

@InternalDokkaApi
@Deprecated("Use computeSourceSetIds() and cache its results instead", replaceWith = ReplaceWith("computeSourceSetIds()"))
public val Iterable<DisplaySourceSet>.sourceSetIDs: List<DokkaSourceSetID> get() = this.flatMap { it.sourceSetIDs.all }

@InternalDokkaApi
public fun Iterable<DisplaySourceSet>.computeSourceSetIds(): Set<DokkaSourceSetID> =
    fold(hashSetOf()) { acc, set -> acc.addAll(set.sourceSetIDs.all); acc }

internal object DisplaySourceSetCaches {
    private val instanceCache = ConcurrentHashMap<DokkaSourceSet, DisplaySourceSet>()
    private val setCache = ConcurrentHashMap<Iterable<DokkaSourceSet>, Set<DisplaySourceSet>>()

    /**
     * Construction of [DisplaySourceSet] from [DokkaSourceSet] requires to create a [CompositeSourceSetID], which has a big memory footprint.
     * [toDisplaySourceSets] is called for almost each [ContentNode] a LOT.
     * In reality, there will be only several [DokkaSourceSet] instances during Dokka execution.
     */
    fun displaySourceSet(sourceSet: DokkaSourceSet): DisplaySourceSet {
        return instanceCache.computeIfAbsent(sourceSet, ::DisplaySourceSet)
    }

    /**
     * [toDisplaySourceSets] mostly called on the same Set<DokkaSourceSet>,
     * so it makes sense to cache them by the set itself.
     */
    fun displaySourceSets(sourceSets: Iterable<DokkaSourceSet>): Set<DisplaySourceSet> {
        return setCache.computeIfAbsent(sourceSets) { it.map(::displaySourceSet).toSet() }
    }

    fun clear() {
        instanceCache.clear()
        setCache.clear()
    }
}
