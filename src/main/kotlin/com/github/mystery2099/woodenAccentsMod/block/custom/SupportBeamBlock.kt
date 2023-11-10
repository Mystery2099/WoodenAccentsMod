package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.rotation.VoxelRotation.flip
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateRight
import com.github.mystery2099.woodenAccentsMod.block.itemModelId
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags.contains
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.customGroup
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.asBlockStateVariant
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.uvLock
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.withXRotationOf
import com.github.mystery2099.woodenAccentsMod.data.client.BlockStateVariantUtil.withYRotationOf
import com.github.mystery2099.woodenAccentsMod.util.WhenUtil
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.PillarBlock
import net.minecraft.block.SideShapeType
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.WorldAccess
import java.util.function.Consumer

/**
 * Support beam block
 *
 * @property baseBlock
 * @constructor Create empty Support beam block
 */
class SupportBeamBlock(val baseBlock: Block) : OmnidirectionalConnectingBlock(run {
    if (baseBlock !is PillarBlock) FabricBlockSettings.copyOf(baseBlock)
    else FabricBlockSettings.of(baseBlock.defaultState.material, baseBlock.defaultMapColor).apply {
        hardness(baseBlock.hardness)
        resistance(baseBlock.blastResistance)
        sounds(baseBlock.getSoundGroup(baseBlock.defaultState))
        if (baseBlock.requiredFeatures.contains(FeatureFlags.UPDATE_1_20)) {
            requires(FeatureFlags.UPDATE_1_20)
        }
    }
}), CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {
    override val centerShape: VoxelShape = VoxelAssembly.createCuboidShape(6, 6, 6, 10, 10, 10)
    override val northShape: VoxelShape = VoxelAssembly.createCuboidShape(6, 6, 0, 10, 10, 6)
    override val eastShape: VoxelShape = northShape.rotateLeft()
    override val southShape: VoxelShape = northShape.flip()
    override val westShape: VoxelShape = northShape.rotateRight()
    override val upShape: VoxelShape = VoxelAssembly.createCuboidShape(6, 10, 6, 10, 16, 10)
    override val downShape: VoxelShape = upShape.offset(0.0, -10 / 16.0, 0.0)

    override val tag: TagKey<Block> = ModBlockTags.supportBeams
    override val itemGroup: CustomItemGroup = ModItemGroups.structuralElements

    private fun canConnect(pos: BlockPos, direction: Direction, world: WorldAccess): Boolean {
        val otherState = world.getBlockState(pos.offset(direction))
        return (otherState.isSideSolid(
            world,
            pos.offset(direction),
            direction.opposite,
            SideShapeType.CENTER
        ) || otherState in tag) && otherState !in BlockTags.FENCE_GATES
    }

    override fun canConnectNorthOf(pos: BlockPos, world: WorldAccess) = canConnect(pos, Direction.NORTH, world)

    override fun canConnectEastOf(pos: BlockPos, world: WorldAccess) = canConnect(pos, Direction.EAST, world)

    override fun canConnectSouthOf(pos: BlockPos, world: WorldAccess) = canConnect(pos, Direction.SOUTH, world)

    override fun canConnectWestOf(pos: BlockPos, world: WorldAccess) = canConnect(pos, Direction.WEST, world)

    override fun canConnectAbove(pos: BlockPos, world: WorldAccess) = canConnect(pos, Direction.UP, world)

    override fun canConnectBelow(pos: BlockPos, world: WorldAccess) = canConnect(pos, Direction.DOWN, world)

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val map = TextureMap.all(baseBlock)
        ModModels.supportBeamItem.upload(this.itemModelId, map, generator.modelCollector)
        val centerVariant =
            ModModels.supportBeamCenter.upload(this, map, generator.modelCollector).asBlockStateVariant()
        val downVariant =
            ModModels.supportBeamDown.upload(this, map, generator.modelCollector).asBlockStateVariant().uvLock()
        generator.blockStateCollector.accept(
            MultipartBlockStateSupplier.create(this).apply {
                val northVariant = downVariant.withXRotationOf(VariantSettings.Rotation.R270)
                mapOf(
                    When.anyOf(
                        WhenUtil.notNorth,
                        WhenUtil.notEast,
                        WhenUtil.notSouth,
                        WhenUtil.notWest,
                        WhenUtil.notUp,
                        WhenUtil.notDown
                    ) to centerVariant,
                    WhenUtil.north to northVariant,
                    WhenUtil.east to northVariant.withYRotationOf(VariantSettings.Rotation.R90),
                    WhenUtil.south to northVariant.withYRotationOf(VariantSettings.Rotation.R180),
                    WhenUtil.west to northVariant.withYRotationOf(VariantSettings.Rotation.R270),
                    WhenUtil.up to downVariant.withXRotationOf(VariantSettings.Rotation.R180),
                    WhenUtil.down to downVariant
                ).forEach{
                    with(it.key, it.value)
                }
            }
        )
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, this, 6).apply {
            input('0', Items.STICK)
            input('#', baseBlock)
            pattern("000")
            pattern("0#0")
            pattern("000")
            customGroup(this@SupportBeamBlock, "support_beams")
            requires(baseBlock)
            offerTo(exporter)
        }
    }
}