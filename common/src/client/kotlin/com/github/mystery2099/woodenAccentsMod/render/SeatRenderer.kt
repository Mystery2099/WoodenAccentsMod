package com.github.mystery2099.woodenAccentsMod.render

import com.github.mystery2099.woodenAccentsMod.entity.custom.SeatEntity
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation

@Suppress("WRONG_TYPE_FOR_JAVA_OVERRIDE", "TYPE_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SeatRenderer(ctx: EntityRendererProvider.Context) : EntityRenderer<SeatEntity>(ctx) {
    override fun getTextureLocation(entity: SeatEntity): ResourceLocation? = null
    override fun shouldRender(entity: SeatEntity, frustum: Frustum, x: Double, y: Double, z: Double): Boolean = false
}