package com.github.mystery2099.woodenAccentsMod.block.custom.enums

import net.minecraft.util.StringIdentifiable

enum class CoffeeTableTypes(private val string: String) : StringIdentifiable {
    SHORT("short"),
    TALL("tall");


    override fun asString() = string
    companion object {
        // Stable serialized key used by block-item NBT.
        const val TAG = "coffee_table_type"
    }
}
