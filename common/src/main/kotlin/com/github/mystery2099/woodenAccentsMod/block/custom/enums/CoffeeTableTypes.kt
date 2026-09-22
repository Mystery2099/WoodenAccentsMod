package com.github.mystery2099.woodenAccentsMod.block.custom.enums

import net.minecraft.util.StringRepresentable

enum class CoffeeTableTypes(private val string: String) : StringRepresentable {
    SHORT("short"),
    TALL("tall");


    override fun getSerializedName() = string
}
