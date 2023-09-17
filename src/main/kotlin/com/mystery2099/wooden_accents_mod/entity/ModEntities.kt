package com.mystery2099.wooden_accents_mod.entity

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.WoodenAccentsModRegistry
import com.mystery2099.wooden_accents_mod.entity.custom.SeatEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModEntities : WoodenAccentsModRegistry {
    val seatEntity: EntityType<SeatEntity> =
        Registry.register(Registries.ENTITY_TYPE, "seat".toIdentifier(), FabricEntityTypeBuilder.create<SeatEntity>().build())

    override fun register() {
        super.register()
    }
}