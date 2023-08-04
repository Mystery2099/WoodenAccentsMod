package com.mystery2099.wooden_accents_mod.data

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asId
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object ModItemTags {

    val chests = "chests".toItemTag("c")

    private fun String.toItemTag(namespace: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, asId(namespace))
    internal fun String.toItemTag(): TagKey<Item> = toItemTag(WoodenAccentsMod.MOD_ID)
}