package com.mystery2099.datagen

import com.mystery2099.WoodenAccentsModItemGroups
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup

class EnglishLangDataGen(dataOutput: FabricDataOutput) : FabricLanguageProvider(dataOutput) {
    private var builder: TranslationBuilder? = null
    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        builder = translationBuilder

        basicNamedBlocks.forEach(this::genLangName)

        WoodenAccentsModItemGroups.itemGroups.forEach(this::genLangName)

    }

    private fun genLangName(itemGroup: ItemGroup) {
        builder!!.add(itemGroup, "Wooden Accents: ${itemGroup.id.path.toName()}")
    }
    private fun genLangName(block: Block) {
        builder!!.add(block, block.lootTableId.path.replace("blocks/", "").toName())
    }

    private fun String.toName(): String {
        val parts = this.split("_")
        var output = ""
        parts.forEach { str ->
            output += str.replaceFirstChar { it.uppercase() }.replace("_", " ") + " "
        }
        return output.removeSuffix(" ")
    }
    companion object {
        @JvmStatic
        val basicNamedBlocks = HashSet<Block>()
    }
}