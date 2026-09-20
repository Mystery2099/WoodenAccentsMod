package com.github.mystery2099.woodenAccentsMod.fluid

import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState

infix fun FluidState?.isOf(fluid: Fluid): Boolean = this?.`is`(fluid) ?: false
