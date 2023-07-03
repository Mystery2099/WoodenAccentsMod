package com.mystery2099.block

import com.mystery2099.WoodenAccentsMod
import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModBlocks {




    private fun Block.register(identifier: Identifier): Block {
        return Registry.register(Registries.BLOCK, identifier, this)

    }
    fun register() {
        WoodenAccentsMod.logger.info("Registering ModBlocks for mod: ${WoodenAccentsMod.modid}")
    }

}