package com.mystery2099.wooden_accents_mod.block.custom;

import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomBlockStateProvider;
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomItemGroupProvider;
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomRecipeProvider;
import com.mystery2099.wooden_accents_mod.block.custom.interfaces.CustomTagProvider;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractDeskBlock extends AbstractWaterloggableBlock implements
        CustomItemGroupProvider, CustomTagProvider, CustomRecipeProvider, CustomBlockStateProvider {
    public static final EnumProperty<StairShape> SHAPE = Properties.STAIR_SHAPE;
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    final Block topBlock, baseBlock;

    public AbstractDeskBlock(FabricBlockSettings settings, Block baseBlock, Block topBlock) {
        super(settings);
        this.topBlock = topBlock;
        this.baseBlock = baseBlock;
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(SHAPE, StairShape.STRAIGHT));
    }

    private StairShape getDeskShape(BlockState state, BlockView world, BlockPos pos) {
        Direction direction2;
        var direction = state.get(FACING);
        var blockState2 = world.getBlockState(pos.offset(direction));
        if (isDesk(blockState2) && (direction2 = blockState2.get(FACING)).getAxis() != state.get(FACING).getAxis() && isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
            return (direction2 == direction.rotateYCounterclockwise()) ? StairShape.INNER_LEFT : StairShape.INNER_RIGHT;
        }
        return StairShape.STRAIGHT;
    }

    public boolean isDesk(BlockState state) {
        return canConnectTo(state);
    }

    private boolean canConnectTo(BlockState blockState) {
        return blockState.isIn(getTag());
    }

    private boolean isDifferentOrientation(BlockState state, BlockView world, BlockPos pos, Direction dir) {
        var blockState = world.getBlockState(pos.offset(dir));
        return !canConnectTo(blockState) || blockState.get(FACING) != state.get(FACING);
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
        return blockState.with(SHAPE, getDeskShape(blockState, ctx.getWorld(), blockPos));
    }

    @Override
    public BlockState getStateForNeighborUpdate(@NotNull BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        var stateForNeighborUpdate = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        if (direction.getAxis().isHorizontal()) {
            assert stateForNeighborUpdate != null;
            return stateForNeighborUpdate.with(SHAPE, getDeskShape(state, world, pos));
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
                return switch (stairShape) {
                    case INNER_LEFT -> state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_RIGHT);
                    case INNER_RIGHT -> state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_LEFT);
                    default -> state.rotate(BlockRotation.CLOCKWISE_180);
                };
            }
            case FRONT_BACK -> {
                if (direction.getAxis() != Direction.Axis.X) break;
                return switch (stairShape) {
                    case INNER_LEFT -> state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_LEFT);
                    case INNER_RIGHT -> state.rotate(BlockRotation.CLOCKWISE_180).with(SHAPE, StairShape.INNER_RIGHT);
                    default -> state.rotate(BlockRotation.CLOCKWISE_180);
                };
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
