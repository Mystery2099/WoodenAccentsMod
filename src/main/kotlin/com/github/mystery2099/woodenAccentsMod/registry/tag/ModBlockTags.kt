package com.github.mystery2099.woodenAccentsMod.registry.tag

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier


object ModBlockTags {

    private val _blockToItemTagMap: MutableMap<TagKey<Block>, TagKey<Item>> = HashMap()

    /** Block tags that should have an item tag with the same contents during data generation. */
    val blockToItemTagMap: Map<TagKey<Block>, TagKey<Item>>
        get() = _blockToItemTagMap

    // Outside
    // Pillars
    val pillars = "pillars".toBlockTag().createMatchingItemTag()
    val thinPillars = "thin_pillars".toBlockTag().createMatchingItemTag()
    val thickPillars = "thick_pillars".toBlockTag().createMatchingItemTag()
    val thinPillarsConnectable = "thin_pillars_connectable".toBlockTag()
    val thickPillarsConnectable = "thick_pillars_connectable".toBlockTag()

    // Walls
    val woodenWalls = "wooden_walls".toBlockTag().createMatchingItemTag()

    // Ladders
    val plankLadders = "plank_ladders".toBlockTag().createMatchingItemTag()
    val connectingLadders = "connecting_ladders".toBlockTag().createMatchingItemTag()

    // Fences
    val modernFences = "modern_fences".toBlockTag().createMatchingItemTag()
    val modernFenceConnectable = "modern_fence_connectable".toBlockTag()
    val modernFenceGates = "modern_fence_gates".toBlockTag().createMatchingItemTag()

    val supportBeams = "support_beams".toBlockTag().createMatchingItemTag()
    val crates = "crates".toBlockTag().createMatchingItemTag()
    // Living room
    val tables = "tables".toBlockTag().createMatchingItemTag()
    val coffeeTables = "coffee_tables".toBlockTag().createMatchingItemTag()
    val thinBookshelves = "thin_bookshelves".toBlockTag().createMatchingItemTag()
    val plankCarpets = "plank_carpets".toBlockTag().createMatchingItemTag()

    @JvmStatic
    val desks = "desks".toBlockTag().createMatchingItemTag()
    val deskDrawers = "desk_drawers".toBlockTag().createMatchingItemTag()
    // Kitchen
    @JvmStatic
    val kitchenCounters = "kitchen_counters".toBlockTag().createMatchingItemTag()
    val kitchenCabinets = "kitchen_cabinets".toBlockTag().createMatchingItemTag()

    val chairs = "chairs".toBlockTag().createMatchingItemTag()
    private fun String.toBlockTag(namespace: String = WoodenAccentsMod.MOD_ID): TagKey<Block> {
        return TagKey.of(RegistryKeys.BLOCK, Identifier(namespace, this))
    }

    /** Records a same-ID item tag for data generation and returns this block tag unchanged. */
    private fun TagKey<Block>.createMatchingItemTag() = also {
        _blockToItemTagMap[this] = TagKey.of(RegistryKeys.ITEM, this.id)
    }

    /** Treats a missing state as a non-match. */
    operator fun TagKey<Block>?.contains(blockState: BlockState?) = blockState?.isIn(this) ?: false

    /** Falls back to a same-ID item tag for block tags that were not registered above. */
    fun getItemTagFrom(blockTag: TagKey<Block>): TagKey<Item> {
        return blockToItemTagMap[blockTag] ?: TagKey.of(RegistryKeys.ITEM, blockTag.id)
    }
}
