package com.github.mystery2099.woodenAccentsMod.state.property

import com.github.mystery2099.woodenAccentsMod.block.custom.enums.CoffeeTableTypes
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.DeskShape
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.SidewaysConnectionShape
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty

/**
 * ModProperties is an object that provides custom property definitions for various block states in the mod.
 */
object ModProperties {
    val left: BooleanProperty = BooleanProperty.of("left")
    val right: BooleanProperty = BooleanProperty.of("right")

    @JvmStatic
    val coffeeTableType: EnumProperty<CoffeeTableTypes> = EnumProperty.of("type", CoffeeTableTypes::class.java)
    val deskShape: EnumProperty<DeskShape> = EnumProperty.of("shape", DeskShape::class.java)
    val sidewaysConnectionShape: EnumProperty<SidewaysConnectionShape> = EnumProperty.of("shape", SidewaysConnectionShape::class.java)

}