package com.mystery2099.wooden_accents_mod.state.property

import com.mystery2099.wooden_accents_mod.block.custom.enums.CoffeeTableType
import com.mystery2099.wooden_accents_mod.block.custom.enums.DeskShape
import com.mystery2099.wooden_accents_mod.block.custom.enums.SidewaysConnectionShape
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty

object ModProperties {
    val left: BooleanProperty = BooleanProperty.of("left")
    val right: BooleanProperty = BooleanProperty.of("right")

    @JvmStatic
    val coffeeTableType: EnumProperty<CoffeeTableType> = EnumProperty.of("type", CoffeeTableType::class.java)
    val deskShape: EnumProperty<DeskShape> = EnumProperty.of("shape", DeskShape::class.java)
    val sidewaysConnectionShape: EnumProperty<SidewaysConnectionShape> = EnumProperty.of("shape", SidewaysConnectionShape::class.java)

}