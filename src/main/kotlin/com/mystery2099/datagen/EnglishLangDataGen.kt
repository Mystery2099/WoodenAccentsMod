package com.mystery2099.datagen

import com.mystery2099.WoodenAccentsModItemGroups
import com.mystery2099.block.ModBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup

class EnglishLangDataGen(dataOutput: FabricDataOutput) : FabricLanguageProvider(dataOutput) {
    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        with(translationBuilder) {
            ModBlocks.blocks.forEach{it.genLangName(this)}
            WoodenAccentsModItemGroups.itemGroups.forEach{it.genLangName(this)}
        }

    }

    private fun ItemGroup.genLangName(translationBuilder: TranslationBuilder) {
        translationBuilder.add(this, "Wooden Accents: ${this.id.path.toName()}")
    }
    private fun Block.genLangName(translationBuilder: TranslationBuilder) {
        translationBuilder.add(this, this.lootTableId.path.replace("blocks/", "").toName())
    }
    private fun String?.toName(): String {
        return if (isNullOrEmpty()) ""
        else split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
    }
}