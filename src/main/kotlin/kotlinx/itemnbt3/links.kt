package kotlinx.itemnbt3

import com.redgrapefruit.itemnbt3.linking.DataLink

/**
 * Generates a [DataLink] instance with reflection.
 *
 * This is a wrapper for the [DataLink.create] method that uses reified generics to avoid passing in [Class]es.
 *
 * Cache the result of this operator if it's needed more than once or use the [getOrCreateDataLink] method that
 * implements caching for you.
 *
 * Example:
 * ```kotlin
 * val MY_DATA_LINK = generateDataLink<MyType>()
 * ```
 */
inline fun <reified T> generateDataLink(): DataLink {
    return DataLink.create(T::class.java)
}

@PublishedApi
internal val linkCache: MutableMap<Class<*>, DataLink> = mutableMapOf()

/**
 * Tries to fetch a [DataLink] instance from cache, if it doesn't exist, creates it with reflection.
 *
 * If you often need your [DataLink] instance and don't want to add an extra property for manually caching it,
 * this method is an optimal solution to the problem.
 */
inline fun <reified T> getOrCreateDataLink(): DataLink {
    val javaClass = T::class.java

    if (!linkCache.containsKey(javaClass)) {
        linkCache[javaClass] = DataLink.create(javaClass)
    }

    return linkCache[javaClass]!!
}
