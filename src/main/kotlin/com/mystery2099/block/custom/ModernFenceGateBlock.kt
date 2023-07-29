package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsMod.woodType
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.FenceGateBlock

class ModernFenceGateBlock(baseGate: FenceGateBlock, val baseBlock: Block) : FenceGateBlock(FabricBlockSettings.copyOf(baseGate), baseGate.woodType()) {
}