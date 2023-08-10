package com.mystery2099.wooden_accents_mod.block_entity

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asWamId
import com.mystery2099.wooden_accents_mod.block.custom.CrateBlock
import com.mystery2099.wooden_accents_mod.block.custom.KitchenCabinetBlock
import com.mystery2099.wooden_accents_mod.block_entity.custom.CrateBlockEntity
import com.mystery2099.wooden_accents_mod.block_entity.custom.KitchenCabinetBlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModBlockEntities {

    lateinit var kitchenCabinet: BlockEntityType<KitchenCabinetBlockEntity>
    lateinit var crate: BlockEntityType<CrateBlockEntity>

    fun register() {
        kitchenCabinet = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            "kitchen_cabinet".asWamId(),
            KitchenCabinetBlock.kitchenCabinetBlockEntityTypeBuilder.build(null)
        )
        crate = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            "crate".asWamId(),
            CrateBlock.crateBlockEntityBuilder.build(null)
        )

        WoodenAccentsMod.logger.info("Registering $this for mod: ${WoodenAccentsMod.MOD_ID}")
    }
}