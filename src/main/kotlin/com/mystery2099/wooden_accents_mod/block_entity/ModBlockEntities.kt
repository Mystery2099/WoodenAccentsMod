package com.mystery2099.wooden_accents_mod.block_entity

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.WoodenAccentsModRegistry
import com.mystery2099.wooden_accents_mod.block.custom.CrateBlock
import com.mystery2099.wooden_accents_mod.block.custom.KitchenCabinetBlock
import com.mystery2099.wooden_accents_mod.block_entity.custom.CrateBlockEntity
import com.mystery2099.wooden_accents_mod.block_entity.custom.KitchenCabinetBlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModBlockEntities : WoodenAccentsModRegistry {

    lateinit var kitchenCabinet: BlockEntityType<KitchenCabinetBlockEntity>
    lateinit var crate: BlockEntityType<CrateBlockEntity>

    override fun register() {
        kitchenCabinet = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            "kitchen_cabinet".toIdentifier(),
            KitchenCabinetBlock.kitchenCabinetBlockEntityTypeBuilder.build(null)
        )
        crate = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            "crate".toIdentifier(),
            CrateBlock.crateBlockEntityBuilder.build(null)
        )
        super.register()
    }
}