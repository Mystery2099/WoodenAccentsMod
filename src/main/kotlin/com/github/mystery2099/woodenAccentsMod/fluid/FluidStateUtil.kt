package com.github.mystery2099.woodenAccentsMod.fluid

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState

infix fun FluidState?.isOf(fluid: Fluid): Boolean = this?.isOf(fluid) ?: false
