package com.mystery2099.wooden_accents_mod

interface WoodenAccentsModRegistry {
    fun register() {
        WoodenAccentsMod.logger.info("Registering ${this::class.simpleName} for mod: ${WoodenAccentsMod.MOD_ID}")
    }
}