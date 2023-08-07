package com.mystery2099.wooden_accents_mod.block

import com.mystery2099.block.custom.KitchenCounterBlock
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod
import com.mystery2099.wooden_accents_mod.WoodenAccentsMod.asWamId
import com.mystery2099.wooden_accents_mod.block.custom.*
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.FenceGateBlock
import net.minecraft.block.WoodType
import net.minecraft.data.client.ModelIds
import net.minecraft.data.client.TextureMap
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

@SuppressWarnings("unused")
object ModBlocks {
    private val registries = mutableSetOf<Block>()
    val blocks : Set<Block>
        get() = registries

    /*---------------Outside Stuff----------------*/

    //Thin plank pillars
    val thinOakPillar = ThinPillarBlock(Blocks.OAK_PLANKS).registerAs("thin_oak_pillar")
    val thinSprucePillar = ThinPillarBlock(Blocks.SPRUCE_PLANKS).registerAs("thin_spruce_pillar")
    val thinBirchPillar = ThinPillarBlock(Blocks.BIRCH_PLANKS).registerAs("thin_birch_pillar")
    val thinJunglePillar = ThinPillarBlock(Blocks.JUNGLE_PLANKS).registerAs("thin_jungle_pillar")
    val thinAcaciaPillar = ThinPillarBlock(Blocks.ACACIA_PLANKS).registerAs("thin_acacia_pillar")
    val thinDarkOakPillar = ThinPillarBlock(Blocks.DARK_OAK_PLANKS).registerAs("thin_dark_oak_pillar")
    val thinMangrovePillar = ThinPillarBlock(Blocks.MANGROVE_PLANKS).registerAs("thin_mangrove_pillar")
    val thinCherryPillar = ThinPillarBlock(Blocks.CHERRY_PLANKS).registerAs("thin_cherry_pillar")
    val thinBambooPillar = ThinPillarBlock(Blocks.BAMBOO_PLANKS).registerAs("thin_bamboo_pillar")
    val thinBambooMosaicPillar = ThinPillarBlock(Blocks.BAMBOO_MOSAIC).registerAs("thin_bamboo_mosaic_pillar")
    val thinCrimsonPillar = ThinPillarBlock(Blocks.CRIMSON_PLANKS).registerAs("thin_crimson_pillar")
    val thinWarpedPillar = ThinPillarBlock(Blocks.WARPED_PLANKS).registerAs("thin_warped_pillar")

    //Thick plank pillars
    val thickOakPillar = ThickPillarBlock(Blocks.OAK_PLANKS).registerAs("thick_oak_pillar")
    val thickSprucePillar = ThickPillarBlock(Blocks.SPRUCE_PLANKS).registerAs("thick_spruce_pillar")
    val thickBirchPillar = ThickPillarBlock(Blocks.BIRCH_PLANKS).registerAs("thick_birch_pillar")
    val thickJunglePillar = ThickPillarBlock(Blocks.JUNGLE_PLANKS).registerAs("thick_jungle_pillar")
    val thickAcaciaPillar = ThickPillarBlock(Blocks.ACACIA_PLANKS).registerAs("thick_acacia_pillar")
    val thickDarkOakPillar = ThickPillarBlock(Blocks.DARK_OAK_PLANKS).registerAs("thick_dark_oak_pillar")
    val thickMangrovePillar = ThickPillarBlock(Blocks.MANGROVE_PLANKS).registerAs("thick_mangrove_pillar")
    val thickCherryPillar = ThickPillarBlock(Blocks.CHERRY_PLANKS).registerAs("thick_cherry_pillar")
    val thickBambooPillar = ThickPillarBlock(Blocks.BAMBOO_PLANKS).registerAs("thick_bamboo_pillar")
    val thickBambooMosaicPillar = ThickPillarBlock(Blocks.BAMBOO_MOSAIC).registerAs("thick_bamboo_mosaic_pillar")
    val thickCrimsonPillar = ThickPillarBlock(Blocks.CRIMSON_PLANKS).registerAs("thick_crimson_pillar")
    val thickWarpedPillar = ThickPillarBlock(Blocks.WARPED_PLANKS).registerAs("thick_warped_pillar")

