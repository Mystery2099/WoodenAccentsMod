package com.mystery2099.block.custom

import com.mystery2099.block.custom.enums.CoffeeTableType
import com.mystery2099.state.property.ModProperties
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.state.property.Properties

class CoffeeTableBlock(private val _legBlock: Block, private val _topBlock: Block) : AbstractWaterloggableBlock(FabricBlockSettings.copyOf(_topBlock)) {
    val legBlock: Block
        get() = _legBlock
    val topBlock: Block
        get() = _topBlock

    init {
        defaultState = defaultState.with(north, false)
            .with(east, false).with(south, false)
            .with(west, false).with(type, CoffeeTableType.SHORT)
        instances.add(this)
    }

    companion object {
        @JvmStatic
        val instances = HashSet<CoffeeTableBlock>()
        @JvmStatic
        val north = Properties.NORTH
        @JvmStatic
        val east = Properties.EAST
        @JvmStatic
        val south = Properties.SOUTH
        @JvmStatic
        val west = Properties.WEST
        @JvmStatic
        val type = ModProperties.coffeeTableType
    }
}