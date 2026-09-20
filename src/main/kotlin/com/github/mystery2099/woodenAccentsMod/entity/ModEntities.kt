package com.github.mystery2099.woodenAccentsMod.entity

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsModRegistry
import com.github.mystery2099.woodenAccentsMod.entity.custom.SeatEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.Registry

object ModEntities : WoodenAccentsModRegistry {
    val seatEntity: EntityType<SeatEntity> = FabricEntityTypeBuilder.create(MobCategory.MISC) { type, world ->
        SeatEntity(
            type,
            world
        )
    }.dimensions(EntityDimensions.fixed(0.01f, 0.01f))
    .trackRangeBlocks(10)
    .trackedUpdateRate(20)
    .build()

    override fun register() {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, "seat".toIdentifier(), seatEntity)
        super.register()
    }
}
