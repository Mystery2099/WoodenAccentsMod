package com.github.mystery2099.woodenAccentsMod.entity

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsModRegistry
import com.github.mystery2099.woodenAccentsMod.entity.custom.SeatEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

/**
 * [ModEntities] is responsible for registering custom [EntityType]s in the Wooden Accents Mod.
 */
object ModEntities : WoodenAccentsModRegistry {
    val seatEntity: EntityType<SeatEntity> = FabricEntityTypeBuilder.create(SpawnGroup.MISC) { type, world ->
        SeatEntity(
            type,
            world
        )
    }.dimensions(EntityDimensions.fixed(0.01f, 0.01f))
    .trackRangeBlocks(10)
    .trackedUpdateRate(20)
    .build()

    override fun register() {
        Registry.register(Registries.ENTITY_TYPE, "seat".toIdentifier(), seatEntity)
        super.register()
    }
}