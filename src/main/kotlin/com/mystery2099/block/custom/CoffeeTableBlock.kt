package com.mystery2099.block.custom

import com.mystery2099.block.custom.enums.CoffeeTableType
import com.mystery2099.state.property.ModProperties
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

class CoffeeTableBlock(val legBlock: Block, val topBlock: Block) : AbstractWaterloggableBlock(FabricBlockSettings.copyOf(topBlock)) {
    init {
        defaultState = defaultState.with(north, false)
            .with(east, false).with(south, false)
            .with(west, false).with(type, CoffeeTableType.SHORT)
        instances.add(this)
    }



    private fun WorldAccess.checkDirection(pos: BlockPos, direction: Direction): Boolean {
        val here = this.getBlockState(pos)
        val there = this.getBlockState(pos.offset(direction))
        return there.block is CoffeeTableBlock && here.get(type) == there.get(type)
    }

    private fun WorldAccess.checkNorth(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.NORTH)
    }

    private fun WorldAccess.checkEast(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.EAST)
    }

    private fun WorldAccess.checkSouth(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.SOUTH)
    }

    private fun WorldAccess.checkWest(pos: BlockPos): Boolean {
        return this.checkDirection(pos, Direction.WEST)
    }



    companion object {
        @JvmStatic
        val instances = HashSet<CoffeeTableBlock>()
        @JvmStatic
        val north: BooleanProperty = Properties.NORTH
        @JvmStatic
        val east: BooleanProperty = Properties.EAST
        @JvmStatic
        val south: BooleanProperty = Properties.SOUTH
        @JvmStatic
        val west: BooleanProperty = Properties.WEST
        @JvmStatic
        val type = ModProperties.coffeeTableType
    }
}