/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package org.jetbrains.dokka

import org.jetbrains.dokka.utilities.cast
import java.io.File
import java.io.Serializable
import java.net.URL

public object DokkaDefaults {
    public val moduleName: String = "root"
    public val moduleVersion: String? = null
    public val outputDir: File = File("./dokka")
    public const val failOnWarning: Boolean = false
    public const val suppressObviousFunctions: Boolean = true
    public const val suppressInheritedMembers: Boolean = false
    public const val offlineMode: Boolean = false

    public const val sourceSetDisplayName: String = "JVM"
    public const val sourceSetName: String = "main"
    public val analysisPlatform: Platform = Platform.DEFAULT

    public const val suppress: Boolean = false
    public const val suppressGeneratedFiles: Boolean = true

    public const val skipEmptyPackages: Boolean = true
    public const val skipDeprecated: Boolean = false

    public const val reportUndocumented: Boolean = false

    public const val noStdlibLink: Boolean = false
    public const val noAndroidSdkLink: Boolean = false
    public const val noJdkLink: Boolean = false
    public const val jdkVersion: Int = 8

    public const val includeNonPublic: Boolean = false
    public val documentedVisibilities: Set<DokkaConfiguration.Visibility> = setOf(DokkaConfiguration.Visibility.PUBLIC)

    public val pluginsConfiguration: List<PluginConfigurationImpl> = mutableListOf()

    public const val delayTemplateSubstitution: Boolean = false

    public val cacheRoot: File? = null
}

public enum class Platform(
    public val key: String
) {
    jvm("jvm"),
    js("js"),
    wasm("wasm"),
    native("native"),
    common("common");

    public companion object {
        public val DEFAULT: Platform = jvm

        public fun fromString(key: String): Platform {
            return when (key.toLowerCase()) {
                jvm.key -> jvm
                js.key -> js
                wasm.key -> wasm
                native.key -> native
                common.key -> common
                "androidjvm", "android" -> jvm
                "metadata" -> common
                else -> throw IllegalArgumentException("Unrecognized platform: $key")
            }
        }
    }
}

public fun interface DokkaConfigurationBuilder<T : Any> {
    public fun build(): T
}

public fun <T : Any> Iterable<DokkaConfigurationBuilder<T>>.build(): List<T> = this.map { it.build() }

/**
 * Represents a unique identifier for a [DokkaConfiguration.DokkaSourceSet].
 * It should be unique across the whole project.
 *
 * @property scopeId The unique identifier of the scope that this source set is placed in.
 *    Each scope provides only unique source set names.
 *    E.g. One DokkaTask inside the Gradle plugin represents one source set scope, since there cannot be multiple
 *    source sets with the same name. However, a Gradle project will not be a proper scope, since there can be
 *    multiple DokkaTasks that contain source sets with the same name (but different configuration)
 * @property sourceSetName The name of the source set.
 */
public data class DokkaSourceSetID(
    val scopeId: String,
    val sourceSetName: String
) : Serializable {
    override fun toString(): String {
        return "$scopeId/$sourceSetName"
    }
}

/**
 * Global options can be configured and applied to all packages and modules at once, overwriting package configuration.
 *
 * These are handy if we have multiple source sets sharing the same global options as it reduces the size of the
 * boilerplate. Otherwise, the user would be forced to repeat all these options for each source set.
 *
 * @see [apply] to learn how to apply global configuration
 */
public data class GlobalDokkaConfiguration(
    val perPackageOptions: List<PackageOptionsImpl>?,
    val externalDocumentationLinks: List<ExternalDocumentationLinkImpl>?,
    val sourceLinks: List<SourceLinkDefinitionImpl>?
)

public fun DokkaConfiguration.apply(globals: GlobalDokkaConfiguration): DokkaConfiguration = this.apply {
    sourceSets.forEach {
        it.perPackageOptions.cast<MutableList<DokkaConfiguration.PackageOptions>>()
            .addAll(globals.perPackageOptions ?: emptyList())
    }

    sourceSets.forEach {
        it.externalDocumentationLinks.cast<MutableSet<DokkaConfiguration.ExternalDocumentationLink>>()
            .addAll(globals.externalDocumentationLinks ?: emptyList())
    }

    sourceSets.forEach {
        it.sourceLinks.cast<MutableSet<SourceLinkDefinitionImpl>>().addAll(globals.sourceLinks ?: emptyList())
    }
}

public interface DokkaConfiguration : Serializable {
    public val moduleName: String
    public val moduleVersion: String?
    public val outputDir: File
    public val cacheRoot: File?
    public val offlineMode: Boolean
    public val failOnWarning: Boolean
    public val sourceSets: List<DokkaSourceSet>
    public val modules: List<DokkaModuleDescription>
    public val pluginsClasspath: List<File>
    public val pluginsConfiguration: List<PluginConfiguration>
    public val delayTemplateSubstitution: Boolean
    public val suppressObviousFunctions: Boolean
    public val includes: Set<File>
    public val suppressInheritedMembers: Boolean

