package com.github.mystery2099.woodenAccentsMod.render

import com.github.mystery2099.woodenAccentsMod.block.custom.BracketShelfBlock
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.BracketShelfBlockEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.RotationAxis

/**
 * Draws each stack stored in a shelf flat against its front face, like vanilla
 * wall-mounted shelves. Slot indices run left to right when viewed from the
 * shelf's front (the left slot is on the counterclockwise side of `facing`).
 */
class BracketShelfBlockEntityRenderer(context: BlockEntityRendererFactory.Context) :
    BlockEntityRenderer<BracketShelfBlockEntity> {

    private val itemRenderer = context.itemRenderer

    override fun render(
        blockEntity: BracketShelfBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val world = blockEntity.world ?: return
        val facing = blockEntity.cachedState[BracketShelfBlock.facing]
        val leftDirection = facing.rotateYCounterclockwise()
        val slotsCount = BracketShelfBlockEntity.SLOT_COUNT

        for (slot in 0 until slotsCount) {
            val stack = blockEntity.getStack(slot)
            if (stack.isEmpty) continue

            val model = itemRenderer.getModel(stack, world, null, slot)
            val scale = if (model.hasDepth()) BLOCK_ITEM_SCALE else FLAT_ITEM_SCALE

            // Distance from the block's center along the shelf's left (-) axis.
            val slotOffset = (1.0 - slot) / slotsCount
            matrices.push()
            matrices.translate(
                0.5 + leftDirection.offsetX * slotOffset - facing.offsetX * DEPTH_OFFSET,
                ITEM_Y,
                0.5 + leftDirection.offsetZ * slotOffset - facing.offsetZ * DEPTH_OFFSET
            )
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(itemYaw(facing)))
            matrices.scale(scale, scale, scale)
            itemRenderer.renderItem(
                stack,
                ModelTransformationMode.FIXED,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                world,
                0
            )
            matrices.pop()
        }
    }

    /** Same readable-from-`facing` rotation vanilla lecterns use. */
    private fun itemYaw(facing: Direction): Float = -facing.rotateYClockwise().asRotation()

    companion object {
        /** Top of the 11..13 shelf plank, where items rest. */
        private const val ITEM_Y = 0.875

        /** Moves items toward the wall and clear of the shelf's front edge. */
        private const val DEPTH_OFFSET = 0.1875
        private const val BLOCK_ITEM_SCALE = 0.5f
        private const val FLAT_ITEM_SCALE = 0.625f
    }
}
