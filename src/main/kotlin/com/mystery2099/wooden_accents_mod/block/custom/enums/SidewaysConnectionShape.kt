package com.mystery2099.wooden_accents_mod.block.custom.enums

import net.minecraft.util.StringIdentifiable

/**
 * Sideways connection shape
 *
 * @property string
 * @constructor Create empty Sideways connection shape
 */
enum class SidewaysConnectionShape(private val string: String) : StringIdentifiable
{
    /**
     * Single
     *
     * @constructor Create empty Single
     */
    SINGLE("single"),

    /**
     * Center
     *
     * @constructor Create empty Center
     */
    CENTER("center"),

    /**
     * Left
     *
     * @constructor Create empty Left
     */
    LEFT("left"),

    /**
     * Right
     *
     * @constructor Create empty Right
     */
    RIGHT("right");


    override fun toString(): String = string

    override fun asString(): String = string
}

