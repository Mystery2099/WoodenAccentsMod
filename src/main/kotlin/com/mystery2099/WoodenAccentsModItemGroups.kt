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
        .icon { ItemStack(Blocks.AIR) }
        .displayName("outside".toItemGroupKey())
        .build()
    @JvmStatic
    val kitchenItemGroup: ItemGroup = FabricItemGroup.builder("kitchen".toId())
        .icon { ItemStack(Blocks.AIR) }
        .displayName("kitchen".toItemGroupKey())
        .build()
    @JvmStatic
    val livingRoomItemGroup: ItemGroup = FabricItemGroup.builder("living_room".toId())
        .icon { ItemStack(Blocks.AIR) }
        .displayName("living_room".toItemGroupKey())
        .build()
    @JvmStatic
    val bedroomItemGroup: ItemGroup = FabricItemGroup.builder("bedroom".toId())
        .icon { ItemStack(Blocks.AIR) }
        .displayName("bedroom".toItemGroupKey())
        .build()
    @JvmStatic
    val storageBlocksItemGroup: ItemGroup = FabricItemGroup.builder("storage_blocks".toId())
        .icon { ItemStack(Blocks.AIR) }
        .displayName("storage_blocks".toItemGroupKey())
        .build()

    fun register() {
        outsideItems.add(Blocks.DIRT)
        kitchenItems.add(Blocks.DIRT)
        livingRoomItems.add(Blocks.DIRT)
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