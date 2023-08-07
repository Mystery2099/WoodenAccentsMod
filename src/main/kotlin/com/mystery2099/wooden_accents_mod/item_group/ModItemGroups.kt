package com.mystery2099.wooden_accents_mod.item_group

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents


object ModItemGroups {

    val outsideBlockItemGroup = CreativeTab("outside")
    @JvmStatic
    val kitchenItemGroup = CreativeTab("kitchen")
    val livingRoomItemGroup = CreativeTab("living_room")
    val bedroomItemGroup = CreativeTab("bedroom")
    val storageBlocksItemGroup  = CreativeTab("storage")

    fun register() {
        /*itemGroupToEntriesMap.forEach { (itemGroup, entries) ->
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register { entries.forEach(it::add)}
        }*/
        CreativeTab.instances.forEach{ group ->
            ItemGroupEvents.modifyEntriesEvent(group.itemGroup).register { group.entries.forEach(it::add) }
        }

        WoodenAccentsMod.logger.info("Registering ItemGroups for mod: ${WoodenAccentsMod.MOD_ID}")
    }

}