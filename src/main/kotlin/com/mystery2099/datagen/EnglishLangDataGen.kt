package com.mystery2099.datagen

import com.mystery2099.WoodenAccentsModItemGroups
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

class EnglishLangDataGen(dataOutput: FabricDataOutput) : FabricLanguageProvider(dataOutput) {
    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        WoodenAccentsModItemGroups.itemGroups.forEach { group ->
            translationBuilder.add(group, "Wooden Accents Mod: ${group.id.path.toName()}")
        }
    }

    private fun String.toName(): String {
        val parts = this.split("_")
        var output = ""
        parts.forEach { str ->
            output += str.replaceFirstChar { it.uppercase() }.replace("_", " ") + " "
        }
        return output.removeSuffix(" ")
    }
}