package com.mystery2099.wooden_accents_mod.state.property

import com.mystery2099.wooden_accents_mod.block.custom.enums.CoffeeTableType
import com.mystery2099.wooden_accents_mod.block.custom.enums.ConnectingLadderShape
import com.mystery2099.wooden_accents_mod.block.custom.enums.DeskShape
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty

object ModProperties {
    val coffeeTableType: EnumProperty<CoffeeTableType> = EnumProperty.of("type", CoffeeTableType::class.java)
    val connectingLadderShape: EnumProperty<ConnectingLadderShape> =
        EnumProperty.of("shape", ConnectingLadderShape::class.java)
    val left: BooleanProperty = BooleanProperty.of("left")
    val right: BooleanProperty = BooleanProperty.of("right")

    @JvmStatic
    val deskShape: EnumProperty<DeskShape> = EnumProperty.of("shape", DeskShape::class.java)
}