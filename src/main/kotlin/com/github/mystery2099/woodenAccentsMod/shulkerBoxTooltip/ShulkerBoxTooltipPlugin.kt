package com.github.mystery2099.woodenAccentsMod.shulkerBoxTooltip

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.custom.CrateBlock
import com.github.mystery2099.woodenAccentsMod.block.custom.KitchenCabinetBlock
import com.github.mystery2099.woodenAccentsMod.block.custom.ThinBookshelfBlock
import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry
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