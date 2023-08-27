package com.mystery2099.wooden_accents_mod.block.custom.enums

import net.minecraft.util.StringIdentifiable


enum class CoffeeTableType(private val string: String) : StringIdentifiable {
    SHORT("short"),
    TALL("tall");

    override fun asString() = string
}

