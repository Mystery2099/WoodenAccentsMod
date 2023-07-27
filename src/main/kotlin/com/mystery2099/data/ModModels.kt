package com.mystery2099.data

import com.mystery2099.WoodenAccentsMod.toBlockId
import com.mystery2099.WoodenAccentsMod.toId
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import java.util.*

object ModModels {
    /*---------------Texture Keys----------------*/
    val legs: TextureKey = TextureKey.of("legs")


    /*---------------Outside Stuff----------------*/

    //Thin Pillar
    val thinPillarInventory = item("thin_pillar", TextureKey.ALL)
    val thinPillarBottom = block("thin_pillar_base", "_base", TextureKey.ALL)
    //Thick Pillar
    val thickPillarInventory = item("thick_pillar", TextureKey.ALL)
    val thickPillarBottom = block("thick_pillar_base", "_base", TextureKey.ALL)

    //Wooden Fences
    val woodenFenceInventory = item("wooden_fence", TextureKey.SIDE, TextureKey.END, TextureKey.UP)
    val woodenFencePost = block("wooden_fence_post", "_post", TextureKey.END, TextureKey.UP)
    val woodenFenceSide = block("wooden_fence_side", "_side", TextureKey.SIDE)

    //Plank Ladder
    val plankLadder = block("plank_ladder", TextureKey.ALL)
    /*---------------Living Room Stuff----------------*/
    //Table Models
    val tableItem = item("table", TextureKey.TOP, legs)
    val tableTop = block("table_top", "_top", TextureKey.TOP)
    val tableCenterLeg = block("table_single_leg", "_single_leg", legs)
    val tableEndLeg = block("table_end_leg", "_end_leg", legs)
    val tableCornerLeg = block("table_corner_leg", "_corner_leg", legs)

    //Coffee Table Models
    val coffeeTableInventory: Model = item("coffee_table", TextureKey.TOP, legs)
    val coffeeTableTopTall: Model = block("coffee_table_top_tall",
        "_top_tall", TextureKey.TOP)
    val coffeeTableLegTall: Model = block(
        "coffee_table_leg_tall",
        "_leg_tall",
        legs
    )
    val coffeeTableTopShort: Model =
        block("coffee_table_top_short", "_top_short", TextureKey.TOP)
    val coffeeTableLegShort: Model = block(
        "coffee_table_leg_short",
        "_leg_short",
        legs
    )

    //Bookshelves
    val thinBookshelfItem = item("thin_bookshelf", TextureKey.ALL)
    val thinBookshelfBlock = block("thin_bookshelf", "_empty", TextureKey.ALL)
    val thinBookshelfSlot0 = "thin_bookshelf_slot_0".toBlockId()
    val thinBookshelfSlot1 = "thin_bookshelf_slot_1".toBlockId()
    val thinBookshelfSlot2 = "thin_bookshelf_slot_2".toBlockId()
    val thinBookshelfSlot3 = "thin_bookshelf_slot_3".toBlockId()
    val thinBookshelfSlot4 = "thin_bookshelf_slot_4".toBlockId()
    val thinBookshelfSlot5 = "thin_bookshelf_slot_5".toBlockId()


    /*---------------Kitchen Stuff----------------*/

    //Kitchen Counters
    val kitchenCounter: Model = block("kitchen_counter", TextureKey.SIDE, TextureKey.TOP)
    val kitchenCounterInnerLeftCorner: Model = block(
        "kitchen_counter_inner_left_corner",
        "_inner_left_corner",
        TextureKey.SIDE,
        TextureKey.TOP
    )
    val kitchenCounterOuterLeftCorner: Model = block(
        "kitchen_counter_outer_left_corner",
        "_outer_left_corner",
        TextureKey.SIDE,
        TextureKey.TOP
    )

    //Kitchen Cabinet
    val kitchenCabinet: Model = block("kitchen_cabinet", TextureKey.SIDE, TextureKey.TOP)

    /*---------------Bedroom Stuff----------------*/


    @Suppress("unused")
    private fun make(vararg requiredTextureKeys: TextureKey): Model {
        return Model(Optional.empty(), Optional.empty(), *requiredTextureKeys)
    }

    private fun block(parent: String, vararg requiredTextureKeys: TextureKey): Model {
        return Model(
            Optional.of("block/$parent".toId()),
            Optional.empty(),
            *requiredTextureKeys
        )
    }

    @Suppress("unused")
    private fun item(parent: String, vararg requiredTextureKeys: TextureKey): Model {
        return Model(
            Optional.of("item/$parent".toId()),
            Optional.empty(),
            *requiredTextureKeys
        )
    }

    private fun block(parent: String, variant: String, vararg requiredTextureKeys: TextureKey): Model {
        return Model(
            Optional.of("block/$parent".toId()),
            Optional.of(variant),
            *requiredTextureKeys
        )
    }
}