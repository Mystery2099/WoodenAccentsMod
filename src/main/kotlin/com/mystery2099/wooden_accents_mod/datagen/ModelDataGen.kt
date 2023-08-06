package com.mystery2099.wooden_accents_mod.datagen

import com.mystery2099.block.custom.KitchenCounterBlock
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asBlockModelId
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asPlanks
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asWamId
import com.mystery2099.wooden_accents_mod.block.ModBlocks
import com.mystery2099.wooden_accents_mod.block.ModBlocks.getItemModelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.modelId
import com.mystery2099.wooden_accents_mod.block.ModBlocks.textureId
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
                ModModels.coffeeTableLegShort.upload("${it.name.lowercase()}_coffee_table_leg_short".asWamId().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.coffeeTableLegTall.upload("${it.name.lowercase()}_coffee_table_leg_tall".asWamId().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.tableCenterLeg.upload("${it.name.lowercase()}_table_single_leg".asWamId().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.tableCornerLeg.upload("${it.name.lowercase()}_table_corner_leg".asWamId().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)

                ModModels.tableEndLeg.upload("${it.name.lowercase()}_table_end_leg".asWamId().asBlockModelId(), TextureMap().put(
                    ModModels.legs, it.asPlanks().textureId), modelCollector)
            }
            ModBlocks.blocks.forEach(::genBlockStateModel)
        }

    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }

    private fun genBlockStateModel(block: Block) {
        when (block) {
            //Outside
            is ThinPillarBlock -> block.genBlockStateModels()
            is ThickPillarBlock -> block.genBlockStateModels()
            is CustomWallBlock -> block.genBlockStateModels()
            is ModernFenceBlock -> block.genBlockStateModels()
            is ModernFenceGateBlock -> block.genBlockStateModels()
            is PlankLadderBlock -> block.genBlockStateModels()
            //Living Room
            is TableBlock -> block.genBlockStateModels()
            is CoffeeTableBlock -> block.genBlockStateModels()
            is ThinBookshelfBlock -> block.genBlockStateModels()
            is FloorCoveringBlock -> block.genBlockStateModels()
            //Kitchen
            is KitchenCounterBlock -> block.genBlockStateModels()
            is KitchenCabinetBlock -> block.genBlockStateModels()
        }
    }

    /*------------ Pillars -----------*/
    private fun AbstractPillarBlock.genBlockStateModels(
        centerModel: Identifier,
        bottomModel: Identifier
    ) {
        MultipartBlockStateSupplier.create(this).apply {
            with(centerModel.asBlockStateVariant())
            with(WhenUtil.notUp, bottomModel.asBlockStateVariant().withXRotationOf(Rotation.R180).uvLock())
            with(WhenUtil.notDown, bottomModel.asBlockStateVariant())
            generator.blockStateCollector.accept(this)
        }
    }
    private fun ThinPillarBlock.genBlockStateModels() {
        val map = TextureMap.all(this.baseBlock)
        this.genBlockStateModels(
            centerModel = Identifier("${this.woodType.name.lowercase()}_fence_post").asBlockModelId(),
            bottomModel = ModModels.thinPillarBottom.upload(this, map, generator.modelCollector)
        )
        ModModels.thinPillarInventory.upload(this.getItemModelId(), map, generator.modelCollector)
    }
    private fun ThickPillarBlock.genBlockStateModels() {
        val map = TextureMap.all(this.baseBlock)
        this.genBlockStateModels(
            centerModel = "${this.woodType.name.lowercase()}_wall_post".asWamId().asBlockModelId(),
            bottomModel = ModModels.thickPillarBottom.upload(this, map, generator.modelCollector)
        )
        ModModels.thickPillarInventory.upload(this.getItemModelId(), map, generator.modelCollector)
    }

    /*------------ End Pillars -----------*/

    /*------------ Walls -----------*/
    private fun CustomWallBlock.genBlockStateModels() {
            TextureMap().put(TextureKey.WALL, this.baseBlock.textureId).let { map ->
                generator.blockStateCollector.accept(
                    BlockStateModelGenerator.createWallBlockState(
                        this,
                        Models.TEMPLATE_WALL_POST.upload(this, map, generator.modelCollector),
                        Models.TEMPLATE_WALL_SIDE.upload(this, map, generator.modelCollector),
                        Models.TEMPLATE_WALL_SIDE_TALL.upload(this, map, generator.modelCollector)
                    )
                )
                generator.registerParentedItemModel(this, Models.WALL_INVENTORY.upload(this, map, generator.modelCollector))
            }
    }
    /*------------ End Walls -----------*/
    /*------------ Custom Fences -----------*/
    private fun ModernFenceBlock.genBlockStateModels() {
        val block = this
        TextureMap().apply {
            put(TextureKey.SIDE, block.sideBlock.textureId)
            put(TextureKey.END, block.postBlock.textureId)
            put(TextureKey.UP, TextureMap.getSubId(block.postBlock, "_top"))
        }.let { map ->
            ModModels.modernFenceInventory.upload(block.getItemModelId(), map, generator.modelCollector)
            generator.blockStateCollector.accept(
                BlockStateModelGenerator.createFenceBlockState(
                    block,
                    ModModels.modernFencePost.upload(block, map, generator.modelCollector),
                    ModModels.modernFenceSide.upload(block, map, generator.modelCollector)
                )
            )
        }
    }

    private fun ModernFenceGateBlock.genBlockStateModels() {
        val block = this
        TextureMap.all(block.baseBlock).let { map ->
            val model = ModModels.modernFenceGate.upload(block, map, generator.modelCollector)
            val modelOpen = ModModels.modernFenceGateOpen.upload(block, map, generator.modelCollector)

            generator.blockStateCollector.accept(BlockStateModelGenerator.createFenceGateBlockState(
                block,
                modelOpen,
                model,
                modelOpen,
                model,
                false
            ))
        }
    }
    /*------------ End Custom Fences -----------*/

    /*------------ Ladders -----------*/
    private fun PlankLadderBlock.genBlockStateModels() {
        ModModels.plankLadder.upload(this, TextureMap.all(this.baseBlock), generator.modelCollector)
        generator.registerNorthDefaultHorizontalRotation(this)
    }
    /*------------ End Ladders -----------*/

    /*------------ Tables -----------*/

    private fun TableBlock.genBlockStateModels() {
        val block = this
            TextureMap().apply {
                put(TextureKey.TOP, block.topBlock.textureId)
                put(ModModels.legs, block.baseBlock.textureId)
            }.also {map ->
                generator.blockStateCollector.accept(
                    block.supplier(
                        ModModels.tableTop.upload(block, map, generator.modelCollector),
                        "${block.woodType.name.lowercase()}_table_single_leg".asWamId().asBlockModelId(),
                        "${block.woodType.name.lowercase()}_table_end_leg".asWamId().asBlockModelId(),
                        "${block.woodType.name.lowercase()}_table_corner_leg".asWamId().asBlockModelId(),
                    )
                )
                ModModels.tableItem.upload(block.getItemModelId(), map, generator.modelCollector)
            }
    }

    private fun TableBlock.supplier(
        topModel: Identifier,
        singleLegModel: Identifier,
        endLegModel: Identifier,
        cornerLegModel: Identifier,
    ): MultipartBlockStateSupplier = MultipartBlockStateSupplier.create(this).apply {
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
    private fun CoffeeTableBlock.genBlockStateModels() {
        val block = this
        TextureMap().apply {
            put(TextureKey.TOP, block.topBlock.textureId)
            put(ModModels.legs, block.baseBlock.textureId)
        }.also { map ->
            generator.blockStateCollector.accept(
                block.supplier(
                    shortTopModel = ModModels.coffeeTableTopShort.upload(block, map, generator.modelCollector),
                    shortLegModel = "${block.woodType.name.lowercase()}_coffee_table_leg_short".asWamId().asBlockModelId(),
                    tallTopModel = ModModels.coffeeTableTopTall.upload(block, map, generator.modelCollector),
                    tallLegModel = "${block.woodType.name.lowercase()}_coffee_table_leg_tall".asWamId().asBlockModelId(),
                )
            )
            ModModels.coffeeTableInventory.upload(block.getItemModelId(), map, generator.modelCollector)
        }
    }

    private fun CoffeeTableBlock.supplier(
        shortTopModel: Identifier,
        shortLegModel: Identifier,
        tallTopModel: Identifier,
        tallLegModel: Identifier
    ): MultipartBlockStateSupplier = MultipartBlockStateSupplier.create(this).apply {
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
    private fun ThinBookshelfBlock.genBlockStateModels() {
        val block = this
        generator.blockStateCollector.accept(
            MultipartBlockStateSupplier.create(block).apply {
                TextureMap.all(block.baseBlock).let { map ->
                    ModModels.thinBookshelfItem.upload(block.getItemModelId(), map, generator.modelCollector)

                    val bookshelfModel = ModModels.thinBookshelfBlock.upload(block, map, generator.modelCollector)

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

    /*------------ End Bookshelves -----------*/

    /*------------ Floor Coverings -----------*/
    private fun FloorCoveringBlock.genBlockStateModels() {
        val block = this
        with(generator) {
            registerSingleton(block, TextureMap().put(TextureKey.WOOL, block.baseBlock.textureId), Models.CARPET)
            registerParentedItemModel(block, block.modelId)
        }
    }
    /*------------ End Floor Coverings -----------*/

    /*------------ Kitchen Counters -----------*/
    private fun KitchenCounterBlock.genBlockStateModels() {
        val block = this
        TextureMap().apply {
            put(TextureKey.TOP, block.topBlock.textureId)
            put(TextureKey.SIDE, block.baseBlock.textureId)
        }.let {map ->
            val normalModel = ModModels.kitchenCounter.upload(block, map, generator.modelCollector)

            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                .coordinate(
                    block.variantMap(
                        blockModel = normalModel,
                        innerLeftModel = ModModels.kitchenCounterInnerLeftCorner.upload(block, map, generator.modelCollector),
                        outerLeftModel = ModModels.kitchenCounterOuterLeftCorner.upload(block, map, generator.modelCollector)
                    )
                )
            )
            generator.registerParentedItemModel(block, normalModel)
        }
    }

    private fun KitchenCounterBlock.variantMap(
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
    private fun KitchenCabinetBlock.genBlockStateModels() {
        val block = this
        TextureMap().apply {
            put(TextureKey.TOP, block.topBlock.textureId)
            put(TextureKey.SIDE, block.baseBlock.textureId)
        }.let { map ->
            val model = ModModels.kitchenCabinet.upload(block, map, generator.modelCollector)
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, model.asBlockStateVariant())
                    .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
            )
            generator.registerParentedItemModel(block, model)
        }
    }

    /*------------ End Kitchen Cabinets -----------*/
}

