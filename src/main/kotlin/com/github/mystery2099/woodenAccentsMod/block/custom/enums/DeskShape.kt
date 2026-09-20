package com.github.mystery2099.woodenAccentsMod.block.custom.enums

import net.minecraft.util.StringRepresentable

enum class DeskShape(val string: String) : StringRepresentable {
    SINGLE("single"),
    LEFT("left"),
    CENTER("center"),
    RIGHT("right"),
    LEFT_CORNER("left_corner"),
    RIGHT_CORNER("right_corner");

    override fun getSerializedName(): String = string
}
