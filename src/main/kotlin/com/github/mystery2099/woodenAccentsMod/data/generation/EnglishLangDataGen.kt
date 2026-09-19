package com.github.mystery2099.woodenAccentsMod.data.generation

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import com.github.mystery2099.woodenAccentsMod.block.ModBlocks
import com.github.mystery2099.woodenAccentsMod.block.id
import com.github.mystery2099.woodenAccentsMod.item.group.CustomItemGroup
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

class EnglishLangDataGen(dataOutput: FabricDataOutput) : FabricLanguageProvider(dataOutput) {
    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        translationBuilder.run {
            ModBlocks.blocks.forEach {
                translationBuilder.add(it, it.id.path.toDisplayName())
            }
            CustomItemGroup.instances.forEach {
                val name = when (it.name) {
                    "decorations" -> "Furniture"
                    "miscellaneous" -> "Storage"
                    else -> "Building"
                }
                add(it.name.toIdentifier().toTranslationKey(), "Wooden Accents: $name")
            }
            add("container.crate.more", "and %s more...")
        }
    }

    private fun String.toDisplayName(): String {
        val displayPath = when {
            startsWith("modern_") -> removePrefix("modern_").replace("fence", "picket_fence")
            endsWith("_plank_carpet") -> replace("_plank_carpet", "_plank_flooring")
            endsWith("_carpet") -> replace("_carpet", "_flooring")
            endsWith("_bookshelf") -> replace("_bookshelf", "_narrow_bookshelf")
            else -> this
        }
        return displayPath.toName()
    }

    private fun String?.toName(): String {
        return if (isNullOrEmpty()) ""
        else lowercase().split("_").joinToString(" ") {
            if (it != "of") {
                it.replaceFirstChar(Char::uppercase)
            } else it
        }
    }

}