    //Walls
    val oakWall = CustomWallBlock(Blocks.OAK_PLANKS).registerAs("oak_wall")
    val spruceWall = CustomWallBlock(Blocks.SPRUCE_PLANKS).registerAs("spruce_wall")
    val birchWall = CustomWallBlock(Blocks.BIRCH_PLANKS).registerAs("birch_wall")
    val jungleWall = CustomWallBlock(Blocks.JUNGLE_PLANKS).registerAs("jungle_wall")
    val acaciaWall = CustomWallBlock(Blocks.ACACIA_PLANKS).registerAs("acacia_wall")
    val darkOakWall = CustomWallBlock(Blocks.DARK_OAK_PLANKS).registerAs("dark_oak_wall")
    val mangroveWall = CustomWallBlock(Blocks.MANGROVE_PLANKS).registerAs("mangrove_wall")
    val cherryWall = CustomWallBlock(Blocks.CHERRY_PLANKS).registerAs("cherry_wall")
    val bambooWall = CustomWallBlock(Blocks.BAMBOO_PLANKS).registerAs("bamboo_wall")
    val bambooMosaicWall = CustomWallBlock(Blocks.BAMBOO_MOSAIC).registerAs("bamboo_mosaic_wall")
    val crimsonWall = CustomWallBlock(Blocks.CRIMSON_PLANKS).registerAs("crimson_wall")
    val warpedWall = CustomWallBlock(Blocks.WARPED_PLANKS).registerAs("warped_wall")

    //Modern Fences
    val modernOakFence = ModernFenceBlock(Blocks.OAK_FENCE, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_LOG).registerAs("modern_oak_fence")
    val modernSpruceFence = ModernFenceBlock(Blocks.SPRUCE_FENCE, Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_LOG).registerAs("modern_spruce_fence")
    val modernBirchFence = ModernFenceBlock(Blocks.BIRCH_FENCE, Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_LOG).registerAs("modern_birch_fence")
    val modernJungleFence = ModernFenceBlock(Blocks.JUNGLE_FENCE, Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_LOG).registerAs("modern_jungle_fence")
    val modernAcaciaFence = ModernFenceBlock(Blocks.ACACIA_FENCE, Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_LOG).registerAs("modern_acacia_fence")
    val modernDarkOakFence = ModernFenceBlock(Blocks.DARK_OAK_FENCE, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_LOG).registerAs("modern_dark_oak_fence")
    val modernMangroveFence = ModernFenceBlock(Blocks.MANGROVE_FENCE, Blocks.STRIPPED_MANGROVE_LOG, Blocks.MANGROVE_LOG).registerAs("modern_mangrove_fence")
    val modernCherryFence = ModernFenceBlock(Blocks.CHERRY_FENCE, Blocks.STRIPPED_CHERRY_LOG, Blocks.CHERRY_LOG).registerAs("modern_cherry_fence")
    val modernBambooFence = ModernFenceBlock(Blocks.BAMBOO_FENCE, Blocks.STRIPPED_BAMBOO_BLOCK, Blocks.BAMBOO_BLOCK).registerAs("modern_bamboo_fence")
    val modernCrimsonFence = ModernFenceBlock(Blocks.CRIMSON_FENCE, Blocks.STRIPPED_CRIMSON_STEM, Blocks.CRIMSON_STEM).registerAs("modern_crimson_fence")
    val modernWarpedFence = ModernFenceBlock(Blocks.WARPED_FENCE, Blocks.STRIPPED_WARPED_STEM, Blocks.WARPED_STEM).registerAs("modern_warped_fence")

    //Modern Fence Gates
    val modernOakFenceGate = ModernFenceGateBlock(Blocks.OAK_FENCE_GATE as FenceGateBlock, Blocks.STRIPPED_OAK_LOG).registerAs("modern_oak_fence_gate")
    val modernSpruceFenceGate = ModernFenceGateBlock(Blocks.SPRUCE_FENCE_GATE as FenceGateBlock, Blocks.STRIPPED_SPRUCE_LOG).registerAs("modern_spruce_fence_gate")
    val modernBirchFenceGate = ModernFenceGateBlock(Blocks.BIRCH_FENCE_GATE as FenceGateBlock, Blocks.STRIPPED_BIRCH_LOG).registerAs("modern_birch_fence_gate")
    val modernJungleFenceGate = ModernFenceGateBlock(Blocks.JUNGLE_FENCE_GATE as FenceGateBlock, Blocks.STRIPPED_JUNGLE_LOG).registerAs("modern_jungle_fence_gate")
    val modernAcaciaFenceGate = ModernFenceGateBlock(Blocks.ACACIA_FENCE_GATE as FenceGateBlock, Blocks.STRIPPED_ACACIA_LOG).registerAs("modern_acacia_fence_gate")
    val modernDarkOakFenceGate = ModernFenceGateBlock(Blocks.DARK_OAK_FENCE_GATE as FenceGateBlock, Blocks.STRIPPED_DARK_OAK_LOG).registerAs("modern_dark_oak_fence_gate")
    val modernMangroveFenceGate = ModernFenceGateBlock(Blocks.MANGROVE_FENCE_GATE as FenceGateBlock, Blocks.STRIPPED_MANGROVE_LOG).registerAs("modern_mangrove_fence_gate")
    val modernCherryFenceGate = ModernFenceGateBlock(Blocks.CHERRY_FENCE_GATE as FenceGateBlock, Blocks.STRIPPED_CHERRY_LOG).registerAs("modern_cherry_fence_gate")
    val modernBambooFenceGate = ModernFenceGateBlock(Blocks.BAMBOO_FENCE_GATE as FenceGateBlock, Blocks.STRIPPED_BAMBOO_BLOCK).registerAs("modern_bamboo_fence_gate")
    val modernCrimsonFenceGate = ModernFenceGateBlock(Blocks.CRIMSON_FENCE_GATE as FenceGateBlock, Blocks.STRIPPED_CRIMSON_STEM).registerAs("modern_crimson_fence_gate")
    val modernWarpedFenceGate = ModernFenceGateBlock(Blocks.WARPED_FENCE_GATE as FenceGateBlock, Blocks.STRIPPED_WARPED_STEM).registerAs("modern_warped_fence_gate")

