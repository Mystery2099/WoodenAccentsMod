package com.github.mystery2099.woodenAccentsMod.item

import com.github.mystery2099.woodenAccentsMod.block.custom.CrateBlock
import net.minecraft.block.Block
import net.minecraft.entity.ItemEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement

/**
 * Custom block item
 *
 * @constructor
 *
 * @param block
 * @param settings
 */
class CustomBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {
    override fun canBeNested(): Boolean = block !is CrateBlock
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
