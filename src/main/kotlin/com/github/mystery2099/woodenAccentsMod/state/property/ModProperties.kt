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
    /**
     * Represents a boolean property indicating whether something is on the left side.
     */
    val left: BooleanProperty = BooleanProperty.of("left")
    /**
     * Represents a boolean property indicating whether something is on the right side.
     */
    val right: BooleanProperty = BooleanProperty.of("right")

    /**
     * Represents the type of a coffee table.
     */
    @JvmStatic
    val coffeeTableType: EnumProperty<CoffeeTableTypes> = EnumProperty.of("type", CoffeeTableTypes::class.java)

    /**
     * The `deskShape` variable is a property that represents the shape of a desk.
     * It is of type `EnumProperty<DeskShape>` and is used to store and access the shape of a desk.
     *
     * @see DeskShape
     */
    val deskShape: EnumProperty<DeskShape> = EnumProperty.of("shape", DeskShape::class.java)
    /**
     * Property representing the sideways connection shape of a block.
     */
    val sidewaysConnectionShape: EnumProperty<SidewaysConnectionShape> = EnumProperty.of("shape", SidewaysConnectionShape::class.java)

}