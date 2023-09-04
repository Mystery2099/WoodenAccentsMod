package com.mystery2099.wooden_accents_mod.util

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod
import net.minecraft.block.Block
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max
import kotlin.math.min

object VoxelShapeHelper {
    private inline val Double.limited
        get() = this.limit()
    inline val Collection<VoxelShape>.combined
        get() = this.unifyElements()
    inline val Array<VoxelShape>.combined
        get() = this.unifyElements()

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

    fun Collection<VoxelShape>.unifyElements(): VoxelShape = fold(VoxelShapes.empty(), VoxelShapes::union)
    fun Array<VoxelShape>.unifyElements(): VoxelShape = fold(VoxelShapes.empty(), VoxelShapes::union)

    infix fun VoxelShape.unifiedWith(otherShape: VoxelShape): VoxelShape = VoxelShapes.union(this, otherShape)
    fun VoxelShape.unifiedWith(vararg otherShapes: VoxelShape): VoxelShape = otherShapes.fold(this, VoxelShapes::union)
    infix fun VoxelShape.unifiedWith(otherShapes: Collection<VoxelShape>): VoxelShape = this.unifiedWith(otherShapes.combined)
    infix operator fun VoxelShape.plus(otherShape: VoxelShape): VoxelShape = this.unifiedWith(otherShape)
    infix operator fun VoxelShape.plus(otherShapes: Collection<VoxelShape>): VoxelShape = this.unifiedWith(otherShapes.combined)
    infix operator fun VoxelShape.plus(otherShapes: Array<VoxelShape>): VoxelShape = this.unifiedWith(otherShapes.combined)
    infix fun VoxelShape.and(otherShape: VoxelShape): VoxelShape = this.unifiedWith(otherShape)
    infix fun VoxelShape.and(otherShapes: Array<VoxelShape>): VoxelShape = this.unifiedWith(otherShapes.combined)
    infix fun VoxelShape.and(otherShapes: Collection<VoxelShape>): VoxelShape = this.unifiedWith(otherShapes.combined)

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

    //Do not use on complex VoxelShapes
    infix fun VoxelShape.rotate(direction: VoxelShapeTransformation): VoxelShape = adjustValues(
        direction, this.minX, this.minZ, this.maxX, this.maxZ
    ).let { adjustedValues ->
        VoxelShapes.cuboid(
            adjustedValues[0], this.minY,
            adjustedValues[1], adjustedValues[2], this.maxY, adjustedValues[3]
        )
    }
    /*fun VoxelShape.rotate(direction: VoxelShapeRotations): VoxelShape {
        val adjustedValues = adjustValues(
            direction, this.getMin(Direction.Axis.X), this.getMin(Direction.Axis.Z), this.getMax(
                Direction.Axis.X
            ), this.getMax(Direction.Axis.Z)
        )
        return VoxelShapes.cuboid(
            adjustedValues[0], this.getMin(Direction.Axis.Y),
            adjustedValues[1], adjustedValues[2], this.getMax(Direction.Axis.Y), adjustedValues[3]
        )
    }*/

    infix fun MutableCollection<VoxelShape>.rotate(direction: VoxelShapeTransformation) = rotateElements(direction).combined
    infix fun Array<VoxelShape>.rotate(direction: VoxelShapeTransformation) = rotateElements(direction).combined


    fun VoxelShape.rotateLeft() = rotate(VoxelShapeTransformation.ROTATE_LEFT)//Do not use on complex VoxelShapes

    fun MutableCollection<VoxelShape>.rotateLeft() = rotate(VoxelShapeTransformation.ROTATE_LEFT)
    fun Array<VoxelShape>.rotateLeft() = rotate(VoxelShapeTransformation.ROTATE_LEFT)


    fun VoxelShape.rotateRight() = rotate(VoxelShapeTransformation.ROTATE_RIGHT)//Do not use on complex VoxelShapes

    fun MutableCollection<VoxelShape>.rotateRight() = rotate(VoxelShapeTransformation.ROTATE_RIGHT)
    fun Array<VoxelShape>.rotateRight() = rotate(VoxelShapeTransformation.ROTATE_RIGHT)


    fun VoxelShape.flip() = rotate(VoxelShapeTransformation.FLIP) //Do not use on complex VoxelShapes

    fun MutableCollection<VoxelShape>.flip() = rotate(VoxelShapeTransformation.FLIP)
    fun Array<VoxelShape>.flip() = rotate(VoxelShapeTransformation.FLIP)

    infix fun <t : Collection<VoxelShape>> t.rotateElements(direction: VoxelShapeTransformation): Collection<VoxelShape> {
        if (isEmpty()) {
            val callerStackTrace = Thread.currentThread().stackTrace[2]
            val callerClassName = callerStackTrace.className
            val callerLineNumber = callerStackTrace.lineNumber
            WoodenAccentsMod.logger.info("Warning: Collection of VoxelShapes is empty in class: $callerClassName, line: $callerLineNumber. Returning a list containing an empty VoxelShape.")
            return listOf(VoxelShapes.empty())
        }
        return this.map { it.rotate(direction) }
    }
    infix fun Array<VoxelShape>.rotateElements(direction: VoxelShapeTransformation): Array<VoxelShape> {
        if (isEmpty()) {
            val callerStackTrace = Thread.currentThread().stackTrace[2]
            val callerClassName = callerStackTrace.className
            val callerLineNumber = callerStackTrace.lineNumber
            WoodenAccentsMod.logger.info("Warning: Array of VoxelShapes is empty in class: $callerClassName, line: $callerLineNumber. Returning an Array containing an empty VoxelShape.")
            return arrayOf(VoxelShapes.empty())
        }
        return map { it.rotate(direction) }.toTypedArray()
    }


    private fun adjustValues(
        direction: VoxelShapeTransformation,
        var1: Double,
        var2: Double,
        var3: Double,
        var4: Double
    ) = when (direction) {
        VoxelShapeTransformation.FLIP -> doubleArrayOf(1.0f - var3, 1.0f - var4, 1.0f - var1, 1.0f - var2)
        VoxelShapeTransformation.ROTATE_RIGHT -> doubleArrayOf(var2, 1.0f - var3, var4, 1.0f - var1)
        VoxelShapeTransformation.ROTATE_LEFT -> doubleArrayOf(1.0f - var4, var1, 1.0f - var2, var3)
        else -> doubleArrayOf(var1, var2, var3, var4)
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
    ROTATE_LEFT, ROTATE_RIGHT, FLIP, ROTATE_UP, ROTATE_DOWN
}