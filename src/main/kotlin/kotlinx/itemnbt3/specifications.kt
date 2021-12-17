package kotlinx.itemnbt3

import com.redgrapefruit.itemnbt3.serializer.TypeSerializer
import com.redgrapefruit.itemnbt3.specification.Specification

/** Marks the [Specification] DSL */
@DslMarker
annotation class SpecificationDsl

/**
 * Creates a new [Specification] with a Kotlin DSL.
 *
 * @param name The name of the [Specification].
 */
@SpecificationDsl
inline fun specification(name: String, build: SpecificationBuildScope.() -> Unit): Specification {
    val scope = SpecificationBuildScope(Specification.builder(name))
    scope.build()
    return scope.create()
}

class SpecificationBuildScope(
    @PublishedApi
    internal val builder: Specification.Builder) {

    /**
     * Adds another [Specification] within the current one, allowing for unlimited levels of nesting.
     */
    inline fun child(key: String, build: SpecificationBuildScope.() -> Unit) {
        val scope = SpecificationBuildScope(Specification.builder(key))
        scope.build()
        builder.add(key, scope.create())
    }

    /**
     * Adds a field to the [Specification] with a custom-made [TypeSerializer].
     */
    fun field(key: String, serializer: TypeSerializer<*>) {
        builder.add(key, serializer)
    }

    // Primitives

    fun byte(key: String) {
        builder.addByte(key)
    }

    fun short(key: String) {
        builder.addShort(key)
    }

    fun int(key: String) {
        builder.addInt(key)
    }

    fun long(key: String) {
        builder.addLong(key)
    }

    fun uuid(key: String) {
        builder.addUUID(key)
    }

    fun float(key: String) {
        builder.addFloat(key)
    }

    fun double(key: String) {
        builder.addDouble(key)
    }

    fun string(key: String) {
        builder.addString(key)
    }

    fun bool(key: String) {
        builder.addBool(key)
    }

    // Arrays

    fun byteArray(key: String) {
        builder.addByteArray(key)
    }

    fun intArray(key: String) {
        builder.addIntArray(key)
    }

    fun longArray(key: String) {
        builder.addLongArray(key)
    }

    @PublishedApi
    internal fun create() = builder.build()
}

/**
 * Generates a [Specification] instance with reflection.
 *
 * This is a wrapper for the [Specification.create] method that uses reified generics to avoid passing in [Class]es.
 *
 * Cache the result of this operation if it's needed more than once or use the [getOrCreateSpecification] method that
 * implements caching for you.
 *
 * Example:
 * ```kotlin
 * val MY_SPECIFICATION = generateSpecification<MyType>()
 * ```
 */
inline fun <reified T> generateSpecification(): Specification {
    return Specification.create(T::class.java)
}

@PublishedApi
internal val specificationCache: MutableMap<Class<*>, Specification> = mutableMapOf()

/**
 * Tries to fetch a [Specification] instance from cache, if it doesn't exist, creates it with the means of reflection.
 *
 * If you often need your [Specification] instance and don't want to add an extra property for manually caching it,
 * this method is an optimal solution to the problem.
 */
inline fun <reified T> getOrCreateSpecification(): Specification {
    val javaClass = T::class.java

    if (!specificationCache.containsKey(javaClass)) {
        specificationCache[javaClass] = Specification.create(javaClass)
    }

    return specificationCache[javaClass]!!
}
