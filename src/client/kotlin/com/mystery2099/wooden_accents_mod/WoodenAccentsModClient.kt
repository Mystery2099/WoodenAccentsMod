package com.mystery2099.wooden_accents_mod

import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.custom.CoffeeTableBlock
import com.mystery2099.wooden_accents_mod.block.custom.enums.CoffeeTableTypes
import com.mystery2099.wooden_accents_mod.entity.ModEntities
import com.mystery2099.wooden_accents_mod.render.SeatRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
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
				if (itemStack.nbt?.getString(CoffeeTableTypes.TAG) != CoffeeTableTypes.TALL.asString()) 0.5f else 1.0f
			}
		}
		EntityRendererRegistry.register(ModEntities.seatEntity, ::SeatRenderer)
	}

}