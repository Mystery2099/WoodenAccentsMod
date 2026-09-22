package com.github.mystery2099.woodenAccentsMod.item.group

import com.github.mystery2099.woodenAccentsMod.WoodenAccentsMod.toIdentifier
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab

/** Fabric-backed creative tab for a shared [ModItemGroup]. */
data class CustomItemGroup(val group: ModItemGroup) {
    val name: String get() = group.path

    val key: ResourceKey<CreativeModeTab> = ResourceKey.create(Registries.CREATIVE_MODE_TAB, name.toIdentifier())

    fun register(): CreativeModeTab = Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB,
        key,
        FabricItemGroup.builder().apply {
            icon { ItemGroupContent.getEntries(group).first() }
            title(Component.translatable(name.toIdentifier().toLanguageKey()))
        }.build()
    )
}
