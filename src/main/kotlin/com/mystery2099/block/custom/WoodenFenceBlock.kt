package com.mystery2099.block.custom

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.FenceBlock

//WIP Name
class WoodenFenceBlock(settings: Block, val strippedLog: Block, val log: Block) : FenceBlock(FabricBlockSettings.copyOf(settings)) {

}