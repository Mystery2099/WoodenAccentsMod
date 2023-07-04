package com.mystery2099.data

import com.mystery2099.WoodenAccentsMod.toId
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import java.util.*

object ModModels {
    /*---------------Texture Keys----------------*/
    val legs = TextureKey.of("legs")


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

    /*---------------Living Room Stuff----------------*/
    //Coffee Table Models
    val COFFEE_TABLE: Model = block("coffee_table", TextureKey.TOP, legs)
    val TALL_COFFEE_TABLE_TOP: Model = block("coffee_table_top_tall",
        "_top_tall", TextureKey.TOP)
    val TALL_COFFEE_TABLE_NORTH_EAST_LEG: Model = block(
        "coffee_table_north_east_leg_tall",
        "_north_east_leg_tall",
        legs
    )
    val TALL_COFFEE_TABLE_NORTH_WEST_LEG: Model = block(
        "coffee_table_north_west_leg_tall",
        "_north_west_leg_tall",
        legs
    )
    val TALL_COFFEE_TABLE_SOUTH_EAST_LEG: Model = block(
        "coffee_table_south_east_leg_tall",
        "_south_east_leg_tall",
        legs
    )
    val TALL_COFFEE_TABLE_SOUTH_WEST_LEG: Model = block(
        "coffee_table_south_west_leg_tall",
        "_south_west_leg_tall",
        legs
    )
    val SHORT_COFFEE_TABLE_TOP: Model =
        block("coffee_table_top_short", "_top_short", TextureKey.TOP)
    val SHORT_COFFEE_TABLE_NORTH_EAST_LEG: Model = block(
        "coffee_table_north_east_leg_short",
        "_north_east_leg_short",
        legs
    )
    val SHORT_COFFEE_TABLE_NORTH_WEST_LEG: Model = block(
        "coffee_table_north_west_leg_short",
        "_north_west_leg_short",
        legs
    )
    val SHORT_COFFEE_TABLE_SOUTH_EAST_LEG: Model = block(
        "coffee_table_south_east_leg_short",
        "_south_east_leg_short",
        legs
    )
    val SHORT_COFFEE_TABLE_SOUTH_WEST_LEG: Model = block(
        "coffee_table_south_west_leg_short",
        "_south_west_leg_short",
        legs
    )
    /*---------------Storage Stuff----------------*/

    /*---------------Kitchen Stuff----------------*/

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