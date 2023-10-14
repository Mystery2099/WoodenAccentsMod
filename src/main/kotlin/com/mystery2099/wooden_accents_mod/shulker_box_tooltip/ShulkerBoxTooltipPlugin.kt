package com.mystery2099.wooden_accents_mod.shulker_box_tooltip

import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.custom.CrateBlock
import com.mystery2099.wooden_accents_mod.block.custom.KitchenCabinetBlock
import com.mystery2099.wooden_accents_mod.block.custom.ThinBookshelfBlock
import net.minecraft.block.Block

object ShulkerBoxTooltipPlugin : ShulkerBoxTooltipApi {
    override fun registerProviders(registry: PreviewProviderRegistry) {
        registry.register(
            "crates".toIdentifier(),
            CratePreviewProvider(),
            *(ModBlocks.blocks.filterIsInstance<CrateBlock>().map(Block::asItem).toTypedArray())
        )
        registry.register(
            "chest_like".toIdentifier(),
            BlockEntityPreviewProvider(27, true),
            *(ModBlocks.blocks.filterIsInstance<KitchenCabinetBlock>().map(Block::asItem).toTypedArray())
        )
        registry.register(
            "chiseled_bookshelf_like".toIdentifier(),
            BlockEntityPreviewProvider(6, true, 3),
            *(ModBlocks.blocks.filterIsInstance<ThinBookshelfBlock>().map(Block::asItem).toTypedArray())
        )
    }
}