    //Plank Ladders
    val oakPlankLadder = PlankLadderBlock(Blocks.OAK_PLANKS).registerAs("oak_plank_ladder")
    val sprucePlankLadder = PlankLadderBlock(Blocks.SPRUCE_PLANKS).registerAs("spruce_plank_ladder")
    val birchPlankLadder = PlankLadderBlock(Blocks.BIRCH_PLANKS).registerAs("birch_plank_ladder")
    val junglePlankLadder = PlankLadderBlock(Blocks.JUNGLE_PLANKS).registerAs("jungle_plank_ladder")
    val acaciaPlankLadder = PlankLadderBlock(Blocks.ACACIA_PLANKS).registerAs("acacia_plank_ladder")
    val darkOakPlankLadder = PlankLadderBlock(Blocks.DARK_OAK_PLANKS).registerAs("dark_oak_plank_ladder")
    val mangrovePlankLadder = PlankLadderBlock(Blocks.MANGROVE_PLANKS).registerAs("mangrove_plank_ladder")
    val cherryPlankLadder = PlankLadderBlock(Blocks.CHERRY_PLANKS).registerAs("cherry_plank_ladder")
    val bambooPlankLadder = PlankLadderBlock(Blocks.BAMBOO_PLANKS).registerAs("bamboo_plank_ladder")
    val bambooMosaicPlankLadder = PlankLadderBlock(Blocks.BAMBOO_MOSAIC).registerAs("bamboo_mosaic_ladder")
    val crimsonPlankLadder = PlankLadderBlock(Blocks.CRIMSON_PLANKS).registerAs("crimson_plank_ladder")
    val warpedPlankLadder = PlankLadderBlock(Blocks.WARPED_PLANKS).registerAs("warped_plank_ladder")

    /*---------------End Outside Stuff----------------*/

    /*---------------Living Room Stuff----------------*/
    //Plank Tables
    val oakPlankTable = TableBlock(Blocks.OAK_PLANKS, Blocks.OAK_PLANKS).registerAs("oak_plank_table")
    val sprucePlankTable = TableBlock(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_PLANKS).registerAs("spruce_plank_table")
    val birchPlankTable = TableBlock(Blocks.BIRCH_PLANKS, Blocks.BIRCH_PLANKS).registerAs("birch_plank_table")
    val junglePlankTable = TableBlock(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_PLANKS).registerAs("jungle_plank_table")
    val acaciaPlankTable = TableBlock(Blocks.ACACIA_PLANKS, Blocks.ACACIA_PLANKS).registerAs("acacia_plank_table")
    val darkOakPlankTable = TableBlock(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_PLANKS).registerAs("dark_oak_plank_table")
    val mangrovePlankTable = TableBlock(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_PLANKS).registerAs("mangrove_plank_table")
    val cherryPlankTable = TableBlock(Blocks.CHERRY_PLANKS, Blocks.CHERRY_PLANKS).registerAs("cherry_plank_table")
    val bambooPlankTable = TableBlock(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_PLANKS).registerAs("bamboo_plank_table")
    val bambooMosaicTable = TableBlock(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_MOSAIC).registerAs("bamboo_mosaic_table")
    val crimsonPlankTable = TableBlock(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_PLANKS).registerAs("crimson_plank_table")
    val warpedPlankTable = TableBlock(Blocks.WARPED_PLANKS, Blocks.WARPED_PLANKS).registerAs("warped_plank_table")

