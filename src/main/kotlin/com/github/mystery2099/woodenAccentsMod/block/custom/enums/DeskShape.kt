package com.github.mystery2099.woodenAccentsMod.block.custom.enums

import net.minecraft.util.StringIdentifiable

/**
 * Desk shape
 *
 * @property string
 * @constructor Create empty Desk shape
 */
enum class DeskShape(val string: String) : StringIdentifiable {
    SINGLE("single"),
    LEFT("left"),
    CENTER("center"),
    RIGHT("right"),
    LEFT_CORNER("left_corner"),
    RIGHT_CORNER("right_corner");

    override fun asString(): String = string
}