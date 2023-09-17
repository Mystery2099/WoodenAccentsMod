package com.mystery2099.wooden_accents_mod.entity

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.WoodenAccentsModRegistry
import com.mystery2099.wooden_accents_mod.entity.custom.SeatEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry


object ModEntities : WoodenAccentsModRegistry {
    lateinit var seatEntity: EntityType<SeatEntity>

    override fun register() {
        seatEntity = Registry.register(
            Registries.ENTITY_TYPE, "seat".toIdentifier(), FabricEntityTypeBuilder.create(
                SpawnGroup.MISC
            ) { _, world -> SeatEntity(world) }.build()
        )

        super.register()
    }
}