    //Wood Tables
    val oakTable = TableBlock(Blocks.OAK_PLANKS, Blocks.OAK_LOG).registerAs("oak_table")
    val spruceTable = TableBlock(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_LOG).registerAs("spruce_table")
    val birchTable = TableBlock(Blocks.BIRCH_PLANKS, Blocks.BIRCH_LOG).registerAs("birch_table")
    val jungleTable = TableBlock(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_LOG).registerAs("jungle_table")
    val acaciaTable = TableBlock(Blocks.ACACIA_PLANKS, Blocks.ACACIA_LOG).registerAs("acacia_table")
    val darkOakTable = TableBlock(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_LOG).registerAs("dark_oak_table")
    val mangroveTable = TableBlock(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_LOG).registerAs("mangrove_table")
    val cherryTable = TableBlock(Blocks.CHERRY_PLANKS, Blocks.CHERRY_LOG).registerAs("cherry_table")
    val bambooTable = TableBlock(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_BLOCK).registerAs("bamboo_table")
    val crimsonTable = TableBlock(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STEM).registerAs("crimson_table")
    val warpedTable = TableBlock(Blocks.WARPED_PLANKS, Blocks.WARPED_STEM).registerAs("warped_table")

    //Stripped Tables
    val strippedOakTable = TableBlock(Blocks.OAK_PLANKS, Blocks.STRIPPED_OAK_LOG).registerAs("stripped_oak_table")
    val strippedSpruceTable = TableBlock(Blocks.SPRUCE_PLANKS, Blocks.STRIPPED_SPRUCE_LOG).registerAs("stripped_spruce_table")
    val strippedBirchTable = TableBlock(Blocks.BIRCH_PLANKS, Blocks.STRIPPED_BIRCH_LOG).registerAs("stripped_birch_table")
    val strippedJungleTable = TableBlock(Blocks.JUNGLE_PLANKS, Blocks.STRIPPED_JUNGLE_LOG).registerAs("stripped_jungle_table")
    val strippedAcaciaTable = TableBlock(Blocks.ACACIA_PLANKS, Blocks.STRIPPED_ACACIA_LOG).registerAs("stripped_acacia_table")
    val strippedDarkOakTable = TableBlock(Blocks.DARK_OAK_PLANKS, Blocks.STRIPPED_DARK_OAK_LOG).registerAs("stripped_dark_oak_table")
    val strippedMangroveTable = TableBlock(Blocks.MANGROVE_PLANKS, Blocks.STRIPPED_MANGROVE_LOG).registerAs("stripped_mangrove_table")
    val strippedCherryTable = TableBlock(Blocks.CHERRY_PLANKS, Blocks.STRIPPED_CHERRY_LOG).registerAs("stripped_cherry_table")
    val strippedBambooTable = TableBlock(Blocks.BAMBOO_PLANKS, Blocks.STRIPPED_BAMBOO_BLOCK).registerAs("stripped_bamboo_table")
    val strippedCrimsonTable = TableBlock(Blocks.CRIMSON_PLANKS, Blocks.STRIPPED_CRIMSON_STEM).registerAs("stripped_crimson_table")
    val strippedWarpedTable = TableBlock(Blocks.WARPED_PLANKS, Blocks.STRIPPED_WARPED_STEM).registerAs("stripped_warped_table")

    //Plank Coffee Tables
    val oakPlankCoffeeTable = CoffeeTableBlock(Blocks.OAK_PLANKS, Blocks.OAK_PLANKS).registerAs("oak_plank_coffee_table")
    val sprucePlankCoffeeTable = CoffeeTableBlock(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_PLANKS).registerAs("spruce_plank_coffee_table")
    val birchPlankCoffeeTable = CoffeeTableBlock(Blocks.BIRCH_PLANKS, Blocks.BIRCH_PLANKS).registerAs("birch_plank_coffee_table")
    val junglePlankCoffeeTable = CoffeeTableBlock(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_PLANKS).registerAs("jungle_plank_coffee_table")
    val acaciaPlankCoffeeTable = CoffeeTableBlock(Blocks.ACACIA_PLANKS, Blocks.ACACIA_PLANKS).registerAs("acacia_plank_coffee_table")
    val darkOakPlankCoffeeTable = CoffeeTableBlock(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_PLANKS).registerAs("dark_oak_plank_coffee_table")
    val mangrovePlankCoffeeTable = CoffeeTableBlock(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_PLANKS).registerAs("mangrove_plank_coffee_table")
    val cherryPlankCoffeeTable = CoffeeTableBlock(Blocks.CHERRY_PLANKS, Blocks.CHERRY_PLANKS).registerAs("cherry_plank_coffee_table")
    val bambooPlankCoffeeTable = CoffeeTableBlock(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_PLANKS).registerAs("bamboo_plank_coffee_table")
    val bambooMosaicCoffeeTable = CoffeeTableBlock(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_MOSAIC).registerAs("bamboo_mosaic_coffee_table")
    val crimsonPlankCoffeeTable = CoffeeTableBlock(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_PLANKS).registerAs("crimson_plank_coffee_table")
    val warpedPlankCoffeeTable = CoffeeTableBlock(Blocks.WARPED_PLANKS, Blocks.WARPED_PLANKS).registerAs("warped_plank_coffee_table")

