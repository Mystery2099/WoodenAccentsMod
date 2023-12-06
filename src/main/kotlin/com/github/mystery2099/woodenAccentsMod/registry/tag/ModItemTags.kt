package com.github.mystery2099.woodenAccentsMod.registry.tag

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
/**
 * The ModItemTags object contains predefined item tags for the Wooden Accents Mod.
 */
object ModItemTags {

    val chests = "chests".createItemTag("c")
    val unnestable = "unnestable".createItemTag()


    /**
     * Creates an item tag key using the given string as the tag name and an optional namespace.
     *
     * @param namespace The namespace prefix for the tag name. Defaults to the value of `WoodenAccentsMod.MOD_ID`.
     * @return The tag key for the item tag.
     */
    private fun String.createItemTag(namespace: String = WoodenAccentsMod.MOD_ID): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, this.toIdentifier(namespace))



    /**
     * Checks if the ItemStack is in the specified tag.
     *
     * @param tag The tag to check the ItemStack against.
     * @return `true` if the ItemStack is in the specified tag, `false` otherwise.
     */
    infix fun ItemStack.isIn(tag: TagKey<Item>) = isIn(tag)

    /**
     * Checks if the specified [ItemStack] is contained within this [TagKey].
     *
     * @param stack The [ItemStack] to check for containment.
     * @return `true` if the [ItemStack] is contained within this [TagKey]; `false` otherwise.
     */
    infix operator fun TagKey<Item>.contains(stack: ItemStack) = stack.isIn(this)

}