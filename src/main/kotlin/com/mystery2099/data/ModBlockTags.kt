package com.mystery2099.data

import com.mystery2099.WoodenAccentsMod
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object ModBlockTags {
    val TEST_TAG = "test_tag".toBlockTag()

    private fun String.toBlockTag(namespace: String): TagKey<Block> {
        return TagKey.of(RegistryKeys.BLOCK, Identifier(namespace, this))
    }
    fun String.toBlockTag(): TagKey<Block> {
        return this.toBlockTag(WoodenAccentsMod.modid)
    }
}