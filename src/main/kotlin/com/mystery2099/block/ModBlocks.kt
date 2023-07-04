package com.mystery2099.block

import com.mystery2099.WoodenAccentsMod
import com.mystery2099.WoodenAccentsMod.toId
import com.mystery2099.block.custom.ThinPillarBlock
import com.mystery2099.datagen.BlockTagDataGen
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModBlocks {

    @JvmStatic
    val thinOakPillar = ThinPillarBlock(Blocks.OAK_PLANKS).register("thin_oak_pillar")


    private fun Block.register(id: String): Block {
        return this.register(id.toId())
    }
    private fun Block.register(identifier: Identifier): Block {
        BlockTagDataGen.axeMineable.add(this)
        Registry.register(Registries.ITEM, identifier, BlockItem(this, FabricItemSettings()))
        return Registry.register(Registries.BLOCK, identifier, this)
    }

    fun register() {
        WoodenAccentsMod.logger.info("Registering ModBlocks for mod: ${WoodenAccentsMod.modid}")
    }

}