    /**
     * Whether coroutines dispatchers should be shutdown after
     * generating documentation via [DokkaGenerator.generate].
     * Additionally, whether the Analysis API and IJ platform global
     * services should be shutdown.
     *
     * If this is enabled, Coroutines *and* the Analysis API *and* IntelliJ
     * platform classes should no longer be used after the documentation is generated once.
     *
     * For example, it effectively stops all background threads associated with
     * coroutines in order to make classes unloadable by the JVM,
     * and rejects all new tasks with [RejectedExecutionException]
     *
     * This is primarily useful for multi-module builds where global services
     * can be shut down after each module's partial task to avoid
     * possible memory leaks.
     *
     * However, this can lead to problems in specific lifecycles where
     * global services are shared and will be reused after documentation generation,
     * and closing it down will leave the build in an inoperable state.
     * One such example is unit tests, for which finalization should be disabled.
     */
    public val finalizeCoroutines: Boolean

    public enum class SerializationFormat : Serializable {
        JSON, XML
    }

    public interface PluginConfiguration : Serializable {
        public val fqPluginName: String
        public val serializationFormat: SerializationFormat
        public val values: String
    }

    /**
     * Each [DokkaSourceSet] is uniquely identified by its [sourceSetID].
     * This means that if two [DokkaSourceSet]s will have the same [sourceSetID] they will be interchangeable.
     * [equals] and [hashCode] must be defined only based on [sourceSetID].
     *
     * **See Also:** [Dokka#3246](https://github.com/Kotlin/dokka/issues/3246)
     */
    public interface DokkaSourceSet : Serializable {
        public val sourceSetID: DokkaSourceSetID
        public val displayName: String
        public val classpath: List<File>
        public val sourceRoots: Set<File>
        public val dependentSourceSets: Set<DokkaSourceSetID>
        public val samples: Set<File>
        public val includes: Set<File>

        @Deprecated(message = "Use [documentedVisibilities] property for a more flexible control over documented visibilities")
        public val includeNonPublic: Boolean
        public val reportUndocumented: Boolean
        public val skipEmptyPackages: Boolean
        public val skipDeprecated: Boolean
        public val jdkVersion: Int
        public val sourceLinks: Set<SourceLinkDefinition>
        public val perPackageOptions: List<PackageOptions>
        public val externalDocumentationLinks: Set<ExternalDocumentationLink>
        public val languageVersion: String?
        public val apiVersion: String?
        public val noStdlibLink: Boolean
        public val noJdkLink: Boolean
        public val suppressedFiles: Set<File>
        public val analysisPlatform: Platform
        public val documentedVisibilities: Set<Visibility>
    }

    public enum class Visibility {
        /**
         * `public` modifier for Java, default visibility for Kotlin
         */
        PUBLIC,

        /**
         * `private` modifier for both Kotlin and Java
         */
        PRIVATE,

        /**
         * `protected` modifier for both Kotlin and Java
         */
        PROTECTED,

        /**
         * Kotlin-specific `internal` modifier
         */
        INTERNAL,

        /**
         * Java-specific package-private visibility (no modifier)
         */
        PACKAGE;

        public companion object {
            public fun fromString(value: String): Visibility = valueOf(value.toUpperCase())
        }
    }

    public interface SourceLinkDefinition : Serializable {
        public val localDirectory: String
        public val remoteUrl: URL
        public val remoteLineSuffix: String?
    }

    public interface DokkaModuleDescription : Serializable {
        public val name: String
        public val relativePathToOutputDirectory: File
        public val sourceOutputDirectory: File
        public val includes: Set<File>
    }

    public interface PackageOptions : Serializable {
        public val matchingRegex: String

        @Deprecated("Use [documentedVisibilities] property for a more flexible control over documented visibilities")
        public val includeNonPublic: Boolean
        public val reportUndocumented: Boolean?
        public val skipDeprecated: Boolean
        public val suppress: Boolean
        public val documentedVisibilities: Set<Visibility>
    }

    public interface ExternalDocumentationLink : Serializable {
        public val url: URL
        public val packageListUrl: URL

        public companion object
    }
}

@Suppress("FunctionName")
public fun ExternalDocumentationLink(
    url: URL? = null,
    packageListUrl: URL? = null
): ExternalDocumentationLinkImpl {
    return if (packageListUrl != null && url != null)
        ExternalDocumentationLinkImpl(url, packageListUrl)
    else if (url != null)
        ExternalDocumentationLinkImpl(url, URL(url, "package-list"))
    else
        throw IllegalArgumentException("url or url && packageListUrl must not be null for external documentation link")
}

@Suppress("FunctionName")
public fun ExternalDocumentationLink(
    url: String, packageListUrl: String? = null
): ExternalDocumentationLinkImpl =
    ExternalDocumentationLink(url.let(::URL), packageListUrl?.let(::URL))
