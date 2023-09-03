package com.mystery2099.wooden_accents_mod.block.custom.enums

import net.minecraft.util.StringIdentifiable

enum class DeskShape(val string: String) : StringIdentifiable {
    SINGLE("single"),
    LEFT("left"),
    CENTER("center"),
    RIGHT("right"),
    LEFT_CORNER("left_corner"),
    RIGHT_CORNER("right_corner");

    override fun asString(): String = string
}