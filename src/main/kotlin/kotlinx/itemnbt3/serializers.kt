package kotlinx.itemnbt3

import com.redgrapefruit.itemnbt3.serializer.SerializerRegistry
import com.redgrapefruit.itemnbt3.serializer.TypeSerializer
import net.minecraft.nbt.NbtCompound

/**
 * Registers a [TypeSerializer] so it can be discovered and automatically used by the reflection generator.
 *
 * Wraps the [SerializerRegistry.register] method and uses a reified generic parameter to avoid passing in a [Class].
 *
 * Example:
 * ```kotlin
 *  // TypeSerializers should typically be object's in Kotlin for convenience
 * registerSerializer<MyType>(SerializerForMyType)
 * ```
 */
inline fun <reified T> registerSerializer(serializer: TypeSerializer<T>) {
    SerializerRegistry.register(T::class.java, serializer)
}

/** Marks the [TypeSerializer] DSL */
@DslMarker
annotation class TypeSerializerDsl

/**
 * Creates a [TypeSerializer] with a simple Kotlin DSL.
 *
 * For the most cases, **overriding the class is preferred**, but feel free to use this method too.
 */
@TypeSerializerDsl
inline fun <T> typeSerializer(build: TypeSerializerBuildScope<T>.() -> Unit): TypeSerializer<T> {
    val scope = TypeSerializerBuildScope<T>()
    scope.build()
    return scope.create()
}

class TypeSerializerBuildScope<T> {
    private var readNbt: ((String, NbtCompound) -> T)? = null
    private var writeNbt: ((String, NbtCompound, T) -> Unit)? = null

    /**
     * Assigns a lambda function for reading an input of this type from disk.
     *
     * See [TypeSerializer.readNbt].
     */
    fun onRead(readNbt: (String, NbtCompound) -> T) {
        this.readNbt = readNbt
    }

    /**
     * Assigns a lambda function for writing an input of this type to disk.
     *
     * See [TypeSerializer.writeNbt].
     */
    fun onWrite(writeNbt: (String, NbtCompound, T) -> Unit) {
        this.writeNbt = writeNbt
    }

    @PublishedApi
    internal fun create(): TypeSerializer<T> {
        requireNotNull(readNbt) { "Cannot create TypeSerializer with DSL. Reading function not set" }
        requireNotNull(writeNbt) { "Cannot create TypeSerializer with DSL. Writing function not set" }

        return LambdaTypeSerializer(readNbt!!, writeNbt!!)
    }
}

/**
 * A [TypeSerializer] implementation that uses lambdas instead of simply overriding methods.
 */
internal class LambdaTypeSerializer<T>(
    private val readNbt: (String, NbtCompound) -> T,
    private val writeNbt: (String, NbtCompound, T) -> Unit
) : TypeSerializer<T> {

    override fun readNbt(key: String, nbt: NbtCompound): T {
        return readNbt.invoke(key, nbt)
    }

    override fun writeNbt(key: String, nbt: NbtCompound, value: T) {
        writeNbt.invoke(key, nbt, value)
    }
}
