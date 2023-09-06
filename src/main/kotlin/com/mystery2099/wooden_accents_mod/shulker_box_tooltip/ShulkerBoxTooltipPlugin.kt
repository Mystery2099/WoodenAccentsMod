package com.mystery2099.wooden_accents_mod.shulker_box_tooltip

import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.custom.CrateBlock
import com.mystery2099.wooden_accents_mod.block.custom.KitchenCabinetBlock
import com.mystery2099.wooden_accents_mod.block.custom.ThinBookshelfBlock

object ShulkerBoxTooltipPlugin : ShulkerBoxTooltipApi {
    override fun registerProviders(registry: PreviewProviderRegistry) {
        ModBlocks.blocks.forEach { block ->
            when (block) {
                is CrateBlock -> {
                    registry.register(
                        "crates".toIdentifier(),
                        CratePreviewProvider(),
                        block.asItem()
                    )
                }

                is KitchenCabinetBlock -> {
                    registry.register(
                        "chest_like".toIdentifier(),
                        BlockEntityPreviewProvider(27, true),
                        block.asItem()
                    )
                }

                is ThinBookshelfBlock -> {
                    registry.register(
                        "chiseled_bookshelf_like".toIdentifier(),
                        BlockEntityPreviewProvider(6, true, 3),
                        block.asItem()
                    )
                }
            }
        }
    }
}