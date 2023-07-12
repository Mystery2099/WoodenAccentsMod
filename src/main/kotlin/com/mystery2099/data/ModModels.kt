package com.mystery2099.data

import com.mystery2099.WoodenAccentsMod.toId
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import java.util.*

object ModModels {
    /*---------------Texture Keys----------------*/
    val legs: TextureKey = TextureKey.of("legs")


    /*---------------Outside Stuff----------------*/

    //Thin Pillar
    val thinPillarInventory = block("thin_pillar", "_inventory", TextureKey.ALL)
    val thinPillarTop = block("thin_pillar_top", "_top", TextureKey.ALL)
    val thinPillarBottom = block("thin_pillar_base", "_base", TextureKey.ALL)
    val thinPillarCenter = block("thin_pillar_center", "_center", TextureKey.ALL)
    //Thick Pillar
    val thickPillarInventory = block("thick_pillar", "_inventory", TextureKey.ALL)
    val thickPillarTop = block("thick_pillar_top", "_top", TextureKey.ALL)
    val thickPillarBottom = block("thick_pillar_base", "_base", TextureKey.ALL)
    val thickPillarCenter = block("thick_pillar_center", "_center", TextureKey.ALL)

    //Plank Ladder
    val plankLadder = block("plank_ladder", TextureKey.ALL)
    /*---------------Living Room Stuff----------------*/
    //Table Models
    val TABLE_INVENTORY = block("table", "_inventory", TextureKey.TOP, legs)
    val TABLE_TOP = block("table_top", "_top", TextureKey.TOP)
    val TABLE_SINGLE_LEG = block("table_single_leg", "_single_leg", legs)
    val TABLE_END_LEG = block("table_end_leg", "_end_leg", legs)
    val TABLE_CORNER_LEG = block("table_corner_leg", "_corner_leg", legs)

    //Coffee Table Models
    val coffeeTableInventory: Model = block("coffee_table", "_inventory", TextureKey.TOP, legs)
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
    /*---------------Storage Stuff----------------*/

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