    //Coffee tables
    val oakCoffeeTable = CoffeeTableBlock(Blocks.OAK_PLANKS, Blocks.OAK_LOG).registerAs("oak_coffee_table")
    val spruceCoffeeTable = CoffeeTableBlock(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_LOG).registerAs("spruce_coffee_table")
    val birchCoffeeTable = CoffeeTableBlock(Blocks.BIRCH_PLANKS, Blocks.BIRCH_LOG).registerAs("birch_coffee_table")
    val jungleCoffeeTable = CoffeeTableBlock(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_LOG).registerAs("jungle_coffee_table")
    val acaciaCoffeeTable = CoffeeTableBlock(Blocks.ACACIA_PLANKS, Blocks.ACACIA_LOG).registerAs("acacia_coffee_table")
    val darkOakCoffeeTable = CoffeeTableBlock(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_LOG).registerAs("dark_oak_coffee_table")
    val mangroveCoffeeTable = CoffeeTableBlock(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_LOG).registerAs("mangrove_coffee_table")
    val cherryCoffeeTable = CoffeeTableBlock(Blocks.CHERRY_PLANKS, Blocks.CHERRY_LOG).registerAs("cherry_coffee_table")
    val bambooCoffeeTable = CoffeeTableBlock(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_BLOCK).registerAs("bamboo_coffee_table")
    val crimsonCoffeeTable = CoffeeTableBlock(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STEM).registerAs("crimson_coffee_table")
    val warpedCoffeeTable = CoffeeTableBlock(Blocks.WARPED_PLANKS, Blocks.WARPED_STEM).registerAs("warped_coffee_table")

    //Stripped Coffee tables
    val strippedOakCoffeeTable = CoffeeTableBlock(Blocks.OAK_PLANKS, Blocks.STRIPPED_OAK_LOG).registerAs("stripped_oak_coffee_table")
    val strippedSpruceCoffeeTable = CoffeeTableBlock(Blocks.SPRUCE_PLANKS, Blocks.STRIPPED_SPRUCE_LOG).registerAs("stripped_spruce_coffee_table")
    val strippedBirchCoffeeTable = CoffeeTableBlock(Blocks.BIRCH_PLANKS, Blocks.STRIPPED_BIRCH_LOG).registerAs("stripped_birch_coffee_table")
    val strippedJungleCoffeeTable = CoffeeTableBlock(Blocks.JUNGLE_PLANKS, Blocks.STRIPPED_JUNGLE_LOG).registerAs("stripped_jungle_coffee_table")
    val strippedAcaciaCoffeeTable = CoffeeTableBlock(Blocks.ACACIA_PLANKS, Blocks.STRIPPED_ACACIA_LOG).registerAs("stripped_acacia_coffee_table")
    val strippedDarkOakCoffeeTable = CoffeeTableBlock(Blocks.DARK_OAK_PLANKS, Blocks.STRIPPED_DARK_OAK_LOG).registerAs("stripped_dark_oak_coffee_table")
    val strippedMangroveCoffeeTable = CoffeeTableBlock(Blocks.MANGROVE_PLANKS, Blocks.STRIPPED_MANGROVE_LOG).registerAs("stripped_mangrove_coffee_table")
    val strippedCherryCoffeeTable = CoffeeTableBlock(Blocks.CHERRY_PLANKS, Blocks.STRIPPED_CHERRY_LOG).registerAs("stripped_cherry_coffee_table")
    val strippedBambooCoffeeTable = CoffeeTableBlock(Blocks.BAMBOO_PLANKS, Blocks.STRIPPED_BAMBOO_BLOCK).registerAs("stripped_bamboo_coffee_table")
    val strippedCrimsonCoffeeTable = CoffeeTableBlock(Blocks.CRIMSON_PLANKS, Blocks.STRIPPED_CRIMSON_STEM).registerAs("stripped_crimson_coffee_table")
    val strippedWarpedCoffeeTable = CoffeeTableBlock(Blocks.WARPED_PLANKS, Blocks.STRIPPED_WARPED_STEM).registerAs("stripped_warped_coffee_table")

