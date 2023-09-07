package com.mystery2099.wooden_accents_mod.block.custom

import com.mystery2099.wooden_accents_mod.block.ModBlocks.itemModelId
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomBlockStateProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomItemGroupProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomRecipeProvider
import com.mystery2099.wooden_accents_mod.data.generation.interfaces.CustomTagProvider
import com.mystery2099.wooden_accents_mod.data.ModBlockTags
import com.mystery2099.wooden_accents_mod.data.ModModels
import com.mystery2099.wooden_accents_mod.data.generation.RecipeDataGen.Companion.requires
import com.mystery2099.wooden_accents_mod.item_group.CustomItemGroup
import com.mystery2099.wooden_accents_mod.item_group.ModItemGroups
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.asBlockStateVariant
import com.mystery2099.wooden_accents_mod.util.BlockStateVariantUtil.withYRotationOf
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.flipped
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedLeft
import com.mystery2099.wooden_accents_mod.util.VoxelShapeHelper.rotatedRight
import com.mystery2099.wooden_accents_mod.util.WhenUtil
import com.mystery2099.wooden_accents_mod.util.WhenUtil.and
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
import net.minecraft.world.BlockView
import java.util.function.Consumer

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
        Direction.EAST -> northShape.rotatedLeft
        Direction.SOUTH -> northShape.flipped
        Direction.WEST -> northShape.rotatedRight
        else -> super.getOutlineShape(state, world, pos, context)
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
                val slotVariants = Array(6) { i ->
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
        private val northShape = VoxelShapeHelper.createCuboidShape(0, 0, 11, 16, 16, 16)
    }


}