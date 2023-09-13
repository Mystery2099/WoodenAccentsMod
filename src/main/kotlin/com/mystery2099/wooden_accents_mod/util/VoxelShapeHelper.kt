package com.mystery2099.wooden_accents_mod.util

import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.plus
import net.minecraft.block.Block
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max
import kotlin.math.min

object VoxelShapeHelper {

    /*//Do not use on complex VoxelShapes
    fun rotate(voxelShape: VoxelShape, direction: VoxelShapeTransformation): VoxelShape {
        val adjustedValues = adjustValues(direction, voxelShape.minX, voxelShape.minZ, voxelShape.maxX, voxelShape.maxZ)
        return VoxelShapes.cuboid(
            adjustedValues[0], voxelShape.minY, adjustedValues[1],
            adjustedValues[2], voxelShape.maxY, adjustedValues[3]
        )
    }*/

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
    val VoxelShape.rotatedLeft: VoxelShape
        get() = this.rotated(VoxelShapeTransformation.ROTATE_LEFT)
    val VoxelShape.flipped: VoxelShape
        get() = this.rotated(VoxelShapeTransformation.FLIP_HORIZONTAL)
    val VoxelShape.rotatedRight: VoxelShape
        get() = this.rotated(VoxelShapeTransformation.ROTATE_RIGHT)

    infix fun VoxelShape.unifiedWith(otherShape: VoxelShape): VoxelShape = VoxelShapes.union(this, otherShape)
    fun VoxelShape.unifiedWith(vararg otherShapes: VoxelShape): VoxelShape = union(this, *otherShapes)
    infix operator fun VoxelShape.plus(otherShape: VoxelShape): VoxelShape = this.unifiedWith(otherShape)
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

    fun VoxelShape.rotated(direction: VoxelShapeTransformation): VoxelShape {
        val shapes = mutableListOf(VoxelShapes.empty())
        this.forEachBox { minX, minY, minZ, maxX, maxY, maxZ ->
            val adjustedValues = adjustValues(direction, minX, minZ, maxX, maxZ)
            shapes += VoxelShapes.cuboid(
                adjustedValues[0], minY,
                adjustedValues[1], adjustedValues[2], maxY, adjustedValues[3]
            )
        }
        return shapes.reduce { a, b -> VoxelShapes.union(a, b) }
    }

    fun combine(function: BooleanBiFunction, vararg voxelShapes: VoxelShape): VoxelShape {
        return voxelShapes.reduce { a, b -> VoxelShapes.combine(a, b, function) }
    }

    fun union(vararg voxelShapes: VoxelShape): VoxelShape = voxelShapes.reduce(VoxelShapes::union)
    private fun adjustValues(
        direction: VoxelShapeTransformation,
        minX: Double,
        minZ: Double,
        maxX: Double,
        maxZ: Double
    ) = when (direction) {
        VoxelShapeTransformation.FLIP_HORIZONTAL -> doubleArrayOf(1.0f - maxX, 1.0f - maxZ, 1.0f - minX, 1.0f - minZ)
        VoxelShapeTransformation.ROTATE_RIGHT -> doubleArrayOf(minZ, 1.0f - maxX, maxZ, 1.0f - minX)
        VoxelShapeTransformation.ROTATE_LEFT -> doubleArrayOf(1.0f - maxZ, minX, 1.0f - minZ, maxX)
        else -> doubleArrayOf(minX, minZ, maxX, maxZ)
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

    infix fun VoxelShape.appendShapes(configure: VoxelShapeModifier.() -> VoxelShape): VoxelShape {
        val modifier = VoxelShapeModifier(this)
        return modifier.configure()
    }
    fun appendShapesTo(shape: VoxelShape, configure: VoxelShapeModifier.() -> VoxelShape): VoxelShape {
        return shape.appendShapes(configure)
    }
}

enum class VoxelShapeTransformation {
    ROTATE_LEFT, ROTATE_RIGHT, FLIP_HORIZONTAL, ROTATE_UP, ROTATE_DOWN
}

class VoxelShapeModifier(private var baseShape: VoxelShape) {
    infix fun VoxelShape.case(condition: Boolean): VoxelShape {
        if (condition) baseShape += this
        return baseShape
    }
    infix fun append(shape: VoxelShape): VoxelShape {
        baseShape += shape
        return baseShape
    }
}

