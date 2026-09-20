package com.github.mystery2099.woodenAccentsMod.item.group

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsModRegistry
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack


object ModItemGroups : WoodenAccentsModRegistry {

    val furniture = CustomItemGroup("decorations")
    val storage = CustomItemGroup("miscellaneous")
    val building = CustomItemGroup("structural_elements")

    override fun register() {
        CustomItemGroup.instances.forEach { group ->
            ItemGroupEvents.modifyEntriesEvent(group.key).register { group.getEntries().forEach(it::accept) }
        }
        super.register()
    }

    infix fun ItemStack.isIn(group: CreativeModeTab) = this in group
}
