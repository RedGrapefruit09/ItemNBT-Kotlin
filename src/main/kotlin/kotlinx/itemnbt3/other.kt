package kotlinx.itemnbt3

import com.redgrapefruit.itemnbt3.specification.DataCompound
import com.redgrapefruit.itemnbt3.util.Utilities
import net.minecraft.nbt.NbtCompound

/**
 * Clears all values in this [NbtCompound].
 *
 * The built-in [NbtCompound] class does not provide the ability to do such operation, hence a mixin is used.
 *
 * This method wraps [Utilities.clearNbt] as a Kotlin extension function.
 */
fun NbtCompound.clear() {
    Utilities.clearNbt(this)
}

/**
 * Creates an [NbtCompound] and puts it in the current [NbtCompound] as a child object.
 *
 * This method wraps [Utilities.getOrCreateSubNbt] as a Kotlin extension function.
 */
fun NbtCompound.getOrCreateSubNbt(name: String): NbtCompound {
    return Utilities.getOrCreateSubNbt(this, name)
}

/**
 * Retrieves a value from a [DataCompound] by its key with a nice indexing operator. Works with any type.
 *
 * Example:
 * ```kotlin
 * val value = dataCompound["key"]
 * ```
 */
operator fun <T> DataCompound.get(key: String): T {
    return getAny(key)
}

/**
 * Sets a value by its key with a nice indexing operator. Works with any type.
 *
 * Example:
 * ```kotlin
 * dataCompound["key"] = "value"
 * ```
 */
operator fun <T> DataCompound.set(key: String, value: T) {
    putAny(key, value!!)
}

/**
 * Constructs an object by the given [initialValue] and applies an [operation] to it.
 */
inline fun <T> make(initialValue: T, operation: (T) -> Unit): T {
    operation.invoke(initialValue)
    return initialValue
}

/**
 * Constructs an object with a lambda expression.
 */
inline fun <T> make(operation: () -> T): T {
    return operation.invoke()
}
