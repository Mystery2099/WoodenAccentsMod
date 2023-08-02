package com.mystery2099.util

import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max
import kotlin.math.min

object VoxelShapeHelper {
    private val Double.limited: Double
        get() = this.limit()
    val Collection<VoxelShape>.combined: VoxelShape
        get() = this.union()
    val Array<VoxelShape>.combined: VoxelShape
        get() = this.union()

    fun Collection<VoxelShape>.union(): VoxelShape = this.fold(VoxelShapes.empty(), VoxelShapes::union)
    fun Array<VoxelShape>.union(): VoxelShape = this.fold(VoxelShapes.empty(), VoxelShapes::union)

    fun VoxelShape.unionWith(otherShape: VoxelShape): VoxelShape = VoxelShapes.union(this, otherShape)
    fun VoxelShape.unionWith(vararg otherShapes: VoxelShape): VoxelShape = otherShapes.fold(this, VoxelShapes::union)
    fun VoxelShape.unionWith(otherShapes: Collection<VoxelShape>): VoxelShape = this.unionWith(otherShapes.combined)

    fun setMaxHeight(source: VoxelShape, height: Double): VoxelShape {
        val result = AtomicReference(VoxelShapes.empty())
        source.forEachBox { minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double ->
            val shape = VoxelShapes.cuboid(minX, minY, minZ, maxX, height, maxZ)
            result.set(result.get().unionWith(shape))
        }
        return result.get()
    }

    fun limitHorizontal(source: VoxelShape): VoxelShape {
        val result = AtomicReference(VoxelShapes.empty())
        source.forEachBox { minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double ->
            val shape = VoxelShapes.cuboid(minX.limited, minY, minZ.limited, maxX.limited, maxY, maxZ.limited)
            result.set(result.get().unionWith(shape))
        }
        return result.get()
    }

    //Do not use on complex VoxelShapes
    fun VoxelShape.rotate(direction: VoxelShapeRotations): VoxelShape {
        val adjustedValues = adjustValues(
            direction, this.getMin(Direction.Axis.X), this.getMin(Direction.Axis.Z), this.getMax(
                Direction.Axis.X
            ), this.getMax(Direction.Axis.Z)
        )
        return VoxelShapes.cuboid(
            adjustedValues[0], this.getMin(Direction.Axis.Y),
            adjustedValues[1], adjustedValues[2], this.getMax(Direction.Axis.Y), adjustedValues[3]
        )
    }

    //Do not use on complex VoxelShapes
    fun VoxelShape.rotateLeft(): VoxelShape = rotate(VoxelShapeRotations.LEFT)

    //Do not use on complex VoxelShapes
    fun VoxelShape.rotateRight(): VoxelShape = rotate(VoxelShapeRotations.RIGHT)

    //Do not use on complex VoxelShapes
    fun VoxelShape.flip(): VoxelShape = rotate(VoxelShapeRotations.FLIP)

    fun Collection<VoxelShape>.rotateElements(direction: VoxelShapeRotations): Collection<VoxelShape> {
        if (isEmpty()) {
            val callerStackTrace = Thread.currentThread().stackTrace[2]
            val callerClassName = callerStackTrace.className
            val callerLineNumber = callerStackTrace.lineNumber
            println("Warning: Collection of VoxelShapes is empty in class: $callerClassName, line: $callerLineNumber. Returning a list containing an empty VoxelShape.")
            return listOf(VoxelShapes.empty())
        }
        return map { it.rotate(direction) }
    }
    fun Array<VoxelShape>.rotateElements(direction: VoxelShapeRotations): Array<VoxelShape> {
        if (isEmpty()) {
            val callerStackTrace = Thread.currentThread().stackTrace[2]
            val callerClassName = callerStackTrace.className
            val callerLineNumber = callerStackTrace.lineNumber
            println("Warning: Array of VoxelShapes is empty in class: $callerClassName, line: $callerLineNumber. Returning an Array containing an empty VoxelShape.")
            return arrayOf(VoxelShapes.empty())
        }
        return map { it.rotate(direction) }.toTypedArray()
    }

    fun Collection<VoxelShape>.rotateAndCombine(direction: VoxelShapeRotations): VoxelShape {
        return rotateElements(direction).combined
    }
    fun Array<VoxelShape>.rotateAndCombine(direction: VoxelShapeRotations): VoxelShape {
        return rotateElements(direction).combined
    }

    private fun adjustValues(
        direction: VoxelShapeRotations,
        var1: Double,
        var2: Double,
        var3: Double,
        var4: Double
    ): DoubleArray = when (direction) {
        VoxelShapeRotations.FLIP -> doubleArrayOf(1.0f - var3, 1.0f - var4, 1.0f - var1, 1.0f - var2)
        VoxelShapeRotations.RIGHT -> doubleArrayOf(var2, 1.0f - var3, var4, 1.0f - var1)
        VoxelShapeRotations.LEFT -> doubleArrayOf(1.0f - var4, var1, 1.0f - var2, var3)
        else -> doubleArrayOf(var1, var2, var3, var4)
    }

    private fun Double.limit(): Double = max(0.0, min(1.0, this))

}
enum class VoxelShapeRotations {
    LEFT, RIGHT, FLIP, UP, DOWN
}