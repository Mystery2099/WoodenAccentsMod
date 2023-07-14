package com.mystery2099.block.custom;

import com.mystery2099.WoodenAccentsModItemGroups;
import com.mystery2099.data.ModBlockTags;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.StairShape;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class KitchenCounterBlock extends AbstractWaterloggableBlock{
    public static final EnumProperty<StairShape> SHAPE = Properties.STAIR_SHAPE;
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final VoxelShape TOP_SHAPE = Block.createCuboidShape(0, 14, 0, 16, 16, 16);
    public static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0, 0, 2, 16, 14, 16);
    public static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0, 0, 0, 14, 14, 16);
    public static final VoxelShape SOUTH_SHAPE = NORTH_SHAPE.offset(0, 0, -((double) 2 / 16));
    public static final VoxelShape WEST_SHAPE = EAST_SHAPE.offset((double) 2 / 16, 0, 0);

    private final Block topBlock, baseBlock;

    public KitchenCounterBlock(Block baseBlock, Block topBlock) {
        super(FabricBlockSettings.copyOf(baseBlock));
        this.baseBlock = baseBlock;
        this.topBlock = topBlock;
        WoodenAccentsModItemGroups.getKitchenItems().add(this);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(SHAPE, StairShape.STRAIGHT));
    }

    private static StairShape getCounterShape(BlockState state, BlockView world, BlockPos pos) {
        Direction direction3;
        Direction direction2;
        Direction direction = state.get(FACING);
        BlockState blockState = world.getBlockState(pos.offset(direction.getOpposite()));
        if (KitchenCounterBlock.isCounter(blockState) && (direction2 = blockState.get(FACING)).getAxis() != state.get(FACING).getAxis() && KitchenCounterBlock.isDifferentOrientation(state, world, pos, direction2)) {
            if (direction2 == direction.rotateYCounterclockwise()) {
                return StairShape.OUTER_LEFT;
            }
            return StairShape.OUTER_RIGHT;
        }
        BlockState blockState2 = world.getBlockState(pos.offset(direction));
        if (KitchenCounterBlock.isCounter(blockState2) && (direction3 = blockState2.get(FACING)).getAxis() != state.get(FACING).getAxis() && KitchenCounterBlock.isDifferentOrientation(state, world, pos, direction3.getOpposite())) {
            if (direction3 == direction.rotateYCounterclockwise()) {
                return StairShape.INNER_LEFT;
            }
            return StairShape.INNER_RIGHT;
        }
        return StairShape.STRAIGHT;
    }
    public static boolean isCounter(BlockState state) {
        return canConnectTo(state);
    }

    private static boolean canConnectTo(BlockState blockState) {
        return blockState.isIn(ModBlockTags.getKitchenCounters());
    }

    private static boolean isDifferentOrientation(BlockState state, BlockView world, BlockPos pos, Direction dir) {
        var blockState = world.getBlockState(pos.offset(dir));
        return !canConnectTo(blockState) || blockState.get(FACING) != state.get(FACING);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        var northWest = VoxelShapes.union(NORTH_SHAPE, WEST_SHAPE);
        var southWest = VoxelShapes.union(SOUTH_SHAPE, WEST_SHAPE);
        var southEast = VoxelShapes.union(SOUTH_SHAPE, EAST_SHAPE);
        var northEast = VoxelShapes.union(NORTH_SHAPE, EAST_SHAPE);

        var northOuterLeft = Block.createCuboidShape(0, 0, 2, 14, 14, 16);
        var northOuterRight = northOuterLeft.offset((double) 2/16, 0, 0);

        var eastOuterLeft = Block.createCuboidShape(0, 0, 0, 14, 14, 14);
        var eastOuterRight = eastOuterLeft.offset(0, 0, (double) 2/16);

        var southOuterLeft = northOuterRight.offset(0, 0, -((double) 2 / 16));
        var southOuterRight = northOuterLeft.offset(0, 0, -((double) 2 / 16));

        var westOuterLeft = eastOuterRight.offset((double) 2 / 16, 0, 0);
        var westOuterRight = eastOuterLeft.offset((double) 2 / 16, 0, 0);


        var stairShape = state.get(SHAPE);
        return VoxelShapes.union(TOP_SHAPE, switch (state.get(FACING)) {
            case NORTH -> switch (stairShape) {
                case STRAIGHT -> NORTH_SHAPE;
                case INNER_LEFT -> northWest;
                case INNER_RIGHT -> northEast;
                case OUTER_LEFT -> northOuterRight;
                case OUTER_RIGHT -> northOuterLeft;
            };
            case SOUTH -> switch (stairShape) {
                case STRAIGHT -> SOUTH_SHAPE;
                case INNER_LEFT -> southEast;
                case INNER_RIGHT -> southWest;
                case OUTER_LEFT -> eastOuterLeft;
                case OUTER_RIGHT -> southOuterLeft;
            };
            case EAST -> switch (stairShape) {
                case STRAIGHT -> EAST_SHAPE;
                case INNER_LEFT -> northEast;
                case INNER_RIGHT -> southEast;
                case OUTER_LEFT -> northOuterLeft;
                case OUTER_RIGHT -> eastOuterLeft;
            };
            case WEST -> switch (stairShape) {
                case STRAIGHT -> WEST_SHAPE;
                case INNER_LEFT -> southWest;
                case INNER_RIGHT -> northWest;
                case OUTER_LEFT -> southOuterLeft;
                case OUTER_RIGHT -> northOuterRight;
            };
            default -> VoxelShapes.fullCube();
        });
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, SHAPE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var blockPos = ctx.getBlockPos();
        var blockState = Objects.requireNonNull(super.getPlacementState(ctx)).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
        return blockState.with(SHAPE, getCounterShape(blockState, ctx.getWorld(), blockPos));
    }

    @Override
    public BlockState getStateForNeighborUpdate(@NotNull BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        var stateForNeighborUpdate = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        if (direction.getAxis().isHorizontal()) {
            assert stateForNeighborUpdate != null;
            return stateForNeighborUpdate.with(SHAPE, getCounterShape(state, world, pos));
        }
        return stateForNeighborUpdate;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        var direction = state.get(FACING);
        var stairShape = state.get(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT -> {
                if (direction.getAxis() != Direction.Axis.Z) break;
                switch (stairShape) {
                    case INNER_LEFT -> {
                        return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_RIGHT);
                    }
                    case INNER_RIGHT -> {
                        return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_LEFT);
                    }
                    case OUTER_LEFT -> {
                        return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_RIGHT);
                    }
                    case OUTER_RIGHT -> {
                        return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_LEFT);
                    }
                }
                return state.rotate(BlockRotation.CLOCKWISE_180);
            }
            case FRONT_BACK -> {
                if (direction.getAxis() != Direction.Axis.X) break;
                switch (stairShape) {
                    case INNER_LEFT -> {
                        return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_LEFT);
                    }
                    case INNER_RIGHT -> {
                        return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_RIGHT);
                    }
                    case OUTER_LEFT -> {
                        return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_RIGHT);
                    }
                    case OUTER_RIGHT -> {
                        return state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.OUTER_LEFT);
                    }
                    case STRAIGHT -> {
                        return state.rotate(BlockRotation.CLOCKWISE_180);
                    }
                }
            }
        }
        return super.mirror(state, mirror);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (state.isOf(state.getBlock())) return;
        world.updateNeighbor(this.baseBlock.getDefaultState(), pos, Blocks.AIR, pos, false);
        this.baseBlock.onBlockAdded(this.baseBlock.getDefaultState(), world, pos, oldState, false);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) return;
        this.baseBlock.getDefaultState().onStateReplaced(world, pos, newState, moved);
    }

    public Block getTopBlock() {
        return topBlock;
    }

    public Block getBaseBlock() {
        return baseBlock;
    }
}
