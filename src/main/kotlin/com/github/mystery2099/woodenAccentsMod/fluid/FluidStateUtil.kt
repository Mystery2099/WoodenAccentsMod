package com.github.mystery2099.woodenAccentsMod.fluid

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState

/**
 * Checks if the given [FluidState] object is of the specified [Fluid].
 *
 * @param fluid The [Fluid] to compare with the [FluidState].
 * @return `true` if the [FluidState] is of the specified [Fluid], `false` otherwise.
 */
infix fun FluidState?.isOf(fluid: Fluid): Boolean = this?.isOf(fluid) ?: false
