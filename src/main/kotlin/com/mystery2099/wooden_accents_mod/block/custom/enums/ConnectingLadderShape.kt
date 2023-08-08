package com.mystery2099.wooden_accents_mod.block.custom.enums

import net.minecraft.util.StringIdentifiable

enum class ConnectingLadderShape(private val string: String) : StringIdentifiable
{
    SINGLE("single"),
    CENTER("center"),
    LEFT("left"),
    RIGHT("right");


    override fun toString(): String = string

    override fun asString(): String = string
}

