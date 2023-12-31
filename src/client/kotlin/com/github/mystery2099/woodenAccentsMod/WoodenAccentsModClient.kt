package com.github.mystery2099.woodenAccentsMod

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.custom.CoffeeTableBlock
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.CoffeeTableTypes
import com.github.mystery2099.woodenAccentsMod.entity.ModEntities
import com.github.mystery2099.woodenAccentsMod.render.SeatRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.util.Identifier

object WoodenAccentsModClient : ClientModInitializer {
    override fun onInitializeClient() {
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