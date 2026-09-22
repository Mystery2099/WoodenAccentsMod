package com.github.mystery2099.woodenAccentsMod.shulkerBoxTooltip

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.Component
import net.minecraft.ChatFormatting


/** Matches the crate's 3x3 layout without revealing unresolved loot-table contents. */
class CratePreviewProvider : BlockEntityPreviewProvider(9, true, 3) {
    override fun showTooltipHints(context: PreviewContext): Boolean = true
    override fun addTooltip(context: PreviewContext): List<Component> {
        val stack = context.stack()
        stack.tag?.let { compound: CompoundTag ->
            if (canUseLootTables && compound.contains("BlockEntityTag", 10)) {
                compound.getCompound("BlockEntityTag").let { blockEntityTag: CompoundTag ->
                    if (blockEntityTag.contains("LootTable", 8)
                    ) {
                        val style = Style.EMPTY.withColor(ChatFormatting.GRAY)
                        return listOf<Component>(Component.literal("???????").setStyle(style))
                    }
                }
            }
        }
        return super.addTooltip(context)
    }

}
