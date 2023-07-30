package com.mystery2099.util

import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max
import kotlin.math.min

object VoxelShapeHelper {
    fun combineAll(shapes: Collection<VoxelShape>): VoxelShape = shapes.fold(VoxelShapes.empty(), VoxelShapes::union)

    fun setMaxHeight(source: VoxelShape, height: Double): VoxelShape {
        val result = AtomicReference(VoxelShapes.empty())
        source.forEachBox { minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double ->
            val shape = VoxelShapes.cuboid(minX, minY, minZ, maxX, height, maxZ)
            result.set(VoxelShapes.combine(result.get(), shape, BooleanBiFunction.OR))
        }
        return result.get().simplify()
    }

    fun limitHorizontal(source: VoxelShape): VoxelShape {
        val result = AtomicReference(VoxelShapes.empty())
        source.forEachBox { minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double ->
            val shape = VoxelShapes.cuboid(limit(minX), minY, limit(minZ), limit(maxX), maxY, limit(maxZ))
            result.set(VoxelShapes.combine(result.get(), shape, BooleanBiFunction.OR))
        }
        return result.get().simplify()
    }

    fun rotate(source: VoxelShape, direction: Direction): VoxelShape {
        val adjustedValues = adjustValues(
            direction, source.getMin(Direction.Axis.X), source.getMin(Direction.Axis.Z), source.getMax(
                Direction.Axis.X
            ), source.getMax(Direction.Axis.Z)
        )
        return VoxelShapes.cuboid(
            adjustedValues[0], source.getMin(Direction.Axis.Y),
            adjustedValues[1], adjustedValues[2], source.getMax(Direction.Axis.Y), adjustedValues[3]
        )
    }

    fun rotate(sources: Collection<VoxelShape>, direction: Direction): VoxelShape {
        return sources.fold(VoxelShapes.empty()) {  acc, shape -> VoxelShapes.union(acc, rotate(shape, direction)) }
    }

    private fun adjustValues(
        direction: Direction,
        var1: Double,
        var2: Double,
        var3: Double,
        var4: Double
    ): DoubleArray {
        return when (direction) {
            Direction.WEST -> doubleArrayOf(1.0f - var3, 1.0f - var4, 1.0f - var1, 1.0f - var2)
            Direction.NORTH -> doubleArrayOf(var2, 1.0f - var3, var4, 1.0f - var1)
            Direction.SOUTH -> doubleArrayOf(1.0f - var4, var1, 1.0f - var2, var3)
            else -> doubleArrayOf(var1, var2, var3, var4)
        }
    }

    private fun limit(value: Double): Double = max(0.0, min(1.0, value))
}