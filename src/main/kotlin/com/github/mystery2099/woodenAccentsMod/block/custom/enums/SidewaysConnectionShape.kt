package com.github.mystery2099.woodenAccentsMod.block.custom.enums

import net.minecraft.util.StringIdentifiable

/**
 * Sideways connection shape
 *
 * @property string
 * @constructor Create empty Sideways connection shape
 */
enum class SidewaysConnectionShape(private val string: String) : StringIdentifiable
{
    SINGLE("single"),
    CENTER("center"),
    LEFT("left"),
    RIGHT("right");


    override fun toString(): String = string

    override fun asString(): String = string
}

