package com.mystery2099.wooden_accents_mod

/**
 * Wooden accents mod registry
 *
 * @constructor Create empty Wooden accents mod registry
 */
interface WoodenAccentsModRegistry {
    /**
     * Register:
     * Logs the name of the class it's called on and stats that its for this mod
     */
    fun register() {
        WoodenAccentsMod.logger.info("Registering ${this::class.simpleName} for mod: ${WoodenAccentsMod.MOD_ID}")
    }
}