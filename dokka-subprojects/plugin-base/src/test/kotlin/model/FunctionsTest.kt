/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package model

import org.jetbrains.dokka.links.DRI
import org.jetbrains.dokka.links.TypeConstructor
import org.jetbrains.dokka.model.*
import utils.AbstractModelTest
import utils.assertNotNull
import utils.comments
import utils.OnlyDescriptors
import utils.OnlySymbols
import utils.name
import utils.text
import kotlin.test.Test
import org.jetbrains.dokka.ExperimentalDokkaApi

@OptIn(ExperimentalDokkaApi::class)
class FunctionTest : AbstractModelTest("/src/main/kotlin/function/Test.kt", "function") {

    @Test
    fun function() {
        inlineModelTest(
            """
            |/**
            | * Function fn
            | */
            |fun fn() {}
        """
        ) {
            with((this / "function" / "fn").cast<DFunction>()) {
                name equals "fn"
                type.name equals "Unit"
                this.children.assertCount(0, "Function children: ")
            }
        }
    }

    @Test
    fun overloads() {
        inlineModelTest(
            """
            |/**
            | * Function fn
            | */
            |fun fn() {}
            | /**
            | * Function fn(Int)
            | */
            |fun fn(i: Int) {}
        """
        ) {
            with((this / "function").cast<DPackage>()) {
                val fn1 = functions.find {
                    it.name == "fn" && it.parameters.isEmpty()
                }.assertNotNull("fn()")
                val fn2 = functions.find {
                    it.name == "fn" && it.parameters.isNotEmpty()
                }.assertNotNull("fn(Int)")

                with(fn1) {
                    name equals "fn"
                    parameters.assertCount(0)
                }

                with(fn2) {
                    name equals "fn"
                    parameters.assertCount(1)
                    parameters.first().type.name equals "Int"
                }
            }
        }
    }

    @Test
    fun functionWithReceiver() {
        inlineModelTest(
            """
            |/**
            | * Function with receiver
            | */
            |fun String.fn() {}
            |
            |/**
            | * Function with receiver
            | */
            |fun String.fn(x: Int) {}
        """
        ) {
            with((this / "function").cast<DPackage>()) {
                val fn1 = functions.find {
                    it.name == "fn" && it.parameters.isEmpty()
                }.assertNotNull("fn()")
                val fn2 = functions.find {
                    it.name == "fn" && it.parameters.count() == 1
                }.assertNotNull("fn(Int)")

                with(fn1) {
                    name equals "fn"
                    parameters counts 0
                    receiver.assertNotNull("fn() receiver")
                }

                with(fn2) {
                    name equals "fn"
                    parameters counts 1
                    receiver.assertNotNull("fn(Int) receiver")
                    parameters.first().type.name equals "Int"
                }
            }
        }
    }

    @Test
    fun functionWithParams() {
        inlineModelTest(
            """
                |/**
                | * Multiline
                | *
                | * Function
                | * Documentation
                | */
                |fun function(/** parameter */ x: Int) {
                |}
        """
        ) {
            with((this / "function" / "function").cast<DFunction>()) {
                comments() equals "Multiline\nFunction Documentation\n"

                name equals "function"
                parameters counts 1
                parameters.firstOrNull().assertNotNull("Parameter: ").also {
                    it.name equals "x"
                    it.type.name equals "Int"
                    it.comments() equals "parameter\n"
                }

                type.assertNotNull("Return type: ").name equals "Unit"
            }
        }
    }

    @Test
    fun functionWithNotDocumentedAnnotation() {
        inlineModelTest(
            """
                |@Suppress("FOO") fun f() {}
        """
        ) {
            with((this / "function" / "f").cast<DFunction>()) {
                with(extra[Annotations]!!.directAnnotations.entries.single().value.assertNotNull("Annotations")) {
                    this counts 1
                    with(first()) {
                        dri.classNames equals "Suppress"
                        params.entries counts 1
                        (params["names"].assertNotNull("param") as ArrayValue).value equals listOf(StringValue("FOO"))
                    }
                }
            }
        }
    }

    @Test
    fun inlineFunction() {
        inlineModelTest(
            """
                |inline fun f(a: () -> String) {}
        """
        ) {
            with((this / "function" / "f").cast<DFunction>()) {
                extra[AdditionalModifiers]!!.content.entries.single().value counts 1
                extra[AdditionalModifiers]!!.content.entries.single().value exists ExtraModifiers.KotlinOnlyModifiers.Inline
            }
        }
    }

