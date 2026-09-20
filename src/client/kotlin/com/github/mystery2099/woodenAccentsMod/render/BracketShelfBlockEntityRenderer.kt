package com.github.mystery2099.woodenAccentsMod.render

import com.github.mystery2099.woodenAccentsMod.block.custom.BracketShelfBlock
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.BracketShelfBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis


/**
 * Draws each stack stored in a shelf flat against its front face, like vanilla
 * wall-mounted shelves. Slot indices run left to right when viewed from the
 * shelf's front (the left slot is on the clockwise side of `facing`).
 */
class BracketShelfBlockEntityRenderer(context: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<BracketShelfBlockEntity> {

    private val itemRenderer = context.itemRenderer

    override fun render(
        blockEntity: BracketShelfBlockEntity,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        overlay: Int
    ) {
        val world = blockEntity.level ?: return
        val facing = blockEntity.blockState.getValue(BracketShelfBlock.facing)
        val leftDirection = facing.clockWise
        val slotsCount = BracketShelfBlockEntity.SLOT_COUNT

        for (slot in 0 until slotsCount) {
            val stack = blockEntity.getItem(slot)
            if (stack.isEmpty) continue

            val model = itemRenderer.getModel(stack, world, null, slot)
            val scale = if (model.usesBlockLight()) BLOCK_ITEM_SCALE else FLAT_ITEM_SCALE

            // Distance from the block's center along the shelf's left (-) axis.
            val slotOffset = (1.0 - slot) / slotsCount
            matrices.pushPose()
            matrices.translate(
                0.5 + leftDirection.stepX * slotOffset - facing.stepX * DEPTH_OFFSET,
                ITEM_Y,
                0.5 + leftDirection.stepZ * slotOffset - facing.stepZ * DEPTH_OFFSET
            )
            matrices.mulPose(Axis.YP.rotationDegrees(itemYaw(facing)))
            matrices.scale(scale, scale, scale)
            itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                light,
                OverlayTexture.NO_OVERLAY,
                matrices,
                vertexConsumers,
                world,
                0
            )
            matrices.popPose()
        }
    }

    /** Same readable-from-`facing` rotation vanilla lecterns use. */
    private fun itemYaw(facing: Direction): Float = -facing.clockWise.toYRot()

    companion object {
        /** Top of the 11..13 shelf plank, where items rest. */
        private const val ITEM_Y = 0.875

        /** Moves items toward the wall and clear of the shelf's front edge. */
        private const val DEPTH_OFFSET = 0.1875
        private const val BLOCK_ITEM_SCALE = 0.5f
        private const val FLAT_ITEM_SCALE = 0.625f
    }
}
