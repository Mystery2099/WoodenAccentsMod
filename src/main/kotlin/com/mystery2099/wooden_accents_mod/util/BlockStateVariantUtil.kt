package com.mystery2099.wooden_accents_mod.util

import net.minecraft.data.client.BlockStateVariant
import net.minecraft.data.client.VariantSettings
import net.minecraft.util.Identifier

object BlockStateVariantUtil {
    infix fun BlockStateVariant.unionWith(other: BlockStateVariant): BlockStateVariant = BlockStateVariant.union(this, other)

    fun BlockStateVariant.unionWith(vararg others: BlockStateVariant) = others.fold(this, BlockStateVariant::union)
    infix fun BlockStateVariant.plus(other: BlockStateVariant) = unionWith(other)

    infix fun BlockStateVariant.putModel(model: Identifier): BlockStateVariant = put(VariantSettings.MODEL, model)
    infix fun Identifier.asBlockStateVariant() = BlockStateVariant().putModel(this)

    infix fun BlockStateVariant.withYRotationOf(rotation: VariantSettings.Rotation) = unionWith(
        BlockStateVariant().put(VariantSettings.Y, rotation)
    )
    infix fun BlockStateVariant.withXRotationOf(rotation: VariantSettings.Rotation) = unionWith(
        BlockStateVariant().put(VariantSettings.X, rotation)
    )
}