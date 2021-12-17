package kotlinx.itemnbt3

import com.redgrapefruit.itemnbt3.CustomData

/**
 * A Kotlin version of [CustomData] that receives the NBT category in its constructor and automatically
 * overloads the [getNbtCategory] method, returning that value.
 *
 * [KCustomData] is fully **compatible** with the built-in [CustomData] and can be passed into any method.
 */
abstract class KCustomData(private val category: String) : CustomData {
    final override fun getNbtCategory(): String = category
}
