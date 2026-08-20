package com.github.mystery2099.woodenAccentsMod.data.client

import net.minecraft.data.client.BlockStateVariant
import net.minecraft.data.client.VariantSetting
import net.minecraft.data.client.VariantSettings
import net.minecraft.util.Identifier

object BlockStateVariantUtil {
    infix fun BlockStateVariant.and(other: BlockStateVariant): BlockStateVariant =
        BlockStateVariant.union(this, other)

    fun BlockStateVariant.unifiedWith(vararg others: BlockStateVariant): BlockStateVariant = others.fold(this, BlockStateVariant::union)

    operator fun BlockStateVariant.plus(other: BlockStateVariant) = and(other)

    fun BlockStateVariant.putModel(model: Identifier): BlockStateVariant = this.put(VariantSettings.MODEL, model)

    fun Identifier.asBlockStateVariant() = BlockStateVariant().putModel(this)

    fun BlockStateVariant.withYRotationOf(rotation: VariantSettings.Rotation) = and(
        BlockStateVariant().put(VariantSettings.Y, rotation)
    )

    fun BlockStateVariant.withXRotationOf(rotation: VariantSettings.Rotation) = and(
        BlockStateVariant().put(VariantSettings.X, rotation)
    )

    fun BlockStateVariant.uvLock(): BlockStateVariant = put(VariantSettings.UVLOCK, true)

    operator fun <T> BlockStateVariant.set(key: VariantSetting<T>, value: T): BlockStateVariant = put(key, value)
}
