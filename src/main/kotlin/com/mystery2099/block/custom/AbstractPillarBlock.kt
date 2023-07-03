package com.mystery2099.block.custom

import com.mystery2099.state.property.ModProperties
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.WorldAccess

abstract class AbstractPillarBlock(baseBlock: Block, size: Size) : AbstractWaterloggableBlock(FabricBlockSettings.copyOf(baseBlock)) {

    private val baseBlock: Block
    private val size: Size
    init {
        this.baseBlock = baseBlock
        this.size = size
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        super.appendProperties(builder)
        builder.add(up, down, connectionLocked)
    }

    open fun getStateAtPos(worldAccess: WorldAccess, blockPos: BlockPos): BlockState {
        return worldAccess.getBlockState(blockPos)
    }
    open fun getUpState(worldAccess: WorldAccess, pos: BlockPos): BlockState {
        return getStateAtPos(worldAccess, pos.up())
    }

    open fun getDownState(worldAccess: WorldAccess, pos: BlockPos): BlockState {
        return getStateAtPos(worldAccess, pos.down())
    }

    abstract fun checkUp(worldAccess: WorldAccess, pos: BlockPos): Boolean
    abstract fun checkDown(worldAccess: WorldAccess, pos: BlockPos): Boolean
    @JvmRecord
    data class Size(val topShape: VoxelShape, val centerShape: VoxelShape, val baseShape: VoxelShape)
    companion object {
        @JvmStatic
        val up: BooleanProperty = Properties.UP!!
        @JvmStatic
        val down: BooleanProperty = Properties.DOWN!!
        @JvmStatic
        val connectionLocked: BooleanProperty = ModProperties.connectionLocked
    }
}