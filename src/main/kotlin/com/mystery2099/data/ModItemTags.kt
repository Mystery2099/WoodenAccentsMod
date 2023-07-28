package com.mystery2099.data

import com.mystery2099.WoodenAccentsMod
import com.mystery2099.WoodenAccentsMod.toId
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object ModItemTags {

    val chests = "chests".toItemTag("c")

    private fun String.toItemTag(namespace: String): TagKey<Item> {
        return TagKey.of(RegistryKeys.ITEM, this.toId(namespace))
    }
    internal fun String.toItemTag(): TagKey<Item> {
        return this.toItemTag(WoodenAccentsMod.MOD_ID)
    }
}