    @Test
    fun suspendFunction() {
        inlineModelTest(
            """
                |suspend fun f() {}
        """
        ) {
            with((this / "function" / "f").cast<DFunction>()) {
                extra[AdditionalModifiers]!!.content.entries.single().value counts 1
                extra[AdditionalModifiers]!!.content.entries.single().value exists ExtraModifiers.KotlinOnlyModifiers.Suspend
            }
        }
    }

    @Test
    fun suspendInlineFunctionOrder() {
        inlineModelTest(
            """
                |suspend inline fun f(a: () -> String) {}
        """
        ) {
            with((this / "function" / "f").cast<DFunction>()) {
                extra[AdditionalModifiers]!!.content.entries.single().value counts 2
                extra[AdditionalModifiers]!!.content.entries.single().value exists ExtraModifiers.KotlinOnlyModifiers.Suspend
                extra[AdditionalModifiers]!!.content.entries.single().value exists ExtraModifiers.KotlinOnlyModifiers.Inline
            }
        }
    }

    @Test
    fun inlineSuspendFunctionOrderChanged() {
        inlineModelTest(
            """
                |inline suspend fun f(a: () -> String) {}
        """
        ) {
            with((this / "function" / "f").cast<DFunction>()) {
                with(extra[AdditionalModifiers]!!.content.entries.single().value.assertNotNull("AdditionalModifiers")) {
                    this counts 2
                    this exists ExtraModifiers.KotlinOnlyModifiers.Suspend
                    this exists ExtraModifiers.KotlinOnlyModifiers.Inline
                }
            }
        }
    }

    @OnlyDescriptors("Bug in descriptors, DRI of entry should have [EnumEntryDRIExtra]")
    @Test
    fun functionWithAnnotatedParam() {
        inlineModelTest(
            """
                |@Target(AnnotationTarget.VALUE_PARAMETER)
                |@Retention(AnnotationRetention.SOURCE)
                |@MustBeDocumented
                |public annotation class Fancy
                |
                |fun function(@Fancy notInlined: () -> Unit) {}
        """
        ) {
            with((this / "function" / "Fancy").cast<DAnnotation>()) {
                with(extra[Annotations]!!.directAnnotations.entries.single().value.assertNotNull("Annotations")) {
                    this counts 3
                    with(associate { it.dri.classNames to it }) {
                        with(this["Target"].assertNotNull("Target")) {
                            (params["allowedTargets"].assertNotNull("allowedTargets") as ArrayValue).value equals listOf(
                                EnumValue(
                                    "AnnotationTarget.VALUE_PARAMETER",
                                    DRI("kotlin.annotation", "AnnotationTarget.VALUE_PARAMETER")
                                )
                            )
                        }
                        with(this["Retention"].assertNotNull("Retention")) {
                            (params["value"].assertNotNull("value") as EnumValue) equals EnumValue(
                                "AnnotationRetention.SOURCE",
                                DRI("kotlin.annotation", "AnnotationRetention.SOURCE")
                            )
                        }
                        this["MustBeDocumented"].assertNotNull("MustBeDocumented").params.entries counts 0
                    }
                }

            }
            with((this / "function" / "function" / "notInlined").cast<DParameter>()) {
                with(this.extra[Annotations]!!.directAnnotations.entries.single().value.assertNotNull("Annotations")) {
                    this counts 1
                    with(first()) {
                        dri.classNames equals "Fancy"
                        params.entries counts 0
                    }
                }
            }
        }
    }

    @Test
    fun functionWithNoinlineParam() {
        inlineModelTest(
            """
                |fun f(noinline notInlined: () -> Unit) {}
        """
        ) {
            with((this / "function" / "f" / "notInlined").cast<DParameter>()) {
                extra[AdditionalModifiers]!!.content.entries.single().value counts 1
                extra[AdditionalModifiers]!!.content.entries.single().value exists ExtraModifiers.KotlinOnlyModifiers.NoInline
            }
        }
    }

