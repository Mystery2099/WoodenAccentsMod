package com.github.mystery2099.woodenAccentsMod.block.custom;

import com.github.mystery2099.woodenAccentsMod.block.BlockStateConfigurer;
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.tinyremapper.extension.mixin.common.data.Pair;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractKitchenCounterBlock extends AbstractWaterloggableBlock {
    public static final EnumProperty<StairShape> SHAPE = Properties.STAIR_SHAPE;
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final VoxelShape TOP_SHAPE = createCuboidShape(0, 14, 0, 16, 16, 16);

    //Straight
    public static final VoxelShape NORTH_SHAPE = createCuboidShape(0, 0, 2, 16, 14, 16);
    public static final VoxelShape EAST_SHAPE = createCuboidShape(0, 0, 0, 14, 14, 16);
    public static final VoxelShape SOUTH_SHAPE = NORTH_SHAPE.offset(0, 0, -((double) 2 / 16));
    public static final VoxelShape WEST_SHAPE = EAST_SHAPE.offset((double) 2 / 16, 0, 0);

    //Inner Corners
    private static final VoxelShape NORTH_WEST_INNER = VoxelShapes.union(NORTH_SHAPE, WEST_SHAPE);
    private static final VoxelShape SOUTH_WEST_INNER = VoxelShapes.union(SOUTH_SHAPE, WEST_SHAPE);
    private static final VoxelShape SOUTH_EAST_INNER = VoxelShapes.union(SOUTH_SHAPE, EAST_SHAPE);
    private static final VoxelShape NORTH_EAST_INNER = VoxelShapes.union(NORTH_SHAPE, EAST_SHAPE);

    //Outer Corners
    private static final VoxelShape NORTH_EAST_OUTER = createCuboidShape(0, 0, 2, 14, 14, 16);
    private static final VoxelShape NORTH_WEST_OUTER = NORTH_EAST_OUTER.offset((double) 2 / 16, 0, 0);
    private static final VoxelShape SOUTH_EAST_OUTER = NORTH_EAST_OUTER.offset(0, 0, -((double) 2 / 16));
    private static final VoxelShape SOUTH_WEST_OUTER = NORTH_WEST_OUTER.offset(0, 0, -((double) 2 / 16));

    private static final Map<Pair<Direction, StairShape>, VoxelShape> SHAPE_MAP = new HashMap<>();

    private final Block topBlock, baseBlock;
    public AbstractKitchenCounterBlock(Block baseBlock, Block topBlock) {
        super(FabricBlockSettings.copyOf(baseBlock));
        this.baseBlock = baseBlock;
        this.topBlock = topBlock;
        this.setDefaultState(BlockStateConfigurer.with(getDefaultState(), c -> {
            c.setProperty(FACING, Direction.NORTH);
            c.setProperty(SHAPE, StairShape.STRAIGHT);
            return null;
        }));
        //North Shapes
        SHAPE_MAP.put(Pair.of(Direction.NORTH, StairShape.STRAIGHT), NORTH_SHAPE);
        SHAPE_MAP.put(Pair.of(Direction.NORTH, StairShape.INNER_LEFT), NORTH_WEST_INNER);
        SHAPE_MAP.put(Pair.of(Direction.NORTH, StairShape.INNER_RIGHT), NORTH_EAST_INNER);
        SHAPE_MAP.put(Pair.of(Direction.NORTH, StairShape.OUTER_LEFT), NORTH_WEST_OUTER);
        SHAPE_MAP.put(Pair.of(Direction.NORTH, StairShape.OUTER_RIGHT), NORTH_EAST_OUTER);

        //East Shapes
        SHAPE_MAP.put(Pair.of(Direction.EAST, StairShape.STRAIGHT), EAST_SHAPE);
        SHAPE_MAP.put(Pair.of(Direction.EAST, StairShape.INNER_LEFT), NORTH_EAST_INNER);
        SHAPE_MAP.put(Pair.of(Direction.EAST, StairShape.INNER_RIGHT), SOUTH_EAST_INNER);
        SHAPE_MAP.put(Pair.of(Direction.EAST, StairShape.OUTER_LEFT), NORTH_EAST_OUTER);
        SHAPE_MAP.put(Pair.of(Direction.EAST, StairShape.OUTER_RIGHT), SOUTH_EAST_OUTER);

        //South Shapes
        SHAPE_MAP.put(Pair.of(Direction.SOUTH, StairShape.STRAIGHT), SOUTH_SHAPE);
        SHAPE_MAP.put(Pair.of(Direction.SOUTH, StairShape.INNER_LEFT), SOUTH_EAST_INNER);
        SHAPE_MAP.put(Pair.of(Direction.SOUTH, StairShape.INNER_RIGHT), SOUTH_WEST_INNER);
        SHAPE_MAP.put(Pair.of(Direction.SOUTH, StairShape.OUTER_LEFT), SOUTH_EAST_OUTER);
        SHAPE_MAP.put(Pair.of(Direction.SOUTH, StairShape.OUTER_RIGHT), SOUTH_WEST_OUTER);

        //West Shapes
        SHAPE_MAP.put(Pair.of(Direction.WEST, StairShape.STRAIGHT), WEST_SHAPE);
        SHAPE_MAP.put(Pair.of(Direction.WEST, StairShape.INNER_LEFT), SOUTH_WEST_INNER);
        SHAPE_MAP.put(Pair.of(Direction.WEST, StairShape.INNER_RIGHT), NORTH_WEST_INNER);
        SHAPE_MAP.put(Pair.of(Direction.WEST, StairShape.OUTER_LEFT), SOUTH_WEST_OUTER);
        SHAPE_MAP.put(Pair.of(Direction.WEST, StairShape.OUTER_RIGHT), NORTH_WEST_OUTER);

    }

    private static StairShape getCounterShape(BlockState state, BlockView world, BlockPos pos) {
        Direction direction3;
        Direction direction2;
        var direction = state.get(FACING);
        var blockState = world.getBlockState(pos.offset(direction.getOpposite()));
        if (AbstractKitchenCounterBlock.isCounter(blockState) && (direction2 = blockState.get(FACING)).getAxis() != state.get(FACING).getAxis() && AbstractKitchenCounterBlock.isDifferentOrientation(state, world, pos, direction2)) {
            if (direction2 == direction.rotateYCounterclockwise()) {
                return StairShape.OUTER_LEFT;
            }
            return StairShape.OUTER_RIGHT;
        }
        var blockState2 = world.getBlockState(pos.offset(direction));
        if (AbstractKitchenCounterBlock.isCounter(blockState2) && (direction3 = blockState2.get(FACING)).getAxis() != state.get(FACING).getAxis() && AbstractKitchenCounterBlock.isDifferentOrientation(state, world, pos, direction3.getOpposite())) {
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
        return VoxelShapes.union(TOP_SHAPE, SHAPE_MAP.get(
                Pair.of(state.get(FACING), state.get(SHAPE))
        ));
    }

    @Override
    protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, SHAPE);
    }

    @Override
    public @NotNull BlockState getPlacementState(ItemPlacementContext ctx) {
        var blockPos = ctx.getBlockPos();
        var blockState = Objects.requireNonNull(super.getPlacementState(ctx)).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
        return blockState.with(SHAPE, getCounterShape(blockState, ctx.getWorld(), blockPos));
    }

    @Deprecated
    @Override
    public @NotNull BlockState getStateForNeighborUpdate(@NotNull BlockState state, Direction direction, BlockState neighborState, @NotNull WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        var stateForNeighborUpdate = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        if (direction.getAxis().isHorizontal()) {
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

    @Deprecated @Override
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
