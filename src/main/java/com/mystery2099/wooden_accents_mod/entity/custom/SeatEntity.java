package com.mystery2099.wooden_accents_mod.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SeatEntity extends Entity {
    public SeatEntity(EntityType<SeatEntity> type, World world) {
        super(type, world);
        this.noClip = true; // make the entity ignore collisions
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.hasPassengers()) {
            this.discard(); // remove the entity if it has no passengers
        }
    }

    @Override
    public double getMountedHeightOffset() {
        return super.getMountedHeightOffset() * 4;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        } else {
            if (!this.world.isClient) {
                player.startRiding(this);
            }
            return ActionResult.SUCCESS;
        }
    }

    @Override
    public void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
