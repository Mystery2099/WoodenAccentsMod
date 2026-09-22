package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.block.BlockStateUtil.isOf
import com.github.mystery2099.woodenAccentsMod.block.entity.ModBlockEntities
import com.github.mystery2099.woodenAccentsMod.block.entity.custom.CrateBlockEntity
import com.github.mystery2099.woodenAccentsMod.block.textureId
import com.github.mystery2099.woodenAccentsMod.data.client.ModModels
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeUtil.customGroup
import com.github.mystery2099.woodenAccentsMod.data.generation.RecipeUtil.requires
import com.github.mystery2099.woodenAccentsMod.data.generation.interfaces.*
import com.github.mystery2099.woodenAccentsMod.item.group.ModItemGroup
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.piglin.PiglinAi
import net.minecraft.world.entity.player.Player
import net.minecraft.world.ContainerHelper
import net.minecraft.world.Container
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.entries.DynamicLoot
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction
import net.minecraft.world.level.storage.loot.functions.SetContainerContents
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.nbt.Tag
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.TagKey
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.ChatFormatting
import net.minecraft.resources.ResourceLocation
import net.minecraft.core.NonNullList
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import java.util.function.Consumer
import net.minecraft.world.level.block.state.BlockBehaviour
import com.github.mystery2099.woodenAccentsMod.util.LootTableUtil
import net.minecraft.data.recipes.RecipeOutput

