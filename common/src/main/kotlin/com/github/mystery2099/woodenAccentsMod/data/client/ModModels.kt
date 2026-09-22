package com.github.mystery2099.woodenAccentsMod.data.client

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.withBlockModelPath
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.TextureSlot
import java.util.*

object ModModels {

    // Texture keys
    val legs: TextureSlot = TextureSlot.create("legs")

    // Outdoor models
    val thinPillarInventory = item("thin_pillar", TextureSlot.ALL)
    val thinPillarBottom = block("thin_pillar_base", "_base", TextureSlot.ALL)

    val thickPillarInventory = item("thick_pillar", TextureSlot.ALL)
    val thickPillarBottom = block("thick_pillar_base", "_base", TextureSlot.ALL)

    val modernFenceInventory = item("modern_fence", TextureSlot.SIDE, TextureSlot.END, TextureSlot.UP)
    val modernFencePost = block("modern_fence_post", "_post", TextureSlot.END, TextureSlot.UP)
    val modernFenceSide = block("modern_fence_side", "_side", TextureSlot.SIDE)

    val modernFenceGate = block("modern_fence_gate", TextureSlot.ALL)
    val modernFenceGateOpen = block("modern_fence_gate_open", "_open", TextureSlot.ALL)
    val modernFenceGateWall = block("modern_fence_gate_wall", "_wall", TextureSlot.ALL)
    val modernFenceGateWallOpen = block("modern_fence_gate_wall_open", "_wall_open", TextureSlot.ALL)

    val plankLadder = block("plank_ladder", TextureSlot.ALL)
    val simpleLadder = block("simple_ladder", TextureSlot.ALL)
    val connectingLadder = block("connecting_ladder", TextureSlot.ALL)
    val connectingLadderLeft = block("connecting_ladder_left", "_left", TextureSlot.ALL)
    val connectingLadderRight = block("connecting_ladder_right", "_right", TextureSlot.ALL)
    val connectingLadderCenter = block("connecting_ladder_center", "_center", TextureSlot.ALL)

    val supportBeamItem = item("support_beam", TextureSlot.ALL)
    val supportBeamCenter = block("support_beam_center", "_center", TextureSlot.ALL)
    val supportBeamDown = block("support_beam_down", "_down", TextureSlot.ALL)

    val crate = block("wooden_crate", TextureSlot.EDGE, TextureSlot.INSIDE, TextureSlot.CROSS)

    // Furniture models
    val tableItem = item("table", TextureSlot.TOP, legs)
    val tableTop = block("table_top", "_top", TextureSlot.TOP)
    val tableCenterLeg = block("table_single_leg", "_single_leg", legs)
    val tableEndLeg = block("table_end_leg", "_end_leg", legs)
    val tableCornerLeg = block("table_corner_leg", "_corner_leg", legs)

    val coffeeTableInventory = item("coffee_table", "_short", TextureSlot.TOP, legs)
    val coffeeTableTallInventory = item("coffee_table_tall", "_tall", TextureSlot.TOP, legs)
    val coffeeTableTopTall = block("coffee_table_top_tall", "_top_tall", TextureSlot.TOP)
    val coffeeTableLegTall = block(
        "coffee_table_leg_tall",
        "_leg_tall",
        legs
    )
    val coffeeTableTopShort =
        block("coffee_table_top_short", "_top_short", TextureSlot.TOP)
    val coffeeTableLegShort = block(
        "coffee_table_leg_short",
        "_leg_short",
        legs
    )

    val basicChair = block("chair", TextureSlot.ALL)

    val bracketShelfItem = item("bracket_shelf", TextureSlot.ALL)
    val bracketShelfBase = block("bracket_shelf_base", "_base", TextureSlot.ALL)
    val bracketShelfLeft = block("bracket_shelf_left", "_left", TextureSlot.ALL)
    val bracketShelfRight = block("bracket_shelf_right", "_right", TextureSlot.ALL)

