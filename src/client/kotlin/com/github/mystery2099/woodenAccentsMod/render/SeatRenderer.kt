package com.github.mystery2099.woodenAccentsMod.render

import com.github.mystery2099.woodenAccentsMod.entity.custom.SeatEntity
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.util.Identifier

class SeatRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SeatEntity>(ctx) {
    override fun getTexture(entity: SeatEntity?): Identifier? = null
    override fun shouldRender(entity: SeatEntity?, frustum: Frustum?, x: Double, y: Double, z: Double): Boolean = false
}