package com.mystery2099

import com.mystery2099.WoodenAccentsMod.toId
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.Blocks
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.Text


object WoodenAccentsModItemGroups {
    //Lists
    @JvmStatic
    val itemGroups: MutableList<ItemGroup> = ArrayList()
    @JvmStatic
    val ItemGroupToList: MutableMap<ItemGroup, MutableList<ItemConvertible>> = HashMap()
    @JvmStatic
    val outsideItems: MutableList<ItemConvertible> = ArrayList()
    @JvmStatic
    val kitchenItems: MutableList<ItemConvertible> = ArrayList()
    @JvmStatic
    val livingRoomItems: MutableList<ItemConvertible> = ArrayList()
    @JvmStatic
    val bedroomItems: MutableList<ItemConvertible> = ArrayList()
    @JvmStatic
    val storageBlocks: MutableList<ItemConvertible> = ArrayList()


    val outsideBlockItemGroup: ItemGroup = createItemGroup("outside", outsideItems)
    val kitchenItemGroup: ItemGroup = createItemGroup("kitchen", kitchenItems)
    val livingRoomItemGroup: ItemGroup = createItemGroup("living_room", livingRoomItems)
    val bedroomItemGroup: ItemGroup = createItemGroup("bedroom", bedroomItems)
    val storageBlocksItemGroup: ItemGroup  = createItemGroup("storage", storageBlocks)

    fun register() {
        bedroomItems.add(Blocks.DIRT)
        storageBlocks.add(Blocks.DIRT)

        ItemGroupToList.forEach { (group, list) ->
            ItemGroupEvents.modifyEntriesEvent(group).register { content -> list.forEach(content::add) }
        }

        WoodenAccentsMod.logger.info("Registering ItemGroups for mod: ${WoodenAccentsMod.MOD_ID}")
    }

    private fun createItemGroup(name: String, itemList: MutableList<ItemConvertible>): ItemGroup {
        return FabricItemGroup.builder(name.toId())
            .icon { ItemStack(itemList[0]) }
            .displayName(name.toItemGroupKey())
            .build().mapTo(itemList).apply { itemGroups.add(this) }
    }

    private fun String.toItemGroupKey(): Text {
        return Text.translatable("itemGroup.${WoodenAccentsMod.MOD_ID}.$this")
    }

    private fun ItemGroup.mapTo(list: MutableList<ItemConvertible>): ItemGroup {
        ItemGroupToList[this] = list
        return this
    }

}