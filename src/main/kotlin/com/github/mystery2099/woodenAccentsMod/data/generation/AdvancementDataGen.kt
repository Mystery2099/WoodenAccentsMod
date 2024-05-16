package com.github.mystery2099.woodenAccentsMod.data.generation

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import java.util.function.Consumer

class AdvancementDataGen(output: FabricDataOutput?) : FabricAdvancementProvider(output) {
    override fun generateAdvancement(consumer: Consumer<Advancement>?) {
    }
}