/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package org.jetbrains.dokka.gradle

import org.gradle.api.NamedDomainObjectFactory

@Suppress("ObjectLiteralToLambda") // Will fail at runtime in Gradle versions <= 6.6
@Deprecated(DOKKA_V1_DEPRECATION_MESSAGE)
fun @Suppress("DEPRECATION") AbstractDokkaTask.gradleDokkaSourceSetBuilderFactory()
        : NamedDomainObjectFactory<@Suppress("DEPRECATION") GradleDokkaSourceSetBuilder> =
    NamedDomainObjectFactory { name ->
        @Suppress("DEPRECATION")
        GradleDokkaSourceSetBuilder(name, project, DokkaSourceSetIdFactory())
    }
