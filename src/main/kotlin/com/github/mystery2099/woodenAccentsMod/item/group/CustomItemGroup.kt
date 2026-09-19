package com.github.mystery2099.woodenAccentsMod.item.group

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.defaultItemStack
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text

/** A creative tab populated from blocks that opt into this group. */
data class CustomItemGroup(val name: String) {
    init {
        mutableInstances += this
    }
    val key: RegistryKey<ItemGroup> = RegistryKey.of(Registries.ITEM_GROUP.key, name.toIdentifier())
    val itemGroup: ItemGroup = Registry.register(
        Registries.ITEM_GROUP,
        key,
        FabricItemGroup.builder().apply {
            icon { getEntries()[0] }
            displayName(Text.translatable(name.toIdentifier().toTranslationKey()))
        }.build()
    )

    internal fun getEntries(): List<ItemStack> {
        val matchingItems = getBlocksWithMatchingItemGroup()
        val stacksList = getStackListWithVariants(matchingItems)

        return addDefaultStackIfEmpty(stacksList)
    }

    /** Registration order keeps finishes in vanilla wood order within each family. */
    private fun getStackListWithVariants(matchingItems: List<CustomItemGroupProvider>): MutableList<ItemStack> {
        val stacks = mutableListOf<ItemStack>()
        for (family in matchingItems.groupBy { it.javaClass }.values) {
            for (entry in family) {
                if (entry is Block) stacks += entry.defaultItemStack
            }
            for (entry in family) {
                if (entry.hasVariantItemGroupStack) stacks += entry.variantItemGroupStack
            }
        }
        return stacks
    }

    /** The group icon reads entry zero, so empty groups need a harmless fallback. */
    private fun addDefaultStackIfEmpty(list: MutableList<ItemStack>): List<ItemStack> {
        return list.ifEmpty { list + Items.DIRT.defaultStack }
    }

    private fun getBlocksWithMatchingItemGroup() = ModBlocks.blocks.filterIsInstance<CustomItemGroupProvider>()
        .filter { it.itemGroup == this }
    fun get() = itemGroup


    infix operator fun contains(stack: ItemStack) = itemGroup.contains(stack)

    companion object {
        val instances: List<CustomItemGroup>
            get() = mutableInstances

        private val mutableInstances: MutableList<CustomItemGroup> = mutableListOf()
    }
}
