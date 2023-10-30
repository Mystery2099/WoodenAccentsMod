package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.voxlib.rotation.VoxelRotation.flip
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateLeft
import com.github.mystery2099.voxlib.rotation.VoxelRotation.rotateRight
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.itemModelId
import com.github.mystery2099.woodenAccentsMod.data.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.data.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomBlockStateProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomItemGroupProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomRecipeProvider
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.CustomTagProvider
import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.util.BlockStateVariantUtil.asBlockStateVariant
import com.github.mystery2099.woodenAccentsMod.util.BlockStateVariantUtil.withYRotationOf
import com.github.mystery2099.woodenAccentsMod.util.WhenUtil
import com.github.mystery2099.woodenAccentsMod.util.WhenUtil.and
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.data.client.*
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureFlags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import java.util.function.Consumer

/**
 * Thin bookshelf block
 *
 * @property baseBlock
 * @constructor Create empty Thin bookshelf block
 */
class ThinBookshelfBlock(val baseBlock: Block) :
    ChiseledBookshelfBlock(FabricBlockSettings.copyOf(baseBlock).requires(FeatureFlags.UPDATE_1_20)),
    CustomItemGroupProvider, CustomRecipeProvider, CustomTagProvider<Block>, CustomBlockStateProvider {

    override val tag: TagKey<Block> = ModBlockTags.thinBookshelves
    override val itemGroup: CustomItemGroup = ModItemGroups.decorations

    @Deprecated("Deprecated in Java")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = when (state.get(HorizontalFacingBlock.FACING)) {
        Direction.NORTH -> northShape
        Direction.EAST -> eastShape
        Direction.SOUTH -> southShape
        Direction.WEST -> westShape
        else -> VoxelShapes.fullCube()
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 2).apply {
            input('#', baseBlock)
            input('_', Ingredient.fromTag(ItemTags.WOODEN_SLABS))
            pattern("##")
            pattern("__")
            pattern("##")
            group("thin_bookshelves")
            requires(baseBlock)
            offerTo(exporter)
        }
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        MultipartBlockStateSupplier.create(this).apply {
            TextureMap.all(baseBlock).let { map ->
                ModModels.thinBookshelfItem.upload(this@ThinBookshelfBlock.itemModelId, map, generator.modelCollector)

                val bookshelfModel =
                    ModModels.thinBookshelfBlock.upload(this@ThinBookshelfBlock, map, generator.modelCollector)

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
                val slotVariants = Array<Array<BlockStateVariant>>(6) { i ->
                    Array(4) {
                        slotModels[i].asBlockStateVariant().withYRotationOf(VariantSettings.Rotation.entries[it])
                    }
                }

                for (i in directions.indices) {
                    with(directions[i], variants[i].withYRotationOf(VariantSettings.Rotation.entries[i]))
                    for (j in slotVariants.indices) {
                        with(
                            directions[i] and When.create().set(SLOT_OCCUPIED_PROPERTIES[j], true),
                            slotVariants[j][i]
                        )
                    }
                }
            }
        }.also { generator.blockStateCollector.accept(it) }
    }

    companion object {
        private val northShape = VoxelAssembly.createCuboidShape(0, 0, 11, 16, 16, 16)
        private val eastShape = northShape.rotateLeft()
        private val southShape = northShape.rotateRight()
        private val westShape = northShape.flip()
    }


}