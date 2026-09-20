package com.github.mystery2099.woodenAccentsMod.state.property

import com.github.mystery2099.woodenAccentsMod.block.custom.enums.CoffeeTableTypes
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.DeskShape
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.SidewaysConnectionShape
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty

object ModProperties {
    val left: BooleanProperty = BooleanProperty.create("left")
    val right: BooleanProperty = BooleanProperty.create("right")

    @JvmStatic
    val coffeeTableType: EnumProperty<CoffeeTableTypes> = EnumProperty.create("type", CoffeeTableTypes::class.java)

    val deskShape: EnumProperty<DeskShape> = EnumProperty.create("shape", DeskShape::class.java)
    val sidewaysConnectionShape: EnumProperty<SidewaysConnectionShape> = EnumProperty.create("shape", SidewaysConnectionShape::class.java)

}
