package com.mystery2099.wooden_accents_mod.data

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asPathIn
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object ModItemTags {

    val chests = "chests" asItemTagOf "c"
    val uncrateable = "uncrateable".toItemTag()

    private infix fun String.asItemTagOf(namespace: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, asPathIn(namespace))
    internal fun String.toItemTag(): TagKey<Item> = asItemTagOf(WoodenAccentsMod.MOD_ID)
}