class CrateBlock(val baseBlock: Block, private val edgeBlock: Block) :
    BaseEntityBlock(BlockBehaviour.Properties.ofFullCopy(baseBlock)),
    CustomBlockStateProvider, CustomItemGroupProvider, CustomTagProvider<Block>, CustomRecipeProvider,
    CustomBlockLootTableProvider {

    override val itemGroup: ModItemGroup = ModItemGroup.STORAGE
    override val tag: TagKey<Block> = ModBlockTags.crates

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = CrateBlockEntity(pos, state)

    @Deprecated("Deprecated in Java", ReplaceWith("RenderShape.MODEL", "net.minecraft.world.level.block.RenderShape"))
    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    override fun useWithoutItem(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        player: Player,
        hit: BlockHitResult
    ): InteractionResult {
        if (world.isClientSide) return InteractionResult.SUCCESS

        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is CrateBlockEntity) {
            player.openMenu(blockEntity)
            PiglinAi.angerNearbyPiglins(player, true)
        }

        return InteractionResult.CONSUME
    }


    override fun playerWillDestroy(world: Level, pos: BlockPos, state: BlockState, player: Player) {
        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is CrateBlockEntity) {

            // Match shulker boxes: creative players keep the crate and its contents together.
            if (!world.isClientSide && player.isCreative && !blockEntity.isEmpty()) {

                val itemStack = ItemStack(this)
                blockEntity.saveToItem(itemStack)
                if (blockEntity.hasCustomName()) {
                    itemStack.setHoverName(blockEntity.customName)
                }

                val itemEntity =
                    ItemEntity(world, pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, itemStack)
                itemEntity.setDefaultPickUpDelay()
                world.addFreshEntity(itemEntity)

            } else blockEntity.unpackLootTable(player)

        }
        super.playerWillDestroy(world, pos, state, player)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun getDrops(state: BlockState, builder: LootParams.Builder): List<ItemStack> {
        var newBuilder: LootParams.Builder = builder
        val blockEntity = newBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY)
        if (blockEntity is CrateBlockEntity) {
            newBuilder = newBuilder.withDynamicDrop(contents) { consumer: Consumer<ItemStack> ->
                for (i in 0 until blockEntity.containerSize) {
                    consumer.accept(blockEntity.getItem(i))
                }
            }
        }
        return super.getDrops(state, newBuilder)
    }

    override fun setPlacedBy(
        world: Level,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack
    ) {
        val blockEntity: BlockEntity? = world.getBlockEntity(pos)
        if (itemStack.hasCustomHoverName() && blockEntity is CrateBlockEntity) {
            blockEntity.customName = itemStack.getHoverName()
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onRemove(
        state: BlockState,
        world: Level,
        pos: BlockPos?,
        newState: BlockState,
        moved: Boolean
    ) {
        if (state isOf newState.block) return
        if (world.getBlockEntity(pos) is CrateBlockEntity) world.updateNeighbourForOutputSignal(pos, state.block)
        super.onRemove(state, world, pos, newState, moved)
    }

    override fun appendHoverText(
        stack: ItemStack,
        world: BlockGetter?,
        tooltip: MutableList<Component>,
        options: TooltipFlag
    ) {
        super.appendHoverText(stack, world, tooltip, options)
        BlockItem.getBlockEntityData(stack)?.let { nbtCompound ->
            if (nbtCompound.contains("LootTable", Tag.TAG_STRING.toInt())) {
                tooltip.add(Component.literal("???????"))
            }
            if (nbtCompound.contains("Items", Tag.TAG_LIST.toInt())) {
                val defaultedList = NonNullList.withSize(9, ItemStack.EMPTY)
                ContainerHelper.loadAllItems(nbtCompound, defaultedList)
                var i : Short = 0
                var j : Short = 0
                for (itemStack in defaultedList) {
                    if (itemStack.isEmpty) continue
                    ++j
                    if (i > 4) continue
                    ++i
                    itemStack.getHoverName().copy()
                        .append(" x")
                        .append(itemStack.count.toString()).also {
                            tooltip.add(it)
                        }
                }
                if (j - i > 0) {
                    tooltip.add(Component.translatable("container.crate.more", j - i).withStyle(ChatFormatting.ITALIC))
                }
            }
        }
    }


    @Deprecated("Deprecated in Java", ReplaceWith("true"))
    override fun hasAnalogOutputSignal(state: BlockState): Boolean = true

    @Deprecated("Deprecated in Java", ReplaceWith(
        "AbstractContainerMenu.getRedstoneSignalFromContainer(world.getBlockEntity(pos) as Container)",
        "net.minecraft.world.inventory.AbstractContainerMenu",
        "net.minecraft.world.Container"
    )
    )
    override fun getAnalogOutputSignal(state: BlockState, world: Level, pos: BlockPos): Int {
        return AbstractContainerMenu.getRedstoneSignalFromContainer(world.getBlockEntity(pos) as Container)
    }

    override fun getCloneItemStack(world: BlockGetter, pos: BlockPos, state: BlockState): ItemStack {
        val itemStack = super.getCloneItemStack(world, pos, state)
        world.getBlockEntity(pos, ModBlockEntities.crate).ifPresent { blockEntity: CrateBlockEntity ->
            blockEntity.saveToItem(itemStack)
            if (blockEntity.hasCustomName()) itemStack.setHoverName(blockEntity.customName)
        }
        return itemStack
    }


    override fun generateBlockStateModels(generator: BlockModelGenerators) {
        val map: TextureMapping = TextureMapping().put(TextureSlot.INSIDE, baseBlock.textureId)
            .put(TextureSlot.EDGE, edgeBlock.textureId)
            .put(TextureSlot.CROSS, edgeBlock.textureId.run {
                if (this.path.contains("stripped")) this
                else this.withPath(this.path.replace("block/", "block/stripped_"))
            })
        ModModels.crate.create(this, map, generator.modelOutput)
        generator.createNonTemplateModelBlock(this)
    }

    override fun offerRecipeTo(recipeExporter: RecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, this).apply {
            define('n', baseBlock)
            define('t', edgeBlock)
            define('u', Items.CHEST)
            pattern("tnt")
            pattern("nun")
            pattern("tnt")
            customGroup(this@CrateBlock, "crates")
            requires(Items.CHEST)
            save(recipeExporter)
        }
    }

    override fun getLootTableBuilder(): LootTable.Builder {
        return LootTable.lootTable().withPool(
            LootTableUtil.applyExplosionCondition(
                this, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(
                    LootItem.lootTableItem(this)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                ).apply(
                    CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                        .copy("Lock", "BlockEntityTag.Lock")
                        .copy("LootTable", "BlockEntityTag.LootTable")
                        .copy("LootTableSeed", "BlockEntityTag.LootTableSeed")
                ).apply(
                    SetContainerContents.setContents(ModBlockEntities.crate)
                        .withEntry(DynamicLoot.dynamicEntry(contents))
                )
            )
        )
    }

    companion object {
        val contents = ResourceLocation("contents")
    }
}
