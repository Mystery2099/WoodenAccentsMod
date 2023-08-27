package com.mystery2099.wooden_accents_mod.data

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.modId
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
    val modernFenceGateWall = block("modern_fence_gate_wall", "_wall", TextureKey.ALL)
    val modernFenceGateWallOpen = block("modern_fence_gate_wall_open", "_wall_open", TextureKey.ALL)

    //Ladders
    val plankLadder = block("plank_ladder", TextureKey.ALL)
    val connectingLadder = block("connecting_ladder", TextureKey.ALL)
    val connectingLadderLeft = block("connecting_ladder_left", "_left", TextureKey.ALL)
    val connectingLadderRight = block("connecting_ladder_right", "_right", TextureKey.ALL)
    val connectingLadderCenter = block("connecting_ladder_center", "_center", TextureKey.ALL)

    //Support Beam
    val supportBeamItem = item("support_beam", TextureKey.ALL)
    val supportBeamCenter = block("support_beam_center", "_center", TextureKey.ALL)
    val supportBeamDown = block("support_beam_down", "_down", TextureKey.ALL)

    val crate = block("wooden_crate", TextureKey.EDGE, TextureKey.INSIDE, TextureKey.CROSS)
    /*---------------Living Room Stuff----------------*/
    //Table Models
    val tableItem = item("table", TextureKey.TOP, legs)
    val tableTop = block("table_top", "_top", TextureKey.TOP)
    val tableCenterLeg = block("table_single_leg", "_single_leg", legs)
    val tableEndLeg = block("table_end_leg", "_end_leg", legs)
    val tableCornerLeg = block("table_corner_leg", "_corner_leg", legs)

    //Coffee Table Models
    val coffeeTableInventory = item("coffee_table", "_short", TextureKey.TOP, legs)
    val coffeeTableTallInventory = item("coffee_table_tall", "_tall", TextureKey.TOP, legs)
    val coffeeTableTopTall = block("coffee_table_top_tall", "_top_tall", TextureKey.TOP)
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
    val thinBookshelfSlot0 = modId("thin_bookshelf_slot_0").asBlockModelId()
    val thinBookshelfSlot1 = modId("thin_bookshelf_slot_1").asBlockModelId()
    val thinBookshelfSlot2 = modId("thin_bookshelf_slot_2").asBlockModelId()
    val thinBookshelfSlot3 = modId("thin_bookshelf_slot_3").asBlockModelId()
    val thinBookshelfSlot4 = modId("thin_bookshelf_slot_4").asBlockModelId()
    val thinBookshelfSlot5 = modId("thin_bookshelf_slot_5").asBlockModelId()


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
        Optional.of(modId(parent).asBlockModelId()),
        Optional.empty(),
        *requiredTextureKeys
    )

    @Suppress("unused")
    private fun item(parent: String, vararg requiredTextureKeys: TextureKey) = Model(
        Optional.of(modId(parent).withPrefixedPath("item/")),
        Optional.empty(),
        *requiredTextureKeys
    )

    private fun block(parent: String, variant: String, vararg requiredTextureKeys: TextureKey) = Model(
        Optional.of(modId(parent).asBlockModelId()),
        Optional.of(variant),
        *requiredTextureKeys
    )
    @Suppress("unused")
    private fun item(parent: String, variant: String, vararg requiredTextureKeys: TextureKey) = Model(
        Optional.of(modId(parent).withPrefixedPath("item/")),
        Optional.of(variant),
        *requiredTextureKeys
    )

}