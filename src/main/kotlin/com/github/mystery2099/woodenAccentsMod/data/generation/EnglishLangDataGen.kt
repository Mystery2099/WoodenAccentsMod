package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.id
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
     * Convert a string, typically in snake_case, to a more human-readable name format.
     *
     * @param input The input string to be transformed.
     * @return The transformed string in a more human-readable format.
     */
    private fun String?.toName(): String {
        return if (isNullOrEmpty()) ""
        else split("_").joinToString(" ") {
            it.replaceFirstChar(Char::uppercase)
        }
    }

}