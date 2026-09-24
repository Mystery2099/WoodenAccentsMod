package com.github.mystery2099.woodenAccentsMod.block.custom;

import com.github.mystery2099.woodenAccentsMod.block.BlockStateConfigurer;
import com.github.mystery2099.woodenAccentsMod.registry.tag.ModBlockTags;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("deprecation")
public abstract class AbstractKitchenCounterBlock extends AbstractWaterloggableBlock {
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final VoxelShape TOP_SHAPE = box(0, 14, 0, 16, 16, 16);

    // Straight counters
    public static final VoxelShape NORTH_SHAPE = box(0, 0, 2, 16, 14, 16);
    public static final VoxelShape EAST_SHAPE = box(0, 0, 0, 14, 14, 16);
    public static final VoxelShape SOUTH_SHAPE = NORTH_SHAPE.move(0, 0, -((double) 2 / 16));
    public static final VoxelShape WEST_SHAPE = EAST_SHAPE.move((double) 2 / 16, 0, 0);

    // Inner corners
    private static final VoxelShape NORTH_WEST_INNER = Shapes.or(NORTH_SHAPE, WEST_SHAPE);
    private static final VoxelShape SOUTH_WEST_INNER = Shapes.or(SOUTH_SHAPE, WEST_SHAPE);
    private static final VoxelShape SOUTH_EAST_INNER = Shapes.or(SOUTH_SHAPE, EAST_SHAPE);
    private static final VoxelShape NORTH_EAST_INNER = Shapes.or(NORTH_SHAPE, EAST_SHAPE);

    // Outer corners
    private static final VoxelShape NORTH_EAST_OUTER = box(0, 0, 2, 14, 14, 16);
    private static final VoxelShape NORTH_WEST_OUTER = NORTH_EAST_OUTER.move((double) 2 / 16, 0, 0);
    private static final VoxelShape SOUTH_EAST_OUTER = NORTH_EAST_OUTER.move(0, 0, -((double) 2 / 16));
    private static final VoxelShape SOUTH_WEST_OUTER = NORTH_WEST_OUTER.move(0, 0, -((double) 2 / 16));

    private static final Map<Pair<Direction, StairsShape>, VoxelShape> SHAPE_MAP = createShapeMap();
    private static final Map<Pair<Direction, StairsShape>, VoxelShape> OUTLINE_SHAPE_MAP = createOutlineShapeMap();

    protected final Block topBlock, baseBlock;
    public AbstractKitchenCounterBlock(Block baseBlock, Block topBlock) {
        super(BlockBehaviour.Properties.ofFullCopy(baseBlock));
        this.baseBlock = baseBlock;
        this.topBlock = topBlock;
        this.registerDefaultState(BlockStateConfigurer.with(defaultBlockState(), c -> {
            c.setProperty(FACING, Direction.NORTH);
            c.setProperty(SHAPE, StairsShape.STRAIGHT);
            return null;
        }));
    }

    private static Map<Pair<Direction, StairsShape>, VoxelShape> createShapeMap() {
        Map<Pair<Direction, StairsShape>, VoxelShape> shapes = new HashMap<>();
        shapes.put(Pair.of(Direction.NORTH, StairsShape.STRAIGHT), NORTH_SHAPE);
        shapes.put(Pair.of(Direction.NORTH, StairsShape.INNER_LEFT), NORTH_WEST_INNER);
        shapes.put(Pair.of(Direction.NORTH, StairsShape.INNER_RIGHT), NORTH_EAST_INNER);
        shapes.put(Pair.of(Direction.NORTH, StairsShape.OUTER_LEFT), NORTH_WEST_OUTER);
        shapes.put(Pair.of(Direction.NORTH, StairsShape.OUTER_RIGHT), NORTH_EAST_OUTER);

        shapes.put(Pair.of(Direction.EAST, StairsShape.STRAIGHT), EAST_SHAPE);
        shapes.put(Pair.of(Direction.EAST, StairsShape.INNER_LEFT), NORTH_EAST_INNER);
        shapes.put(Pair.of(Direction.EAST, StairsShape.INNER_RIGHT), SOUTH_EAST_INNER);
        shapes.put(Pair.of(Direction.EAST, StairsShape.OUTER_LEFT), NORTH_EAST_OUTER);
        shapes.put(Pair.of(Direction.EAST, StairsShape.OUTER_RIGHT), SOUTH_EAST_OUTER);

        shapes.put(Pair.of(Direction.SOUTH, StairsShape.STRAIGHT), SOUTH_SHAPE);
        shapes.put(Pair.of(Direction.SOUTH, StairsShape.INNER_LEFT), SOUTH_EAST_INNER);
        shapes.put(Pair.of(Direction.SOUTH, StairsShape.INNER_RIGHT), SOUTH_WEST_INNER);
        shapes.put(Pair.of(Direction.SOUTH, StairsShape.OUTER_LEFT), SOUTH_EAST_OUTER);
        shapes.put(Pair.of(Direction.SOUTH, StairsShape.OUTER_RIGHT), SOUTH_WEST_OUTER);

        shapes.put(Pair.of(Direction.WEST, StairsShape.STRAIGHT), WEST_SHAPE);
        shapes.put(Pair.of(Direction.WEST, StairsShape.INNER_LEFT), SOUTH_WEST_INNER);
        shapes.put(Pair.of(Direction.WEST, StairsShape.INNER_RIGHT), NORTH_WEST_INNER);
        shapes.put(Pair.of(Direction.WEST, StairsShape.OUTER_LEFT), SOUTH_WEST_OUTER);
        shapes.put(Pair.of(Direction.WEST, StairsShape.OUTER_RIGHT), NORTH_WEST_OUTER);
        return Map.copyOf(shapes);
    }

