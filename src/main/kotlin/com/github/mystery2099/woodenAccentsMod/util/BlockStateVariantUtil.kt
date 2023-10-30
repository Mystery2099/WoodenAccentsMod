package com.github.mystery2099.woodenAccentsMod.util

import net.minecraft.data.client.BlockStateVariant
import net.minecraft.data.client.VariantSetting
import net.minecraft.data.client.VariantSettings
import net.minecraft.util.Identifier

object BlockStateVariantUtil {
    infix fun BlockStateVariant.unifiedWith(other: BlockStateVariant): BlockStateVariant =
        BlockStateVariant.union(this, other)

    fun BlockStateVariant.unifiedWith(vararg others: BlockStateVariant): BlockStateVariant = others.fold(this, BlockStateVariant::union)
    infix fun BlockStateVariant.plus(other: BlockStateVariant) = unifiedWith(other)

    infix fun BlockStateVariant.putModel(model: Identifier): BlockStateVariant = this.put(VariantSettings.MODEL, model)
    fun Identifier.asBlockStateVariant() = BlockStateVariant().putModel(this)

    infix fun BlockStateVariant.withYRotationOf(rotation: VariantSettings.Rotation) = unifiedWith(
        BlockStateVariant().put(VariantSettings.Y, rotation)
    )

    infix fun BlockStateVariant.withXRotationOf(rotation: VariantSettings.Rotation) = unifiedWith(
        BlockStateVariant().put(VariantSettings.X, rotation)
    )

    fun BlockStateVariant.uvLock(): BlockStateVariant = put(VariantSettings.UVLOCK, true)
    operator fun <T> BlockStateVariant.set(key: VariantSetting<T>, value: T): BlockStateVariant = put(key, value)
}