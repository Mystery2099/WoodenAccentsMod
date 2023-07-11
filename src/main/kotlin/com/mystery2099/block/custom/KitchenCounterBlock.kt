package com.mystery2099.block.custom

import com.mystery2099.WoodenAccentsModItemGroups
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block

class KitchenCounterBlock(val baseBlock: Block, val topBlock: Block) : AbstractKitchenCounterBlock(FabricBlockSettings.copyOf(baseBlock)) {

    init {
        WoodenAccentsModItemGroups.kitchenItems += this
    }
}
