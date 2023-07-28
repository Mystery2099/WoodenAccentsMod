package com.mystery2099.block_entity

import com.mystery2099.WoodenAccentsMod
import com.mystery2099.WoodenAccentsMod.toId
import com.mystery2099.block.custom.KitchenCabinetBlock
import com.mystery2099.block_entity.custom.KitchenCabinetBlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModBlockEntities {

    lateinit var kitchenCabinet: BlockEntityType<KitchenCabinetBlockEntity>

    fun register() {
        kitchenCabinet = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            "kitchen_cabinet".toId(),
            KitchenCabinetBlock.kitchenCabinetBlockEntityTypeBuilder.build(null)
        )

        WoodenAccentsMod.logger.info("Registering ModBlockEntities for mod: ${WoodenAccentsMod.MOD_ID}")
    }
}