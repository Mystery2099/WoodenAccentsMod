package com.mystery2099.wooden_accents_mod.util

import net.minecraft.data.client.BlockStateVariant
import net.minecraft.data.client.VariantSettings
import net.minecraft.util.Identifier

object BlockStateVariantUtil {
    fun BlockStateVariant.unionWith(other: BlockStateVariant): BlockStateVariant = BlockStateVariant.union(this, other)

    fun BlockStateVariant.unionWith(vararg others: BlockStateVariant) = others.fold(this, BlockStateVariant::union)

    fun BlockStateVariant.putModel(model: Identifier): BlockStateVariant = put(VariantSettings.MODEL, model)
    fun Identifier.asBlockStateVariant() = BlockStateVariant().putModel(this)

    fun BlockStateVariant.withYRotationOf(rotation: VariantSettings.Rotation) = unionWith(
        BlockStateVariant().put(VariantSettings.Y, rotation)
    )
    fun BlockStateVariant.withXRotationOf(rotation: VariantSettings.Rotation) = unionWith(
        BlockStateVariant().put(VariantSettings.X, rotation)
    )
}