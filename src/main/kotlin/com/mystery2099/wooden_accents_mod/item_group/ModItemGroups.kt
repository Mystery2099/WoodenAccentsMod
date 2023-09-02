package com.mystery2099.wooden_accents_mod.item_group

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents


object ModItemGroups {

    val structuralElements = CustomItemGroup("structural_elements")
    val decorations = CustomItemGroup("decorations")
    val miscellaneous  = CustomItemGroup("miscellaneous")

    fun register() {
        CustomItemGroup.instances.forEach{ group ->
            ItemGroupEvents.modifyEntriesEvent(group.get()).register { group.entries.forEach(it::add) }
        }
        WoodenAccentsMod.logger.info("Registering ItemGroups for mod: ${WoodenAccentsMod.MOD_ID}")
    }

}