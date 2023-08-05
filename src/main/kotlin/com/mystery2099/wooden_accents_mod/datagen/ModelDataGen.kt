package com.mystery2099.wooden_accents_mod.datagen

import com.mystery2099.block.custom.KitchenCounterBlock
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asPlanks
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asVanillaId
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.ModBlocks.getItemModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.woodType
import com.mystery2099.wooden_accents_mod.block.custom.*
import com.mystery2099.wooden_accents_mod.block.custom.enums.CoffeeTableType
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.state.property.ModProperties
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.putModel
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.uvLock
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withXRotationOf
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import com.mystery2099.wooden_accents_mod.util.WhenUtil
import com.mystery2099.wooden_accents_mod.util.WhenUtil.and
import com.mystery2099.wooden_accents_mod.util.WhenUtil.newWhen
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.ChiseledBookshelfBlock
import net.minecraft.block.WoodType
import net.minecraft.block.enums.StairShape
import net.minecraft.data.client.*
import net.minecraft.data.client.VariantSettings.Rotation
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction

class ModelDataGen(output: FabricDataOutput) : FabricModelProvider(output) {

    val block = "block/"

    //Collections
    private val horizontalDirections = arrayOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)

    private lateinit var generator: BlockStateModelGenerator

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        blockStateModelGenerator.run {
            generator = this
            WoodType.stream().forEach{
                ModModels.coffeeTableLegShort.upload("${it.name.lowercase()}_coffee_table_leg_short".asBlockId(), TextureMap().put(
                    ModModels.legs, TextureMap.getId(it.asPlanks())), modelCollector)
                ModModels.coffeeTableLegTall.upload("${it.name.lowercase()}_coffee_table_leg_tall".asBlockId(), TextureMap().put(
                    ModModels.legs, TextureMap.getId(it.asPlanks())), modelCollector)

                ModModels.tableCenterLeg.upload("${it.name.lowercase()}_table_single_leg".asBlockId(), TextureMap().put(
                    ModModels.legs, TextureMap.getId(it.asPlanks())), modelCollector)
                ModModels.tableCornerLeg.upload("${it.name.lowercase()}_table_corner_leg".asBlockId(), TextureMap().put(
                    ModModels.legs, TextureMap.getId(it.asPlanks())), modelCollector)
                ModModels.tableEndLeg.upload("${it.name.lowercase()}_table_end_leg".asBlockId(), TextureMap().put(
                    ModModels.legs, TextureMap.getId(it.asPlanks())), modelCollector)
            }
            ModBlocks.blocks.forEach(::genBlockStateModel)
        }

    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }

    private fun genBlockStateModel(block: Block) {
        when (block) {
            //Outside
            is ThinPillarBlock -> genThinPillarBlockStateModels(block)
            is ThickPillarBlock -> genThickPillarBlockStateModels(block)
            is CustomWallBlock -> genWallBlockStateModels(block)
            is ModernFenceBlock -> genModernFenceBlockStateModels(block)
            is ModernFenceGateBlock -> genModernFenceGateBlockStateModels(block)
            is PlankLadderBlock -> genPlankLadderBlockStateModels(block)
            //Living Room
            is TableBlock -> genTableBlockStateModels(block)
            is CoffeeTableBlock -> genCoffeeTableBlockStateModels(block)
            is ThinBookshelfBlock -> genThinBookshelfBlockStateModels(block)
            is FloorCoveringBlock -> genFloorCoveringBlockStateModels(block)
            //Kitchen
            is KitchenCounterBlock -> genKitchenCounterBlockStateModels(block)
            is KitchenCabinetBlock -> genKitchenCabinetBlockStateModels(block)
        }
    }

    /*------------ Pillars -----------*/
    private fun genPillarBlockStateModels(
        block: Block,
        centerModel: Identifier,
        bottomModel: Identifier
    ) {
        generator.run {
            MultipartBlockStateSupplier.create(block).apply {
                with(centerModel.asBlockStateVariant())
                with(WhenUtil.notUp, bottomModel.asBlockStateVariant().withXRotationOf(Rotation.R180).uvLock())
                with(WhenUtil.notDown, bottomModel.asBlockStateVariant())
                blockStateCollector.accept(this)
            }
        }

    }
    private fun genThinPillarBlockStateModels(pillarBlock: ThinPillarBlock) {
        with(generator) {
            TextureMap.all(pillarBlock.baseBlock).run {
                ModModels.thinPillarInventory.upload(pillarBlock.getItemModelId(), this, modelCollector)
                genPillarBlockStateModels(
                    block = pillarBlock,
                    centerModel = "block/${pillarBlock.woodType.name.lowercase()}_fence_post".asVanillaId(),
                    bottomModel = ModModels.thinPillarBottom.upload(pillarBlock, this, modelCollector)
                )
            }
        }
    }

    private fun genThickPillarBlockStateModels(pillarBlock: ThickPillarBlock) {
        with(generator) {
            TextureMap.all(pillarBlock.baseBlock).run {
                ModModels.thickPillarInventory.upload(pillarBlock.getItemModelId(), this, modelCollector)
                genPillarBlockStateModels(
                    block = pillarBlock,
                    centerModel = "block/${pillarBlock.woodType.name.lowercase()}_wall_post".asId(),
                    bottomModel = ModModels.thickPillarBottom.upload(pillarBlock, this, modelCollector)
                )
            }
        }

    }

    /*------------ End Pillars -----------*/

    /*------------ Walls -----------*/
    private fun genWallBlockStateModels(block: CustomWallBlock) {
        with(generator) {
            TextureMap().put(TextureKey.WALL, TextureMap.getId(block.baseBlock)).run {
                blockStateCollector.accept(
                    BlockStateModelGenerator.createWallBlockState(
                        block,
                        Models.TEMPLATE_WALL_POST.upload(block, this, modelCollector),
                        Models.TEMPLATE_WALL_SIDE.upload(block, this, modelCollector),
                        Models.TEMPLATE_WALL_SIDE_TALL.upload(block, this, modelCollector)
                    )
                )
                registerParentedItemModel(block, Models.WALL_INVENTORY.upload(block, this, modelCollector))
            }
        }
    }
    /*------------ End Walls -----------*/
    /*------------ Custom Fences -----------*/
    private fun genModernFenceBlockStateModels(block: ModernFenceBlock) {
        with(generator) {
            TextureMap().apply {
                put(TextureKey.SIDE, TextureMap.getId(block.sideBlock))
                put(TextureKey.END, TextureMap.getId(block.postBlock))
                put(TextureKey.UP, TextureMap.getSubId(block.postBlock, "_top"))
            }.run {
                ModModels.modernFenceInventory.upload(block.getItemModelId(), this, modelCollector)
                blockStateCollector.accept(
                    BlockStateModelGenerator.createFenceBlockState(
                        block,
                        ModModels.modernFencePost.upload(block, this, modelCollector),
                        ModModels.modernFenceSide.upload(block, this, modelCollector)
                    )
                )
            }
        }

    }

    private fun genModernFenceGateBlockStateModels(block: ModernFenceGateBlock) {
        with(generator) {
            TextureMap.all(block.baseBlock).run {
                val model = ModModels.modernFenceGate.upload(block, this, modelCollector)
                val modelOpen = ModModels.modernFenceGateOpen.upload(block, this, modelCollector)

                blockStateCollector.accept(
                    BlockStateModelGenerator.createFenceGateBlockState(
                        block,
                        modelOpen,
                        model,
                        modelOpen,
                        model,
                        false
                    )
                )
            }
        }
    }
    /*------------ End Custom Fences -----------*/

    /*------------ Ladders -----------*/
    private fun genPlankLadderBlockStateModels(block: PlankLadderBlock) {
        with(generator) {
            TextureMap.all(block.baseBlock).run {
                ModModels.plankLadder.upload(block, this, modelCollector)
                this@with.registerNorthDefaultHorizontalRotation(block)
            }
        }
    }
    /*------------ End Ladders -----------*/

    /*------------ Tables -----------*/

    private fun genTableBlockStateModels(block: TableBlock) {
        with(generator) {
            TextureMap().apply {
                put(TextureKey.TOP, TextureMap.getId(block.topBlock))
                put(ModModels.legs, TextureMap.getId(block.baseBlock))
            }.run {
                blockStateCollector.accept(
                    tableSupplier(
                        block,
                        ModModels.tableTop.upload(block, this, modelCollector),
                        "${block.woodType.name.lowercase()}_table_single_leg".asBlockId(),
                        "${block.woodType.name.lowercase()}_table_end_leg".asBlockId(),
                        "${block.woodType.name.lowercase()}_table_corner_leg".asBlockId(),
                    )
                )
                ModModels.tableItem.upload(block.getItemModelId(), this, modelCollector)
            }
        }
    }

    private fun tableSupplier(
        block: TableBlock,
        topModel: Identifier,
        singleLegModel: Identifier,
        endLegModel: Identifier,
        cornerLegModel: Identifier,
    ): MultipartBlockStateSupplier = MultipartBlockStateSupplier.create(block).apply {
        val northEndLegVariant = endLegModel.asBlockStateVariant()
        val northEastCornerVariant = cornerLegModel.asBlockStateVariant()

        with(topModel.asBlockStateVariant())
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.notEast, WhenUtil.notSouth, WhenUtil.notWest),
            singleLegModel.asBlockStateVariant()
        )
        //Ends
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.notEast, WhenUtil.south, WhenUtil.notWest),
            northEndLegVariant
        )
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.notEast, WhenUtil.notSouth, WhenUtil.west),
            northEndLegVariant.withYRotationOf(Rotation.R90)
        )
        with(
            When.allOf(WhenUtil.north, WhenUtil.notEast, WhenUtil.notSouth, WhenUtil.notWest),
            northEndLegVariant.withYRotationOf(Rotation.R180)
        )
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.east, WhenUtil.notSouth, WhenUtil.notWest),
            northEndLegVariant.withYRotationOf(Rotation.R270)
        )
        //Corners
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.notEast, WhenUtil.south, WhenUtil.west),
            northEastCornerVariant
        )
        with(
            When.allOf(WhenUtil.notNorth, WhenUtil.east, WhenUtil.south, WhenUtil.notWest),
            northEastCornerVariant.withYRotationOf(Rotation.R270)
        )
        with(
            When.allOf(WhenUtil.north, WhenUtil.notEast, WhenUtil.notSouth, WhenUtil.west),
            northEastCornerVariant.withYRotationOf(Rotation.R90)
        )
        with(
            When.allOf(WhenUtil.north, WhenUtil.east, WhenUtil.notSouth, WhenUtil.notWest),
            northEastCornerVariant.withYRotationOf(Rotation.R180)
        )
    }
    /*------------ End Tables -----------*/

    /*------------ Coffee Tables -----------*/
    private fun genCoffeeTableBlockStateModels(block: CoffeeTableBlock) {
        with(generator) {
            TextureMap().apply {
                put(TextureKey.TOP, TextureMap.getId(block.topBlock))
                put(ModModels.legs, TextureMap.getId(block.baseBlock))
            }.run {
                blockStateCollector.accept(
                    MultipartBlockStateSupplier.create(block).coffeeTableSupplier(
                        shortTopModel = ModModels.coffeeTableTopShort.upload(block, this, modelCollector),
                        shortLegModel = "${block.woodType.name.lowercase()}_coffee_table_leg_short".asBlockId(),
                        tallTopModel = ModModels.coffeeTableTopTall.upload(block, this, modelCollector),
                        tallLegModel = "${block.woodType.name.lowercase()}_coffee_table_leg_tall".asBlockId(),
                    )
                )
                ModModels.coffeeTableInventory.upload(block.getItemModelId(), this, modelCollector)
            }
        }
    }

    private fun MultipartBlockStateSupplier.coffeeTableSupplier(
        shortTopModel: Identifier,
        shortLegModel: Identifier,
        tallTopModel: Identifier,
        tallLegModel: Identifier
    ): MultipartBlockStateSupplier = this.apply {
        val shortNorthEastVariant = shortLegModel.asBlockStateVariant()
        val tallNorthEastVariant = tallLegModel.asBlockStateVariant()

        val isTall = newWhen.set(ModProperties.coffeeTableType, CoffeeTableType.TALL)
        val isShort = newWhen.set(ModProperties.coffeeTableType, CoffeeTableType.SHORT)

        mapOf(
            isShort to BlockStateVariant().putModel(shortTopModel),
            WhenUtil.notNorthEast to shortNorthEastVariant,
            WhenUtil.notNorthWest to shortNorthEastVariant.withYRotationOf(Rotation.R270),
            WhenUtil.notSouthEast to shortNorthEastVariant.withYRotationOf(Rotation.R90),
            WhenUtil.notSouthWest to shortNorthEastVariant.withYRotationOf(Rotation.R180),
            isTall to tallTopModel.asBlockStateVariant(),
            WhenUtil.notNorthEast and isTall to tallNorthEastVariant,
            WhenUtil.notNorthWest and isTall to tallNorthEastVariant.withYRotationOf(Rotation.R270),
            WhenUtil.notSouthEast and isTall to tallNorthEastVariant.withYRotationOf(Rotation.R90),
            WhenUtil.notSouthWest and isTall to tallNorthEastVariant.withYRotationOf(Rotation.R180)
        ).forEach(::with)
    }


    /*------------ End Coffee Tables -----------*/

    /*------------ Bookshelves -----------*/
    private fun genThinBookshelfBlockStateModels(block: ThinBookshelfBlock) {
        with(generator) {
            blockStateCollector.accept(
                MultipartBlockStateSupplier.create(block).apply {
                    TextureMap.all(block.baseBlock).run {
                        ModModels.thinBookshelfItem.upload(block.getItemModelId(), this, modelCollector)

                        val bookshelfModel = ModModels.thinBookshelfBlock.upload(block, this, modelCollector)

                        val slotModels = arrayOf(
                            ModModels.thinBookshelfSlot0,
                            ModModels.thinBookshelfSlot1,
                            ModModels.thinBookshelfSlot2,
                            ModModels.thinBookshelfSlot3,
                            ModModels.thinBookshelfSlot4,
                            ModModels.thinBookshelfSlot5
                        )
                        val directions = arrayOf(
                            WhenUtil.facingNorthHorizontal,
                            WhenUtil.facingEastHorizontal,
                            WhenUtil.facingSouthHorizontal,
                            WhenUtil.facingWestHorizontal
                        )
                        val variants = Array(4) { bookshelfModel.asBlockStateVariant() }
                        val slotVariants = Array(6) { i ->
                            Array(4) { slotModels[i].asBlockStateVariant().withYRotationOf(Rotation.entries[it]) }
                        }

                        for (i in directions.indices) {
                            with(directions[i], variants[i].withYRotationOf(Rotation.entries[i]))
                            for (j in slotVariants.indices) {
                                with(
                                    directions[i] and newWhen.set(ChiseledBookshelfBlock.SLOT_OCCUPIED_PROPERTIES[j], true),
                                    slotVariants[j][i]
                                )
                            }
                        }
                    }
                }
            )
        }
    }

    /*------------ End Bookshelves -----------*/

    /*------------ Floor Coverings -----------*/
    private fun genFloorCoveringBlockStateModels(block: FloorCoveringBlock) {
        with(generator) {
            registerSingleton(block, TextureMap().put(TextureKey.WOOL, TextureMap.getId(block.baseBlock)), Models.CARPET)
            registerParentedItemModel(block, ModelIds.getBlockModelId(block))
        }
    }
    /*------------ End Floor Coverings -----------*/

    /*------------ Kitchen Counters -----------*/
    private fun genKitchenCounterBlockStateModels(block: KitchenCounterBlock) {
        with(generator) {
            TextureMap().apply {
                put(TextureKey.TOP, TextureMap.getId(block.topBlock))
                put(TextureKey.SIDE, TextureMap.getId(block.baseBlock))
            }.run {
                val normalModel = ModModels.kitchenCounter.upload(block, this, modelCollector)

                blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                    .coordinate(
                        createKitchenCounterVariantMap(
                            blockModel = normalModel,
                            innerLeftModel = ModModels.kitchenCounterInnerLeftCorner.upload(block, this, modelCollector),
                            outerLeftModel = ModModels.kitchenCounterOuterLeftCorner.upload(block, this, modelCollector)
                        )
                    )
                )
                registerParentedItemModel(block, normalModel)
            }
        }
    }

    private fun createKitchenCounterVariantMap(
        blockModel: Identifier,
        innerLeftModel: Identifier,
        outerLeftModel: Identifier
    ) = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.STAIR_SHAPE).apply {
        val northBlock = blockModel.asBlockStateVariant()
        val northInnerLeft = innerLeftModel.asBlockStateVariant()
        val northOuterLeft = outerLeftModel.asBlockStateVariant()

        mapOf(
            Direction.NORTH to mapOf(
                StairShape.STRAIGHT to northBlock,
                StairShape.INNER_LEFT to northInnerLeft,
                StairShape.OUTER_LEFT to northOuterLeft,
                StairShape.INNER_RIGHT to  northInnerLeft.withYRotationOf(Rotation.R90),
                StairShape.OUTER_RIGHT to northOuterLeft.withYRotationOf(Rotation.R90),
            ),
            Direction.EAST to mapOf(
                StairShape.STRAIGHT to northBlock.withYRotationOf(Rotation.R90),
                StairShape.INNER_LEFT to northInnerLeft.withYRotationOf(Rotation.R90),
                StairShape.OUTER_LEFT to northOuterLeft.withYRotationOf(Rotation.R90),
                StairShape.INNER_RIGHT to  northInnerLeft.withYRotationOf(Rotation.R180),
                StairShape.OUTER_RIGHT to northOuterLeft.withYRotationOf(Rotation.R180),
            ),
            Direction.SOUTH to mapOf(
                StairShape.STRAIGHT to northBlock.withYRotationOf(Rotation.R180),
                StairShape.INNER_LEFT to northInnerLeft.withYRotationOf(Rotation.R180),
                StairShape.OUTER_LEFT to northOuterLeft.withYRotationOf(Rotation.R180),
                StairShape.INNER_RIGHT to  northInnerLeft.withYRotationOf(Rotation.R270),
                StairShape.OUTER_RIGHT to northOuterLeft.withYRotationOf(Rotation.R270),
            ),
            Direction.WEST to mapOf(
                StairShape.STRAIGHT to northBlock.withYRotationOf(Rotation.R270),
                StairShape.INNER_LEFT to northInnerLeft.withYRotationOf(Rotation.R270),
                StairShape.OUTER_LEFT to northOuterLeft.withYRotationOf(Rotation.R270),
                StairShape.INNER_RIGHT to  northInnerLeft,
                StairShape.OUTER_RIGHT to northOuterLeft,
            )
        ).forEach { i -> i.value.forEach { j -> register(i.key, j.key, j.value) } }
    }


    /*------------ End Kitchen Counters -----------*/

    /*------------ Kitchen Cabinets -----------*/
    private fun genKitchenCabinetBlockStateModels(block: KitchenCabinetBlock) {
        with (generator) {
            TextureMap().apply {
                put(TextureKey.TOP, TextureMap.getId(block.topBlock))
                put(TextureKey.SIDE, TextureMap.getId(block.baseBlock))
            }.run {
                val model = ModModels.kitchenCabinet.upload(block, this, modelCollector)
                blockStateCollector.accept(
                    VariantsBlockStateSupplier.create(block, model.asBlockStateVariant())
                        .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
                )
                this@with.registerParentedItemModel(block, model)
            }
        }
    }

    /*------------ End Kitchen Cabinets -----------*/
}

