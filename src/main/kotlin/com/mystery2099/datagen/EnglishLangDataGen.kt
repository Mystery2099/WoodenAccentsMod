package com.mystery2099.datagen

import com.mystery2099.WoodenAccentsModItemGroups
import com.mystery2099.block.ModBlocks
import com.mystery2099.block.custom.CoffeeTableBlock
import com.mystery2099.block.custom.enums.CoffeeTableType
import com.mystery2099.state.property.ModProperties
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup

class EnglishLangDataGen(dataOutput: FabricDataOutput) : FabricLanguageProvider(dataOutput) {
    private var builder: TranslationBuilder? = null
    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        builder = translationBuilder

        ModBlocks.blocks.forEach(::genLangName)

        WoodenAccentsModItemGroups.itemGroups.forEach(::genLangName)

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
}