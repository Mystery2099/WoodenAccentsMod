package com.mystery2099.block.custom

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.FenceBlock

class ModernFenceBlock(settings: Block, val sideBlock: Block, val postBlock: Block) : FenceBlock(FabricBlockSettings.copyOf(settings)) {

}