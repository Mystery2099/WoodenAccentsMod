package com.github.mystery2099.woodenAccentsMod.registry.tag

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey

object ModItemTags {

    val chests = "chests".createItemTag("c")
    val unnestable = "unnestable".createItemTag()

    private fun String.createItemTag(namespace: String = WoodenAccentsMod.MOD_ID): TagKey<Item> =
        TagKey.create(Registries.ITEM, this.toIdentifier(namespace))

    infix fun ItemStack.isIn(tag: TagKey<Item>) = `is`(tag)

    infix operator fun TagKey<Item>.contains(stack: ItemStack) = stack.isIn(this)

}