    @OnlyDescriptors("Bug in descriptors, DRI of entry should have [EnumEntryDRIExtra]")
    @Test
    fun annotatedFunctionWithAnnotationParameters() {
        inlineModelTest(
            """
                |@Target(AnnotationTarget.VALUE_PARAMETER)
                |@Retention(AnnotationRetention.SOURCE)
                |@MustBeDocumented
                |public annotation class Fancy(val size: Int)
                |
                |@Fancy(1) fun f() {}
        """
        ) {
            with((this / "function" / "Fancy").cast<DAnnotation>()) {
                constructors counts 1
                with(constructors.first()) {
                    parameters counts 1
                    with(parameters.first()) {
                        type.name equals "Int"
                        name equals "size"
                    }
                }

                with(extra[Annotations]!!.directAnnotations.entries.single().value.assertNotNull("Annotations")) {
                    this counts 3
                    with(associate { it.dri.classNames to it }) {
                        with(this["Target"].assertNotNull("Target")) {
                            (params["allowedTargets"].assertNotNull("allowedTargets") as ArrayValue).value equals listOf(
                                EnumValue(
                                    "AnnotationTarget.VALUE_PARAMETER",
                                    DRI("kotlin.annotation", "AnnotationTarget.VALUE_PARAMETER")
                                )
                            )
                        }
                        with(this["Retention"].assertNotNull("Retention")) {
                            (params["value"].assertNotNull("value") as EnumValue) equals EnumValue(
                                "AnnotationRetention.SOURCE",
                                DRI("kotlin.annotation", "AnnotationRetention.SOURCE")
                            )
                        }
                        this["MustBeDocumented"].assertNotNull("MustBeDocumented").params.entries counts 0
                    }
                }

            }
            with((this / "function" / "f").cast<DFunction>()) {
                with(this.extra[Annotations]!!.directAnnotations.entries.single().value.assertNotNull("Annotations")) {
                    this counts 1
                    with(this.first()) {
                        dri.classNames equals "Fancy"
                        params.entries counts 1
                        (params["size"] as IntValue).value equals 1
                    }
                }
            }
        }
    }

    @Test
    fun functionWithDefaultStringParameter() {
        inlineModelTest(
            """
                |/src/main/kotlin/function/Test.kt
                |package function
                |fun f(x: String = "") {}
        """
        ) {
            with((this / "function" / "f").cast<DFunction>()) {
                parameters.forEach { p ->
                    p.name equals "x"
                    p.type.name.assertNotNull("Parameter type: ") equals "String"
                    p.extra[DefaultValue]?.expression?.get(sourceSets.single()) equals StringConstant("")
                }
            }
        }
    }

    @Test
    fun functionWithDefaultFloatParameter() {
        inlineModelTest(
            """
                |/src/main/kotlin/function/Test.kt
                |package function
                |fun f(x: Float = 3.14f) {}
        """
        ) {
            with((this / "function" / "f").cast<DFunction>()) {
                parameters.forEach { p ->
                    p.name equals "x"
                    p.type.name.assertNotNull("Parameter type: ") equals "Float"
                    p.extra[DefaultValue]?.expression?.get(sourceSets.single()) equals FloatConstant(3.14f)
                }
            }
        }
    }

    @Test
    fun sinceKotlin() {
        inlineModelTest(
            """
                |/**
                | * Quite useful [String]
                | */
                |@SinceKotlin("1.1")
                |fun f(): String = "1.1 rulezz"
                """
        ) {
            with((this / "function" / "f").cast<DFunction>()) {
                with(extra[Annotations]!!.directAnnotations.entries.single().value.assertNotNull("Annotations")) {
                    this counts 1
                    with(first()) {
                        dri.classNames equals "SinceKotlin"
                        params.entries counts 1
                        (params["version"].assertNotNull("version") as StringValue).value equals "1.1"
                    }
                }
            }
        }
    }

    @Test
    @OnlySymbols("context parameters")
    fun `function with context parameters should have them, correct DRI, and documentation`() {
            inlineModelTest(
                """
                |/src/sample/ParentInKotlin.kt
                |package sample
                |
                |/** Some doc */
                |context(s: String, _:Int)
                |fun Int.f(b: Boolean):String = if(b) "Hello" else "Bye"
                """.trimIndent()
            ) {
                with((this / "sample" / "f").cast<DFunction>()) {
                    dri.callable equals org.jetbrains.dokka.links.Callable(
                        name = "f",
                        receiver = TypeConstructor("kotlin.Int", emptyList()),
                        params = listOf(TypeConstructor("kotlin.Boolean", emptyList())),
                        contextParameters = listOf(
                            TypeConstructor("kotlin.String", emptyList()),
                            TypeConstructor("kotlin.Int", emptyList())
                        )
                    )
                    dri.toString() equals "sample//f/kotlin.String#kotlin.Int#kotlin.Int#kotlin.Boolean/PointingToDeclaration/"
                    documentation.values.single().children.single().text() equals "Some doc\n"
                    contextParameters counts 2
                    contextParameters[0].name equals "s"
                    contextParameters[1].name equals "_"
                    with(contextParameters[0].type.assertNotNull("type")) {
                        name equals "String"
                    }
                    with(contextParameters[1].type.assertNotNull("type")) {
                        name equals "Int"
                    }
                }
            }
        }

}
