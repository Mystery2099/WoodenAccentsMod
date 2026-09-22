package com.github.mystery2099.woodenAccentsMod.registry.component

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsModRegistry
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.CoffeeTableTypes
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.StringRepresentable

object ModDataComponents : WoodenAccentsModRegistry {
    /** Marks the coffee table item variant; persists as the same string the old NBT tag used. */
    val coffeeTableType: DataComponentType<CoffeeTableTypes> = DataComponentType.builder<CoffeeTableTypes>()
        .persistent(StringRepresentable.fromEnum { CoffeeTableTypes.values() })
        .networkSynchronized(
            ByteBufCodecs.idMapper({ CoffeeTableTypes.values()[it] }, CoffeeTableTypes::ordinal)
        )
        .build()

    override fun register() {
        Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            "coffee_table_type".toIdentifier(),
            coffeeTableType
        )
        super.register()
    }
}
