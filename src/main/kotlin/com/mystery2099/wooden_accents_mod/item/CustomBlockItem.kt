package com.mystery2099.wooden_accents_mod.item

import com.mystery2099.wooden_accents_mod.block.custom.CrateBlock
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomTagProvider
import net.minecraft.block.Block
import net.minecraft.entity.ItemEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.registry.tag.TagKey

class CustomBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {
    override fun canBeNested(): Boolean = block !is CrateBlock
    val tagFromProvider: TagKey<Block>?
        get() {
            return if (block is CustomTagProvider) {
                (block as CustomTagProvider).tag
            } else null
        }

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
