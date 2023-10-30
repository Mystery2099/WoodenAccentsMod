package com.github.mystery2099.woodenAccentsMod.item.group

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsModRegistry
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack


object ModItemGroups : WoodenAccentsModRegistry {

    val structuralElements = CustomItemGroup("structural_elements")
    val decorations = CustomItemGroup("decorations")
    val miscellaneous = CustomItemGroup("miscellaneous")

    override fun register() {
        CustomItemGroup.instances.forEach { group ->
            ItemGroupEvents.modifyEntriesEvent(group.get()).register { group.entries.forEach(it::add) }
        }
        super.register()
    }

    infix fun ItemStack.isIn(group: ItemGroup) = this in group
}