package com.github.mystery2099.woodenAccentsMod.block.custom

import com.github.mystery2099.woodenAccentsMod.item.CustomBlockItem
import net.minecraft.block.Block
import net.minecraft.item.Item

/**
 * If implemented on a [Block] whose [Item] form is an instance of [CustomBlockItem],
 * that [Item] will be unable to be nested
 * */
interface Unnestable