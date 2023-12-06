package com.github.mystery2099.woodenAccentsMod.registry.tag

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier


/**
 * ModBlockTags is a class that represents a collection of custom block tags.
 *
 * This class provides methods for creating block tags, creating matching item tags, checking if a block state
 * is in a block tag, and getting the matching item tag for a block tag.
 */
object ModBlockTags {

    /**
     * A map to associate block tags with their matching item tags.
     * It is used to store the mapping between block tags and their corresponding item tags.
     *
     * @property _blockToItemTagMap A mutable map that stores the mapping between block tags and item tags.
     */
    private val _blockToItemTagMap: MutableMap<TagKey<Block>, TagKey<Item>> = HashMap()


    /**
     * Map that associates block tags with corresponding item tags.
     */
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

    val chairs = "chairs".toBlockTag().createMatchingItemTag()
    /*---------------End Kitchen Tags----------------*/

    /*---------------Storage Tags----------------*/
    /*---------------End Storage Tags----------------*/


    /**
     * Converts the given string to a block tag key.
     *
     * @param namespace the namespace of the block tag key (default is the MOD_ID)
     * @return the block tag key
     */
    private fun String.toBlockTag(namespace: String = WoodenAccentsMod.MOD_ID): TagKey<Block> {
        return TagKey.of(RegistryKeys.BLOCK, Identifier(namespace, this))
    }

    /**
     * Creates a matching item tag based on this block tag key.
     *
     * @return The modified block tag key.
     */
    private fun TagKey<Block>.createMatchingItemTag() = also {
        _blockToItemTagMap[this] = TagKey.of(RegistryKeys.ITEM, this.id)
    }


    /**
     * Checks if the given [BlockState] is contained within the tag specified by [TagKey].
     * Returns true if the block is in the tag or if the blockState is null.
     *
     * @param blockState The block state to check if it is contained within the tag.
     * @return True if the block is in the tag or if the blockState is null, false otherwise.
     */
    operator fun TagKey<Block>?.contains(blockState: BlockState?) = blockState?.isIn(this) ?: false

    /**
     * Retrieves the item tag associated with the given block tag.
     *
     * @param blockTag The block tag key for which to retrieve the item tag.
     * @return The item tag key associated with the given block tag.
     */
    fun getItemTagFrom(blockTag: TagKey<Block>): TagKey<Item> {
        return blockToItemTagMap[blockTag] ?: TagKey.of(RegistryKeys.ITEM, blockTag.id)
    }
}