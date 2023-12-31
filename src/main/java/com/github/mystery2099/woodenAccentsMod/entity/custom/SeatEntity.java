package com.github.mystery2099.woodenAccentsMod.entity.custom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.Objects;

public class SeatEntity extends Entity {
    private static final ImmutableMap<EntityPose, ImmutableList<Integer>> DISMOUNT_FREE_Y_SPACES_NEEDED = ImmutableMap.of(EntityPose.STANDING, ImmutableList.of(0, 1, -1), EntityPose.CROUCHING, ImmutableList.of(0, 1, -1), EntityPose.SWIMMING, ImmutableList.of(0, 1));

    public SeatEntity(EntityType<SeatEntity> type, World world) {
        super(type, world);
        this.noClip = true; // make the entity ignore collisions
    }

    @Override
    public void tick() {
        super.tick();
        this.setYaw(this.getBlockStateAtPos().get(Properties.HORIZONTAL_FACING).asRotation());
        if (!this.hasPassengers()) {
            this.discard();
        }
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return super.canAddPassenger(passenger) && !passenger.hasVehicle();
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        } else {
            if (!this.world.isClient && !player.hasVehicle()) {
                player.startRiding(this);
            }
            return ActionResult.SUCCESS;
        }
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        super.updatePassengerPosition(passenger);
        if (this.hasPassenger(passenger)) {
            passenger.setBodyYaw(this.getYaw());
        }
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        var direction = this.getMovementDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.updatePassengerForDismount(passenger);
        }
        int[][] is = Dismounting.getDismountOffsets(direction);
        var blockPos = this.getBlockPos();
        var mutable = new BlockPos.Mutable();
        ImmutableList<EntityPose> immutableList = passenger.getPoses();
        for (EntityPose entityPose : immutableList) {
            EntityDimensions entityDimensions = passenger.getDimensions(entityPose);
            var f = Math.min(entityDimensions.width, 1.0f) / 2.0f;
            for (var i : Objects.requireNonNull(DISMOUNT_FREE_Y_SPACES_NEEDED.get(entityPose))) {
                for (int[] js : is) {
                    Vec3d vec3d;
                    mutable.set(blockPos.getX() + js[0], blockPos.getY() + i, blockPos.getZ() + js[1]);
                    var d = this.world.getDismountHeight(Dismounting.getCollisionShape(this.world, mutable), () -> Dismounting.getCollisionShape(this.world, mutable.down()));
                    if (!Dismounting.canDismountInBlock(d) || !Dismounting.canPlaceEntityAt(this.world, passenger, new Box(-f, 0.0, -f, f, entityDimensions.height, f).offset(vec3d = Vec3d.ofCenter(mutable, d))))
                        continue;
                    passenger.setPose(entityPose);
                    return vec3d;
                }
            }
        }
        var e = this.getBoundingBox().maxY;
        mutable.set(blockPos.getX(), e, blockPos.getZ());
        for (EntityPose entityPose2 : immutableList) {
            double g = passenger.getDimensions(entityPose2).height;
            int j = MathHelper.ceil(e - (double) mutable.getY() + g);
            double h = Dismounting.getCeilingHeight(mutable, j, pos -> this.world.getBlockState(pos).getCollisionShape(this.world, pos));
            if (!(e + g <= h)) continue;
            passenger.setPose(entityPose2);
            break;
        }
        return super.updatePassengerForDismount(passenger);
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