    val thinBookshelfItem = item("thin_bookshelf", TextureSlot.ALL)
    val thinBookshelfBlock = block("thin_bookshelf", "_empty", TextureSlot.ALL)
    val thinBookshelfSlot0 = "thin_bookshelf_slot_0".toIdentifier().withBlockModelPath()
    val thinBookshelfSlot1 = "thin_bookshelf_slot_1".toIdentifier().withBlockModelPath()
    val thinBookshelfSlot2 = "thin_bookshelf_slot_2".toIdentifier().withBlockModelPath()
    val thinBookshelfSlot3 = "thin_bookshelf_slot_3".toIdentifier().withBlockModelPath()
    val thinBookshelfSlot4 = "thin_bookshelf_slot_4".toIdentifier().withBlockModelPath()
    val thinBookshelfSlot5 = "thin_bookshelf_slot_5".toIdentifier().withBlockModelPath()

    // Kitchen models
    val kitchenCounter = block("kitchen_counter", TextureSlot.SIDE, TextureSlot.TOP)
    val kitchenCounterInnerLeftCorner = block(
        "kitchen_counter_inner_left_corner",
        "_inner_left_corner",
        TextureSlot.SIDE,
        TextureSlot.TOP
    )
    val kitchenCounterOuterLeftCorner = block(
        "kitchen_counter_outer_left_corner",
        "_outer_left_corner",
        TextureSlot.SIDE,
        TextureSlot.TOP
    )

    val kitchenCabinet = block("kitchen_cabinet", TextureSlot.SIDE, TextureSlot.TOP)

    // Bedroom models
    val desk = block("desk", TextureSlot.SIDE, TextureSlot.TOP)
    val deskLeft = block("desk_left", "_left", TextureSlot.SIDE, TextureSlot.TOP)
    val deskCenter = block("desk_center", "_center", TextureSlot.SIDE, TextureSlot.TOP)
    val deskRight = block("desk_right", "_right", TextureSlot.SIDE, TextureSlot.TOP)
    val deskLeftCorner = block("desk_left_corner", "_left_corner", TextureSlot.SIDE, TextureSlot.TOP)

    val deskDrawer = block("desk_drawer", TextureSlot.SIDE, TextureSlot.EDGE)
    val deskDrawerLeft = block("desk_drawer_left", "_left", TextureSlot.SIDE, TextureSlot.EDGE)
    val deskDrawerCenter = block("desk_drawer_center", "_center", TextureSlot.SIDE, TextureSlot.EDGE)
    val deskDrawerRight = block("desk_drawer_right", "_right", TextureSlot.SIDE, TextureSlot.EDGE)

    @Suppress("unused")
    private fun make(vararg requiredTextureSlots: TextureSlot) =
        ModelTemplate(Optional.empty(), Optional.empty(), *requiredTextureSlots)

    private fun block(parent: String, vararg requiredTextureSlots: TextureSlot) = ModelTemplate(
        Optional.of(parent.toIdentifier().withBlockModelPath()),
        Optional.empty(),
        *requiredTextureSlots
    )

    @Suppress("unused")
    private fun item(parent: String, vararg requiredTextureSlots: TextureSlot) = ModelTemplate(
        Optional.of(parent.toIdentifier().withPrefix("item/")),
        Optional.empty(),
        *requiredTextureSlots
    )

    private fun block(parent: String, variant: String, vararg requiredTextureSlots: TextureSlot) = ModelTemplate(
        Optional.of(parent.toIdentifier().withBlockModelPath()),
        Optional.of(variant),
        *requiredTextureSlots
    )

    @Suppress("unused")
    private fun item(parent: String, variant: String, vararg requiredTextureSlots: TextureSlot) = ModelTemplate(
        Optional.of(parent.toIdentifier().withPrefix("item/")),
        Optional.of(variant),
        *requiredTextureSlots
    )

}
