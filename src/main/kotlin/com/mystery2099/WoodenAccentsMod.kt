package com.mystery2099

import com.mystery2099.block.ModBlocks
import com.mystery2099.block_entity.ModBlockEntities
import net.fabricmc.api.ModInitializer
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.WoodType
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object WoodenAccentsMod : ModInitializer {
	const val modid = "myst2099_wooden_accents_mod"
	val logger: Logger
		get() = LoggerFactory.getLogger("myst2099_wooden_accents_mod")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")
		ModBlocks.register()
		ModBlockEntities.register()
		WoodenAccentsModItemGroups.register()
	}

	fun String.toId(namespace: String): Identifier {
		return Identifier(namespace, this)
	}

	fun String.toId(): Identifier {
		return this.toId(modid)
	}
	fun String.toBlockId(): Identifier {
		return this.toId(modid).withPrefixedPath("block/")
	}

	fun String.toVanillaId(): Identifier {
		return this.toId("minecraft")
	}

	fun String.toCommonId(): Identifier {
		return this.toId("c")
	}
	fun Block.getItemModelId(): Identifier? {
		val identifier = Registries.BLOCK.getId(this)
		return identifier.withPrefixedPath("item/")
	}
	fun WoodType.planks(): Block {
		return when (this) {
			WoodType.OAK -> Blocks.OAK_PLANKS
			WoodType.SPRUCE -> Blocks.SPRUCE_PLANKS
			WoodType.BIRCH -> Blocks.BIRCH_PLANKS
			WoodType.ACACIA -> Blocks.ACACIA_PLANKS
			WoodType.CHERRY -> Blocks.CHERRY_PLANKS
			WoodType.JUNGLE -> Blocks.JUNGLE_PLANKS
			WoodType.DARK_OAK -> Blocks.DARK_OAK_PLANKS
			WoodType.CRIMSON -> Blocks.CRIMSON_PLANKS
			WoodType.WARPED -> Blocks.WARPED_PLANKS
			WoodType.MANGROVE -> Blocks.MANGROVE_PLANKS
			WoodType.BAMBOO -> Blocks.BAMBOO_PLANKS
			else -> Blocks.OAK_PLANKS
		}
	}
	fun Block.woodType(): WoodType {
		lateinit var type: WoodType
		WoodType.stream().forEach {
			if (this.translationKey.contains(it.name.lowercase())) {
				type = it
				if (!it.name.lowercase().contains("dark") && !this.translationKey.contains("dark")) {
					return@forEach
				}
			}
		}
		return type
	}
}