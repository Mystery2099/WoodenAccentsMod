package com.mystery2099.wooden_accents_mod.util

import net.minecraft.block.Block
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max
import kotlin.math.min

object VoxelShapeHelper {
    private inline val Double.limited
        get() = this.limit()

    private inline val VoxelShape.minX
        get() = getMin(Direction.Axis.X)
    private inline val VoxelShape.minY
        get() = getMin(Direction.Axis.Y)
    private inline val VoxelShape.minZ
        get() = getMin(Direction.Axis.Z)
    private inline val VoxelShape.maxX
        get() = getMax(Direction.Axis.X)
    private inline val VoxelShape.maxY
        get() = getMax(Direction.Axis.Y)
    private inline val VoxelShape.maxZ
        get() = getMax(Direction.Axis.Z)

    infix fun VoxelShape.unifiedWith(otherShape: VoxelShape): VoxelShape = VoxelShapes.union(this, otherShape)
    fun VoxelShape.unifiedWith(vararg otherShapes: VoxelShape): VoxelShape = union(this, *otherShapes)
    infix operator fun VoxelShape.plus(otherShape: VoxelShape): VoxelShape = VoxelShapes.union(this, otherShape)
    fun setMaxHeight(source: VoxelShape, height: Double): VoxelShape {
        val result = AtomicReference(VoxelShapes.empty())
        source.forEachBox { minX: Double, minY: Double, minZ: Double, maxX: Double, _: Double, maxZ: Double ->
            val shape = VoxelShapes.cuboid(minX, minY, minZ, maxX, height, maxZ)
            result.set(result.get() + shape)
        }
        return result.get()
    }

    infix fun limitHorizontal(source: VoxelShape): VoxelShape {
        val result = AtomicReference(VoxelShapes.empty())
        source.forEachBox { minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double ->
            val shape = VoxelShapes.cuboid(minX.limited, minY, minZ.limited, maxX.limited, maxY, maxZ.limited)
            result.set(result.get() + shape)
        }
        return result.get()
    }

    /*//Do not use on complex VoxelShapes
    fun rotate(voxelShape: VoxelShape, direction: VoxelShapeTransformation): VoxelShape {
        val adjustedValues = adjustValues(direction, voxelShape.minX, voxelShape.minZ, voxelShape.maxX, voxelShape.maxZ)
        return VoxelShapes.cuboid(
            adjustedValues[0], voxelShape.minY, adjustedValues[1],
            adjustedValues[2], voxelShape.maxY, adjustedValues[3]
        )
    }*/

    val VoxelShape.rotatedLeft: VoxelShape
        get() = this.rotated(VoxelShapeTransformation.ROTATE_LEFT)
    val VoxelShape.flipped: VoxelShape
        get() = this.rotated(VoxelShapeTransformation.FLIP_HORIZONTAL)
    val VoxelShape.rotatedRight: VoxelShape
        get() = this.rotated(VoxelShapeTransformation.ROTATE_RIGHT)
    fun VoxelShape.rotated(direction: VoxelShapeTransformation): VoxelShape {
        val shapes = mutableListOf(VoxelShapes.empty())
        this.forEachBox { minX, minY, minZ, maxX, maxY, maxZ ->
            shapes += voxelShapeFromAdjustedValues(
                direction = direction,
                minX = minX,
                minY = minY,
                minZ = minZ,
                maxX = maxX,
                maxY = maxY,
                maxZ = maxZ
            )
        }
        return shapes.reduce { a, b -> VoxelShapes.union(a, b) }
    }

    fun combine(function: BooleanBiFunction, vararg voxelShapes: VoxelShape): VoxelShape {
        return voxelShapes.reduce { a, b -> VoxelShapes.combine(a, b, function) }
    }

    fun union(vararg voxelShapes: VoxelShape): VoxelShape = voxelShapes.reduce { a, b -> VoxelShapes.union(a, b) }

    //Adjuts values depending on the transformation, then returns a new VoxelShape from those values
    private fun voxelShapeFromAdjustedValues(
        direction: VoxelShapeTransformation,
        minX: Double,
        minY: Double,
        minZ: Double,
        maxX: Double,
        maxY: Double,
        maxZ: Double
    ) = when (direction) {
        VoxelShapeTransformation.FLIP_HORIZONTAL -> VoxelShapes.cuboid(
            /* minX = */ 1.0f - maxX,
            /* minY = */ minY,
            /* minZ = */ 1.0f - maxZ,
            /* maxX = */ 1.0f - minX,
            /* maxY = */ maxY,
            /* maxZ = */ 1.0f - minZ
        )

        VoxelShapeTransformation.ROTATE_RIGHT -> VoxelShapes.cuboid(
            /* minX = */ minZ,
            /* minY = */ minY,
            /* minZ = */ 1.0f - maxX,
            /* maxX = */ maxZ,
            /* maxY = */ maxY,
            /* maxZ = */ 1.0f - minX
        )

        VoxelShapeTransformation.ROTATE_LEFT -> VoxelShapes.cuboid(
            /* minX = */ 1.0f - maxZ,
            /* minY = */ minY,
            /* minZ = */ minX,
            /* maxX = */ 1.0f - minZ,
            /* maxY = */ maxZ,
            /* maxZ = */ maxX
        )

        else -> VoxelShapes.cuboid(minX, minY, minZ, maxX, maxY, maxZ)
    }

    private fun Double.limit() = max(0.0, min(1.0, this))
    fun createCuboidShape(
        minX: Number,
        minY: Number,
        minZ: Number,
        maxX: Number,
        maxY: Number,
        maxZ: Number
    ): VoxelShape = Block.createCuboidShape(
        minX.toDouble(),
        minY.toDouble(),
        minZ.toDouble(),
        maxX.toDouble(),
        maxY.toDouble(),
        maxZ.toDouble()
    )
}

enum class VoxelShapeTransformation {
    ROTATE_LEFT, ROTATE_RIGHT, FLIP_HORIZONTAL, ROTATE_UP, ROTATE_DOWN
}
