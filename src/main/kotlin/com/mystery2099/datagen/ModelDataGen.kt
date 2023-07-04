package com.mystery2099.datagen

import com.google.gson.JsonElement
import com.mystery2099.block.custom.CoffeeTableBlock
import com.mystery2099.block.custom.ThickPillarBlock
import com.mystery2099.block.custom.ThinPillarBlock
import com.mystery2099.block.custom.enums.CoffeeTableType
import com.mystery2099.data.ModModels
import com.mystery2099.state.property.ModProperties
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.data.client.VariantSettings.Rotation
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier

class ModelDataGen(output: FabricDataOutput) : FabricModelProvider(output) {

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

    private val whenNotUp: When = When.create().set(Properties.UP, false)
    private val whenNotDown: When = When.create().set(Properties.DOWN, false)
    private val whenNotNorth: When = When.create().set(Properties.NORTH, false)
    private val whenNotEast: When = When.create().set(Properties.EAST, false)
    private val whenNotSouth: When = When.create().set(Properties.SOUTH, false)
    private val whenNotWest: When = When.create().set(Properties.WEST, false)

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


    private var generator: BlockStateModelGenerator? = null

    private var modelCollector: BiConsumer<Identifier, Supplier<JsonElement>>? = null

    private var stateCollector: Consumer<BlockStateSupplier>? = null
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        generator = blockStateModelGenerator
        modelCollector = blockStateModelGenerator.modelCollector
        stateCollector = blockStateModelGenerator.blockStateCollector

