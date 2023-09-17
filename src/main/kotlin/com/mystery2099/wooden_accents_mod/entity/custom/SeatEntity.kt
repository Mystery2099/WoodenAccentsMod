package com.mystery2099.wooden_accents_mod.entity.custom

import com.mystery2099.wooden_accents_mod.entity.ModEntities
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.World


class SeatEntity(world: World) : Entity(ModEntities.seatEntity, world) {
    init {
        setNoGravity(true)
    }
    constructor(world: World, source: BlockPos, yOffset: Double, direction: Direction) : this(world) {
        this.setPos(source.x + 0.5, source.y + yOffset, source.z + 0.5);
        this.setRotation(direction.opposite.asRotation(), 0F);
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

    override fun canStartRiding(entity: Entity): Boolean = true
    override fun tick() {
        if (!world.isClient) {
            if (passengerList.isEmpty() || world.isSpaceEmpty(null, this.boundingBox)) {
                remove(RemovalReason.DISCARDED)
                world.updateNeighbors(blockPos, world.getBlockState(blockPos).block)
            }
        }
    }

    companion object {
        fun create(world: World, pos: BlockPos, yOffset: Double, player: PlayerEntity, direction: Direction): ActionResult {
            if (!world.isClient()) {
                val seats: List<SeatEntity> = world.getEntitiesByClass(
                    SeatEntity::class.java,
                    Box(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), pos.x + 1.0, pos.y + 1.0, pos.z + 1.0)
                ) { true }
                if (seats.isEmpty()) {
                    val seat = SeatEntity(world, pos, yOffset, direction)
                    world.spawnEntity(seat)
                    player.startRiding(seat, false)
                }
            }
            return ActionResult.SUCCESS
        }
    }
}