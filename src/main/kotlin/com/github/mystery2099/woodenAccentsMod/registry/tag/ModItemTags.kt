package com.github.mystery2099.woodenAccentsMod.registry.tag

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object ModItemTags {

    val chests = "chests".createItemTag("c")
    val unnestable = "unnestable".createItemTag()

    private fun String.createItemTag(namespace: String = WoodenAccentsMod.MOD_ID): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, this.toIdentifier(namespace))

    infix fun ItemStack.isIn(tag: TagKey<Item>) = isIn(tag)

    infix operator fun TagKey<Item>.contains(stack: ItemStack) = stack.isIn(this)

}
