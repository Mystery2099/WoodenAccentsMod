package com.mystery2099.wooden_accents_mod.entity.custom

import com.mystery2099.wooden_accents_mod.entity.ModEntities
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.world.World


class SeatEntity(world: World) : Entity(ModEntities.seatEntity, world) {
    init {
        setNoGravity(true)
    }

    override fun initDataTracker() {

    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

    override fun createSpawnPacket(): Packet<ClientPlayPacketListener> {
        return EntitySpawnS2CPacket(this)
    }

    override fun getMountedHeightOffset(): Double {
        return 0.0
    }

    override fun canStartRiding(entity: Entity): Boolean = entity.isPlayer && !hasPassengers()
    override fun tick() {
        if (!world.isClient) {
            if (passengerList.isEmpty() || world.isSpaceEmpty(this)) {
                remove(RemovalReason.DISCARDED)
                world.updateNeighbors(blockPos, world.getBlockState(blockPos).block)
            }
        }
    }
}