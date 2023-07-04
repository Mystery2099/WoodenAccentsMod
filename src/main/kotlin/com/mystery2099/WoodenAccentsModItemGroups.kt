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
        .icon { ItemStack(ModBlocks.thinOakPillar) }
        .displayName("outside".toItemGroupKey())
        .build().woodenAccentsMod()
    @JvmStatic
    val kitchenItemGroup: ItemGroup = FabricItemGroup.builder("kitchen".toId())
        .icon { ItemStack(Blocks.AIR) }
        .displayName("kitchen".toItemGroupKey())
        .build().woodenAccentsMod()
    @JvmStatic
    val livingRoomItemGroup: ItemGroup = FabricItemGroup.builder("living_room".toId())
        .icon { ItemStack(ModBlocks.oakCoffeeTable) }
        .displayName("living_room".toItemGroupKey())
        .build().woodenAccentsMod()
    @JvmStatic
    val bedroomItemGroup: ItemGroup = FabricItemGroup.builder("bedroom".toId())
        .icon { ItemStack(Blocks.AIR) }
        .displayName("bedroom".toItemGroupKey())
        .build().woodenAccentsMod()
    @JvmStatic
    val storageBlocksItemGroup: ItemGroup = FabricItemGroup.builder("storage".toId())
        .icon { ItemStack(Blocks.AIR) }
        .displayName("storage".toItemGroupKey())
        .build().woodenAccentsMod()

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

    private fun ItemGroup.woodenAccentsMod(): ItemGroup {
        itemGroups.add(this)
        return this
    }
}