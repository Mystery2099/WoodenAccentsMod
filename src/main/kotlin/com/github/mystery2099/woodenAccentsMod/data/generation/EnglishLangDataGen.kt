package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks.id
import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup

/**
 * English lang data gen
 *
 * @constructor
 *
 * @param dataOutput
 */
class EnglishLangDataGen(dataOutput: FabricDataOutput) : FabricLanguageProvider(dataOutput) {
    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        with(translationBuilder) {
            com.github.mystery2099.woodenAccentsMod.block.ModBlocks.blocks.forEach { it.genLangName(this) }
            CustomItemGroup.instances.forEach { it.itemGroup.genLangName(this) }
            add("container.crate.more", "and %s more...")
        }

    }

    private fun ItemGroup.genLangName(translationBuilder: TranslationBuilder) {
        translationBuilder.add(this, "WAM: ${this.id.path.toName()}")
    }

    private fun Block.genLangName(translationBuilder: TranslationBuilder) {
        translationBuilder.add(this, id.path.toName())
    }

    private fun String?.toName(): String = if (isNullOrEmpty()) ""
    else split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercase) }

}