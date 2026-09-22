package com.github.mystery2099.woodenAccentsMod.block.custom.enums

import net.minecraft.util.StringRepresentable

enum class SidewaysConnectionShape(private val string: String) : StringRepresentable
{
    SINGLE("single"),
    CENTER("center"),
    LEFT("left"),
    RIGHT("right");


    override fun toString(): String = string

    override fun getSerializedName(): String = string
}

