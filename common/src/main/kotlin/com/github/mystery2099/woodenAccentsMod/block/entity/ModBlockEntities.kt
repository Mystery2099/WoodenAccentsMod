package com.github.mystery2099.woodenAccentsMod.block.entity

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.WoodenAccentsModRegistry
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.custom.BracketShelfBlock
import com.github.mystery2099.woodenAccentsMod.block.custom.CrateBlock
import com.github.mystery2099.woodenAccentsMod.block.custom.DeskDrawerBlock
import com.github.mystery2099.woodenAccentsMod.block.custom.KitchenCabinetBlock
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.BracketShelfBlockEntity
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.CrateBlockEntity
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.DeskDrawerBlockEntity
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.KitchenCabinetBlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.Registry

@Suppress("TYPE_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object ModBlockEntities : WoodenAccentsModRegistry {

    lateinit var kitchenCabinet: BlockEntityType<KitchenCabinetBlockEntity>
    lateinit var crate: BlockEntityType<CrateBlockEntity>
    lateinit var deskDrawer: BlockEntityType<DeskDrawerBlockEntity>
    lateinit var bracketShelf: BlockEntityType<BracketShelfBlockEntity>

    override fun register() {
        val kitchenCabinets = ModBlocks.blocks.filterIsInstance<KitchenCabinetBlock>().toTypedArray()
        kitchenCabinet = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            "kitchen_cabinet".toIdentifier(),
            BlockEntityType.Builder.of(::KitchenCabinetBlockEntity, *kitchenCabinets).build(null)
        )
        val crateBlocks = ModBlocks.blocks.filterIsInstance<CrateBlock>().toTypedArray()
        crate = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            "crate".toIdentifier(),
            BlockEntityType.Builder.of(::CrateBlockEntity, *crateBlocks).build(null)
        )
        val deskDrawerBlocks = ModBlocks.blocks.filterIsInstance<DeskDrawerBlock>().toTypedArray()
        deskDrawer = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            "desk_drawer".toIdentifier(),
            BlockEntityType.Builder.of(::DeskDrawerBlockEntity, *deskDrawerBlocks).build(null)
        )
        val bracketShelfBlocks = ModBlocks.blocks.filterIsInstance<BracketShelfBlock>().toTypedArray()
        bracketShelf = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            "bracket_shelf".toIdentifier(),
            BlockEntityType.Builder.of(::BracketShelfBlockEntity, *bracketShelfBlocks).build(null)
        )

        super.register()
    }
}
