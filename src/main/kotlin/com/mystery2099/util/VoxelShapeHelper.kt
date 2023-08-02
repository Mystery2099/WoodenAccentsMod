package com.mystery2099.util

import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max
import kotlin.math.min

object VoxelShapeHelper {
    private val Double.limited
        get() = this.limit()
    val Collection<VoxelShape>.combined
        get() = this.unifyElements()
    val Array<VoxelShape>.combined
        get() = this.unifyElements()

    fun Collection<VoxelShape>.unifyElements(): VoxelShape = this.fold(VoxelShapes.empty(), VoxelShapes::union)
    fun Array<VoxelShape>.unifyElements(): VoxelShape = this.fold(VoxelShapes.empty(), VoxelShapes::union)

    fun VoxelShape.unifyWith(otherShape: VoxelShape): VoxelShape = VoxelShapes.union(this, otherShape)
    fun VoxelShape.unifyWith(vararg otherShapes: VoxelShape): VoxelShape = otherShapes.fold(this, VoxelShapes::union)
    fun VoxelShape.unifyWith(otherShapes: Collection<VoxelShape>): VoxelShape = this.unifyWith(otherShapes.combined)

    fun setMaxHeight(source: VoxelShape, height: Double): VoxelShape {
        val result = AtomicReference(VoxelShapes.empty())
        source.forEachBox { minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double ->
            val shape = VoxelShapes.cuboid(minX, minY, minZ, maxX, height, maxZ)
            result.set(result.get().unifyWith(shape))
        }
        return result.get()
    }

    fun limitHorizontal(source: VoxelShape): VoxelShape {
        val result = AtomicReference(VoxelShapes.empty())
        source.forEachBox { minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double ->
            val shape = VoxelShapes.cuboid(minX.limited, minY, minZ.limited, maxX.limited, maxY, maxZ.limited)
            result.set(result.get().unifyWith(shape))
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

    fun Collection<VoxelShape>.rotate(direction: VoxelShapeRotations) = rotateElements(direction).combined
    fun Array<VoxelShape>.rotate(direction: VoxelShapeRotations) = rotateElements(direction).combined


    fun VoxelShape.rotateLeft() = rotate(VoxelShapeRotations.LEFT)//Do not use on complex VoxelShapes

    fun Collection<VoxelShape>.rotateLeft() = rotate(VoxelShapeRotations.LEFT)
    fun Array<VoxelShape>.rotateLeft() = rotate(VoxelShapeRotations.LEFT)


    fun VoxelShape.rotateRight() = rotate(VoxelShapeRotations.RIGHT)//Do not use on complex VoxelShapes

    fun Collection<VoxelShape>.rotateRight() = rotate(VoxelShapeRotations.RIGHT)
    fun Array<VoxelShape>.rotateRight() = rotate(VoxelShapeRotations.RIGHT)


    fun VoxelShape.flip() = rotate(VoxelShapeRotations.FLIP) //Do not use on complex VoxelShapes

    fun Collection<VoxelShape>.flip() = rotate(VoxelShapeRotations.FLIP)
    fun Array<VoxelShape>.flip() = rotate(VoxelShapeRotations.FLIP)

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


    private fun adjustValues(
        direction: VoxelShapeRotations,
        var1: Double,
        var2: Double,
        var3: Double,
        var4: Double
    ) = when (direction) {
        VoxelShapeRotations.FLIP -> doubleArrayOf(1.0f - var3, 1.0f - var4, 1.0f - var1, 1.0f - var2)
        VoxelShapeRotations.RIGHT -> doubleArrayOf(var2, 1.0f - var3, var4, 1.0f - var1)
        VoxelShapeRotations.LEFT -> doubleArrayOf(1.0f - var4, var1, 1.0f - var2, var3)
        else -> doubleArrayOf(var1, var2, var3, var4)
    }

    private fun Double.limit() = max(0.0, min(1.0, this))

}
enum class VoxelShapeRotations {
    LEFT, RIGHT, FLIP, UP, DOWN
}