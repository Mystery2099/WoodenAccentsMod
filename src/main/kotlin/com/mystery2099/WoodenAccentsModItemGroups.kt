package com.mystery2099

import com.mystery2099.WoodenAccentsMod.toId
import com.mystery2099.block.ModBlocks
import com.mystery2099.block.custom.interfaces.GroupedBlock
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items


object WoodenAccentsModItemGroups {

    val outsideBlockItemGroup: ItemGroup = createItemGroup("outside")
    val kitchenItemGroup: ItemGroup = createItemGroup("kitchen")
    val livingRoomItemGroup: ItemGroup = createItemGroup("living_room")
    val bedroomItemGroup: ItemGroup = createItemGroup("bedroom")
    val storageBlocksItemGroup: ItemGroup  = createItemGroup("storage")

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

    private fun createItemGroup(name: String) = FabricItemGroup.builder(name.toId()).apply {
        icon { ItemStack(itemGroupToEntriesMap.byId()[name.toId()]?.get(0) ?: Items.DIRT) }
    }.build()
}