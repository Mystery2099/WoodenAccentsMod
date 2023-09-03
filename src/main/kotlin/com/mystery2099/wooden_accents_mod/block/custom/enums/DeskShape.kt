package com.mystery2099.wooden_accents_mod.block.custom.enums

import net.minecraft.util.StringIdentifiable

enum class DeskShape(val string: String) : StringIdentifiable {
    STRAIGHT("straight"),
    LEFT("left"),
    RIGHT("right");

    override fun asString(): String = string
}