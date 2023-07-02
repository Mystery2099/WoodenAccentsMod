package com.mystery2099

import com.mystery2099.block.ModBlocks
import com.mystery2099.block_entity.ModBlockEntities
import net.fabricmc.api.ModInitializer
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

	fun String.toVanillaId(): Identifier {
		return this.toId("minecraft")
	}

	fun String.toCommonId(): Identifier {
		return this.toId("c")
	}
}