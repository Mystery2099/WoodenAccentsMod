package com.mystery2099.wooden_accents_mod

import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.custom.CoffeeTableBlock
import com.mystery2099.wooden_accents_mod.block.custom.enums.CoffeeTableType
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.util.Identifier

object WoodenAccentsModClient : ClientModInitializer {
    override fun onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        BlockRenderLayerMap.INSTANCE.apply {
        }
		ModBlocks.blocks.filterIsInstance<CoffeeTableBlock>().forEach {
			ModelPredicateProviderRegistry.register(
				it.asItem(), Identifier("height")
			) { itemStack, _, _, _ ->
				if (itemStack.nbt?.getString("coffee_table_type") != CoffeeTableType.TALL.asString()) 0.5f else 1.0f
			}
		}
    }
}