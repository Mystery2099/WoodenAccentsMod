package com.github.mystery2099.woodenAccentsMod.item.group

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.defaultItemStack
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.world.level.block.Block
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.network.chat.Component

/** A creative tab populated from blocks that opt into this group. */
data class CustomItemGroup(val name: String) {
    init {
        mutableInstances += this
    }
    val key: ResourceKey<CreativeModeTab> = ResourceKey.create(Registries.CREATIVE_MODE_TAB, name.toIdentifier())
    val itemGroup: CreativeModeTab = Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB,
        key,
        FabricItemGroup.builder().apply {
            icon { getEntries()[0] }
            title(Component.translatable(name.toIdentifier().toLanguageKey()))
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
        return list.ifEmpty { list + Items.DIRT.defaultInstance }
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