        ThinPillarBlock.instances.forEach(this::genThinPillarBlockStateModels)
        ThickPillarBlock.instances.forEach(this::genThickPillarBlockStateModels)
        CoffeeTableBlock.instances.forEach(this::genCoffeeTableBlockStateModels)
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

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
        stateCollector?.accept(supplier)
        generator?.registerParentedItemModel(block, inventoryModel)
    }

    private fun genThinPillarBlockStateModels(pillarBlock: ThinPillarBlock) {
        val textureMap = TextureMap.all(pillarBlock.baseBlock)
        val inventory = ModModels.thinPillarInventory.upload(pillarBlock, textureMap, modelCollector)
        val top = ModModels.thinPillarTop.upload(pillarBlock, textureMap, modelCollector)
        val center = ModModels.thinPillarCenter.upload(pillarBlock, textureMap, modelCollector)
        val bottom = ModModels.thinPillarBottom.upload(pillarBlock, textureMap, modelCollector)
        pillar(pillarBlock, inventory, top, center, bottom)
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

    /*------------ Coffee Tables -----------*/
    private fun genCoffeeTableBlockStateModels(block: CoffeeTableBlock) {
        val map = TextureMap().put(TextureKey.TOP, TextureMap.getId(block.topBlock))
            .put(ModModels.legs, TextureMap.getId(block.legBlock))
        val itemModel: Identifier = ModModels.coffeeTableInventory.upload(block, map, modelCollector)
        //Short Models
        val shortTop: Identifier = ModModels.coffeeTableTopShort.upload(block, map, modelCollector)
        val shortNorthEastLeg: Identifier = ModModels.coffeeTableNorthEastLegShort.upload(block, map, modelCollector)
        val shortNorthWestLeg: Identifier = ModModels.coffeeTableNorthWestLegShort.upload(block, map, modelCollector)
        val shortSouthEastLeg: Identifier = ModModels.coffeeTableSouthEastLegShort.upload(block, map, modelCollector)
        val shortSouthWestLeg: Identifier = ModModels.coffeeTableSouthWestLegShort.upload(block, map, modelCollector)

        //Tall Models
        val tallTop: Identifier = ModModels.coffeeTableTopTall.upload(block, map, modelCollector)
        val tallNorthEastLeg: Identifier = ModModels.coffeeTableNorthEastLegTall.upload(block, map, modelCollector)
        val tallNorthWestLeg: Identifier = ModModels.coffeeTableNorthWestLegTall.upload(block, map, modelCollector)
        val tallSouthEastLeg: Identifier = ModModels.coffeeTableSouthEastLegTall.upload(block, map, modelCollector)
        val tallSouthWestLeg: Identifier = ModModels.coffeeTableSouthWestLegTall.upload(block, map, modelCollector)
        stateCollector!!.accept(
            coffeeTableSupplier(
                block,
                shortTop,
                shortNorthEastLeg,
                shortNorthWestLeg,
                shortSouthEastLeg,
                shortSouthWestLeg,
                tallTop,
                tallNorthEastLeg,
                tallNorthWestLeg,
                tallSouthEastLeg,
                tallSouthWestLeg
            )
        )
        generator?.registerParentedItemModel(block, itemModel)
    }
    private fun coffeeTableSupplier(
        block: CoffeeTableBlock,
        shortTopModel: Identifier,
        shortNorthEastLegModel: Identifier,
        shortNorthWestLegModel: Identifier,
        shortSouthEastLegModel: Identifier,
        shortSouthWestLegModel: Identifier,
        tallTopModel: Identifier,
        tallNorthEastLegModel: Identifier,
        tallNorthWestLegModel: Identifier,
        tallSouthEastLegModel: Identifier,
        tallSouthWestLegModel: Identifier
    ): MultipartBlockStateSupplier {
        // Short Variants
        val shortTopVariant = BlockStateVariant().putModel(shortTopModel)
        val shortNorthEastVariant = BlockStateVariant().putModel(shortNorthEastLegModel)
        val shortNorthWestVariant = BlockStateVariant().putModel(shortNorthWestLegModel)
        val shortSouthEastVariant = BlockStateVariant().putModel(shortSouthEastLegModel)
        val shortSouthWestVariant = BlockStateVariant().putModel(shortSouthWestLegModel)

        // Tall Variants
        val tallTopVariant = BlockStateVariant().putModel(tallTopModel)
        val tallNorthEastVariant = BlockStateVariant().putModel(tallNorthEastLegModel)
        val tallNorthWestVariant = BlockStateVariant().putModel(tallNorthWestLegModel)
        val tallSouthEastVariant = BlockStateVariant().putModel(tallSouthEastLegModel)
        val tallSouthWestVariant = BlockStateVariant().putModel(tallSouthWestLegModel)

        // Property Conditions
        val isTall = When.create().set(ModProperties.coffeeTableType, CoffeeTableType.TALL)
        val isNotTall = When.create().set(ModProperties.coffeeTableType, CoffeeTableType.SHORT)
        val northEast = When.create().set(Properties.NORTH, false).set(Properties.EAST, false) // south-west
        val northWest = When.create().set(Properties.NORTH, false).set(Properties.WEST, false) // south-east
        val southEast = When.create().set(Properties.SOUTH, false).set(Properties.EAST, false) // north-west
        val southWest = When.create().set(Properties.SOUTH, false).set(Properties.WEST, false) // north-east

        // Whens
        val northEastTall = When.allOf(northEast, isTall)
        val northWestTall = When.allOf(northWest, isTall)
        val southEastTall = When.allOf(southEast, isTall)
        val southWestTall = When.allOf(southWest, isTall)

        return MultipartBlockStateSupplier.create(block)
            .with(isNotTall, shortTopVariant)
            .with(northEast, shortNorthEastVariant)
            .with(northWest, shortNorthWestVariant)
            .with(southEast, shortSouthEastVariant)
            .with(southWest, shortSouthWestVariant)
            .with(isTall, tallTopVariant)
            .with(northEastTall, tallNorthEastVariant)
            .with(northWestTall, tallNorthWestVariant)
            .with(southEastTall, tallSouthEastVariant)
            .with(southWestTall, tallSouthWestVariant)
    }

    /*------------ End Coffee Tables -----------*/


}