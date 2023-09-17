package com.mystery2099.wooden_accents_mod.block.entity

import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.toIdentifier
import com.mystery2099.wooden_accents_mod.WoodenAccentsModRegistry
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.custom.CrateBlock
import com.mystery2099.wooden_accents_mod.block.custom.DeskDrawerBlock
import com.mystery2099.wooden_accents_mod.block.custom.KitchenCabinetBlock
import com.mystery2099.wooden_accents_mod.block.entity.custom.CrateBlockEntity
import com.mystery2099.wooden_accents_mod.block.entity.custom.DeskDrawerBlockEntity
import com.mystery2099.wooden_accents_mod.block.entity.custom.KitchenCabinetBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModBlockEntities : WoodenAccentsModRegistry {

    lateinit var kitchenCabinet: BlockEntityType<KitchenCabinetBlockEntity>
    lateinit var crate: BlockEntityType<CrateBlockEntity>
    lateinit var deskDrawer: BlockEntityType<DeskDrawerBlockEntity>

    override fun register() {
        kitchenCabinet = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            "kitchen_cabinet".toIdentifier(),
            FabricBlockEntityTypeBuilder.create(::KitchenCabinetBlockEntity).run {
                val kitchenCabinets = ModBlocks.blocks.filterIsInstance<KitchenCabinetBlock>().toTypedArray()
                this.addBlocks(*kitchenCabinets)
            }.build()
        )
        crate = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            "crate".toIdentifier(),
            FabricBlockEntityTypeBuilder.create(::CrateBlockEntity).run {
                val crateBlocks = ModBlocks.blocks.filterIsInstance<CrateBlock>().toTypedArray()
                this.addBlocks(*crateBlocks)
            }.build()
        )
        deskDrawer = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            "desk_drawer".toIdentifier(),
            FabricBlockEntityTypeBuilder.create(::DeskDrawerBlockEntity).run {
                val crateBlocks = ModBlocks.blocks.filterIsInstance<DeskDrawerBlock>().toTypedArray()
                this.addBlocks(*crateBlocks)
            }.build()
        )

        super.register()
    }
}