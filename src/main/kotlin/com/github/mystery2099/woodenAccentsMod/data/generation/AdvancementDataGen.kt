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

        // 1. Furniture
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
            .build(consumer, WoodenAccentsMod.MOD_ID + "/furniture/root")

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
            .build(consumer, WoodenAccentsMod.MOD_ID + "/furniture/coffee_break")

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
            .build(consumer, WoodenAccentsMod.MOD_ID + "/furniture/better_table")


        // 2. Building (
        // Interior Design (Wooden Carpet)
        // Ladders Up (Ladders) - parent = Interior Design
        // Storage Solutions (Crates) - parent = Interior Design
        // Stacking Crates (min 2 crates in slot, recommend trying with same items in crate) - parent = storage solutions
        // Modern Touches (modern fences + fence gates) - parent = Interior Design
        // Structural Support (Craft & use supports) - parent = Interior Design
        // Pillars of Strength (Craft thick & thin pillars) - parent = In
        // connections (place a thin pillar on above or below a modern fence)  - parent = Structural Support
        //
    }
    private inline fun <reified R : Block> inventoryChangedConditionsOfInstance(): InventoryChangedCriterion.Conditions {
        val blocks = ModBlocks.blocks.filterIsInstance<R>().toTypedArray()
        return InventoryChangedCriterion.Conditions.items(*blocks)
    }
    private fun inventoryChangedConditionsInTag(tag: TagKey<Block>): InventoryChangedCriterion.Conditions {
        val itemTag = ModBlockTags.getItemTagFrom(tag)
        val pred = ItemPredicate(itemTag, null, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, EnchantmentPredicate.ARRAY_OF_ANY, EnchantmentPredicate.ARRAY_OF_ANY, null, NbtPredicate.ANY)
        return InventoryChangedCriterion.Conditions.items(pred)
    }

    private fun placedBlockInTagConditions(tag: TagKey<Block>): PlacedBlockCriterion.Conditions {
        val itemTag = ModBlockTags.getItemTagFrom(tag)
        val state = StatePredicate.Builder.create().exactMatch(ModProperties.coffeeTableType, CoffeeTableTypes.TALL).build()
        val pred = ItemPredicate(itemTag, null, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, EnchantmentPredicate.ARRAY_OF_ANY, EnchantmentPredicate.ARRAY_OF_ANY, null, NbtPredicate.ANY)

        return PlacedBlockCriterion.Conditions(Extended.EMPTY, null, state, LocationPredicate.ANY, pred)
    }
}