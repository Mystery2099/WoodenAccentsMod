package com.github.mystery2099.woodenAccentsMod


/**
 * This interface defines a registry for the Wooden Accents Mod.
 * It provides a method to register a component for the mod.
 */
interface WoodenAccentsModRegistry {
    /**
     * Register a component for the mod.
     * This method logs the name of the class it's called on and states that it's for the Wooden Accents Mod.
     *
     * Should be called in the mod initializer
     */
    fun register() {
        WoodenAccentsMod.logger.info("Registering ${this::class.simpleName} for mod: ${WoodenAccentsMod.MOD_ID}")
    }
}