package com.mystery2099.datagen

import com.google.gson.JsonElement
import com.mystery2099.block.custom.ThinPillarBlock
import com.mystery2099.data.ModModels
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

    private fun createVariant(): BlockStateVariant = BlockStateVariant.create()

    private fun variantUnion(first: BlockStateVariant, second: BlockStateVariant): BlockStateVariant {
        return BlockStateVariant.union(first, second)
    }
    private fun BlockStateVariant.union(other: BlockStateVariant): BlockStateVariant {
        return BlockStateVariant.union(this, other)
    }
    private fun BlockStateVariant.union(vararg others: BlockStateVariant) {
        var newVariant = this;
        return others.forEach { other -> newVariant = newVariant.union(other) }
    }
    private fun BlockStateVariant.putModel(model: Identifier): BlockStateVariant {
        this.put(VariantSettings.MODEL, model)
        return this
    }



    val BLOCK = "block/"

    //Block State Variants
    private val y0: BlockStateVariant =
        BlockStateVariant.create().put(VariantSettings.Y, Rotation.R0)
    private val y90: BlockStateVariant =
        BlockStateVariant.create().put(VariantSettings.Y, Rotation.R90)
    private val y180: BlockStateVariant =
        BlockStateVariant.create().put(VariantSettings.Y, Rotation.R180)
    private val y270: BlockStateVariant =
        BlockStateVariant.create().put(VariantSettings.Y, Rotation.R270)
    private val x0: BlockStateVariant =
        BlockStateVariant.create().put(VariantSettings.X, Rotation.R0)
    private val x90: BlockStateVariant =
        BlockStateVariant.create().put(VariantSettings.X, Rotation.R90)
    private val x180: BlockStateVariant =
        BlockStateVariant.create().put(VariantSettings.X, Rotation.R90)
    private val x270: BlockStateVariant =
        BlockStateVariant.create().put(VariantSettings.X, Rotation.R270)

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

        ThinPillarBlock.instances.forEach(this::thinPillar)
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }

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

    private fun thinPillar(pillarBlock: ThinPillarBlock) {
        val textureMap = TextureMap.all(pillarBlock.baseBlock)
        val inventory = ModModels.thinPillarInventory.upload(pillarBlock, textureMap, modelCollector)
        val top = ModModels.thinPillarTop.upload(pillarBlock, textureMap, modelCollector)
        val center = ModModels.thinPillarCenter.upload(pillarBlock, textureMap, modelCollector)
        val bottom = ModModels.thinPillarBottom.upload(pillarBlock, textureMap, modelCollector)
        pillar(pillarBlock, inventory, top, center, bottom)
    }
}