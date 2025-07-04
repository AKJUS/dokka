/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("dokkabuild.kotlin-jvm")
}

dependencies {
    implementation(projects.dokkaSubprojects.dokkaTestApi)

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.test)
}
