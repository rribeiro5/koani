package io.github.rribeiro5.koani

import kotlin.annotation.AnnotationRetention
import kotlin.annotation.AnnotationTarget

/**
 * Marks declarations that are experimental and may change in the future.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is experimental and its signature may change in the future."
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.CONSTRUCTOR
)
public annotation class ExperimentalKoaniApi