    //Thin Bookshelves
    val oakBookshelf = ThinBookshelfBlock(Blocks.OAK_PLANKS).registerAs("oak_bookshelf")
    val spruceBookshelf = ThinBookshelfBlock(Blocks.SPRUCE_PLANKS).registerAs("spruce_bookshelf")
    val birchBookshelf = ThinBookshelfBlock(Blocks.BIRCH_PLANKS).registerAs("birch_bookshelf")
    val jungleBookshelf = ThinBookshelfBlock(Blocks.JUNGLE_PLANKS).registerAs("jungle_bookshelf")
    val acaciaBookshelf = ThinBookshelfBlock(Blocks.ACACIA_PLANKS).registerAs("acacia_bookshelf")
    val darkOakBookshelf = ThinBookshelfBlock(Blocks.DARK_OAK_PLANKS).registerAs("dark_oak_bookshelf")
    val mangroveBookshelf = ThinBookshelfBlock(Blocks.MANGROVE_PLANKS).registerAs("mangrove_bookshelf")
    val cherryBookshelf = ThinBookshelfBlock(Blocks.CHERRY_PLANKS).registerAs("cherry_bookshelf")
    val bambooBookshelf = ThinBookshelfBlock(Blocks.BAMBOO_PLANKS).registerAs("bamboo_bookshelf")
    val bambooMosaicBookshelf = ThinBookshelfBlock(Blocks.BAMBOO_MOSAIC).registerAs("bamboo_mosaic_bookshelf")
    val crimsonBookshelf = ThinBookshelfBlock(Blocks.CRIMSON_PLANKS).registerAs("crimson_bookshelf")
    val warpedBookshelf = ThinBookshelfBlock(Blocks.WARPED_PLANKS).registerAs("warped_bookshelf")

    //Wood Floor Coverings
    val oakFloorCovering = FloorCoveringBlock(Blocks.OAK_PLANKS).registerAs("oak_floor_covering")
    val spruceFloorCovering = FloorCoveringBlock(Blocks.SPRUCE_PLANKS).registerAs("spruce_floor_covering")
    val birchFloorCovering = FloorCoveringBlock(Blocks.BIRCH_PLANKS).registerAs("birch_floor_covering")
    val jungleFloorCovering = FloorCoveringBlock(Blocks.JUNGLE_PLANKS).registerAs("jungle_floor_covering")
    val acaciaFloorCovering = FloorCoveringBlock(Blocks.ACACIA_PLANKS).registerAs("acacia_floor_covering")
    val darkOakFloorCovering = FloorCoveringBlock(Blocks.DARK_OAK_PLANKS).registerAs("dark_oak_floor_covering")
    val mangroveFloorCovering = FloorCoveringBlock(Blocks.MANGROVE_PLANKS).registerAs("mangrove_floor_covering")
    val cherryFloorCovering = FloorCoveringBlock(Blocks.CHERRY_PLANKS).registerAs("cherry_floor_covering")
    val bambooFloorCovering = FloorCoveringBlock(Blocks.BAMBOO_PLANKS).registerAs("bamboo_floor_covering")
    val bambooMosaicFloorCovering = FloorCoveringBlock(Blocks.BAMBOO_MOSAIC).registerAs("bamboo_mosaic_floor_covering")
    val crimsonFloorCovering = FloorCoveringBlock(Blocks.CRIMSON_PLANKS).registerAs("crimson_floor_covering")
    val warpedFloorCovering = FloorCoveringBlock(Blocks.WARPED_PLANKS).registerAs("warped_floor_covering")

    /*---------------Kitchen Stuff----------------*/
    //Kitchen counters
    val oakKitchenCounter = KitchenCounterBlock(Blocks.OAK_PLANKS, Blocks.OAK_LOG).registerAs("oak_kitchen_counter")
    val spruceKitchenCounter = KitchenCounterBlock(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_LOG).registerAs("spruce_kitchen_counter")
    val birchKitchenCounter = KitchenCounterBlock(Blocks.BIRCH_PLANKS, Blocks.BIRCH_LOG).registerAs("birch_kitchen_counter")
    val jungleKitchenCounter = KitchenCounterBlock(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_LOG).registerAs("jungle_kitchen_counter")
    val acaciaKitchenCounter = KitchenCounterBlock(Blocks.ACACIA_PLANKS, Blocks.ACACIA_LOG).registerAs("acacia_kitchen_counter")
    val darkOakKitchenCounter = KitchenCounterBlock(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_LOG).registerAs("dark_oak_kitchen_counter")
    val mangroveKitchenCounter = KitchenCounterBlock(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_LOG).registerAs("mangrove_kitchen_counter")
    val cherryKitchenCounter = KitchenCounterBlock(Blocks.CHERRY_PLANKS, Blocks.CHERRY_LOG).registerAs("cherry_kitchen_counter")
    val bambooKitchenCounter = KitchenCounterBlock(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_BLOCK).registerAs("bamboo_kitchen_counter")
    val bambooMosaicKitchenCounter = KitchenCounterBlock(Blocks.BAMBOO_MOSAIC, Blocks.BAMBOO_BLOCK).registerAs("bamboo_mosaic_kitchen_counter")
    val crimsonKitchenCounter = KitchenCounterBlock(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STEM).registerAs("crimson_kitchen_counter")
    val warpedKitchenCounter = KitchenCounterBlock(Blocks.WARPED_PLANKS, Blocks.WARPED_STEM).registerAs("warped_kitchen_counter")

