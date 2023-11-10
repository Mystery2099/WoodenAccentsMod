package com.github.mystery2099.woodenAccentsMod.registry.tag

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

/**
 * [ModItemTags] defines custom item tags for items used in the Wooden Accents Mod.
 */
object ModItemTags {

    val chests = "chests".createItemTag("c")
    val unnestable = "unnestable".createItemTag()


    private fun String.createItemTag(namespace: String = WoodenAccentsMod.MOD_ID): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, this.toIdentifier(namespace))

    /**
     * Checks if the current [ItemStack] is in the specified item tag.
     *
     * @param tag The item tag to check against.
     * @return True if the [ItemStack] is in the specified item tag; otherwise, false.
     */
    infix fun ItemStack.isIn(tag: TagKey<Item>) = isIn(tag)

    /**
     * Checks if the specified item tag contains the current [ItemStack].
     *
     * @param stack The [ItemStack] to check for.
     * @return True if the item tag contains the current [ItemStack]; otherwise, false.
     */
    infix operator fun TagKey<Item>.contains(stack: ItemStack) = stack.isIn(this)

}