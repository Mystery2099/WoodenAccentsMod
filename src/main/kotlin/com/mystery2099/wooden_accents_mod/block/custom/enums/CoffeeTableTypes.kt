package com.mystery2099.wooden_accents_mod.block.custom.enums

import net.minecraft.util.StringIdentifiable


/**
 * Coffee table types
 *
 * @property string
 * @constructor Create empty Coffee table types
 */
enum class CoffeeTableTypes(private val string: String) : StringIdentifiable {
    /**
     * Short
     *
     * @constructor Create empty Short
     */
    SHORT("short"),

    /**
     * Tall
     *
     * @constructor Create empty Tall
     */
    TALL("tall");

    override fun asString() = string
}