    //Stripped Kitchen counters
    val strippedOakKitchenCounter = KitchenCounterBlock(Blocks.OAK_PLANKS, Blocks.STRIPPED_OAK_LOG).registerAs("stripped_oak_kitchen_counter")
    val strippedSpruceKitchenCounter = KitchenCounterBlock(Blocks.SPRUCE_PLANKS, Blocks.STRIPPED_SPRUCE_LOG).registerAs("stripped_spruce_kitchen_counter")
    val strippedBirchKitchenCounter = KitchenCounterBlock(Blocks.BIRCH_PLANKS, Blocks.STRIPPED_BIRCH_LOG).registerAs("stripped_birch_kitchen_counter")
    val strippedJungleKitchenCounter = KitchenCounterBlock(Blocks.JUNGLE_PLANKS, Blocks.STRIPPED_JUNGLE_LOG).registerAs("stripped_jungle_kitchen_counter")
    val strippedAcaciaKitchenCounter = KitchenCounterBlock(Blocks.ACACIA_PLANKS, Blocks.STRIPPED_ACACIA_LOG).registerAs("stripped_acacia_kitchen_counter")
    val strippedDarkOakKitchenCounter = KitchenCounterBlock(Blocks.DARK_OAK_PLANKS, Blocks.STRIPPED_DARK_OAK_LOG).registerAs("stripped_dark_oak_kitchen_counter")
    val strippedMangroveKitchenCounter = KitchenCounterBlock(Blocks.MANGROVE_PLANKS, Blocks.STRIPPED_MANGROVE_LOG).registerAs("stripped_mangrove_kitchen_counter")
    val strippedCherryKitchenCounter = KitchenCounterBlock(Blocks.CHERRY_PLANKS, Blocks.STRIPPED_CHERRY_LOG).registerAs("stripped_cherry_kitchen_counter")
    val strippedBambooKitchenCounter = KitchenCounterBlock(Blocks.BAMBOO_PLANKS, Blocks.STRIPPED_BAMBOO_BLOCK).registerAs("stripped_bamboo_kitchen_counter")
    val strippedBambooMosaicKitchenCounter = KitchenCounterBlock(Blocks.BAMBOO_MOSAIC, Blocks.STRIPPED_BAMBOO_BLOCK).registerAs("stripped_bamboo_mosaic_kitchen_counter")
    val strippedCrimsonKitchenCounter = KitchenCounterBlock(Blocks.CRIMSON_PLANKS, Blocks.STRIPPED_CRIMSON_STEM).registerAs("stripped_crimson_kitchen_counter")
    val strippedWarpedKitchenCounter = KitchenCounterBlock(Blocks.WARPED_PLANKS, Blocks.STRIPPED_WARPED_STEM).registerAs("stripped_warped_kitchen_counter")

    //Kitchen Cabinets
    val oakKitchenCabinet = KitchenCabinetBlock(Blocks.OAK_PLANKS, Blocks.OAK_LOG).registerAs("oak_kitchen_cabinet")
    val spruceKitchenCabinet = KitchenCabinetBlock(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_LOG).registerAs("spruce_kitchen_cabinet")
    val birchKitchenCabinet = KitchenCabinetBlock(Blocks.BIRCH_PLANKS, Blocks.BIRCH_LOG).registerAs("birch_kitchen_cabinet")
    val jungleKitchenCabinet = KitchenCabinetBlock(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_LOG).registerAs("jungle_kitchen_cabinet")
    val acaciaKitchenCabinet = KitchenCabinetBlock(Blocks.ACACIA_PLANKS, Blocks.ACACIA_LOG).registerAs("acacia_kitchen_cabinet")
    val darkOakKitchenCabinet = KitchenCabinetBlock(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_LOG).registerAs("dark_oak_kitchen_cabinet")
    val mangroveKitchenCabinet = KitchenCabinetBlock(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_LOG).registerAs("mangrove_kitchen_cabinet")
    val cherryKitchenCabinet = KitchenCabinetBlock(Blocks.CHERRY_PLANKS, Blocks.CHERRY_LOG).registerAs("cherry_kitchen_cabinet")
    val bambooKitchenCabinet = KitchenCabinetBlock(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_BLOCK).registerAs("bamboo_kitchen_cabinet")
    val bambooMosaicKitchenCabinet = KitchenCabinetBlock(Blocks.BAMBOO_MOSAIC, Blocks.BAMBOO_BLOCK).registerAs("bamboo_mosaic_kitchen_cabinet")
    val crimsonKitchenCabinet = KitchenCabinetBlock(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STEM).registerAs("crimson_kitchen_cabinet")
    val warpedKitchenCabinet = KitchenCabinetBlock(Blocks.WARPED_PLANKS, Blocks.WARPED_STEM).registerAs("warped_kitchen_cabinet")