    private static Map<Pair<Direction, StairsShape>, VoxelShape> createOutlineShapeMap() {
        Map<Pair<Direction, StairsShape>, VoxelShape> outlineShapes = new HashMap<>();
        SHAPE_MAP.forEach((key, shape) -> outlineShapes.put(key, Shapes.or(TOP_SHAPE, shape)));
        return Map.copyOf(outlineShapes);
    }

    private static StairsShape getCounterShape(BlockState state, BlockGetter world, BlockPos pos) {
        Direction direction3;
        Direction direction2;
        var direction = state.getValue(FACING);
        var blockState = world.getBlockState(pos.relative(direction.getOpposite()));
        if (AbstractKitchenCounterBlock.isCounter(blockState) && (direction2 = blockState.getValue(FACING)).getAxis() != state.getValue(FACING).getAxis() && AbstractKitchenCounterBlock.isDifferentOrientation(state, world, pos, direction2)) {
            if (direction2 == direction.getCounterClockWise()) {
                return StairsShape.OUTER_LEFT;
            }
            return StairsShape.OUTER_RIGHT;
        }
        var blockState2 = world.getBlockState(pos.relative(direction));
        if (AbstractKitchenCounterBlock.isCounter(blockState2) && (direction3 = blockState2.getValue(FACING)).getAxis() != state.getValue(FACING).getAxis() && AbstractKitchenCounterBlock.isDifferentOrientation(state, world, pos, direction3.getOpposite())) {
            if (direction3 == direction.getCounterClockWise()) {
                return StairsShape.INNER_LEFT;
            }
            return StairsShape.INNER_RIGHT;
        }
        return StairsShape.STRAIGHT;
    }

    public static boolean isCounter(BlockState state) {
        return canConnectTo(state);
    }

    private static boolean canConnectTo(BlockState blockState) {
        return blockState.is(ModBlockTags.getKitchenCounters());
    }

    private static boolean isDifferentOrientation(BlockState state, BlockGetter world, BlockPos pos, Direction dir) {
        var blockState = world.getBlockState(pos.relative(dir));
        return !canConnectTo(blockState) || blockState.getValue(FACING) != state.getValue(FACING);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return OUTLINE_SHAPE_MAP.get(
                Pair.of(state.getValue(FACING), state.getValue(SHAPE))
        );
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, SHAPE);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var blockPos = ctx.getClickedPos();
        var blockState = Objects.requireNonNull(super.getStateForPlacement(ctx)).setValue(FACING, ctx.getHorizontalDirection().getOpposite());
        return blockState.setValue(SHAPE, getCounterShape(blockState, ctx.getLevel(), blockPos));
    }

    @Deprecated
    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(@NotNull BlockState state, Direction direction, BlockState neighborState, @NotNull LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        var stateForNeighborUpdate = super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        if (direction.getAxis().isHorizontal()) {
            return stateForNeighborUpdate.setValue(SHAPE, getCounterShape(state, world, pos));
        }
        return stateForNeighborUpdate;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror) {
        var direction = state.getValue(FACING);
        var stairShape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT -> {
                if (direction.getAxis() != Direction.Axis.Z) break;
                switch (stairShape) {
                    case INNER_LEFT -> {
                        return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                    }
                    case INNER_RIGHT -> {
                        return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                    }
                    case OUTER_LEFT -> {
                        return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                    }
                    case OUTER_RIGHT -> {
                        return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                    }
                }
                return state.rotate(Rotation.CLOCKWISE_180);
            }
            case FRONT_BACK -> {
                if (direction.getAxis() != Direction.Axis.X) break;
                switch (stairShape) {
                    case INNER_LEFT -> {
                        return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                    }
                    case INNER_RIGHT -> {
                        return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                    }
                    case OUTER_LEFT -> {
                        return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                    }
                    case OUTER_RIGHT -> {
                        return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                    }
                    case STRAIGHT -> {
                        return state.rotate(Rotation.CLOCKWISE_180);
                    }
                }
            }
        }
        return super.mirror(state, mirror);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (state.is(state.getBlock())) return;
        world.neighborChanged(this.baseBlock.defaultBlockState(), pos, Blocks.AIR, pos, false);
        // BlockBehaviour.onPlace is protected on other instances in modern versions, so instead
        // replicate the base block's observable place sound; behavior like neighbor updates is
        // covered by the call above.
        SoundType baseSound = this.baseBlock.defaultBlockState().getSoundType();
        world.playSound(null, pos, baseSound.getPlaceSound(), SoundSource.BLOCKS, baseSound.getVolume(), baseSound.getPitch());
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.is(newState.getBlock())) return;
        this.baseBlock.defaultBlockState().onRemove(world, pos, newState, moved);
    }

    public Block getTopBlock() {
        return topBlock;
    }

    public Block getBaseBlock() {
        return baseBlock;
    }
}
