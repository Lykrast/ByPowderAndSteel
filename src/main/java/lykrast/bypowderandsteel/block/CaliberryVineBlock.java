package lykrast.bypowderandsteel.block;

import java.util.List;

import javax.annotation.Nullable;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

public class CaliberryVineBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
	//because vines apparently don't behave like I want, had to copy lots of stuff from ladders
	public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
	//vine hitboxes
	private static final VoxelShape EAST_AABB = Block.box(0, 0, 0, 1, 16, 16);
	private static final VoxelShape WEST_AABB = Block.box(15, 0, 0, 16, 16, 16);
	private static final VoxelShape SOUTH_AABB = Block.box(0, 0, 0, 16, 16, 1);
	private static final VoxelShape NORTH_AABB = Block.box(0, 0, 15, 16, 16, 16);

	public CaliberryVineBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		switch (state.getValue(FACING)) {
			case NORTH:
				return NORTH_AABB;
			case SOUTH:
				return SOUTH_AABB;
			case WEST:
				return WEST_AABB;
			case EAST:
			default:
				return EAST_AABB;
		}
	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		//same ageing logic as sweet berry bushes, but less chance cause less stages
		int age = state.getValue(AGE);
		if (age < 2 && world.getRawBrightness(pos.above(), 0) >= 9 && ForgeHooks.onCropsGrowPre(world, pos, state, random.nextInt(6) == 0)) {
			BlockState blockstate = state.setValue(AGE, Integer.valueOf(age + 1));
			world.setBlock(pos, blockstate, 2);
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
			net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state);
		}
		//growing down logic, simple part from the vine one cause it was too much hassle to get the horizontal stuff and shit
		//so it just grows down
		if (world.getRawBrightness(pos.above(), 0) >= 9 && ForgeHooks.onCropsGrowPre(world, pos, state, random.nextInt(8) == 0)) {
			if (random.nextBoolean()) {
				//up
				if (pos.getY() < world.getMaxBuildHeight() - 1) {
					BlockPos target = pos.above();
					if (world.getBlockState(target).isAir() && canSurvive(state, world, target)) world.setBlock(target, defaultBlockState().setValue(FACING, state.getValue(FACING)), 2);
				}
			}
			else {
				//down
				if (pos.getY() > world.getMinBuildHeight()) {
					BlockPos target = pos.below();
					//going below should always have the vine survive, where going up requires having a solid block
					if (world.getBlockState(target).isAir()) world.setBlock(target, defaultBlockState().setValue(FACING, state.getValue(FACING)), 2);
				}
			}
		}
	}

	private static final ResourceLocation HARVEST_LOOT = ByPowderAndSteel.rl("blocks/caliberry_vine_harvest");

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		//collecting like sweet berry
		int age = state.getValue(AGE);
		if (age < 2 && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
			return InteractionResult.PASS;
		}
		else if (age == 2) {
			//but with a loot table and aaaa why so many stuff to call
			if (!world.isClientSide) {
				LootParams lootparams = (new LootParams.Builder((ServerLevel) world)).withParameter(LootContextParams.ORIGIN, pos.getCenter())
						.withParameter(LootContextParams.TOOL, player.getItemInHand(hand)).withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
				LootTable loottable = world.getServer().getLootData().getLootTable(HARVEST_LOOT);
				List<ItemStack> list = loottable.getRandomItems(lootparams);
				for (ItemStack stack : list) popResourceFromFace(world, pos, state.getValue(FACING).getOpposite(), stack);
			}
			//TODO custom sound for subtitles
			world.playSound((Player) null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
			BlockState blockstate = state.setValue(AGE, 0);
			world.setBlock(pos, blockstate, 2);
			world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
			return InteractionResult.sidedSuccess(world.isClientSide);
		}
		else {
			return super.use(state, world, pos, player, hand, trace);
		}
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state, boolean client) {
		return state.getValue(AGE) < 2;
	}

	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		world.setBlock(pos, state.setValue(AGE, Math.min(2, state.getValue(AGE) + 1)), 2);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}

	//copied from atmospheric passion vines (which is very similar to ladder) cause copying vanilla vine did not work
	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		return canAttachTo(level, pos.relative(direction.getOpposite()), direction)
				|| (level.getBlockState(pos.above()).is(this) && level.getBlockState(pos.above()).getValue(FACING) == state.getValue(FACING));
	}

	//that too
	private boolean canAttachTo(BlockGetter level, BlockPos pos, Direction direction) {
		BlockState blockstate = level.getBlockState(pos);
		return blockstate.isFaceSturdy(level, pos, direction) || blockstate.is(BlockTags.LEAVES);
	}

	//this is ladder
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos) {
		if (!state.canSurvive(world, pos)) return Blocks.AIR.defaultBlockState();
		else return super.updateShape(state, direction, facingState, world, pos, facingPos);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		if (!context.replacingClickedOnBlock()) {
			BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite()));
			if (blockstate.is(this) && blockstate.getValue(FACING) == context.getClickedFace()) { return null; }
		}

		BlockState blockstate1 = this.defaultBlockState();
		LevelReader levelreader = context.getLevel();
		BlockPos blockpos = context.getClickedPos();

		for (Direction direction : context.getNearestLookingDirections()) {
			if (direction.getAxis().isHorizontal()) {
				blockstate1 = blockstate1.setValue(FACING, direction.getOpposite());
				if (blockstate1.canSurvive(levelreader, blockpos)) { return blockstate1; }
			}
		}

		return null;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54370_) {
		p_54370_.add(FACING, AGE);
	}

}
