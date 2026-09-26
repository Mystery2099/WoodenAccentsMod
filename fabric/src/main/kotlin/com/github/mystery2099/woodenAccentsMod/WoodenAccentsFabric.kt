package com.github.mystery2099.woodenAccentsMod

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.custom.ThinBookshelfBlock
import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import com.github.mystery2099.woodenAccentsMod.entity.ModEntities
import com.github.mystery2099.woodenAccentsMod.item.group.ModCreativeTabs
import com.github.mystery2099.woodenAccentsMod.registry.component.ModDataComponents
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityType
import net.minecraft.world.level.block.entity.BlockEntityType

object WoodenAccentsFabric : ModInitializer {
    override fun onInitialize() {
        WoodenAccentsMod.logger.info("Initializing ${WoodenAccentsMod.MOD_ID}")
        ModDataComponents.register()
        ModBlocks.register()
        ModBlocks.registerItems()
        ModBlockEntities.register()
        ModEntities.register()
        ModCreativeTabs.register()
        allowThinBookshelvesOnChiseledBookshelfEntity()
    }

    /**
     * Thin bookshelves extend [net.minecraft.world.level.block.ChiseledBookShelfBlock] and reuse its
     * block entity. Vanilla only lists the chiseled bookshelf as valid, so tell Fabric to accept ours too.
     */
    private fun allowThinBookshelvesOnChiseledBookshelfEntity() {
        val type = BlockEntityType.CHISELED_BOOKSHELF as FabricBlockEntityType
        ModBlocks.blocks.filterIsInstance<ThinBookshelfBlock>().forEach(type::addSupportedBlock)
    }
}
