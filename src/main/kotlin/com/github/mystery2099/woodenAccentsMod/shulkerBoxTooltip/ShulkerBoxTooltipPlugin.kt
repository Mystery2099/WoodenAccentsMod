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

/**
 * ShulkerBoxTooltipPlugin is a plugin for the ShulkerBoxTooltip mod that registers block preview providers
 * for various custom blocks in the Wooden Accents Mod.
 */
object ShulkerBoxTooltipPlugin : ShulkerBoxTooltipApi {
    /**
     * Registers block preview providers for custom blocks in the Wooden Accents Mod.
     *
     * @param registry The [PreviewProviderRegistry] for registering block preview providers.
     */
    override fun registerProviders(registry: PreviewProviderRegistry) {
        // Register preview providers for crates
        registry.register(
            "crates".toIdentifier(),
            CratePreviewProvider(),
            *(ModBlocks.blocks.filterIsInstance<CrateBlock>().map(Block::asItem).toTypedArray())
        )

        // Register preview providers for chest-like blocks
        registry.register(
            "chest_like".toIdentifier(),
            BlockEntityPreviewProvider(27, true),
            *(ModBlocks.blocks.filterIsInstance<KitchenCabinetBlock>().map(Block::asItem).toTypedArray())
        )

        // Register preview providers for chiseled bookshelf-like blocks
        registry.register(
            "chiseled_bookshelf_like".toIdentifier(),
            BlockEntityPreviewProvider(6, true, 3),
            *(ModBlocks.blocks.filterIsInstance<ThinBookshelfBlock>().map(Block::asItem).toTypedArray())
        )
    }
}