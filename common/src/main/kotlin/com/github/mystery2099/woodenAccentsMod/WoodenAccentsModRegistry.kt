package com.github.mystery2099.woodenAccentsMod
/**
 * Common initialization hook for registry objects. Registries with delayed values override [register] and call
 * `super` after registration; eager registries use the default implementation only for consistent logging.
 */
interface WoodenAccentsModRegistry {
    fun register() {
        WoodenAccentsMod.logger.info("Registering ${this::class.simpleName} for mod: ${WoodenAccentsMod.MOD_ID}")
    }
}
