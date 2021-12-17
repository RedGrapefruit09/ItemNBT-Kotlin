package kotlinx.itemnbt3

import com.redgrapefruit.itemnbt3.CustomData
import com.redgrapefruit.itemnbt3.DataClient
import com.redgrapefruit.itemnbt3.linking.DataLink
import com.redgrapefruit.itemnbt3.specification.DataCompound
import com.redgrapefruit.itemnbt3.specification.Specification
import net.minecraft.item.ItemStack

/**
 * Applies some computation to data managed with the custom-serialization (CS) method.
 *
 * This is a global-function wrapper for the first overload of [DataClient.use].
 */
fun <T> computeData(factory: () -> T, stack: ItemStack, computation: (T) -> Unit) where T : CustomData {
    DataClient.use(factory, stack, computation)
}

/**
 * Applies some computation to data managed with the specification-serialization (SS) method.
 *
 * This is a global-function wrapper for the second overload of [DataClient.use].
 */
fun computeData(stack: ItemStack, specification: Specification, computation: (DataCompound) -> Unit) {
    DataClient.use(stack, specification, computation)
}

/**
 * Applies some computation to data managed with the linked-specification-serialization (LSS) method.
 *
 * This is a global-function wrapper for the third overload of [DataClient.use].
 */
fun <T> computeData(stack: ItemStack, specification: Specification, link: DataLink, instance: T, computation: (T) -> Unit) {
    DataClient.use(stack, specification, link, instance!!, computation)
}
