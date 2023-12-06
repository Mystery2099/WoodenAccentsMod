package com.github.mystery2099.woodenAccentsMod.block.entity

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsModRegistry
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.custom.CrateBlock
import com.github.mystery2099.woodenAccentsMod.block.custom.DeskDrawerBlock
import com.github.mystery2099.woodenAccentsMod.block.custom.KitchenCabinetBlock
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.CrateBlockEntity
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.DeskDrawerBlockEntity
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.KitchenCabinetBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

/**
 * The ModBlockEntities class is responsible for registering and managing block entities for the mod.
 */
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