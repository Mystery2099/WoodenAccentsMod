package com.github.mystery2099.woodenAccentsMod.util

import net.minecraft.data.client.BlockStateVariant
import net.minecraft.data.client.VariantSetting
import net.minecraft.data.client.VariantSettings
import net.minecraft.util.Identifier

/**
 * [BlockStateVariantUtil] is a utility object for working with [BlockStateVariant]s.
 */
object BlockStateVariantUtil {
    /**
     * Combines the current [BlockStateVariant] with another.
     *
     * @param other The other [BlockStateVariant] to combine with.
     * @return The combined [BlockStateVariant]
     * @see BlockStateVariant.union
     * @see BlockStateVariant.plus
     */
    infix fun BlockStateVariant.and(other: BlockStateVariant): BlockStateVariant =
        BlockStateVariant.union(this, other)

    /**
     * Combines the current [BlockStateVariant] with an array of other [BlockStateVariant]s.
     *
     * @param others The array of other [BlockStateVariant]s to combine with.
     * @return The combined [BlockStateVariant].
     */
    fun BlockStateVariant.unifiedWith(vararg others: BlockStateVariant): BlockStateVariant = others.fold(this, BlockStateVariant::union)

    /**
     * Combines the current [BlockStateVariant] with another.
     *
     * @param other The other [BlockStateVariant] to combine with.
     * @return The combined [BlockStateVariant].
     */
    operator fun BlockStateVariant.plus(other: BlockStateVariant) = and(other)

    /**
     * Associates a model with the current [BlockStateVariant]
     *
     * @param model The [Identifier] of the model to associate with the [BlockStateVariant].
     * @return [BlockStateVariant]with the model setting applied.
     */
    fun BlockStateVariant.putModel(model: Identifier): BlockStateVariant = this.put(VariantSettings.MODEL, model)

    /**
     * Converts an [Identifier] to a [BlockStateVariant] by associating it with a model.
     *
     * @return A [BlockStateVariant] with the model setting based on the provided [Identifier].
     */
    fun Identifier.asBlockStateVariant() = BlockStateVariant().putModel(this)

    /**
     * Combines the current [BlockStateVariant] with a Y-axis rotation setting.
     *
     * @param rotation The Y-axis rotation setting to combine with.
     * @return The combined [BlockStateVariant].
     */
    fun BlockStateVariant.withYRotationOf(rotation: VariantSettings.Rotation) = and(
        BlockStateVariant().put(VariantSettings.Y, rotation)
    )

    /**
     * Combines the current [BlockStateVariant] with an X-axis rotation setting.
     *
     * @param rotation The X-axis rotation setting to combine with.
     * @return The combined [BlockStateVariant].
     */
    fun BlockStateVariant.withXRotationOf(rotation: VariantSettings.Rotation) = and(
        BlockStateVariant().put(VariantSettings.X, rotation)
    )

    /**
     * Applies UV lock to the current [BlockStateVariant].
     *
     * @return The [BlockStateVariant] with UV lock applied.
     */
    fun BlockStateVariant.uvLock(): BlockStateVariant = put(VariantSettings.UVLOCK, true)

    /**
     * Sets a custom variant setting for the current [BlockStateVariant].
     *
     * @param key The variant setting key.
     * @param value The value associated with the key.
     * @return The [BlockStateVariant] with the custom setting applied.
     */
    operator fun <T> BlockStateVariant.set(key: VariantSetting<T>, value: T): BlockStateVariant = put(key, value)
}