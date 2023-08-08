package com.mystery2099.wooden_accents_mod.util

import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateElements
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.unifyElements
import net.minecraft.block.Block
import net.minecraft.util.shape.VoxelShape

class CompositeVoxelShape(vararg voxelShapes: VoxelShape) {
    private var _voxelShapes: MutableList<VoxelShape> = mutableListOf()
    val voxelShapes: List<VoxelShape>
        get() = _voxelShapes
    inline val voxelShape: VoxelShape
        get()= voxelShapes.unifyElements()
    init {
        _voxelShapes += voxelShapes.toList()
    }
    constructor(voxelShapes: Collection<VoxelShape>) : this(*voxelShapes.toTypedArray())
    operator fun invoke(index: Int) = get(index)
    infix operator fun get(index: Int): VoxelShape = _voxelShapes[index]
    fun get() = _voxelShapes.unifyElements()
    operator fun set(index: Int, voxelShape: VoxelShape) {
        _voxelShapes[index] = voxelShape
    }
    infix fun add(voxelShape: VoxelShape): CompositeVoxelShape = this.also {
        _voxelShapes += voxelShape
    }
    infix fun addAll(voxelShapes: Collection<VoxelShape>): CompositeVoxelShape = this.also {
        _voxelShapes += voxelShapes
    }
    infix fun addAll(voxelShapes: Array<VoxelShape>): CompositeVoxelShape = this.also {
        _voxelShapes += voxelShapes
    }
    operator fun plus(voxelShape: VoxelShape): CompositeVoxelShape {
        val newShape = CompositeVoxelShape(this.voxelShapes)
        return newShape.add(voxelShape)
    }
    operator fun plus(voxelShapes: Collection<VoxelShape>): CompositeVoxelShape {
        val newShape = CompositeVoxelShape(this.voxelShapes)
        return newShape.addAll(voxelShapes)
    }
    operator fun plus(voxelShapes: Array<VoxelShape>): CompositeVoxelShape {
        val newShape = CompositeVoxelShape(this.voxelShapes)
        return newShape.addAll(voxelShapes)
    }
    operator fun plusAssign(voxelShape: VoxelShape) { add(voxelShape) }
    operator fun plusAssign(voxelShapes: Collection<VoxelShape>) {
        addAll(voxelShapes)
    }
    operator fun plusAssign(voxelShapes: Array<VoxelShape>) {
        addAll(voxelShapes)
    }
    fun remove(voxelShape: VoxelShape) = this.also { _voxelShapes -= voxelShape }
    fun removeAll(voxelShapes: Collection<VoxelShape>) = this.also {
        _voxelShapes -= voxelShapes.toSet()
    }
    fun removeAll(voxelShapes: Array<VoxelShape>) = this.also {
        _voxelShapes -= voxelShapes.toSet()
    }
    operator fun minus(voxelShape: VoxelShape): CompositeVoxelShape {
        val newShape = CompositeVoxelShape(this.voxelShapes)
        return newShape.remove(voxelShape)
    }
    operator fun minus(voxelShapes: Collection<VoxelShape>): CompositeVoxelShape {
        val newShape = CompositeVoxelShape(this.voxelShapes)
        return newShape.removeAll(voxelShapes)
    }
    operator fun minus(voxelShapes: Array<VoxelShape>): CompositeVoxelShape {
        val newShape = CompositeVoxelShape(this.voxelShapes)
        return newShape.removeAll(voxelShapes)
    }
    operator fun minusAssign(voxelShape: VoxelShape) {
        remove(voxelShape)
    }
    operator fun minusAssign(voxelShapes: Collection<VoxelShape>) {
        removeAll(voxelShapes)
    }
    operator fun minusAssign(voxelShapes: Array<VoxelShape>) {
        removeAll(voxelShapes)
    }

    fun rotateLeft() {
        _voxelShapes = _voxelShapes.rotateElements(VoxelShapeTransformation.ROTATE_LEFT).toMutableList()
    }
    fun rotatedLeft(): CompositeVoxelShape {
        val newShape = CompositeVoxelShape(this.voxelShapes)
        newShape.rotateLeft()
        return newShape
    }
    fun rotateRight() {
        _voxelShapes = _voxelShapes.rotateElements(VoxelShapeTransformation.ROTATE_RIGHT).toMutableList()
    }
    fun rotatedRight(): CompositeVoxelShape {
        val newShape = CompositeVoxelShape(this.voxelShapes)
        newShape.rotateRight()
        return newShape
    }
    fun flip() {
        _voxelShapes = _voxelShapes.rotateElements(VoxelShapeTransformation.FLIP).toMutableList()
    }
    fun flipped(): CompositeVoxelShape {
        val newShape = CompositeVoxelShape(this.voxelShapes)
        newShape.flip()
        return newShape
    }

    companion object {
        fun of(vararg voxelShapes: VoxelShape) = CompositeVoxelShape(*voxelShapes)
        fun of(voxelShapes: Collection<VoxelShape>) = CompositeVoxelShape(voxelShapes)
        fun createCuboidShape(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): CompositeVoxelShape{
            return CompositeVoxelShape(Block.createCuboidShape(minX, minY, minZ, maxX, maxY, maxZ))
        }
    }
}