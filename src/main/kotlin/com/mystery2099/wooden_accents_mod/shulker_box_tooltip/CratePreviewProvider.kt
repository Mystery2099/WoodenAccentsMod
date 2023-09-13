package com.mystery2099.wooden_accents_mod.shulker_box_tooltip

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting

/**
 * Crate preview provider
 *
 * @constructor Create empty Crate preview provider
 */
class CratePreviewProvider : BlockEntityPreviewProvider(9, true, 3) {
    override fun showTooltipHints(context: PreviewContext): Boolean = true
    override fun addTooltip(context: PreviewContext): List<Text> {
        val stack = context.stack()
        stack.nbt?.let { compound: NbtCompound ->
            if (canUseLootTables && compound.contains("BlockEntityTag", 10)) {
                compound.getCompound("BlockEntityTag")?.let { blockEntityTag: NbtCompound ->
                    if (blockEntityTag.contains("LootTable", 8)
                    ) {
                        val style = Style.EMPTY.withColor(Formatting.GRAY)
                        return listOf<Text>(Text.literal("???????").setStyle(style))
                    }
                }
            }
        }
        return super.addTooltip(context)
    }

}