    //Stripped Cabinets
    val strippedOakKitchenCabinet = KitchenCabinetBlock(Blocks.OAK_PLANKS, Blocks.STRIPPED_OAK_LOG).registerAs("stripped_oak_kitchen_cabinet")
    val strippedSpruceKitchenCabinet = KitchenCabinetBlock(Blocks.SPRUCE_PLANKS, Blocks.STRIPPED_SPRUCE_LOG).registerAs("stripped_spruce_kitchen_cabinet")
    val strippedBirchKitchenCabinet = KitchenCabinetBlock(Blocks.BIRCH_PLANKS, Blocks.STRIPPED_BIRCH_LOG).registerAs("stripped_birch_kitchen_cabinet")
    val strippedJungleKitchenCabinet = KitchenCabinetBlock(Blocks.JUNGLE_PLANKS, Blocks.STRIPPED_JUNGLE_LOG).registerAs("stripped_jungle_kitchen_cabinet")
    val strippedAcaciaKitchenCabinet = KitchenCabinetBlock(Blocks.ACACIA_PLANKS, Blocks.STRIPPED_ACACIA_LOG).registerAs("stripped_acacia_kitchen_cabinet")
    val strippedDarkOakKitchenCabinet = KitchenCabinetBlock(Blocks.DARK_OAK_PLANKS, Blocks.STRIPPED_DARK_OAK_LOG).registerAs("stripped_dark_oak_kitchen_cabinet")
    val strippedMangroveKitchenCabinet = KitchenCabinetBlock(Blocks.MANGROVE_PLANKS, Blocks.STRIPPED_MANGROVE_LOG).registerAs("stripped_mangrove_kitchen_cabinet")
    val strippedCherryKitchenCabinet = KitchenCabinetBlock(Blocks.CHERRY_PLANKS, Blocks.STRIPPED_CHERRY_LOG).registerAs("stripped_cherry_kitchen_cabinet")
    val strippedBambooKitchenCabinet = KitchenCabinetBlock(Blocks.BAMBOO_PLANKS, Blocks.STRIPPED_BAMBOO_BLOCK).registerAs("stripped_bamboo_kitchen_cabinet")
    val strippedBambooMosaicKitchenCabinet = KitchenCabinetBlock(Blocks.BAMBOO_MOSAIC, Blocks.STRIPPED_BAMBOO_BLOCK).registerAs("stripped_bamboo_mosaic_kitchen_cabinet")
    val strippedCrimsonKitchenCabinet = KitchenCabinetBlock(Blocks.CRIMSON_PLANKS, Blocks.STRIPPED_CRIMSON_STEM).registerAs("stripped_crimson_kitchen_cabinet")
    val strippedWarpedKitchenCabinet = KitchenCabinetBlock(Blocks.WARPED_PLANKS, Blocks.STRIPPED_WARPED_STEM).registerAs("stripped_warped_kitchen_cabinet")

    /*---------------End Kitchen Stuff----------------*/

    /*---------------Bedroom Stuff----------------*/



    /*---------------End Bedroom Stuff----------------*/

    /*---------------Misc Stuff----------------*/


    /*---------------End Misc Stuff----------------*/

    fun Block.getItemModelId(): Identifier = id.withPrefixedPath("item/")
    inline val Block.id: Identifier
        get() = Registries.BLOCK.getId(this)
    inline val Block.modelId: Identifier
        get() = ModelIds.getBlockModelId(this)
    inline val Block.textureId: Identifier
        get() = TextureMap.getId(this)

    inline val Block.woodType: WoodType
        get() {
            lateinit var type: WoodType
            WoodType.stream().forEach {
                if (this.id.path.contains(it.name)) {
                    type = it
                    if (!it.name.contains("dark") && !this.id.path.contains("dark")) {
                        return@forEach
                    }
                }
            }
            return type
        }
    val Block.isStripped: Boolean
        get() = id.path.contains("stripped")
    val Block.isPlank
        get() = id.path.contains("plank")
    private infix fun Block.registerAs(id: String): Block = registerAs(id.asWamId())
    private infix fun Block.registerAs(identifier: Identifier) =
        Registry.register(Registries.BLOCK, identifier, this).also {
            registries += it
            Registry.register(Registries.ITEM, identifier, BlockItem(it, FabricItemSettings()))
        }

    fun ModBlocks.register() {
        WoodenAccentsMod.logger.info("Registering $this for mod: ${WoodenAccentsMod.MOD_ID}")
    }

}

