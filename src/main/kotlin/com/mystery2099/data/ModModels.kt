package com.mystery2099.data

import com.mystery2099.WoodenAccentsMod.toId
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import java.util.*

object ModModels {
    /*---------------Outside Stuff----------------*/

    //Thin Pillar
    val thinPillarInventory = block("thin_pillar", "_inventory", TextureKey.ALL)
    val thinPillarTop = block("thin_pillar_top", "_top", TextureKey.ALL)
    val thinPillarBottom = block("thin_pillar_bottom", "_base", TextureKey.ALL)
    val thinPillarCenter = block("thin_pillar_center", "_center", TextureKey.ALL)
    //Thick Pillar
    val thickPillarInventory = block("thick_pillar", "_inventory", TextureKey.ALL)
    val thickPillarTop = block("thick_pillar_top", "_top", TextureKey.ALL)
    val thickPillarBottom = block("thick_pillar_bottom", "_base", TextureKey.ALL)
    val thickPillarCenter = block("thick_pillar_center", "_center", TextureKey.ALL)

    /*---------------Living Room Stuff----------------*/

    /*---------------Storage Stuff----------------*/

    /*---------------Kitchen Stuff----------------*/

    /*---------------Bedroom Stuff----------------*/


    @Suppress("unused")
    private fun make(vararg requiredTextureKeys: TextureKey): Model {
        return Model(Optional.empty(), Optional.empty(), *requiredTextureKeys)
    }

    private fun block(parent: String, vararg requiredTextureKeys: TextureKey): Model? {
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