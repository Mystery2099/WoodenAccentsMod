package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.custom.enums.CoffeeTableTypes
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import com.github.mystery2099.woodenAccentsMod.state.property.ModProperties
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.advancement.criterion.PlacedBlockCriterion
import net.minecraft.advancement.criterion.TickCriterion
import net.minecraft.block.Block
import net.minecraft.predicate.NbtPredicate
import net.minecraft.predicate.NumberRange
import net.minecraft.predicate.StatePredicate
import net.minecraft.predicate.entity.EntityPredicate.Extended
import net.minecraft.predicate.entity.LocationPredicate
import net.minecraft.predicate.item.EnchantmentPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.registry.tag.TagKey
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
            .build(consumer, WoodenAccentsMod.MOD_ID + "/root")

        // 1. Decor
        val basicComfort = Advancement.Builder.create().parent(root)
            .display(
                ModBlocks.oakPlankChair,
                Text.literal("Welcome Home!"),
                Text.literal("Craft a cozy place to sit and a surface for your belongings"),
                Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_chair",
                inventoryChangedConditionsInTag(ModBlockTags.chairs))
            .criterion("has_table",
                inventoryChangedConditionsInTag(ModBlockTags.tables))
            .build(consumer, WoodenAccentsMod.MOD_ID + "/decor/root")

        val coffeeBreak = Advancement.Builder.create().parent(basicComfort)
            .display(
                ModBlocks.oakCoffeeTable,
                Text.literal("Take a break!"),
                Text.literal("Craft a coffee table because why not?"),
                Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_coffee_table",
                inventoryChangedConditionsInTag(ModBlockTags.coffeeTables))
            .build(consumer, WoodenAccentsMod.MOD_ID + "/decor/coffee_break")

        val betterTable = Advancement.Builder.create().parent(coffeeBreak)
            .display(
                ModBlocks.oakCoffeeTable,
                Text.literal("A better table?"),
                Text.literal("Stack one coffee table on top of another to create a brand new table"),
                Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("made_tall_coffee_table",
                placedBlockInTagConditions(ModBlockTags.coffeeTables))
            .build(consumer, WoodenAccentsMod.MOD_ID + "/decor/better_table")

        val interiorDesign = Advancement.Builder.create().parent(basicComfort)
            .display(
                ModBlocks.oakPlankCarpet,
                Text.literal("Spruce Up Your Space!"),
                Text.literal("Craft a plank carpet for a soft touch and cover up the ugly dirt or stone floor"),
                Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_plank_carpet",
                inventoryChangedConditionsInTag(ModBlockTags.plankCarpets))
            .build(consumer, WoodenAccentsMod.MOD_ID + "/decor/interior_design")



        // Storage
        val crates = Advancement.Builder.create().parent(root)
            .display(
                ModBlocks.oakCrate,
                Text.literal("I can't believe it's not a Shulker Box!"),
                Text.literal("Craft a Crate for portable storage"),
                Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_crate", inventoryChangedConditionsInTag(ModBlockTags.crates))
            .build(consumer, WoodenAccentsMod.MOD_ID + "/storage/root")

        val stackingCrates = Advancement.Builder.create().parent(crates)
            .display(
                ModBlocks.oakCrate,
                Text.literal("Better than Shulker Box"),
                Text.literal("Crates are stackable? Make sure they contain the same items"),
                Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("stacked_crates", inventoryChangedConditionsInTag(ModBlockTags.crates, NumberRange.IntRange.atLeast(2)))
            .build(consumer, WoodenAccentsMod.MOD_ID + "/storage/stacking_crates")

        // Structural
        val structuralSupport = Advancement.Builder.create().parent(root)
            .display(
                ModBlocks.oakSupportBeam,
                Text.literal("Build it Strong!"),
                Text.literal("Craft Supports to reinforce your structures and connect them in different directions for added stability"),
                Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_support_beam", inventoryChangedConditionsInTag(ModBlockTags.supportBeams))
            .build(consumer, WoodenAccentsMod.MOD_ID + "/structural/root")

        val modernTouches = Advancement.Builder.create().parent(structuralSupport)
            .display(
                ModBlocks.modernOakFence,
                Text.literal("Time to Modernize!"),
                Text.literal("Protect your property with a Modern Fence and a Modern Fence Gate to enter"),
                Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_modern_fence", inventoryChangedConditionsInTag(ModBlockTags.modernFences))
            .criterion("has_modern_fence_gate", inventoryChangedConditionsInTag(ModBlockTags.modernFenceGates))
            .build(consumer, WoodenAccentsMod.MOD_ID + "/structural/modern_touches")

        val ladderUp = Advancement.Builder.create().parent(structuralSupport)
            .display(
                ModBlocks.oakPlankLadder,
                Text.literal("Climb High!"),
                Text.literal("Craft a Stripped Wood Ladder for a classic look and a Plank Ladder for a simpler option"),
                Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_plank_ladder", inventoryChangedConditionsInTag(ModBlockTags.plankLadders))
            .criterion("has_connecting_ladder", inventoryChangedConditionsInTag(ModBlockTags.connectingLadders))
            .build(consumer, WoodenAccentsMod.MOD_ID + "/structural/ladder_up")

        val pillars = Advancement.Builder.create().parent(structuralSupport)
            .display(
                ModBlocks.oakPlankLadder,
                Text.literal("Reach for the Sky!"),
                Text.literal("Craft a Thin Pillar for a slim look and a Thick Pillar for a bolder design"),
                Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementFrame.TASK,
                true,
                false,
                false
            )
            .criterion("has_thin_pillar", inventoryChangedConditionsInTag(ModBlockTags.thinPillars))
            .criterion("has_thick_pillar", inventoryChangedConditionsInTag(ModBlockTags.thickPillars))
            .build(consumer, WoodenAccentsMod.MOD_ID + "/structural/pillars_of_strength")

        // Planned Advancements
        // Advancements for desks + desk drawers
        // Advancements for kitchen counter + cabinet
        // connections (place a thin pillar on above or below a modern fence)  - parent = Structural Support
    }

    private inline fun <reified R : Block> inventoryChangedConditionsOfInstance(): InventoryChangedCriterion.Conditions {
        val blocks = ModBlocks.blocks.filterIsInstance<R>().toTypedArray()
        return InventoryChangedCriterion.Conditions.items(*blocks)
    }
    private fun inventoryChangedConditionsInTag(tag: TagKey<Block>, itemCount: NumberRange.IntRange = NumberRange.IntRange.ANY): InventoryChangedCriterion.Conditions {
        val itemTag = ModBlockTags.getItemTagFrom(tag)
        val pred = ItemPredicate(itemTag, null, itemCount, NumberRange.IntRange.ANY, EnchantmentPredicate.ARRAY_OF_ANY, EnchantmentPredicate.ARRAY_OF_ANY, null, NbtPredicate.ANY)
        return InventoryChangedCriterion.Conditions.items(pred)
    }

    private fun placedBlockInTagConditions(tag: TagKey<Block>): PlacedBlockCriterion.Conditions {
        val itemTag = ModBlockTags.getItemTagFrom(tag)
        val state = StatePredicate.Builder.create().exactMatch(ModProperties.coffeeTableType, CoffeeTableTypes.TALL).build()
        val pred = ItemPredicate(itemTag, null, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, EnchantmentPredicate.ARRAY_OF_ANY, EnchantmentPredicate.ARRAY_OF_ANY, null, NbtPredicate.ANY)

        return PlacedBlockCriterion.Conditions(Extended.EMPTY, null, state, LocationPredicate.ANY, pred)
    }
}