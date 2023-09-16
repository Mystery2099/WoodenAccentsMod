package com.mystery2099.wooden_accents_mod.data

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object ModBlockTags {
    private val _blockToItemTagMap: MutableMap<TagKey<Block>, TagKey<Item>> = HashMap()
    val blockToItemTagMap: Map<TagKey<Block>, TagKey<Item>>
        get() = _blockToItemTagMap

    /*---------------Outside Tags----------------*/
    //Pillars
    val pillars = "pillars".toBlockTag().createMatchingItemTag()
    val thinPillars = "thin_pillars".toBlockTag().createMatchingItemTag()
    val thickPillars = "thick_pillars".toBlockTag().createMatchingItemTag()
    val thinPillarsConnectable = "thin_pillars_connectable".toBlockTag()
    val thickPillarsConnectable = "thick_pillars_connectable".toBlockTag()

    //Walls
    val woodenWalls = "wooden_walls".toBlockTag().createMatchingItemTag()

    //Ladders
    val plankLadders = "plank_ladders".toBlockTag().createMatchingItemTag()
    val connectingLadders = "connecting_ladders".toBlockTag().createMatchingItemTag()

    //Fences
    val modernFences = "modern_fences".toBlockTag().createMatchingItemTag()
    val modernFenceConnectable = "modern_fence_connectable".toBlockTag()
    val modernFenceGates = "modern_fence_gates".toBlockTag().createMatchingItemTag()

    val supportBeams = "support_beams".toBlockTag().createMatchingItemTag()
    val crates = "crates".toBlockTag().createMatchingItemTag()
    /*---------------End Outside Tags----------------*/

    /*---------------Living Room Tags----------------*/
    val tables = "tables".toBlockTag().createMatchingItemTag()
    val coffeeTables = "coffee_tables".toBlockTag().createMatchingItemTag()
    val thinBookshelves = "thin_bookshelves".toBlockTag().createMatchingItemTag()
    val plankCarpets = "plank_carpets".toBlockTag().createMatchingItemTag()

    @JvmStatic
    val desks = "desks".toBlockTag().createMatchingItemTag()
    val deskDrawers = "desk_drawers".toBlockTag().createMatchingItemTag()

    /*---------------End Living Room Tags----------------*/

    /*---------------Kitchen Tags----------------*/
    @JvmStatic
    val kitchenCounters = "kitchen_counters".toBlockTag().createMatchingItemTag()
    val kitchenCabinets = "kitchen_cabinets".toBlockTag().createMatchingItemTag()
    /*---------------End Kitchen Tags----------------*/

    /*---------------Storage Tags----------------*/
    /*---------------End Storage Tags----------------*/

    private fun String.toBlockTag(namespace: String = WoodenAccentsMod.MOD_ID): TagKey<Block> {
        return TagKey.of(RegistryKeys.BLOCK, Identifier(namespace, this))
    }

    private fun TagKey<Block>.createMatchingItemTag() = also {
        _blockToItemTagMap[this] = TagKey.of(RegistryKeys.ITEM, this.id)
    }

    infix operator fun TagKey<Block>?.contains(block: BlockState?) = block?.isIn(this) ?: false

}