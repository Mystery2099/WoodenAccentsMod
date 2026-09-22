package com.github.mystery2099.woodenAccentsMod.util

import net.minecraft.advancements.critereon.EnchantmentPredicate
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.world.level.storage.loot.predicates.MatchTool
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition

/**
 * Loot-table helpers mirroring vanilla [net.minecraft.data.loot.BlockLootSubProvider]'s protected
 * convenience methods, kept here so block classes need no loader-specific provider base class.
 */
object LootTableUtil {
    val hasSilkTouch: LootItemCondition.Builder = MatchTool.toolMatches(
        ItemPredicate.Builder.item()
            .hasEnchantment(EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))
    )

    val hasNoSilkTouch: LootItemCondition.Builder = hasSilkTouch.invert()

    /** Drops only survive if the entity wasn't caught in an explosion that destroyed the block. */
    fun <T : ConditionUserBuilder<T>> applyExplosionCondition(item: ItemLike, builder: T): T {
        return builder.`when`(ExplosionCondition.survivesExplosion())
    }

    /** Stack sizes decay alongside the explosion that dropped them unless the item resists explosions. */
    fun <T : FunctionUserBuilder<T>> applyExplosionDecay(item: ItemLike, builder: T): T {
        return builder.apply(ApplyExplosionDecay.explosionDecay())
    }

    /** Adds every condition to the same loot function builder. */
    fun <T : LootItemConditionalFunction.Builder<*>> conditionally(
        builder: T,
        vararg builders: LootItemCondition.Builder
    ): T {
        builders.forEach { builder.`when`(it) }
        return builder
    }
}
