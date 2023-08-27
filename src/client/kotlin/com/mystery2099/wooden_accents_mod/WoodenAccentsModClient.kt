package com.mystery2099.wooden_accents_mod

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.item.ModelPredicateProviderRegistry

object WoodenAccentsModClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		BlockRenderLayerMap.INSTANCE.apply {

		}
		ModelPredicateProviderRegistry.register(ModBlocks.oakCoffeeTable.asItem(), "height".toIdentifier()
		) { itemStack, _, _, _ ->
			if (itemStack.nbt?.getBoolean("tall") == true) 1.0f else 0.0f
		}
	}
}