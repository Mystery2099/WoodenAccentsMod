package com.github.mystery2099.woodenAccentsMod.block.custom.enums

import net.minecraft.util.StringRepresentable

enum class CoffeeTableTypes(private val string: String) : StringRepresentable {
    SHORT("short"),
    TALL("tall");


    override fun getSerializedName() = string
    companion object {
        // Stable serialized key used by block-item NBT.
        const val TAG = "coffee_table_type"
    }
}
