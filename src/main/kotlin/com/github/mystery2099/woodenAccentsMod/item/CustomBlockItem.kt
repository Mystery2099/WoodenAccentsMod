package com.github.mystery2099.woodenAccentsMod.item

import com.github.mystery2099.woodenAccentsMod.block.item
import com.github.mystery2099.woodenAccentsMod.block.custom.CrateBlock
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModItemTags
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModItemTags.contains
import net.minecraft.block.Block
import net.minecraft.entity.ItemEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement

/**
 * [CustomBlockItem] is a specialized [BlockItem] for custom blocks in the Wooden Accents Mod.
 *
 * @param block The block associated with this custom item.
 * @param settings The settings for the item.
 */
class CustomBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {
    override fun canBeNested(): Boolean = super.canBeNested() && block.item.defaultStack !in ModItemTags.unnestable
    override fun onItemEntityDestroyed(entity: ItemEntity) {
        super.onItemEntityDestroyed(entity)
        if (block is CrateBlock) {
            getBlockEntityNbt(entity.stack)?.let { nbtCompound ->
                if (nbtCompound.contains("Items", NbtElement.LIST_TYPE.toInt())) {
                    val nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE.toInt())
                    ItemUsage.spawnItemContents(entity, nbtList.stream()
                        .map { it as NbtCompound }
                        .map { ItemStack.fromNbt(it) })
                }
            }
        }
    }
}
