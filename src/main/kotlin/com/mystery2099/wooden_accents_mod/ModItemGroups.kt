package com.mystery2099.wooden_accents_mod

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asWamId
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.GroupedBlock
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items


object ModItemGroups {

    val outsideBlockItemGroup: ItemGroup = itemGroupCalled("outside")
    val kitchenItemGroup: ItemGroup = itemGroupCalled("kitchen")
    val livingRoomItemGroup: ItemGroup = itemGroupCalled("living_room")
    val bedroomItemGroup: ItemGroup = itemGroupCalled("bedroom")
    val storageBlocksItemGroup: ItemGroup  = itemGroupCalled("storage")

    private val itemGroupToEntriesMap = mapOf<ItemGroup, MutableList<ItemConvertible>>(
        outsideBlockItemGroup to mutableListOf(),
        kitchenItemGroup to mutableListOf(),
        livingRoomItemGroup to mutableListOf(),
        bedroomItemGroup to mutableListOf(),
        storageBlocksItemGroup to mutableListOf()
    ).also {
        ModBlocks.blocks.forEach { block ->
            if (block is GroupedBlock) it[block.itemGroup]?.add(block)
        }
        it.forEach { (_, entries) ->
            if (entries.isEmpty()) entries += (Items.DIRT)
        }
    }
    private fun Map<ItemGroup, MutableList<ItemConvertible>>.byId() = mapKeys { (key, _) -> key.id }
    val itemGroups get() = itemGroupToEntriesMap.keys
    val ItemGroup.entries get() = itemGroupToEntriesMap[this] ?: emptyList()
    fun register() {
        itemGroupToEntriesMap.forEach { (itemGroup, entries) ->
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register { entries.forEach(it::add)}
        }

        WoodenAccentsMod.logger.info("Registering ItemGroups for mod: ${WoodenAccentsMod.MOD_ID}")
    }

    private infix fun itemGroupCalled(name: String) = FabricItemGroup.builder(name.asWamId()).apply {
        icon { ItemStack(itemGroupToEntriesMap.byId()[name.asWamId()]?.get(0) ?: Items.DIRT) }
    }.build()
}