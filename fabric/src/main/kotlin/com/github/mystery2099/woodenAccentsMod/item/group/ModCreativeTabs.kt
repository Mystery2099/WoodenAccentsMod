package com.github.mystery2099.woodenAccentsMod.item.group

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsModRegistry
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents

object ModCreativeTabs : WoodenAccentsModRegistry {

    val furniture = CustomItemGroup(ModItemGroup.FURNITURE)
    val storage = CustomItemGroup(ModItemGroup.STORAGE)
    val building = CustomItemGroup(ModItemGroup.BUILDING)

    private val groups: List<CustomItemGroup> = listOf(furniture, storage, building)

    override fun register() {
        groups.forEach { it.register() }
        groups.forEach { group ->
            ItemGroupEvents.modifyEntriesEvent(group.key).register { event ->
                ItemGroupContent.getEntries(group.group).forEach(event::accept)
            }
        }
        super.register()
    }
}
