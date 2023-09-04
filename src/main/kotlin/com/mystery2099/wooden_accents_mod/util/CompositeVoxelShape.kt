package com.mystery2099.wooden_accents_mod.util

import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flip
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateElements
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotateRight
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.unifyElements
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes

class CompositeVoxelShape(vararg voxelShapes: VoxelShape) {
    private var _voxelShapes: ArrayList<VoxelShape> = ArrayList()
    val voxelShapes: List<VoxelShape>
        get() = _voxelShapes

    init {
        _voxelShapes.addAll(voxelShapes)
    }

    constructor(voxelShapes: Collection<VoxelShape>) : this(*voxelShapes.toTypedArray())
    constructor() : this(VoxelShapes.empty())

    fun get() = _voxelShapes.unifyElements()

    fun mergeWith(other: CompositeVoxelShape) = this.also { this._voxelShapes += other.voxelShapes }
    fun mergeWith(vararg others: CompositeVoxelShape): CompositeVoxelShape = this.also {
        it._voxelShapes.addAll(others.reduce { a, b -> a.mergeWith(b) }.voxelShapes)
    }

    infix fun add(voxelShape: VoxelShape): CompositeVoxelShape = this.also {
        _voxelShapes += voxelShape
    }

    infix fun addAll(voxelShapes: Collection<VoxelShape>): CompositeVoxelShape = this.also {
        _voxelShapes += voxelShapes
    }

    fun addAll(vararg voxelShapes: VoxelShape): CompositeVoxelShape = this.also {
        _voxelShapes += voxelShapes
    }

    operator fun plus(voxelShape: VoxelShape): CompositeVoxelShape = copy().add(voxelShape)

    operator fun plus(voxelShapes: Collection<VoxelShape>): CompositeVoxelShape = this.copy().addAll(voxelShapes)

    operator fun plus(voxelShapes: Array<VoxelShape>): CompositeVoxelShape = this.copy().addAll(*voxelShapes)

    operator fun plusAssign(other: CompositeVoxelShape) {
        this.mergeWith(other)
    }

    operator fun plusAssign(voxelShape: VoxelShape?) {
        add(voxelShape ?: return)
    }

    operator fun plusAssign(voxelShapes: Collection<VoxelShape>) {
        addAll(voxelShapes)
    }

    operator fun plusAssign(voxelShapes: Array<VoxelShape>) {
        addAll(*voxelShapes)
    }

    infix fun VoxelShape.shallBeAddedIf(boolean: Boolean) {
        if (boolean) add(this)
    }

    infix fun CompositeVoxelShape.shallBeAddedIf(boolean: Boolean) {
        if (boolean) mergeWith(this)
    }

    fun remove(voxelShape: VoxelShape) = this.also { _voxelShapes -= voxelShape }
    fun removeAll(voxelShapes: Collection<VoxelShape>) = this.also {
        _voxelShapes.removeAll(voxelShapes.toSet())
    }

    fun removeAll(voxelShapes: Array<VoxelShape>) = this.also {
        _voxelShapes.removeAll(voxelShapes.toSet())
    }

    operator fun minus(voxelShape: VoxelShape): CompositeVoxelShape = copy().remove(voxelShape)

    operator fun minus(voxelShapes: Collection<VoxelShape>): CompositeVoxelShape = this.copy().removeAll(voxelShapes)

    operator fun minus(voxelShapes: Array<VoxelShape>): CompositeVoxelShape = this.copy().removeAll(voxelShapes)

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
        val newShapes: ArrayList<VoxelShape> = ArrayList<VoxelShape>().apply {
            addAll(_voxelShapes.rotateElements(VoxelShapeTransformation.ROTATE_LEFT))
        }
        _voxelShapes.clear()
        _voxelShapes.addAll(newShapes)
    }

    val rotatedLeft: CompositeVoxelShape
        get() = CompositeVoxelShape().also { shape ->
            shape._voxelShapes.addAll(this._voxelShapes.map { it.rotateLeft() })
        }

    fun rotateRight() {
        val newShapes: ArrayList<VoxelShape> = ArrayList<VoxelShape>().apply {
            addAll(_voxelShapes.rotateElements(VoxelShapeTransformation.ROTATE_RIGHT))
        }
        _voxelShapes.clear()
        _voxelShapes.addAll(newShapes)
    }

    val rotatedRight: CompositeVoxelShape
        get() = CompositeVoxelShape().also { shape ->
            shape._voxelShapes.addAll(this._voxelShapes.map { it.rotateRight() })
        }

    fun flip() {
        val newShapes: ArrayList<VoxelShape> = ArrayList<VoxelShape>().apply {
            addAll(_voxelShapes.rotateElements(VoxelShapeTransformation.FLIP))
        }
        _voxelShapes.clear()
        _voxelShapes.addAll(newShapes)
    }

    val flipped: CompositeVoxelShape
        get() = CompositeVoxelShape().also { shape ->
            shape._voxelShapes.addAll(this._voxelShapes.map { it.flip() })
        }

    fun copy() = CompositeVoxelShape(this.voxelShapes)

    companion object {
        @JvmStatic
        fun of(vararg voxelShapes: VoxelShape) = CompositeVoxelShape(*voxelShapes)

        @JvmStatic
        infix fun of(voxelShape: VoxelShape) = CompositeVoxelShape(voxelShape)

        @JvmStatic
        infix fun of(voxelShapes: Collection<VoxelShape>) = CompositeVoxelShape(voxelShapes)

        @JvmStatic
        fun copy(compositeVoxelShape: CompositeVoxelShape) = compositeVoxelShape.copy()

        fun VoxelShape.toCompositeVoxelShape() = CompositeVoxelShape(this)

    }
}