package com.github.mystery2099.woodenAccentsMod.data.client

import net.minecraft.data.models.blockstates.Variant
import net.minecraft.data.models.blockstates.VariantProperties
import net.minecraft.data.models.blockstates.VariantProperty
import net.minecraft.resources.ResourceLocation

object BlockStateVariantUtil {
    infix fun Variant.and(other: Variant): Variant =
        Variant.merge(this, other)

    fun Variant.unifiedWith(vararg others: Variant): Variant = others.fold(this, Variant::merge)

    operator fun Variant.plus(other: Variant) = and(other)

    fun Variant.putModel(model: ResourceLocation): Variant = this.with(VariantProperties.MODEL, model)

    fun ResourceLocation.asBlockStateVariant() = Variant().putModel(this)

    fun Variant.withYRotationOf(rotation: VariantProperties.Rotation) = and(
        Variant().with(VariantProperties.Y_ROT, rotation)
    )

    fun Variant.withXRotationOf(rotation: VariantProperties.Rotation) = and(
        Variant().with(VariantProperties.X_ROT, rotation)
    )

    fun Variant.uvLock(): Variant = with(VariantProperties.UV_LOCK, true)

    operator fun <T> Variant.set(key: VariantProperty<T>, value: T): Variant = with(key, value)
}
