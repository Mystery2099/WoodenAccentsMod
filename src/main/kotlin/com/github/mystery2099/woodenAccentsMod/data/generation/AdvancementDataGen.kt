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
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.CriterionMerger
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.advancement.criterion.ItemCriterion
import net.minecraft.advancement.criterion.TickCriterion
import net.minecraft.block.FenceGateBlock
import net.minecraft.block.enums.StairShape
import net.minecraft.block.Block
import net.minecraft.loot.condition.LocationCheckLootCondition
import net.minecraft.predicate.BlockPredicate
import net.minecraft.predicate.NbtPredicate
import net.minecraft.predicate.NumberRange
import net.minecraft.predicate.StatePredicate
import net.minecraft.predicate.entity.LocationPredicate
import net.minecraft.predicate.item.EnchantmentPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.function.Consumer


class AdvancementDataGen(output: FabricDataOutput?) : FabricAdvancementProvider(output) {
    override fun generateAdvancement(consumer: Consumer<Advancement>) {

        val root = Advancement.Builder.create()
            .display(
                ModBlocks.oakPlankTable,
                Text.literal("Wooden Accents Mod"),
                Text.literal("Add some charm and warmth to your world with the Wooden Accents Mod"),
                Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("start", TickCriterion.Conditions.createTick())
            .build(consumer, WoodenAccentsMod.MOD_ID + ":root")

        val basicComfort = Advancement.Builder.create().parent(root)
            .display(
                ModBlocks.oakPlankChair,
                Text.literal("Welcome Home!"),
                Text.literal("Craft a cozy place to sit and a surface for your belongings"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_chair",
                inventoryChangedConditionsInTag(ModBlockTags.chairs))
            .criterion("has_table",
                inventoryChangedConditionsInTag(ModBlockTags.tables))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":decor/root")

        val coffeeBreak = Advancement.Builder.create().parent(basicComfort)
            .display(
                ModBlocks.oakCoffeeTable,
                Text.literal("Take a break!"),
                Text.literal("Craft a coffee table"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_coffee_table",
                inventoryChangedConditionsInTag(ModBlockTags.coffeeTables))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":decor/coffee_break")

        Advancement.Builder.create().parent(coffeeBreak)
            .display(
                ModBlocks.oakCoffeeTable,
                Text.literal("A better table?"),
                Text.literal("Stack two matching coffee tables to make a tall coffee table"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("made_tall_coffee_table",
                placedBlockInTagConditions(ModBlockTags.coffeeTables, ModProperties.coffeeTableType, CoffeeTableTypes.TALL))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":decor/better_table")

        Advancement.Builder.create().parent(basicComfort)
            .display(
                ModBlocks.oakPlankCarpet,
                Text.literal("Spruce Up Your Space!"),
                Text.literal("Cover a floor with plank flooring"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_plank_carpet",
                inventoryChangedConditionsInTag(ModBlockTags.plankCarpets))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":decor/interior_design")

        Advancement.Builder.create().parent(basicComfort)
            .display(
                ModBlocks.oakPlankBookshelf,
                Text.literal("Getting Organized!"),
                Text.literal("Craft a Narrow Bookshelf to store your books"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_thin_bookshelf",
                inventoryChangedConditionsInTag(ModBlockTags.thinBookshelves))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":decor/getting_organized")

        Advancement.Builder.create().parent(basicComfort)
            .display(
                ModBlocks.oakBracketShelf,
                Text.literal("Shelf Improvement!"),
                Text.literal("Craft a Bracket Shelf to hang your display items on the wall"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_bracket_shelf",
                inventoryChangedConditionsInTag(ModBlockTags.bracketShelves))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":decor/shelf_improvement")

        Advancement.Builder.create().parent(basicComfort)
            .display(
                ModBlocks.oakBracketShelf,
                Text.literal("Side by Side!"),
                Text.literal("Link Bracket Shelves together into one long shelving wall"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("connected_left",
                placedBlockInTagConditions(ModBlockTags.bracketShelves, ModProperties.left, true))
            .criterion("connected_right",
                placedBlockInTagConditions(ModBlockTags.bracketShelves, ModProperties.right, true))
            .criteriaMerger(CriterionMerger.OR)
            .build(consumer, WoodenAccentsMod.MOD_ID + ":decor/side_by_side")

        val desk = Advancement.Builder.create().parent(basicComfort)
            .display(ModBlocks.oakDesk, Text.literal("A place to work"),
                Text.literal("Craft a desk"), null, AdvancementFrame.TASK, true, false, false)
            .criterion("has_desk", inventoryChangedConditionsInTag(ModBlockTags.desks))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":decor/desk")

        Advancement.Builder.create().parent(desk)
            .display(ModBlocks.oakDesk, Text.literal("Corner Office!"),
                Text.literal("Connect desks around a corner for an L-shaped workspace"),
                null, AdvancementFrame.TASK, true, false, false)
            .criterion("left_corner",
                placedBlockInTagConditions(ModBlockTags.desks, ModProperties.deskShape, DeskShape.LEFT_CORNER))
            .criterion("right_corner",
                placedBlockInTagConditions(ModBlockTags.desks, ModProperties.deskShape, DeskShape.RIGHT_CORNER))
            .criteriaMerger(CriterionMerger.OR)
            .build(consumer, WoodenAccentsMod.MOD_ID + ":decor/corner_office")

        Advancement.Builder.create().parent(desk)
            .display(ModBlocks.oakDeskDrawer, Text.literal("Everything in its drawer"),
                Text.literal("Craft a desk drawer for your supplies"), null, AdvancementFrame.TASK, true, false, false)
            .criterion("has_desk_drawer", inventoryChangedConditionsInTag(ModBlockTags.deskDrawers))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":storage/desk_drawer")

        val counter = Advancement.Builder.create().parent(basicComfort)
            .display(ModBlocks.oakKitchenCounter, Text.literal("Room to cook"),
                Text.literal("Craft a kitchen counter or cabinet"), null, AdvancementFrame.TASK, true, false, false)
            .criterion("has_kitchen_counter", inventoryChangedConditionsInTag(ModBlockTags.kitchenCounters))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":decor/kitchen_counter")

        Advancement.Builder.create().parent(counter)
            .display(ModBlocks.oakKitchenCounter, Text.literal("Corner Kitchen!"),
                Text.literal("Connect counters around a corner for a seamless kitchen layout"),
                null, AdvancementFrame.TASK, true, false, false)
            .criterion("inner_left",
                placedBlockInTagConditions(ModBlockTags.kitchenCounters, Properties.STAIR_SHAPE, StairShape.INNER_LEFT))
            .criterion("inner_right",
                placedBlockInTagConditions(ModBlockTags.kitchenCounters, Properties.STAIR_SHAPE, StairShape.INNER_RIGHT))
            .criterion("outer_left",
                placedBlockInTagConditions(ModBlockTags.kitchenCounters, Properties.STAIR_SHAPE, StairShape.OUTER_LEFT))
            .criterion("outer_right",
                placedBlockInTagConditions(ModBlockTags.kitchenCounters, Properties.STAIR_SHAPE, StairShape.OUTER_RIGHT))
            .criteriaMerger(CriterionMerger.OR)
            .build(consumer, WoodenAccentsMod.MOD_ID + ":decor/corner_kitchen")

        Advancement.Builder.create().parent(counter)
            .display(ModBlocks.oakKitchenCabinet, Text.literal("Stock the kitchen"),
                Text.literal("Craft a kitchen cabinet for your ingredients"), null, AdvancementFrame.TASK, true, false, false)
            .criterion("has_kitchen_cabinet", inventoryChangedConditionsInTag(ModBlockTags.kitchenCabinets))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":storage/kitchen_cabinet")

        val crates = Advancement.Builder.create().parent(root)
            .display(
                ModBlocks.oakCrate,
                Text.literal("I can't believe it's not a Shulker Box!"),
                Text.literal("Craft a Crate for portable storage"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_crate", inventoryChangedConditionsInTag(ModBlockTags.crates))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":storage/root")

        Advancement.Builder.create().parent(crates)
            .display(
                ModBlocks.oakCrate,
                Text.literal("Better than Shulker Box"),
                Text.literal("Crates are stackable? Make sure they contain the same items"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("stacked_crates", inventoryChangedConditionsInTag(ModBlockTags.crates, NumberRange.IntRange.atLeast(2)))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":storage/stacking_crates")

        val structuralSupport = Advancement.Builder.create().parent(root)
            .display(
                ModBlocks.oakSupportBeam,
                Text.literal("Build it Strong!"),
                Text.literal("Craft Supports to reinforce your structures and connect them in different directions for added stability"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_support_beam", inventoryChangedConditionsInTag(ModBlockTags.supportBeams))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":structural/root")

        Advancement.Builder.create().parent(structuralSupport)
            .display(
                ModBlocks.oakSupportBeam,
                Text.literal("Standing Tall!"),
                Text.literal("Stack support beams into a full vertical column"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("column_beam", placedBlockInTagConditions(ModBlockTags.supportBeams) {
                exactMatch(Properties.UP, true)
                exactMatch(Properties.DOWN, true)
            })
            .build(consumer, WoodenAccentsMod.MOD_ID + ":structural/standing_tall")

        Advancement.Builder.create().parent(structuralSupport)
            .display(
                ModBlocks.modernOakFence,
                Text.literal("Time to Modernize!"),
                Text.literal("Craft a picket fence and a matching picket fence gate"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_modern_fence", inventoryChangedConditionsInTag(ModBlockTags.modernFences))
            .criterion("has_modern_fence_gate", inventoryChangedConditionsInTag(ModBlockTags.modernFenceGates))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":structural/modern_touches")

        Advancement.Builder.create().parent(structuralSupport)
            .display(
                ModBlocks.modernOakFenceGate,
                Text.literal("Gatekeeper!"),
                Text.literal("Fit a Picket Fence Gate into a wall opening"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("fitted_gate", placedBlockInTagConditions(ModBlockTags.modernFenceGates) {
                exactMatch(FenceGateBlock.IN_WALL, true)
            })
            .build(consumer, WoodenAccentsMod.MOD_ID + ":structural/gatekeeper")

        Advancement.Builder.create().parent(structuralSupport)
            .display(
                ModBlocks.oakPlankLadder,
                Text.literal("Climb High!"),
                Text.literal("Craft all three ladder styles: Stripped Wood for a classic look, Plank for a simpler option, and Simple for a minimal touch"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_plank_ladder", inventoryChangedConditionsInTag(ModBlockTags.plankLadders))
            .criterion("has_connecting_ladder", inventoryChangedConditionsInTag(ModBlockTags.connectingLadders))
            .criterion("has_simple_ladder", inventoryChangedConditionsInTag(ModBlockTags.simpleLadders))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":structural/ladder_up")

        Advancement.Builder.create().parent(structuralSupport)
            .display(
                ModBlocks.strippedOakLadder,
                Text.literal("Going Sideways!"),
                Text.literal("Link connecting ladders together to climb across walls"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("linked_center",
                placedBlockInTagConditions(ModBlockTags.connectingLadders, ModProperties.sidewaysConnectionShape, SidewaysConnectionShape.CENTER))
            .criterion("linked_left",
                placedBlockInTagConditions(ModBlockTags.connectingLadders, ModProperties.sidewaysConnectionShape, SidewaysConnectionShape.LEFT))
            .criterion("linked_right",
                placedBlockInTagConditions(ModBlockTags.connectingLadders, ModProperties.sidewaysConnectionShape, SidewaysConnectionShape.RIGHT))
            .criteriaMerger(CriterionMerger.OR)
            .build(consumer, WoodenAccentsMod.MOD_ID + ":structural/going_sideways")

        Advancement.Builder.create().parent(structuralSupport)
            .display(
                ModBlocks.thinOakPillar,
                Text.literal("Reach for the Sky!"),
                Text.literal("Craft a Thin Pillar for a slim look and a Thick Pillar for a bolder design"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_thin_pillar", inventoryChangedConditionsInTag(ModBlockTags.thinPillars))
            .criterion("has_thick_pillar", inventoryChangedConditionsInTag(ModBlockTags.thickPillars))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":structural/pillars_of_strength")

        Advancement.Builder.create().parent(structuralSupport)
            .display(
                ModBlocks.thinOakPillar,
                Text.literal("Pillar Talk!"),
                Text.literal("Connect pillars of any style into one seamless column"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("on_thin_pillar", placedBlockInTagConditions(ModBlockTags.thinPillars) {
                exactMatch(AbstractPillarBlock.down, true)
            })
            .criterion("on_thick_pillar", placedBlockInTagConditions(ModBlockTags.thickPillars) {
                exactMatch(AbstractPillarBlock.down, true)
            })
            .criteriaMerger(CriterionMerger.OR)
            .build(consumer, WoodenAccentsMod.MOD_ID + ":structural/pillar_talk")

        Advancement.Builder.create().parent(structuralSupport)
            .display(
                ModBlocks.oakPlankWall,
                Text.literal("Walled In!"),
                Text.literal("Craft a Plank Wall for boundaries that blend right into your builds"),
                null,
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_plank_wall", inventoryChangedConditionsInTag(ModBlockTags.woodenWalls))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":structural/walled_in")

        Advancement.Builder.create().parent(root)
            .display(
                ModBlocks.oakPlankTable,
                Text.literal("Home Sweet Home!"),
                Text.literal("Have furniture from every Wooden Accents category in your inventory at once"),
                null,
                AdvancementFrame.GOAL,
                true,
                false,
                false
            )
            .criterion("has_all_categories", InventoryChangedCriterion.Conditions.items(
                *arrayOf(ModBlockTags.chairs, ModBlockTags.tables, ModBlockTags.desks,
                    ModBlockTags.crates, ModBlockTags.supportBeams, ModBlockTags.kitchenCounters)
                    .map { ItemPredicate.Builder.create().tag(ModBlockTags.getItemTagFrom(it)).build() }
                    .toTypedArray()
            ))
            .build(consumer, WoodenAccentsMod.MOD_ID + ":home_sweet_home")
    }

    private fun inventoryChangedConditionsInTag(tag: TagKey<Block>, itemCount: NumberRange.IntRange = NumberRange.IntRange.ANY): InventoryChangedCriterion.Conditions {
        val itemTag = ModBlockTags.getItemTagFrom(tag)
        val pred = ItemPredicate(itemTag, null, itemCount, NumberRange.IntRange.ANY, EnchantmentPredicate.ARRAY_OF_ANY, EnchantmentPredicate.ARRAY_OF_ANY, null, NbtPredicate.ANY)
        return InventoryChangedCriterion.Conditions.items(pred)
    }

    /** Matches placing a block from [tag] whose state satisfies every condition configured in [stateBuilder]. */
    private fun placedBlockInTagConditions(
        tag: TagKey<Block>,
        stateBuilder: StatePredicate.Builder.() -> Unit = {}
    ): ItemCriterion.Conditions {
        val state = StatePredicate.Builder.create().apply(stateBuilder).build()
        val blockPredicate = BlockPredicate.Builder.create().tag(tag).state(state).build()
        val location = LocationPredicate.Builder.create().block(blockPredicate)

        return ItemCriterion.Conditions.createPlacedBlock(LocationCheckLootCondition.builder(location))
    }

    /** Convenience overload matching a single enum-valued block state property. */
    private fun <T> placedBlockInTagConditions(
        tag: TagKey<Block>,
        property: net.minecraft.state.property.Property<T>,
        value: T
    ): ItemCriterion.Conditions where T : Comparable<T>, T : net.minecraft.util.StringIdentifiable = placedBlockInTagConditions(tag) {
        exactMatch(property, value)
    }

    /** Convenience overload matching a single boolean block state property. */
    private fun placedBlockInTagConditions(
        tag: TagKey<Block>,
        property: net.minecraft.state.property.BooleanProperty,
        value: Boolean
    ): ItemCriterion.Conditions = placedBlockInTagConditions(tag) {
        exactMatch(property, value)
    }
}
