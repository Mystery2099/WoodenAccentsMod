package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.voxlib.combination.VoxelAssembly
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.textureId
import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.CrateBlockEntity
import com.github.mystery2099.woodenAccentsMod.data.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.data.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.customGroup
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeDataGen.Companion.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.*
import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroups
import com.github.mystery2099.woodenAccentsMod.util.BlockStateUtil.isOf
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.item.TooltipContext
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureKey
import net.minecraft.data.client.TextureMap
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.PiglinBrain
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.loot.entry.DynamicEntry
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.CopyNameLootFunction
import net.minecraft.loot.function.CopyNbtLootFunction
import net.minecraft.loot.function.SetContentsLootFunction
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.nbt.NbtElement
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.TagKey
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.function.Consumer

/**
 * Crate block
 *
 * @property baseBlock
 * @property edgeBlock
 * @constructor Create Crate block from the block settings of another block
 */
class CrateBlock(val baseBlock: Block, private val edgeBlock: Block) :
    BlockWithEntity(FabricBlockSettings.copyOf(baseBlock)),
    CustomBlockStateProvider, CustomItemGroupProvider, CustomTagProvider<Block>, CustomRecipeProvider,
    CustomBlockLootTableProvider {
    override val itemGroup: CustomItemGroup = ModItemGroups.miscellaneous
    override val tag: TagKey<Block> = ModBlockTags.crates

    @Deprecated("Deprecated in Java",
        ReplaceWith("shape", "com.mystery2099.wooden_accents_mod.block.custom.CrateBlock.Companion.shape")
    )
    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = shape

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = CrateBlockEntity(pos, state)

    @Deprecated("Deprecated in Java", ReplaceWith("BlockRenderType.MODEL", "net.minecraft.block.BlockRenderType"))
    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.MODEL

    @Deprecated("Deprecated in Java")
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS

        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is CrateBlockEntity) {
            player.openHandledScreen(blockEntity)
            PiglinBrain.onGuardedBlockInteracted(player, true)
        }

        return ActionResult.CONSUME
    }


    override fun onBreak(world: World, pos: BlockPos, state: BlockState?, player: PlayerEntity) {
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is CrateBlockEntity) {

            if (!world.isClient && player.isCreative && !blockEntity.isEmpty()) {

                val itemStack = ItemStack(this)
                blockEntity.setStackNbt(itemStack)
                if (blockEntity.hasCustomName()) {
                    itemStack.setCustomName(blockEntity.customName)
                }

                val itemEntity =
                    ItemEntity(world, pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, itemStack)
                itemEntity.setToDefaultPickupDelay()
                world.spawnEntity(itemEntity)

            } else blockEntity.checkLootInteraction(player)

        }
        super.onBreak(world, pos, state, player)
    }

    @Deprecated("Deprecated in Java")
    override fun getDroppedStacks(state: BlockState?, builder: LootContext.Builder): List<ItemStack> {
        var newBuilder: LootContext.Builder = builder
        val blockEntity = newBuilder.getNullable(LootContextParameters.BLOCK_ENTITY)
        if (blockEntity is CrateBlockEntity) {
            newBuilder = newBuilder.putDrop(
                contents
            ) { _: LootContext?, consumer: Consumer<ItemStack?> ->
                for (i in 0..<blockEntity.size()) {
                    consumer.accept(blockEntity.getStack(i))
                }
            }
        }
        return super.getDroppedStacks(state, newBuilder)
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        val blockEntity: BlockEntity? = world.getBlockEntity(pos)
        if (itemStack.hasCustomName() && blockEntity is CrateBlockEntity) {
            blockEntity.customName = itemStack.getName()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos?,
        newState: BlockState,
        moved: Boolean
    ) {
        if (state isOf newState.block) return
        if (world.getBlockEntity(pos) is CrateBlockEntity) world.updateComparators(pos, state.block)
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: BlockView?,
        tooltip: MutableList<Text>,
        options: TooltipContext
    ) {
        super.appendTooltip(stack, world, tooltip, options)
        BlockItem.getBlockEntityNbt(stack)?.let { nbtCompound ->
            if (nbtCompound.contains("LootTable", NbtElement.STRING_TYPE.toInt())) {
                tooltip.add(Text.literal("???????"))
            }
            if (nbtCompound.contains("Items", NbtElement.LIST_TYPE.toInt())) {
                val defaultedList = DefaultedList.ofSize(9, ItemStack.EMPTY)
                Inventories.readNbt(nbtCompound, defaultedList)
                var i : Short = 0
                var j : Short = 0
                for (itemStack in defaultedList) {
                    if (itemStack.isEmpty()) continue
                    ++j
                    if (i > 4) continue
                    ++i
                    itemStack.getName().copy()
                        .append(" x")
                        .append(itemStack.count.toString()).also {
                            tooltip.add(it)
                        }
                }
                if (j - i > 0) {
                    tooltip.add(Text.translatable("container.crate.more", j - i).formatted(Formatting.ITALIC))
                }
            }
        }
    }


    @Deprecated("Deprecated in Java", ReplaceWith("true"))
    override fun hasComparatorOutput(state: BlockState?): Boolean = true

    @Deprecated("Deprecated in Java", ReplaceWith(
        "ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos) as Inventory)",
        "net.minecraft.screen.ScreenHandler",
        "net.minecraft.inventory.Inventory"
    )
    )
    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos) as Inventory)
    }

    override fun getPickStack(world: BlockView, pos: BlockPos?, state: BlockState?): ItemStack {
        val itemStack = super.getPickStack(world, pos, state)
        world.getBlockEntity(pos, ModBlockEntities.crate).ifPresent { blockEntity: CrateBlockEntity ->
            blockEntity.setStackNbt(itemStack)
        }
        return itemStack
    }


    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        val map: TextureMap = TextureMap().put(TextureKey.INSIDE, baseBlock.textureId)
            .put(TextureKey.EDGE, edgeBlock.textureId)
            .put(TextureKey.CROSS, edgeBlock.textureId.run {
                if (this.path.contains("stripped")) this
                else this.withPath(this.path.replace("block/", "block/stripped_"))
            })
        ModModels.crate.upload(this, map, generator.modelCollector)
        generator.registerSimpleState(this)
    }

    override fun offerRecipeTo(exporter: Consumer<RecipeJsonProvider>) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, this).apply {
            input('n', baseBlock)
            input('t', edgeBlock)
            input('u', Items.CHEST)
            pattern("tnt")
            pattern("nun")
            pattern("tnt")
            customGroup(this@CrateBlock, "crates")
            requires(Items.CHEST)
            offerTo(exporter)
        }
    }

    override fun getLootTableBuilder(provider: FabricBlockLootTableProvider): LootTable.Builder {
        return LootTable.builder().pool(
            provider.addSurvivesExplosionCondition(
                this, LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(
                    ItemEntry.builder(this)
                        .apply(CopyNameLootFunction.builder(CopyNameLootFunction.Source.BLOCK_ENTITY))
                ).apply(
                    CopyNbtLootFunction.builder(ContextLootNbtProvider.BLOCK_ENTITY)
                        .withOperation("Lock", "BlockEntityTag.Lock")
                        .withOperation("LootTable", "BlockEntityTag.LootTable")
                        .withOperation("LootTableSeed", "BlockEntityTag.LootTableSeed")
                ).apply(
                    SetContentsLootFunction.builder(ModBlockEntities.crate)
                        .withEntry(DynamicEntry.builder(contents))
                )
            )
        )
    }

    companion object {
        val shape = VoxelAssembly.union(
            VoxelAssembly.createCuboidShape(0, 0, 2, 2, 2, 14),
            VoxelAssembly.createCuboidShape(14, 0, 2, 16, 2, 14),
            VoxelAssembly.createCuboidShape(2, 0, 0, 14, 2, 2),
            VoxelAssembly.createCuboidShape(2, 0, 14, 14, 2, 16),
            VoxelAssembly.createCuboidShape(0, 14, 2, 2, 16, 14),
            VoxelAssembly.createCuboidShape(14, 14, 2, 16, 16, 14),
            VoxelAssembly.createCuboidShape(2, 14, 0, 14, 16, 2),
            VoxelAssembly.createCuboidShape(2, 14, 14, 14, 16, 16),
            VoxelAssembly.createCuboidShape(0, 0, 0, 2, 16, 2),
            VoxelAssembly.createCuboidShape(14, 0, 14, 16, 16, 16),
            VoxelAssembly.createCuboidShape(14, 0, 0, 16, 16, 2),
            VoxelAssembly.createCuboidShape(0, 0, 14, 2, 16, 16),
            VoxelAssembly.createCuboidShape(1, 1, 1, 15, 15, 15)
        )
        val contents = Identifier("contents")

    }
}