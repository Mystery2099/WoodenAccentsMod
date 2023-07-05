package com.mystery2099

import com.mystery2099.WoodenAccentsMod.toId
import com.mystery2099.block.ModBlocks
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
    val outsideItems: MutableList<ItemConvertible> = ArrayList()
    @JvmStatic
    val kitchenItems: MutableList<ItemConvertible> = ArrayList()
    @JvmStatic
    val livingRoomItems: MutableList<ItemConvertible> = ArrayList()
    @JvmStatic
    val bedroomItems: MutableList<ItemConvertible> = ArrayList()
    @JvmStatic
    val storageBlocks: MutableList<ItemConvertible> = ArrayList()


    @JvmStatic
    val outsideBlockItemGroup: ItemGroup = FabricItemGroup.builder("outside".toId())
        .icon { ItemStack(outsideItems[0]) }
        .displayName("outside".toItemGroupKey())
        .build().apply { itemGroups.add(this) }
    @JvmStatic
    val kitchenItemGroup: ItemGroup = FabricItemGroup.builder("kitchen".toId())
        .icon { ItemStack(kitchenItems[0]) }
        .displayName("kitchen".toItemGroupKey())
        .build().apply { itemGroups.add(this) }
    @JvmStatic
    val livingRoomItemGroup: ItemGroup = FabricItemGroup.builder("living_room".toId())
        .icon { ItemStack(livingRoomItems[0]) }
        .displayName("living_room".toItemGroupKey())
        .build().apply { itemGroups.add(this) }
    @JvmStatic
    val bedroomItemGroup: ItemGroup = FabricItemGroup.builder("bedroom".toId())
        .icon { ItemStack(bedroomItems[0]) }
        .displayName("bedroom".toItemGroupKey())
        .build().apply { itemGroups.add(this) }
    @JvmStatic
    val storageBlocksItemGroup: ItemGroup = FabricItemGroup.builder("storage".toId())
        .icon { ItemStack(storageBlocks[0]) }
        .displayName("storage".toItemGroupKey())
        .build().apply { itemGroups.add(this) }

    fun register() {
        kitchenItems.add(Blocks.DIRT)
        bedroomItems.add(Blocks.DIRT)
        storageBlocks.add(Blocks.DIRT)


        ItemGroupEvents.modifyEntriesEvent(outsideBlockItemGroup).register { content -> outsideItems.forEach(content::add) }
        ItemGroupEvents.modifyEntriesEvent(kitchenItemGroup).register { content -> kitchenItems.forEach(content::add) }
        ItemGroupEvents.modifyEntriesEvent(livingRoomItemGroup).register { content -> livingRoomItems.forEach(content::add) }
        ItemGroupEvents.modifyEntriesEvent(bedroomItemGroup).register { content -> bedroomItems.forEach(content::add) }
        ItemGroupEvents.modifyEntriesEvent(storageBlocksItemGroup).register { content -> storageBlocks.forEach(content::add) }

        WoodenAccentsMod.logger.info("Registering ItemGroups for mod: ${WoodenAccentsMod.modid}")
    }

    private fun String.toItemGroupKey(): Text {
        return Text.translatable("itemGroup.${WoodenAccentsMod.modid}.$this")
    }


}