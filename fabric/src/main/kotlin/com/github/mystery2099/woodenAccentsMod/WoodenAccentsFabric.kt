package com.github.mystery2099.woodenAccentsMod

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import com.github.mystery2099.woodenAccentsMod.entity.ModEntities
import com.github.mystery2099.woodenAccentsMod.item.group.ModCreativeTabs
import net.fabricmc.api.ModInitializer

object WoodenAccentsFabric : ModInitializer {
    override fun onInitialize() {
        WoodenAccentsMod.logger.info("Initializing ${WoodenAccentsMod.MOD_ID}")
        ModBlocks.register()
        ModBlockEntities.register()
        ModEntities.register()
        ModCreativeTabs.register()
    }
}
