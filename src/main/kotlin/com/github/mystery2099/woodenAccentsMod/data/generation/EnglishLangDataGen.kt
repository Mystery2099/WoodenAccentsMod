package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.id
import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

/**
 * English lang data gen
 *
 * @constructor
 *
 * @param dataOutput
 */
class EnglishLangDataGen(dataOutput: FabricDataOutput) : FabricLanguageProvider(dataOutput) {
    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        translationBuilder.run {
            ModBlocks.blocks.forEach {
                translationBuilder.add(it, it.id.path.toName())
            }
            CustomItemGroup.instances.map { it.itemGroup }.forEach {
                translationBuilder.add(it, "WAM: ${it.id.path.toName()}")
            }
            add("container.crate.more", "and %s more...")
        }
    }

    /**
     * Convert a snake_case [String], to a more human-readable title_case format.
     *
     * @return The transformed string in a more human-readable, title_case format.
     */
    private fun String?.toName(): String {
        return if (isNullOrEmpty()) ""
        else lowercase().split("_").joinToString(" ") {
            if (it != "of") {
                it.replaceFirstChar(Char::uppercase)
            } else it
        }
    }

}