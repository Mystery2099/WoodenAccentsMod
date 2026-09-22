package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.custom.AbstractPillarBlock
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.CoffeeTableTypes
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.DeskShape
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.SidewaysConnectionShape
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.state.property.ModProperties
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.FrameType
import net.minecraft.advancements.RequirementsStrategy
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger
import net.minecraft.advancements.critereon.PlayerTrigger
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.state.properties.StairsShape
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.predicates.LocationCheck
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.advancements.critereon.NbtPredicate
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.advancements.critereon.EnchantmentPredicate
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import java.util.function.Consumer


class AdvancementDataGen(output: FabricDataOutput?) : FabricAdvancementProvider(output) {
    override fun generateAdvancement(consumer: Consumer<Advancement>) {

        val root = Advancement.Builder.advancement()
            .display(
                ModBlocks.oakPlankTable,
                Component.literal("Wooden Accents Mod"),
                Component.literal("Add some charm and warmth to your world with the Wooden Accents Mod"),
                ResourceLocation("textures/gui/advancements/backgrounds/adventure.png"),
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("start", PlayerTrigger.TriggerInstance.tick())
            .save(consumer,WoodenAccentsMod.MOD_ID + ":root")

        val basicComfort = Advancement.Builder.advancement().parent(root)
            .display(
                ModBlocks.oakPlankChair,
                Component.literal("Welcome Home!"),
                Component.literal("Craft a cozy place to sit and a surface for your belongings"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("has_chair",
                inventoryChangedConditionsInTag(ModBlockTags.chairs))
            .addCriterion("has_table",
                inventoryChangedConditionsInTag(ModBlockTags.tables))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":decor/root")

        val coffeeBreak = Advancement.Builder.advancement().parent(basicComfort)
            .display(
                ModBlocks.oakCoffeeTable,
                Component.literal("Take a break!"),
                Component.literal("Craft a coffee table"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("has_coffee_table",
                inventoryChangedConditionsInTag(ModBlockTags.coffeeTables))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":decor/coffee_break")

        Advancement.Builder.advancement().parent(coffeeBreak)
            .display(
                ModBlocks.oakCoffeeTable,
                Component.literal("A better table?"),
                Component.literal("Stack two matching coffee tables to make a tall coffee table"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("made_tall_coffee_table",
                placedBlockInTagConditions(ModBlockTags.coffeeTables, ModProperties.coffeeTableType, CoffeeTableTypes.TALL))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":decor/better_table")

        Advancement.Builder.advancement().parent(basicComfort)
            .display(
                ModBlocks.oakPlankCarpet,
                Component.literal("Spruce Up Your Space!"),
                Component.literal("Cover a floor with plank flooring"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("has_plank_carpet",
                inventoryChangedConditionsInTag(ModBlockTags.plankCarpets))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":decor/interior_design")

        Advancement.Builder.advancement().parent(basicComfort)
            .display(
                ModBlocks.oakPlankBookshelf,
                Component.literal("Getting Organized!"),
                Component.literal("Craft a Narrow Bookshelf to store your books"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("has_thin_bookshelf",
                inventoryChangedConditionsInTag(ModBlockTags.thinBookshelves))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":decor/getting_organized")

        Advancement.Builder.advancement().parent(basicComfort)
            .display(
                ModBlocks.oakBracketShelf,
                Component.literal("Shelf Improvement!"),
                Component.literal("Craft a Bracket Shelf to hang your display items on the wall"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("has_bracket_shelf",
                inventoryChangedConditionsInTag(ModBlockTags.bracketShelves))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":decor/shelf_improvement")

        Advancement.Builder.advancement().parent(basicComfort)
            .display(
                ModBlocks.oakBracketShelf,
                Component.literal("Side by Side!"),
                Component.literal("Link Bracket Shelves together into one long shelving wall"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("connected_left",
                placedBlockInTagConditions(ModBlockTags.bracketShelves, ModProperties.left, true))
            .addCriterion("connected_right",
                placedBlockInTagConditions(ModBlockTags.bracketShelves, ModProperties.right, true))
            .requirements(RequirementsStrategy.OR)
            .save(consumer,WoodenAccentsMod.MOD_ID + ":decor/side_by_side")

        val desk = Advancement.Builder.advancement().parent(basicComfort)
            .display(ModBlocks.oakDesk, Component.literal("A place to work"),
                Component.literal("Craft a desk"), null, FrameType.TASK, true, false, false)
            .addCriterion("has_desk", inventoryChangedConditionsInTag(ModBlockTags.desks))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":decor/desk")

        Advancement.Builder.advancement().parent(desk)
            .display(ModBlocks.oakDesk, Component.literal("Corner Office!"),
                Component.literal("Connect desks around a corner for an L-shaped workspace"),
                null, FrameType.TASK, true, false, false)
            .addCriterion("left_corner",
                placedBlockInTagConditions(ModBlockTags.desks, ModProperties.deskShape, DeskShape.LEFT_CORNER))
            .addCriterion("right_corner",
                placedBlockInTagConditions(ModBlockTags.desks, ModProperties.deskShape, DeskShape.RIGHT_CORNER))
            .requirements(RequirementsStrategy.OR)
            .save(consumer,WoodenAccentsMod.MOD_ID + ":decor/corner_office")

        Advancement.Builder.advancement().parent(desk)
            .display(ModBlocks.oakDeskDrawer, Component.literal("Everything in its drawer"),
                Component.literal("Craft a desk drawer for your supplies"), null, FrameType.TASK, true, false, false)
            .addCriterion("has_desk_drawer", inventoryChangedConditionsInTag(ModBlockTags.deskDrawers))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":storage/desk_drawer")

        val counter = Advancement.Builder.advancement().parent(basicComfort)
            .display(ModBlocks.oakKitchenCounter, Component.literal("Room to cook"),
                Component.literal("Craft a kitchen counter or cabinet"), null, FrameType.TASK, true, false, false)
            .addCriterion("has_kitchen_counter", inventoryChangedConditionsInTag(ModBlockTags.kitchenCounters))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":decor/kitchen_counter")

        Advancement.Builder.advancement().parent(counter)
            .display(ModBlocks.oakKitchenCounter, Component.literal("Corner Kitchen!"),
                Component.literal("Connect counters around a corner for a seamless kitchen layout"),
                null, FrameType.TASK, true, false, false)
            .addCriterion("inner_left",
                placedBlockInTagConditions(ModBlockTags.kitchenCounters, BlockStateProperties.STAIRS_SHAPE, StairsShape.INNER_LEFT))
            .addCriterion("inner_right",
                placedBlockInTagConditions(ModBlockTags.kitchenCounters, BlockStateProperties.STAIRS_SHAPE, StairsShape.INNER_RIGHT))
            .addCriterion("outer_left",
                placedBlockInTagConditions(ModBlockTags.kitchenCounters, BlockStateProperties.STAIRS_SHAPE, StairsShape.OUTER_LEFT))
            .addCriterion("outer_right",
                placedBlockInTagConditions(ModBlockTags.kitchenCounters, BlockStateProperties.STAIRS_SHAPE, StairsShape.OUTER_RIGHT))
            .requirements(RequirementsStrategy.OR)
            .save(consumer,WoodenAccentsMod.MOD_ID + ":decor/corner_kitchen")

        Advancement.Builder.advancement().parent(counter)
            .display(ModBlocks.oakKitchenCabinet, Component.literal("Stock the kitchen"),
                Component.literal("Craft a kitchen cabinet for your ingredients"), null, FrameType.TASK, true, false, false)
            .addCriterion("has_kitchen_cabinet", inventoryChangedConditionsInTag(ModBlockTags.kitchenCabinets))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":storage/kitchen_cabinet")

        val crates = Advancement.Builder.advancement().parent(root)
            .display(
                ModBlocks.oakCrate,
                Component.literal("I can't believe it's not a Shulker Box!"),
                Component.literal("Craft a Crate for portable storage"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("has_crate", inventoryChangedConditionsInTag(ModBlockTags.crates))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":storage/root")

        Advancement.Builder.advancement().parent(crates)
            .display(
                ModBlocks.oakCrate,
                Component.literal("Better than Shulker Box"),
                Component.literal("Crates are stackable? Make sure they contain the same items"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("stacked_crates", inventoryChangedConditionsInTag(ModBlockTags.crates, MinMaxBounds.Ints.atLeast(2)))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":storage/stacking_crates")

        val structuralSupport = Advancement.Builder.advancement().parent(root)
            .display(
                ModBlocks.oakSupportBeam,
                Component.literal("Build it Strong!"),
                Component.literal("Craft Supports to reinforce your structures and connect them in different directions for added stability"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("has_support_beam", inventoryChangedConditionsInTag(ModBlockTags.supportBeams))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":structural/root")

        Advancement.Builder.advancement().parent(structuralSupport)
            .display(
                ModBlocks.oakSupportBeam,
                Component.literal("Standing Tall!"),
                Component.literal("Stack support beams into a full vertical column"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("column_beam", placedBlockInTagConditions(ModBlockTags.supportBeams) {
                exactMatch(BlockStateProperties.UP, true)
                exactMatch(BlockStateProperties.DOWN, true)
            })
            .save(consumer,WoodenAccentsMod.MOD_ID + ":structural/standing_tall")

        Advancement.Builder.advancement().parent(structuralSupport)
            .display(
                ModBlocks.modernOakFence,
                Component.literal("Time to Modernize!"),
                Component.literal("Craft a picket fence and a matching picket fence gate"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("has_modern_fence", inventoryChangedConditionsInTag(ModBlockTags.modernFences))
            .addCriterion("has_modern_fence_gate", inventoryChangedConditionsInTag(ModBlockTags.modernFenceGates))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":structural/modern_touches")

        Advancement.Builder.advancement().parent(structuralSupport)
            .display(
                ModBlocks.modernOakFenceGate,
                Component.literal("Gatekeeper!"),
                Component.literal("Fit a Picket Fence Gate into a wall opening"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("fitted_gate", placedBlockInTagConditions(ModBlockTags.modernFenceGates) {
                exactMatch(FenceGateBlock.IN_WALL, true)
            })
            .save(consumer,WoodenAccentsMod.MOD_ID + ":structural/gatekeeper")

        Advancement.Builder.advancement().parent(structuralSupport)
            .display(
                ModBlocks.oakPlankLadder,
                Component.literal("Climb High!"),
                Component.literal("Craft all three ladder styles: Stripped Wood for a classic look, Plank for a simpler option, and Simple for a minimal touch"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("has_plank_ladder", inventoryChangedConditionsInTag(ModBlockTags.plankLadders))
            .addCriterion("has_connecting_ladder", inventoryChangedConditionsInTag(ModBlockTags.connectingLadders))
            .addCriterion("has_simple_ladder", inventoryChangedConditionsInTag(ModBlockTags.simpleLadders))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":structural/ladder_up")

        Advancement.Builder.advancement().parent(structuralSupport)
            .display(
                ModBlocks.strippedOakLadder,
                Component.literal("Going Sideways!"),
                Component.literal("Link connecting ladders together to climb across walls"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("linked_center",
                placedBlockInTagConditions(ModBlockTags.connectingLadders, ModProperties.sidewaysConnectionShape, SidewaysConnectionShape.CENTER))
            .addCriterion("linked_left",
                placedBlockInTagConditions(ModBlockTags.connectingLadders, ModProperties.sidewaysConnectionShape, SidewaysConnectionShape.LEFT))
            .addCriterion("linked_right",
                placedBlockInTagConditions(ModBlockTags.connectingLadders, ModProperties.sidewaysConnectionShape, SidewaysConnectionShape.RIGHT))
            .requirements(RequirementsStrategy.OR)
            .save(consumer,WoodenAccentsMod.MOD_ID + ":structural/going_sideways")

        Advancement.Builder.advancement().parent(structuralSupport)
            .display(
                ModBlocks.thinOakPillar,
                Component.literal("Reach for the Sky!"),
                Component.literal("Craft a Thin Pillar for a slim look and a Thick Pillar for a bolder design"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("has_thin_pillar", inventoryChangedConditionsInTag(ModBlockTags.thinPillars))
            .addCriterion("has_thick_pillar", inventoryChangedConditionsInTag(ModBlockTags.thickPillars))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":structural/pillars_of_strength")

        Advancement.Builder.advancement().parent(structuralSupport)
            .display(
                ModBlocks.thinOakPillar,
                Component.literal("Pillar Talk!"),
                Component.literal("Connect pillars of any style into one seamless column"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("on_thin_pillar", placedBlockInTagConditions(ModBlockTags.thinPillars) {
                exactMatch(AbstractPillarBlock.down, true)
            })
            .addCriterion("on_thick_pillar", placedBlockInTagConditions(ModBlockTags.thickPillars) {
                exactMatch(AbstractPillarBlock.down, true)
            })
            .requirements(RequirementsStrategy.OR)
            .save(consumer,WoodenAccentsMod.MOD_ID + ":structural/pillar_talk")

        Advancement.Builder.advancement().parent(structuralSupport)
            .display(
                ModBlocks.oakPlankWall,
                Component.literal("Walled In!"),
                Component.literal("Craft a Plank Wall for boundaries that blend right into your builds"),
                null,
                FrameType.TASK,
                true,
                false,
                false
            )
            .addCriterion("has_plank_wall", inventoryChangedConditionsInTag(ModBlockTags.woodenWalls))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":structural/walled_in")

        Advancement.Builder.advancement().parent(root)
            .display(
                ModBlocks.oakPlankTable,
                Component.literal("Home Sweet Home!"),
                Component.literal("Have furniture from every Wooden Accents category in your inventory at once"),
                null,
                FrameType.GOAL,
                true,
                false,
                false
            )
            .addCriterion("has_all_categories", InventoryChangeTrigger.TriggerInstance.hasItems(
                *arrayOf(ModBlockTags.chairs, ModBlockTags.tables, ModBlockTags.desks,
                    ModBlockTags.crates, ModBlockTags.supportBeams, ModBlockTags.kitchenCounters)
                    .map { ItemPredicate.Builder.item().of(ModBlockTags.getItemTagFrom(it)).build() }
                    .toTypedArray()
            ))
            .save(consumer,WoodenAccentsMod.MOD_ID + ":home_sweet_home")
    }

    private fun inventoryChangedConditionsInTag(tag: TagKey<Block>, itemCount: MinMaxBounds.Ints = MinMaxBounds.Ints.ANY): InventoryChangeTrigger.TriggerInstance {
        val itemTag = ModBlockTags.getItemTagFrom(tag)
        val pred = ItemPredicate(itemTag, null, itemCount, MinMaxBounds.Ints.ANY, arrayOf(EnchantmentPredicate.ANY), arrayOf(EnchantmentPredicate.ANY), null, NbtPredicate.ANY)
        return InventoryChangeTrigger.TriggerInstance.hasItems(pred)
    }

    /** Matches placing a block from [tag] whose state satisfies every condition configured in [stateBuilder]. */
    private fun placedBlockInTagConditions(
        tag: TagKey<Block>,
        stateBuilder: StatePropertiesPredicate.Builder.() -> Unit = {}
    ): ItemUsedOnLocationTrigger.TriggerInstance {
        val state = StatePropertiesPredicate.Builder.properties().apply(stateBuilder).build()
        val blockPredicate = BlockPredicate.Builder.block().of(tag).setProperties(state).build()
        val location = LocationPredicate.Builder.location().setBlock(blockPredicate)

        return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(LocationCheck.checkLocation(location))
    }

    /** Convenience overload matching a single enum-valued block state property. */
    private fun <T> placedBlockInTagConditions(
        tag: TagKey<Block>,
        property: net.minecraft.world.level.block.state.properties.Property<T>,
        value: T
    ): ItemUsedOnLocationTrigger.TriggerInstance where T : Comparable<T>, T : net.minecraft.util.StringRepresentable = placedBlockInTagConditions(tag) {
        exactMatch(property, value)
    }

    /** Convenience overload matching a single boolean block state property. */
    private fun placedBlockInTagConditions(
        tag: TagKey<Block>,
        property: net.minecraft.world.level.block.state.properties.BooleanProperty,
        value: Boolean
    ): ItemUsedOnLocationTrigger.TriggerInstance = placedBlockInTagConditions(tag) {
        exactMatch(property, value)
    }

    /** Matches a block state property against the serialized name of its value. */
    private fun StatePropertiesPredicate.Builder.exactMatch(property: net.minecraft.world.level.block.state.properties.Property<*>, value: Any?) =
        hasProperty(property, (value as? net.minecraft.util.StringRepresentable)?.getSerializedName() ?: value.toString())
}
