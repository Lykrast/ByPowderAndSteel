package lykrast.bypowderandsteel.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ElectronicLampBlock extends FaceAttachedHorizontalDirectionalBlock {
	private static final int RADIUS = 4, HEIGHT = 4;
	protected static final VoxelShape CEILING_AABB = Block.box(8 - RADIUS, 16 - HEIGHT, 8 - RADIUS, 8 + RADIUS, 16, 8 + RADIUS);
	protected static final VoxelShape FLOOR_AABB = Block.box(8 - RADIUS, 0, 8 - RADIUS, 8 + RADIUS, HEIGHT, 8 + RADIUS);
	protected static final VoxelShape NORTH_AABB = Block.box(8 - RADIUS, 8 - RADIUS, 16 - HEIGHT, 8 + RADIUS, 8 + RADIUS, 16);
	protected static final VoxelShape SOUTH_AABB = Block.box(8 - RADIUS, 8 - RADIUS, 0, 8 + RADIUS, 8 + RADIUS, HEIGHT);
	protected static final VoxelShape WEST_AABB = Block.box(16 - HEIGHT, 8 - RADIUS, 8 - RADIUS, 16, 8 + RADIUS, 8 + RADIUS);
	protected static final VoxelShape EAST_AABB = Block.box(0, 8 - RADIUS, 8 - RADIUS, HEIGHT, 8 + RADIUS, 8 + RADIUS);

	public ElectronicLampBlock(Properties props) {
		super(props);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.FLOOR));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		switch (state.getValue(FACE)) {
			default:
			case FLOOR:
				return FLOOR_AABB;
			case CEILING:
				return CEILING_AABB;
			case WALL:
				switch (state.getValue(FACING)) {
					case EAST:
						return EAST_AABB;
					case WEST:
						return WEST_AABB;
					case SOUTH:
						return SOUTH_AABB;
					case NORTH:
					case UP:
					case DOWN:
						return NORTH_AABB;
					default:
						throw new IncompatibleClassChangeError();
				}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACE, FACING);
	}

}
