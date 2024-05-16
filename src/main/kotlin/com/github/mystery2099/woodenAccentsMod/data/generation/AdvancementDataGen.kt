package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.custom.ChairBlock
import com.github.mystery2099.woodenAccentsMod.block.custom.TableBlock
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.advancement.criterion.PlacedBlockCriterion
import net.minecraft.block.Block
import net.minecraft.predicate.NbtPredicate
import net.minecraft.predicate.NumberRange
import net.minecraft.predicate.item.EnchantmentPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.function.Consumer


class AdvancementDataGen(output: FabricDataOutput?) : FabricAdvancementProvider(output) {
    override fun generateAdvancement(consumer: Consumer<Advancement>) {
        // 1. Furniture
        val basicComfort = Advancement.Builder.create()
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
}