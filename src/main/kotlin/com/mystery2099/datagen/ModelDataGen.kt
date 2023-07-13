package com.mystery2099.datagen

import com.google.gson.JsonElement
import com.mystery2099.WoodenAccentsMod.toId
import com.mystery2099.WoodenAccentsMod.toVanillaId
import com.mystery2099.block.ModBlocks
import com.mystery2099.block.custom.*
import com.mystery2099.block.custom.enums.CoffeeTableType
import com.mystery2099.data.ModModels
import com.mystery2099.state.property.ModProperties
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
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

    private lateinit var modelCollector: BiConsumer<Identifier, Supplier<JsonElement>>

    private lateinit var stateCollector: Consumer<BlockStateSupplier>
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        generator = blockStateModelGenerator
        modelCollector = blockStateModelGenerator.modelCollector
        stateCollector = blockStateModelGenerator.blockStateCollector

        ModBlocks.blocks.forEach(::genBlockStateModel)
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }

    private fun genBlockStateModel(block: Block) {
        when (block) {
            is ThinPillarBlock -> genThinPillarBlockStateModels(block)
            is ThickPillarBlock -> genThickPillarBlockStateModels(block)
            is CustomWallBlock -> genWallBlockStateModels(block)
            is PlankLadderBlock -> genPlankLadderBlockStateModels(block)
            is TableBlock -> genTableBlockStateModels(block)
            is CoffeeTableBlock -> genCoffeeTableBlockStateModels(block)
            is KitchenCounterBlock -> genKitchenCounterBlockStateModels(block)
        }
    }

    /*------------ Pillars -----------*/
    private fun pillar(
        block: Block,
        inventoryModel: Identifier,
        topModel: Identifier,
        centerModel: Identifier,
        bottomModel: Identifier
    ) {
        val bottomVariant = BlockStateVariant().putModel(bottomModel)
        val topVariant = bottomVariant.withXRotationOf(Rotation.R180).put(VariantSettings.UVLOCK, true)
        val supplier = MultipartBlockStateSupplier.create(block)
            .with(BlockStateVariant().putModel(centerModel))
            .with(whenNotUp, topVariant)
            .with(whenNotDown, bottomVariant)
        stateCollector.accept(supplier)
        generator.registerParentedItemModel(block, inventoryModel)
    }

    private fun genThinPillarBlockStateModels(pillarBlock: ThinPillarBlock) {
        val textureMap = TextureMap.all(pillarBlock.baseBlock)
        val inventory = ModModels.thinPillarInventory.upload(pillarBlock, textureMap, modelCollector)
        val top = ModModels.thinPillarTop.upload(pillarBlock, textureMap, modelCollector)
        val bottom = ModModels.thinPillarBottom.upload(pillarBlock, textureMap, modelCollector)
        lateinit var type: WoodType
        WoodType.stream().forEach {
            if (pillarBlock.baseBlock.translationKey.contains(it.name.lowercase())) {
                type = it
                if (!it.name.lowercase().contains("dark") && !pillarBlock.baseBlock.translationKey.contains("dark")) {
                    return@forEach
                }
            }
        }
        pillar(
            pillarBlock,
            inventory,
            top,
            "block/${type.name.lowercase()}_fence_post".toVanillaId(),
            bottom
        )
    }

    private fun genThickPillarBlockStateModels(pillarBlock: ThickPillarBlock) {
        val textureMap = TextureMap.all(pillarBlock.baseBlock)
        val inventory = ModModels.thickPillarInventory.upload(pillarBlock, textureMap, modelCollector)
        val top = ModModels.thickPillarTop.upload(pillarBlock, textureMap, modelCollector)
        val bottom = ModModels.thickPillarBottom.upload(pillarBlock, textureMap, modelCollector)
        lateinit var type: WoodType
        WoodType.stream().forEach {
            if (pillarBlock.baseBlock.translationKey.contains(it.name.lowercase())) {
                type = it
                if (!it.name.lowercase().contains("dark") && !pillarBlock.baseBlock.translationKey.contains("dark")) {
                    return@forEach
                }
            }
        }
        pillar(pillarBlock, inventory, top, "block/${type.name.lowercase()}_wall_post".toId(), bottom)
    }

    /*------------ End Pillars -----------*/

    /*------------ Walls -----------*/
    private fun genWallBlockStateModels(block: CustomWallBlock) {
        val map = TextureMap().put(TextureKey.WALL, TextureMap.getId(block.baseBlock))
        with(generator) {
            blockStateCollector.accept(
                BlockStateModelGenerator.createWallBlockState(
                    block,
                    Models.TEMPLATE_WALL_POST.upload(block, map, modelCollector),
                    Models.TEMPLATE_WALL_SIDE.upload(block, map, modelCollector),
                    Models.TEMPLATE_WALL_SIDE_TALL.upload(block, map, modelCollector)
                )
            )
            registerParentedItemModel(block, Models.WALL_INVENTORY.upload(block, map, modelCollector))
        }
    }
    /*------------ End Walls -----------*/

    /*------------ Ladders -----------*/
    private fun genPlankLadderBlockStateModels(block: PlankLadderBlock) {
        val map: TextureMap = TextureMap.all(block.baseBlock)
        ModModels.plankLadder.upload(block, map, modelCollector)
        generator.registerNorthDefaultHorizontalRotation(block)
    }
    /*------------ End Ladders -----------*/

    /*------------ Tables -----------*/

    private fun genTableBlockStateModels(tableBlock: TableBlock) {
        val map = TextureMap().apply {
            put(TextureKey.TOP, TextureMap.getId(tableBlock.topBlock))
            put(ModModels.legs, TextureMap.getId(tableBlock.baseBlock))
        }

        stateCollector.accept(tableSupplier(tableBlock,
            ModModels.TABLE_TOP.upload(tableBlock, map, modelCollector),
            ModModels.TABLE_SINGLE_LEG.upload(tableBlock, map, modelCollector),
            ModModels.TABLE_END_LEG.upload(tableBlock, map, modelCollector),
            ModModels.TABLE_CORNER_LEG.upload(tableBlock, map, modelCollector))
        )
        generator.registerParentedItemModel(tableBlock, ModModels.TABLE_INVENTORY.upload(tableBlock, map, modelCollector))
    }

    private fun tableSupplier(
        block: TableBlock,
        topModel: Identifier,
        singleLegModel: Identifier,
        endLegModel: Identifier,
        cornerLegModel: Identifier,
    ): MultipartBlockStateSupplier {
        val northEndLegVariant = BlockStateVariant().putModel(endLegModel)
        val eastEndLegVariant = northEndLegVariant.withYRotationOf(Rotation.R90)
        val southEndLegVariant = northEndLegVariant.withYRotationOf(Rotation.R180)
        val westEndLegVariant = northEndLegVariant.withYRotationOf(Rotation.R270)

        val northEastCornerVariant = BlockStateVariant().putModel(cornerLegModel)
        val northWestCornerVariant = northEastCornerVariant.withYRotationOf(Rotation.R270)
        val southEastCornerVariant = northEastCornerVariant.withYRotationOf(Rotation.R90)
        val southWestCornerVariant = northEastCornerVariant.withYRotationOf(Rotation.R180)

        
        return MultipartBlockStateSupplier.create(block).apply {
            with(BlockStateVariant().putModel(topModel))
            with(
                When.allOf(whenNotNorth, whenNotEast, whenNotSouth, whenNotWest),
                BlockStateVariant().putModel(singleLegModel)
            )
            //Ends
            with(When.allOf(whenNotNorth, whenNotEast, whenSouth, whenNotWest), northEndLegVariant)
            with(When.allOf(whenNotNorth, whenNotEast, whenNotSouth, whenWest), eastEndLegVariant)
            with(When.allOf(whenNorth, whenNotEast, whenNotSouth, whenNotWest), southEndLegVariant)
            with(When.allOf(whenNotNorth, whenEast, whenNotSouth, whenNotWest), westEndLegVariant)
            //Corners
            with(When.allOf(whenNotNorth, whenNotEast, whenSouth, whenWest), northEastCornerVariant)
            with(When.allOf(whenNotNorth, whenEast, whenSouth, whenNotWest), northWestCornerVariant)
            with(When.allOf(whenNorth, whenNotEast, whenNotSouth, whenWest), southEastCornerVariant)
            with(When.allOf(whenNorth, whenEast, whenNotSouth, whenNotWest), southWestCornerVariant)
        }
    }
    /*------------ End Tables -----------*/

    /*------------ Coffee Tables -----------*/
    private fun genCoffeeTableBlockStateModels(block: CoffeeTableBlock) {
        val map = TextureMap().apply {
            put(TextureKey.TOP, TextureMap.getId(block.topBlock))
            put(ModModels.legs, TextureMap.getId(block.baseBlock))
        }
        with(generator) {
            blockStateCollector.run {
                accept(MultipartBlockStateSupplier.create(block).coffeeTableSupplier(
                        shortTopModel = ModModels.coffeeTableTopShort.upload(block, map, modelCollector),
                        shortLegModel = ModModels.coffeeTableLegShort.upload(block, map, modelCollector),
                        tallTopModel = ModModels.coffeeTableTopTall.upload(block, map, modelCollector),
                        tallLegModel = ModModels.coffeeTableLegTall.upload(block, map, modelCollector))
                    )
            }
            registerParentedItemModel(block, ModModels.coffeeTableInventory.upload(block, map, modelCollector))
        }
    }

    private fun MultipartBlockStateSupplier.coffeeTableSupplier(
        shortTopModel: Identifier,
        shortLegModel: Identifier,
        tallTopModel: Identifier,
        tallLegModel: Identifier
    ): MultipartBlockStateSupplier {
        val shortNorthEastVariant = BlockStateVariant().putModel(shortLegModel)
        val tallNorthEastVariant = BlockStateVariant().putModel(tallLegModel)

        val isTall = When.create().set(ModProperties.coffeeTableType, CoffeeTableType.TALL)
        val isShort = When.create().set(ModProperties.coffeeTableType, CoffeeTableType.SHORT)
        
        val map = mapOf(
            isShort to BlockStateVariant().putModel(shortTopModel),
            whenNotNorthEast to shortNorthEastVariant,
            whenNotNorthWest to shortNorthEastVariant.withYRotationOf(Rotation.R270),
            whenNotSouthEast to shortNorthEastVariant.withYRotationOf(Rotation.R90),
            whenNotSouthWest to shortNorthEastVariant.withYRotationOf(Rotation.R180),
            isTall to BlockStateVariant().putModel(tallTopModel),
            When.allOf(whenNotNorthEast, isTall) to tallNorthEastVariant,
            When.allOf(whenNotNorthWest, isTall) to tallNorthEastVariant.withYRotationOf(Rotation.R270),
            When.allOf(whenNotSouthEast, isTall) to tallNorthEastVariant.withYRotationOf(Rotation.R90),
            When.allOf(whenNotSouthWest, isTall) to tallNorthEastVariant.withYRotationOf(Rotation.R180)
        )
        return this.apply { map.forEach(::with) }
    }


    /*------------ End Coffee Tables -----------*/


    /*------------ Kitchen Counters -----------*/
    private fun genKitchenCounterBlockStateModels(block: KitchenCounterBlock) {
        val map = TextureMap()
            .put(TextureKey.TOP, TextureMap.getId(block.topBlock))
            .put(TextureKey.SIDE, TextureMap.getId(block.baseBlock))

        val normalModel = ModModels.kitchenCounter.upload(block, map, modelCollector)

        with(generator) {
            blockStateCollector.run {
                accept(VariantsBlockStateSupplier.create(block).coordinate(
                        createKitchenCounterVariantMap(
                            blockModel = normalModel,
                            innerLeftModel = ModModels.kitchenCounterInnerLeftCorner.upload(block, map, modelCollector),
                            outerLeftModel = ModModels.kitchenCounterOuterLeftCorner.upload(block, map, modelCollector)
                        )
                    ))
            }
            registerParentedItemModel(block, normalModel)
        }
    }

    private fun createKitchenCounterVariantMap(
        blockModel: Identifier,
        innerLeftModel: Identifier,
        outerLeftModel: Identifier
    ): BlockStateVariantMap {
        val southBlockVariant = BlockStateVariant().putModel(blockModel)
        val southInnerLeftVariant = BlockStateVariant().putModel(innerLeftModel)
        val southInnerRightVariant = southInnerLeftVariant.withYRotationOf(Rotation.R90)
        val southOuterLeftVariant = BlockStateVariant().putModel(outerLeftModel)
        val southOuterRightVariant = southOuterLeftVariant.withYRotationOf(Rotation.R90)

        return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.STAIR_SHAPE).apply {
            for (direction in horizontalDirections) {
                for (shape in StairShape.values()) {
                    val variant = when (direction) {
                        Direction.NORTH -> when (shape) {
                            StairShape.STRAIGHT -> southBlockVariant.withYRotationOf(Rotation.R180)
                            StairShape.INNER_LEFT -> southInnerLeftVariant.withYRotationOf(Rotation.R180)
                            StairShape.INNER_RIGHT -> southInnerRightVariant.withYRotationOf(Rotation.R270)
                            StairShape.OUTER_LEFT -> southOuterLeftVariant.withYRotationOf(Rotation.R180)
                            StairShape.OUTER_RIGHT -> southOuterLeftVariant.withYRotationOf(Rotation.R270)
                            else -> throw IllegalStateException("Invalid shape: $shape")
                        }
                        Direction.EAST -> when (shape) {
                            StairShape.STRAIGHT -> southBlockVariant.withYRotationOf(Rotation.R270)
                            StairShape.INNER_LEFT -> southInnerLeftVariant.withYRotationOf(Rotation.R270)
                            StairShape.INNER_RIGHT -> southInnerRightVariant.withYRotationOf(Rotation.R0)
                            StairShape.OUTER_LEFT -> southOuterLeftVariant.withYRotationOf(Rotation.R270)
                            StairShape.OUTER_RIGHT -> southOuterRightVariant.withYRotationOf(Rotation.R0)
                            else -> throw IllegalStateException("Invalid shape: $shape")
                        }
                        Direction.SOUTH -> when (shape) {
                            StairShape.STRAIGHT -> southBlockVariant
                            StairShape.INNER_LEFT -> southInnerLeftVariant
                            StairShape.INNER_RIGHT -> southInnerRightVariant
                            StairShape.OUTER_LEFT -> southOuterLeftVariant
                            StairShape.OUTER_RIGHT -> southOuterRightVariant
                            else -> throw IllegalStateException("Invalid shape: $shape")
                        }
                        Direction.WEST -> when (shape) {
                            StairShape.STRAIGHT -> southBlockVariant.withYRotationOf(Rotation.R90)
                            StairShape.INNER_LEFT -> southInnerLeftVariant.withYRotationOf(Rotation.R90)
                            StairShape.INNER_RIGHT -> southInnerLeftVariant.withYRotationOf(Rotation.R180)
                            StairShape.OUTER_LEFT -> southOuterLeftVariant.withYRotationOf(Rotation.R90)
                            StairShape.OUTER_RIGHT -> southOuterLeftVariant.withYRotationOf(Rotation.R180)
                            else -> throw IllegalStateException("Invalid shape: $shape")
                        }

                        else -> throw IllegalStateException("Invalid direction: $direction")
                    }
                    register(direction, shape, variant)
                }
            }
        }
    }


    /*------------ End Kitchen Counters -----------*/
    
    
    
    /*------------ Extension Methods -----------*/
    //Variant Extension Models
    private fun BlockStateVariant.union(other: BlockStateVariant): BlockStateVariant {
        return BlockStateVariant.union(this, other)
    }

    private fun BlockStateVariant.union(vararg others: BlockStateVariant) {
        var newVariant = this
        return others.forEach { other -> newVariant = newVariant.union(other) }
    }

    private fun BlockStateVariant.putModel(model: Identifier): BlockStateVariant {
        this.put(VariantSettings.MODEL, model)
        return this
    }

    private fun BlockStateVariant.withYRotationOf(rotation: Rotation): BlockStateVariant {
        return this.union(BlockStateVariant().put(VariantSettings.Y, rotation))
    }
    private fun BlockStateVariant.withXRotationOf(rotation: Rotation): BlockStateVariant {
        return this.union(BlockStateVariant().put(VariantSettings.X, rotation))
    }
}