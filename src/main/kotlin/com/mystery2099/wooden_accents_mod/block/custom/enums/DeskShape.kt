package com.mystery2099.wooden_accents_mod.block.custom.enums

import net.minecraft.util.StringIdentifiable

/**
 * Desk shape
 *
 * @property string
 * @constructor Create empty Desk shape
 */
enum class DeskShape(val string: String) : StringIdentifiable {
    /**
     * Single
     *
     * @constructor Create empty Single
     */
    SINGLE("single"),

    /**
     * Left
     *
     * @constructor Create empty Left
     */
    LEFT("left"),

    /**
     * Center
     *
     * @constructor Create empty Center
     */
    CENTER("center"),

    /**
     * Right
     *
     * @constructor Create empty Right
     */
    RIGHT("right"),

    /**
     * Left Corner
     *
     * @constructor Create empty Left Corner
     */
    LEFT_CORNER("left_corner"),

    /**
     * Right Corner
     *
     * @constructor Create empty Right Corner
     */
    RIGHT_CORNER("right_corner");

    override fun asString(): String = string
}