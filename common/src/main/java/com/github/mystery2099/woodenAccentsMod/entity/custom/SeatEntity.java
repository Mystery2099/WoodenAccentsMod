package com.github.mystery2099.woodenAccentsMod.entity.custom;

import com.github.mystery2099.woodenAccentsMod.block.custom.ChairBlock;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class SeatEntity extends Entity {
    private static final ImmutableMap<Pose, ImmutableList<Integer>> DISMOUNT_FREE_Y_SPACES_NEEDED = ImmutableMap.of(Pose.STANDING, ImmutableList.of(0, 1, -1), Pose.CROUCHING, ImmutableList.of(0, 1, -1), Pose.SWIMMING, ImmutableList.of(0, 1));

    public SeatEntity(EntityType<SeatEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            return;
        }
        var state = this.getFeetBlockState();
        if (!(state.getBlock() instanceof ChairBlock) || !this.getPassengers().isEmpty()) {
            this.ejectPassengers();
            this.discard();
            return;
        }
        this.setYRot(state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot());
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return super.canAddPassenger(passenger) && !passenger.isPassenger();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else {
            if (!this.level().isClientSide && !player.isPassenger()) {
                player.startRiding(this);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        // Clear the seat's front edge with vanilla's downward-angled thighs.
        // Keep the rider height independent of this entity's tiny hitbox.
        return 1.0 / 16.0;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction positionUpdater) {
        super.positionRider(passenger, positionUpdater);
        if (this.hasPassenger(passenger)) {
            passenger.setYBodyRot(this.getYRot());
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        // Use Vanilla's vehicle-style dismount search so the rider is placed beside the chair.
        var direction = this.getMotionDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(passenger);
        }
        int[][] is = DismountHelper.offsetsForDirection(direction);
        var blockPos = this.blockPosition();
        var mutable = new BlockPos.MutableBlockPos();
        ImmutableList<Pose> immutableList = passenger.getDismountPoses();
        for (Pose entityPose : immutableList) {
            EntityDimensions entityDimensions = passenger.getDimensions(entityPose);
            var f = Math.min(entityDimensions.width, 1.0f) / 2.0f;
            for (var i : Objects.requireNonNull(DISMOUNT_FREE_Y_SPACES_NEEDED.get(entityPose))) {
                for (int[] js : is) {
                    Vec3 vec3d;
                    mutable.set(blockPos.getX() + js[0], blockPos.getY() + i, blockPos.getZ() + js[1]);
                    var d = this.level().getBlockFloorHeight(DismountHelper.nonClimbableShape(this.level(), mutable), () -> DismountHelper.nonClimbableShape(this.level(), mutable.below()));
                    if (!DismountHelper.isBlockFloorValid(d) || !DismountHelper.canDismountTo(this.level(), passenger, new AABB(-f, 0.0, -f, f, entityDimensions.height, f).move(vec3d = Vec3.upFromBottomCenterOf(mutable, d))))
                        continue;
                    passenger.setPose(entityPose);
                    return vec3d;
                }
            }
        }
        var e = this.getBoundingBox().maxY;
        mutable.set(blockPos.getX(), e, blockPos.getZ());
        for (Pose entityPose2 : immutableList) {
            double g = passenger.getDimensions(entityPose2).height;
            int j = Mth.ceil(e - (double) mutable.getY() + g);
            double h = DismountHelper.findCeilingFrom(mutable, j, pos -> this.level().getBlockState(pos).getCollisionShape(this.level(), pos));
            if (!(e + g <= h)) continue;
            passenger.setPose(entityPose2);
            break;
        }
        return super.getDismountLocationForPassenger(passenger);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {

    }
}
