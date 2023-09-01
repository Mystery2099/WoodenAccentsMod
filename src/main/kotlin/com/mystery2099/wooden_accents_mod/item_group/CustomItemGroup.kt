package com.mystery2099.wooden_accents_mod.item_group

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomItemGroupProvider
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text
import java.util.function.Consumer

class CustomItemGroup(name: String) {
    val itemGroup: ItemGroup
    inline val entries: List<ItemConvertible>
        get() = run {
            val list = mutableListOf<ItemConvertible>()
            ModBlocks.blocks.forEach { block ->
                if (block is CustomItemGroupProvider && block.itemGroup == this) list += block
            }
            return list.ifEmpty { listOf(Items.DIRT) }
        }
    inline val displayName: Text
        get() = itemGroup.displayName

    inline val icon : ItemStack
        get() = itemGroup.icon

    inline val texture: String
        get() = itemGroup.texture

    inline val shouldRenderName: Boolean
        get() = itemGroup.shouldRenderName()

    inline val hasScrollbar: Boolean
        get() = itemGroup.hasScrollbar()

    inline val column: Int
        get() = itemGroup.column

    inline val row: ItemGroup.Row
        get() = itemGroup.row

    inline val hasStacks: Boolean
        get() = itemGroup.hasStacks()

    inline val shouldDisplay: Boolean
        get() = itemGroup.shouldDisplay()

    inline val isSpecial: Boolean
        get() = itemGroup.isSpecial

    val type: ItemGroup.Type
        get() = itemGroup.type
    inline val displayStacks: Collection<ItemStack>
        get() = itemGroup.displayStacks

    inline val searchTabStacks: Collection<ItemStack>
        get() = itemGroup.searchTabStacks

    init {
        itemGroup = FabricItemGroup.builder(name.toIdentifier()).apply {
            icon { ItemStack(if (entries.isNotEmpty()) entries[0] else Items.DIRT) }
        }.build()
        mutableInstances += this
    }

    fun updateEntries(displayContext: ItemGroup.DisplayContext) = itemGroup.updateEntries(displayContext)

    infix operator fun contains(stack: ItemStack) = itemGroup.contains(stack)

    infix fun setSearchProviderReloader(searchProviderReloader: Consumer<List<ItemStack>>) {
        itemGroup.setSearchProviderReloader(searchProviderReloader)
    }

    fun reloadSearchProvider() = itemGroup.reloadSearchProvider()

    companion object {
        val instances: List<CustomItemGroup>
            get() = mutableInstances

        private val mutableInstances: MutableList<CustomItemGroup> = mutableListOf()
    }
}