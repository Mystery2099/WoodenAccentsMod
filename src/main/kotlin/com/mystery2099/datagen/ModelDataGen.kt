package com.mystery2099.datagen

import com.google.gson.JsonElement
import com.mystery2099.block.ModBlocks
import com.mystery2099.block.custom.*
import com.mystery2099.block.custom.enums.CoffeeTableType
import com.mystery2099.data.ModModels
import com.mystery2099.state.property.ModProperties
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
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
    private val whenUp: When = When.create().set(Properties.UP, true)
    private val whenDown: When = When.create().set(Properties.DOWN, true)
    private val whenNorth: When = When.create().set(Properties.NORTH, true)
    private val whenEast: When = When.create().set(Properties.EAST, true)
    private val whenSouth: When = When.create().set(Properties.SOUTH, true)
    private val whenWest: When = When.create().set(Properties.WEST, true)

    private val whenNorthEast: When = When.allOf(whenNorth, whenEast)
    private val whenSouthEast: When = When.allOf(whenSouth, whenEast)
    private val whenNorthWest: When = When.allOf(whenNorth, whenWest)
    private val whenSouthWest: When = When.allOf(whenSouth, whenWest)

    private val whenNotUp: When = When.create().set(Properties.UP, false)
    private val whenNotDown: When = When.create().set(Properties.DOWN, false)
    private val whenNotNorth: When = When.create().set(Properties.NORTH, false)
    private val whenNotEast: When = When.create().set(Properties.EAST, false)
    private val whenNotSouth: When = When.create().set(Properties.SOUTH, false)
    private val whenNotWest: When = When.create().set(Properties.WEST, false)

    private val whenNotNorthEast: When = When.allOf(whenNotNorth, whenNotEast)
    private val whenNotSouthEast: When = When.allOf(whenNotSouth, whenNotEast)
    private val whenNotNorthWest: When = When.allOf(whenNotNorth, whenNotWest)
    private val whenNotSouthWest: When = When.allOf(whenNotSouth, whenNotWest)

    private val whenFacingNorth: When = When.create().set(Properties.FACING, Direction.NORTH)
    private val whenFacingEast: When = When.create().set(Properties.FACING, Direction.EAST)
    private val whenFacingSouth: When = When.create().set(Properties.FACING, Direction.SOUTH)
    private val whenFacingWest: When = When.create().set(Properties.FACING, Direction.WEST)
    private val whenFacingUp: When = When.create().set(Properties.FACING, Direction.UP)
    private val whenFacingDown: When = When.create().set(Properties.FACING, Direction.DOWN)

    //Horizontal Facing
    private val whenFacingNorthHorizontal: When = When.create().set(Properties.HORIZONTAL_FACING, Direction.NORTH)
    private val whenFacingEastHorizontal: When = When.create().set(Properties.HORIZONTAL_FACING, Direction.EAST)
    private val whenFacingSouthHorizontal: When = When.create().set(Properties.HORIZONTAL_FACING, Direction.SOUTH)
    private val whenFacingWestHorizontal: When = When.create().set(Properties.HORIZONTAL_FACING, Direction.WEST)


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
        val supplier = MultipartBlockStateSupplier.create(block)
            .with(BlockStateVariant().putModel(centerModel))
            .with(whenNotUp, BlockStateVariant().putModel(topModel))
            .with(whenNotDown, BlockStateVariant().putModel(bottomModel))
        stateCollector.accept(supplier)
        generator.registerParentedItemModel(block, inventoryModel)
    }

    private fun genThinPillarBlockStateModels(pillarBlock: ThinPillarBlock) {
        val textureMap = TextureMap.all(pillarBlock.baseBlock)
        val inventory = ModModels.thinPillarInventory.upload(pillarBlock, textureMap, modelCollector)
        val top = ModModels.thinPillarTop.upload(pillarBlock, textureMap, modelCollector)
        val center = ModModels.thinPillarCenter.upload(pillarBlock, textureMap, modelCollector)
        val bottom = ModModels.thinPillarBottom.upload(pillarBlock, textureMap, modelCollector)
        pillar(
            pillarBlock,
            inventory,
            top,
            center,
            bottom
        )
    }

    private fun genThickPillarBlockStateModels(pillarBlock: ThickPillarBlock) {
        val textureMap = TextureMap.all(pillarBlock.baseBlock)
        val inventory = ModModels.thickPillarInventory.upload(pillarBlock, textureMap, modelCollector)
        val top = ModModels.thickPillarTop.upload(pillarBlock, textureMap, modelCollector)
        val center = ModModels.thickPillarCenter.upload(pillarBlock, textureMap, modelCollector)
        val bottom = ModModels.thickPillarBottom.upload(pillarBlock, textureMap, modelCollector)
        pillar(pillarBlock, inventory, top, center, bottom)
    }

    /*------------ End Pillars -----------*/

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

        val itemModel = ModModels.coffeeTableInventory.upload(block, map, modelCollector)
        val shortTop = ModModels.coffeeTableTopShort.upload(block, map, modelCollector)
        val shortLeg = ModModels.coffeeTableLegShort.upload(block, map, modelCollector)
        val tallTop = ModModels.coffeeTableTopTall.upload(block, map, modelCollector)
        val tallLeg = ModModels.coffeeTableLegTall.upload(block, map, modelCollector)

        stateCollector.accept(coffeeTableSupplier(block, shortTop, shortLeg, tallTop, tallLeg))
        generator.registerParentedItemModel(block, itemModel)
    }

    private fun coffeeTableSupplier(
        block: CoffeeTableBlock,
        shortTopModel: Identifier,
        shortLegModel: Identifier,
        tallTopModel: Identifier,
        tallLegModel: Identifier
    ): MultipartBlockStateSupplier {
        val shortNorthEastVariant = BlockStateVariant().putModel(shortLegModel)
        val tallNorthEastVariant = BlockStateVariant().putModel(tallLegModel)

        val isTall = When.create().set(ModProperties.coffeeTableType, CoffeeTableType.TALL)
        val isShort = When.create().set(ModProperties.coffeeTableType, CoffeeTableType.SHORT)
        
        val map = mapOf<When, BlockStateVariant>(
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

        return MultipartBlockStateSupplier.create(block).apply { map.forEach(::with) }
    }


    /*------------ End Coffee Tables -----------*/


    /*------------ Kitchen Counters -----------*/
    private fun genKitchenCounterBlockStateModels(block: KitchenCounterBlock) {
        val map = TextureMap()
            .put(TextureKey.TOP, TextureMap.getId(block.topBlock))
            .put(TextureKey.SIDE, TextureMap.getId(block.baseBlock))

        val normalModel = ModModels.kitchenCounter.upload(block, map, modelCollector)
        val innerLeftModel = ModModels.kitchenCounterInnerLeftCorner.upload(block, map, modelCollector)
        val outerLeftModel = ModModels.kitchenCounterOuterLeftCorner.upload(block, map, modelCollector)

        stateCollector.accept(createKitchenCounterVariantSupplier(block, normalModel, innerLeftModel, outerLeftModel))
        generator.registerParentedItemModel(block, normalModel)
    }

    private fun createKitchenCounterVariantSupplier(
        block: KitchenCounterBlock,
        blockModel: Identifier,
        innerLeftModel: Identifier,
        outerLeftModel: Identifier
    ): VariantsBlockStateSupplier {
        val southBlockVariant: BlockStateVariant = BlockStateVariant().putModel(blockModel)
        val southInnerLeftVariant: BlockStateVariant = BlockStateVariant().putModel(innerLeftModel)
        val southInnerRightVariant: BlockStateVariant = southInnerLeftVariant.withYRotationOf(Rotation.R90)
        val southOuterLeftVariant: BlockStateVariant = BlockStateVariant().putModel(outerLeftModel)
        val southOuterRightVariant: BlockStateVariant = southOuterLeftVariant.withYRotationOf(Rotation.R90)

        val westBlockVariant: BlockStateVariant = southBlockVariant.withYRotationOf(Rotation.R90)
        val westInnerLeftVariant: BlockStateVariant = southInnerLeftVariant.withYRotationOf(Rotation.R90)
        val westInnerRightVariant: BlockStateVariant = southInnerLeftVariant.withYRotationOf(Rotation.R180)
        val westOuterLeftVariant: BlockStateVariant = southOuterLeftVariant.withYRotationOf(Rotation.R90)
        val westOuterRightVariant: BlockStateVariant = southOuterLeftVariant.withYRotationOf(Rotation.R180)

        val northVariant = southBlockVariant.withYRotationOf(Rotation.R180)
        val northInnerLeftVariant = southInnerLeftVariant.withYRotationOf(Rotation.R180)
        val northInnerRightVariant = southInnerRightVariant.withYRotationOf(Rotation.R270)
        val northOuterLeftVariant = southOuterLeftVariant.withYRotationOf(Rotation.R180)
        val northOuterRightVariant = southOuterLeftVariant.withYRotationOf(Rotation.R270)

        val eastVariant = southBlockVariant.withYRotationOf(Rotation.R270)
        val eastInnerLeftVariant = southInnerLeftVariant.withYRotationOf(Rotation.R270)
        val eastInnerRightVariant = southInnerRightVariant.union(y0)
        val eastOuterLeftVariant = southOuterLeftVariant.withYRotationOf(Rotation.R270)
        val eastOuterRightVariant = southOuterRightVariant.union(y0)

        val map: BlockStateVariantMap = createKitchenCounterVariantMap(
            northVariant,
            northInnerLeftVariant,
            northInnerRightVariant,
            northOuterLeftVariant,
            northOuterRightVariant,
            eastVariant,
            eastInnerLeftVariant,
            eastInnerRightVariant,
            eastOuterLeftVariant,
            eastOuterRightVariant,
            southBlockVariant,
            southInnerLeftVariant,
            southInnerRightVariant,
            southOuterLeftVariant,
            southOuterRightVariant,
            westBlockVariant,
            westInnerLeftVariant,
            westInnerRightVariant,
            westOuterLeftVariant,
            westOuterRightVariant
        )

        return VariantsBlockStateSupplier.create(block).coordinate(map)
    }

    private fun createKitchenCounterVariantMap(vararg states: BlockStateVariant): BlockStateVariantMap {
        require(states.size >= 20) { "There must be at least 20 variants (0-19)" }

        return BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.STAIR_SHAPE)
            .register(Direction.NORTH, StairShape.STRAIGHT, states[0])
            .register(Direction.NORTH, StairShape.INNER_LEFT, states[1])
            .register(Direction.NORTH, StairShape.INNER_RIGHT, states[2])
            .register(Direction.NORTH, StairShape.OUTER_LEFT, states[3])
            .register(Direction.NORTH, StairShape.OUTER_RIGHT, states[4])
            .register(Direction.EAST, StairShape.STRAIGHT, states[5])
            .register(Direction.EAST, StairShape.INNER_LEFT, states[6])
            .register(Direction.EAST, StairShape.INNER_RIGHT, states[7])
            .register(Direction.EAST, StairShape.OUTER_LEFT, states[8])
            .register(Direction.EAST, StairShape.OUTER_RIGHT, states[9])
            .register(Direction.SOUTH, StairShape.STRAIGHT, states[10])
            .register(Direction.SOUTH, StairShape.INNER_LEFT, states[11])
            .register(Direction.SOUTH, StairShape.INNER_RIGHT, states[12])
            .register(Direction.SOUTH, StairShape.OUTER_LEFT, states[13])
            .register(Direction.SOUTH, StairShape.OUTER_RIGHT, states[14])
            .register(Direction.WEST, StairShape.STRAIGHT, states[15])
            .register(Direction.WEST, StairShape.INNER_LEFT, states[16])
            .register(Direction.WEST, StairShape.INNER_RIGHT, states[17])
            .register(Direction.WEST, StairShape.OUTER_LEFT, states[18])
            .register(Direction.WEST, StairShape.OUTER_RIGHT, states[19])
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
}