package com.mystery2099.wooden_accents_mod

import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.ModBlocks.register
import com.mystery2099.wooden_accents_mod.block_entity.ModBlockEntities
import com.mystery2099.wooden_accents_mod.block_entity.ModBlockEntities.register
import net.fabricmc.api.ModInitializer
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.WoodType
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object WoodenAccentsMod : ModInitializer {
	const val MOD_ID = "myst2099_wooden_accents_mod"
	inline val logger: Logger
		get() = LoggerFactory.getLogger("myst2099_wooden_accents_mod")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")
		ModBlocks.register()
		ModBlockEntities.register()
		ModItemGroups.register()
	}

	infix fun String.asPathIn(namespace: String): Identifier = Identifier(namespace, this)
	fun modId(path: String): Identifier = path.asPathIn(MOD_ID)
	fun String.asWamId(): Identifier = modId(this)
	fun Identifier.asBlockModelId(): Identifier = this.withPrefixedPath("block/")
	fun WoodType.asPlanks(): Block = when (this) {
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