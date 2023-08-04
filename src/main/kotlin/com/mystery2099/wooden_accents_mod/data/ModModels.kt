package com.mystery2099.wooden_accents_mod.data

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asId
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

    //Fences
    val modernFenceInventory = item("modern_fence", TextureKey.SIDE, TextureKey.END, TextureKey.UP)
    val modernFencePost = block("modern_fence_post", "_post", TextureKey.END, TextureKey.UP)
    val modernFenceSide = block("modern_fence_side", "_side", TextureKey.SIDE)

    //Fence Gates
    val modernFenceGate = block("modern_fence_gate", TextureKey.ALL)
    val modernFenceGateOpen = block("modern_fence_gate_open", "_open", TextureKey.ALL)

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
    val coffeeTableInventory = item("coffee_table", TextureKey.TOP, legs)
    val coffeeTableTopTall = block("coffee_table_top_tall",
        "_top_tall", TextureKey.TOP)
    val coffeeTableLegTall = block(
        "coffee_table_leg_tall",
        "_leg_tall",
        legs
    )
    val coffeeTableTopShort =
        block("coffee_table_top_short", "_top_short", TextureKey.TOP)
    val coffeeTableLegShort = block(
        "coffee_table_leg_short",
        "_leg_short",
        legs
    )

    //Bookshelves
    val thinBookshelfItem = item("thin_bookshelf", TextureKey.ALL)
    val thinBookshelfBlock = block("thin_bookshelf", "_empty", TextureKey.ALL)
    val thinBookshelfSlot0 = "thin_bookshelf_slot_0".asBlockId()
    val thinBookshelfSlot1 = "thin_bookshelf_slot_1".asBlockId()
    val thinBookshelfSlot2 = "thin_bookshelf_slot_2".asBlockId()
    val thinBookshelfSlot3 = "thin_bookshelf_slot_3".asBlockId()
    val thinBookshelfSlot4 = "thin_bookshelf_slot_4".asBlockId()
    val thinBookshelfSlot5 = "thin_bookshelf_slot_5".asBlockId()


    /*---------------Kitchen Stuff----------------*/

    //Kitchen Counters
    val kitchenCounter = block("kitchen_counter", TextureKey.SIDE, TextureKey.TOP)
    val kitchenCounterInnerLeftCorner = block(
        "kitchen_counter_inner_left_corner",
        "_inner_left_corner",
        TextureKey.SIDE,
        TextureKey.TOP
    )
    val kitchenCounterOuterLeftCorner = block(
        "kitchen_counter_outer_left_corner",
        "_outer_left_corner",
        TextureKey.SIDE,
        TextureKey.TOP
    )

    //Kitchen Cabinet
    val kitchenCabinet = block("kitchen_cabinet", TextureKey.SIDE, TextureKey.TOP)

    /*---------------Bedroom Stuff----------------*/


    @Suppress("unused")
    private fun make(vararg requiredTextureKeys: TextureKey) = Model(Optional.empty(), Optional.empty(), *requiredTextureKeys)

    private fun block(parent: String, vararg requiredTextureKeys: TextureKey) = Model(
        Optional.of("block/$parent".asId()),
        Optional.empty(),
        *requiredTextureKeys
    )

    @Suppress("unused")
    private fun item(parent: String, vararg requiredTextureKeys: TextureKey) = Model(
        Optional.of("item/$parent".asId()),
        Optional.empty(),
        *requiredTextureKeys
    )

    private fun block(parent: String, variant: String, vararg requiredTextureKeys: TextureKey) = Model(
        Optional.of("block/$parent".asId()),
        Optional.of(variant),
        *requiredTextureKeys
    )
}