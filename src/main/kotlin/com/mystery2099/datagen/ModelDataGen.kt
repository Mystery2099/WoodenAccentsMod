package com.mystery2099.datagen

import com.google.gson.JsonElement
import com.mystery2099.WoodenAccentsMod.getItemModelId
import com.mystery2099.WoodenAccentsMod.planks
import com.mystery2099.WoodenAccentsMod.toBlockId
import com.mystery2099.WoodenAccentsMod.toId
import com.mystery2099.WoodenAccentsMod.toVanillaId
import com.mystery2099.WoodenAccentsMod.woodType
import com.mystery2099.block.ModBlocks
import com.mystery2099.block.custom.*
import com.mystery2099.block.custom.enums.CoffeeTableType
import com.mystery2099.data.ModModels
import com.mystery2099.state.property.ModProperties
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
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier

class ModelDataGen(output: FabricDataOutput) : FabricModelProvider(output) {

    val block = "block/"

    //Collections
    private val horizontalDirections = listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)

    //Block State Rotation Variants
    private val y0: BlockStateVariant =
        BlockStateVariant().put(VariantSettings.Y, Rotation.R0)
    private val y90: BlockStateVariant =
        BlockStateVariant().put(VariantSettings.Y, Rotation.R90)
    private val y180: BlockStateVariant =
        BlockStateVariant().put(VariantSettings.Y, Rotation.R180)
    private val y270: BlockStateVariant =
        BlockStateVariant().put(VariantSettings.Y, Rotation.R270)
    private val x0: BlockStateVariant =
        BlockStateVariant().put(VariantSettings.X, Rotation.R0)
    private val x90: BlockStateVariant =
        BlockStateVariant().put(VariantSettings.X, Rotation.R90)
    private val x180: BlockStateVariant =
        BlockStateVariant().put(VariantSettings.X, Rotation.R90)
    private val x270: BlockStateVariant =
        BlockStateVariant().put(VariantSettings.X, Rotation.R270)

    //Whens
    private val whenUp = When.create().set(Properties.UP, true)
    private val whenDown = When.create().set(Properties.DOWN, true)
    private val whenNorth = When.create().set(Properties.NORTH, true)
    private val whenEast = When.create().set(Properties.EAST, true)
    private val whenSouth = When.create().set(Properties.SOUTH, true)
    private val whenWest = When.create().set(Properties.WEST, true)

    private val whenNorthEast = When.allOf(whenNorth, whenEast)
    private val whenSouthEast = When.allOf(whenSouth, whenEast)
    private val whenNorthWest = When.allOf(whenNorth, whenWest)
    private val whenSouthWest = When.allOf(whenSouth, whenWest)

    private val whenNotUp = When.create().set(Properties.UP, false)
    private val whenNotDown = When.create().set(Properties.DOWN, false)
    private val whenNotNorth = When.create().set(Properties.NORTH, false)
    private val whenNotEast = When.create().set(Properties.EAST, false)
    private val whenNotSouth = When.create().set(Properties.SOUTH, false)
    private val whenNotWest = When.create().set(Properties.WEST, false)

    private val whenNotNorthEast = When.allOf(whenNotNorth, whenNotEast)
    private val whenNotSouthEast = When.allOf(whenNotSouth, whenNotEast)
    private val whenNotNorthWest = When.allOf(whenNotNorth, whenNotWest)
    private val whenNotSouthWest = When.allOf(whenNotSouth, whenNotWest)

    private val whenFacingNorth = When.create().set(Properties.FACING, Direction.NORTH)
    private val whenFacingEast = When.create().set(Properties.FACING, Direction.EAST)
    private val whenFacingSouth = When.create().set(Properties.FACING, Direction.SOUTH)
    private val whenFacingWest = When.create().set(Properties.FACING, Direction.WEST)
    private val whenFacingUp = When.create().set(Properties.FACING, Direction.UP)
    private val whenFacingDown = When.create().set(Properties.FACING, Direction.DOWN)

    //Horizontal Facing
    private val whenFacingNorthHorizontal = When.create().set(Properties.HORIZONTAL_FACING, Direction.NORTH)
    private val whenFacingEastHorizontal = When.create().set(Properties.HORIZONTAL_FACING, Direction.EAST)
    private val whenFacingSouthHorizontal = When.create().set(Properties.HORIZONTAL_FACING, Direction.SOUTH)
    private val whenFacingWestHorizontal = When.create().set(Properties.HORIZONTAL_FACING, Direction.WEST)


    private lateinit var generator: BlockStateModelGenerator

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        blockStateModelGenerator.run {
            generator = this
            WoodType.stream().forEach{
                ModModels.coffeeTableLegShort.upload("${it.name.lowercase()}_coffee_table_leg_short".toBlockId(), TextureMap().put(ModModels.legs, TextureMap.getId(it.planks())), modelCollector)
                ModModels.coffeeTableLegTall.upload("${it.name.lowercase()}_coffee_table_leg_tall".toBlockId(), TextureMap().put(ModModels.legs, TextureMap.getId(it.planks())), modelCollector)

                ModModels.tableCenterLeg.upload("${it.name.lowercase()}_table_single_leg".toBlockId(), TextureMap().put(ModModels.legs, TextureMap.getId(it.planks())), modelCollector)
                ModModels.tableCornerLeg.upload("${it.name.lowercase()}_table_corner_leg".toBlockId(), TextureMap().put(ModModels.legs, TextureMap.getId(it.planks())), modelCollector)
                ModModels.tableEndLeg.upload("${it.name.lowercase()}_table_end_leg".toBlockId(), TextureMap().put(ModModels.legs, TextureMap.getId(it.planks())), modelCollector)
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
                with(centerModel.blockStateVariant())
                with(whenNotUp, bottomModel.blockStateVariant().withXRotationOf(Rotation.R180).put(VariantSettings.UVLOCK, true))
                with(whenNotDown, bottomModel.blockStateVariant())
                blockStateCollector.accept(this)
            }
        }

    }
    private fun genThinPillarBlockStateModels(pillarBlock: ThinPillarBlock) {
        with(generator) {
            TextureMap.all(pillarBlock.baseBlock).run {
                ModModels.thinPillarInventory.upload(pillarBlock.getItemModelId(), this, modelCollector)
                genPillarBlockStateModels(
                    pillarBlock,
                    "block/${pillarBlock.woodType().name.lowercase()}_fence_post".toVanillaId(),
                    ModModels.thinPillarBottom.upload(pillarBlock, this, modelCollector)
                )
            }
        }
    }

    private fun genThickPillarBlockStateModels(pillarBlock: ThickPillarBlock) {
        with(generator) {
            TextureMap.all(pillarBlock.baseBlock).run {
                ModModels.thickPillarInventory.upload(pillarBlock.getItemModelId(), this, modelCollector)
                genPillarBlockStateModels(pillarBlock,
                    centerModel = "block/${pillarBlock.woodType().name.lowercase()}_wall_post".toId(),
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
                        "${block.woodType().name.lowercase()}_table_single_leg".toBlockId(),
                        "${block.woodType().name.lowercase()}_table_end_leg".toBlockId(),
                        "${block.woodType().name.lowercase()}_table_corner_leg".toBlockId(),
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
    ): MultipartBlockStateSupplier {
        return MultipartBlockStateSupplier.create(block).apply {
            val northEndLegVariant = endLegModel.blockStateVariant()
            val northEastCornerVariant = cornerLegModel.blockStateVariant()

            with(topModel.blockStateVariant())
            with(
                When.allOf(whenNotNorth, whenNotEast, whenNotSouth, whenNotWest),
                singleLegModel.blockStateVariant()
            )
            //Ends
            with(
                When.allOf(whenNotNorth, whenNotEast, whenSouth, whenNotWest),
                northEndLegVariant
            )
            with(
                When.allOf(whenNotNorth, whenNotEast, whenNotSouth, whenWest),
                northEndLegVariant.withYRotationOf(Rotation.R90)
            )
            with(
                When.allOf(whenNorth, whenNotEast, whenNotSouth, whenNotWest),
                northEndLegVariant.withYRotationOf(Rotation.R180)
            )
            with(
                When.allOf(whenNotNorth, whenEast, whenNotSouth, whenNotWest),
                northEndLegVariant.withYRotationOf(Rotation.R270)
            )
            //Corners
            with(
                When.allOf(whenNotNorth, whenNotEast, whenSouth, whenWest),
                northEastCornerVariant
            )
            with(
                When.allOf(whenNotNorth, whenEast, whenSouth, whenNotWest),
                northEastCornerVariant.withYRotationOf(Rotation.R270)
            )
            with(
                When.allOf(whenNorth, whenNotEast, whenNotSouth, whenWest),
                northEastCornerVariant.withYRotationOf(Rotation.R90)
            )
            with(
                When.allOf(whenNorth, whenEast, whenNotSouth, whenNotWest),
                northEastCornerVariant.withYRotationOf(Rotation.R180)
            )
        }
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
                        shortLegModel = "${block.woodType().name.lowercase()}_coffee_table_leg_short".toBlockId(),
                        tallTopModel = ModModels.coffeeTableTopTall.upload(block, this, modelCollector),
                        tallLegModel = "${block.woodType().name.lowercase()}_coffee_table_leg_tall".toBlockId(),
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
    ): MultipartBlockStateSupplier {
        return this.apply {
            val shortNorthEastVariant = shortLegModel.blockStateVariant()
            val tallNorthEastVariant = tallLegModel.blockStateVariant()

            val isTall = When.create().set(ModProperties.coffeeTableType, CoffeeTableType.TALL)
            val isShort = When.create().set(ModProperties.coffeeTableType, CoffeeTableType.SHORT)

            mapOf(
                isShort to BlockStateVariant().putModel(shortTopModel),
                whenNotNorthEast to shortNorthEastVariant,
                whenNotNorthWest to shortNorthEastVariant.withYRotationOf(Rotation.R270),
                whenNotSouthEast to shortNorthEastVariant.withYRotationOf(Rotation.R90),
                whenNotSouthWest to shortNorthEastVariant.withYRotationOf(Rotation.R180),
                isTall to tallTopModel.blockStateVariant(),
                When.allOf(whenNotNorthEast, isTall) to tallNorthEastVariant,
                When.allOf(whenNotNorthWest, isTall) to tallNorthEastVariant.withYRotationOf(Rotation.R270),
                When.allOf(whenNotSouthEast, isTall) to tallNorthEastVariant.withYRotationOf(Rotation.R90),
                When.allOf(whenNotSouthWest, isTall) to tallNorthEastVariant.withYRotationOf(Rotation.R180)
            ).forEach(::with)
        }
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
                            whenFacingNorthHorizontal,
                            whenFacingEastHorizontal,
                            whenFacingSouthHorizontal,
                            whenFacingWestHorizontal
                        )
                        val variants = Array(4) { bookshelfModel.blockStateVariant() }
                        val slotVariants = Array(6) { i ->
                            Array(4) { slotModels[i].blockStateVariant().withYRotationOf(Rotation.entries[it]) }
                        }

                        for (i in directions.indices) {
                            with(directions[i], variants[i].withYRotationOf(Rotation.entries[i]))
                            for (j in slotVariants.indices) {
                                with(
                                    directions[i].and(When.create().set(ChiseledBookshelfBlock.SLOT_OCCUPIED_PROPERTIES[j], true)),
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
    ): BlockStateVariantMap {
        return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.STAIR_SHAPE).apply {
            val northBlock = blockModel.blockStateVariant()
            val northInnerLeft = innerLeftModel.blockStateVariant()
            val northOuterLeft = outerLeftModel.blockStateVariant()

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
                    VariantsBlockStateSupplier.create(block, model.blockStateVariant())
                        .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
                )
                this@with.registerParentedItemModel(block, model)
            }
        }
    }

    /*------------ End Kitchen Cabinets -----------*/

    
    /*------------ Extension Methods -----------*/
    //Variant Extension Models
    private fun BlockStateVariant.union(other: BlockStateVariant): BlockStateVariant =
        BlockStateVariant.union(this, other)

    private fun BlockStateVariant.union(vararg others: BlockStateVariant) {
        var newVariant = this
        return others.forEach { other -> newVariant = newVariant.union(other) }
    }

    private fun BlockStateVariant.putModel(model: Identifier): BlockStateVariant {
        this.put(VariantSettings.MODEL, model)
        return this
    }
    private fun Identifier.blockStateVariant(): BlockStateVariant = BlockStateVariant().putModel(this)

    private fun BlockStateVariant.withYRotationOf(rotation: Rotation): BlockStateVariant {
        return this.union(BlockStateVariant().put(VariantSettings.Y, rotation))
    }
    private fun BlockStateVariant.withXRotationOf(rotation: Rotation): BlockStateVariant {
        return this.union(BlockStateVariant().put(VariantSettings.X, rotation))
    }

    private fun When.PropertyCondition.and(other: When.PropertyCondition): When = When.allOf(this, other)
    private fun When.and(other: When): When = When.allOf(this, other)

}

