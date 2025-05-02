package lykrast.bypowderandsteel.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class PropulsionPadDirectionalBlock extends HorizontalFacingBlock {
	protected double speedHorizontal, speedVertical;
	protected boolean diagonal;
	protected static final double DIAGSQRT = Math.sqrt(2)/2.0;
	
	public PropulsionPadDirectionalBlock(Properties properties, double speedHorizontal, double speedVertical, boolean diagonal) {
		super(properties);
		this.speedHorizontal = speedHorizontal;
		this.speedVertical = speedVertical;
		this.diagonal = diagonal;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Vec3 clicked = context.getClickLocation();
		BlockPos pos = context.getClickedPos();
		//0 is center of block face
		double relX = clicked.x - pos.getX() - 0.5, relZ = clicked.z - pos.getZ() - 0.5;
		if (diagonal) {
			//diagonal, cut the block in 4 squares like the tile blocks
			//facing is diagonal with the counterclockwise, so top left click should be north, bottom left west...
			Direction diag;
			if (relX < 0) diag = (relZ < 0) ? Direction.NORTH : Direction.WEST;
			else diag = (relZ < 0) ? Direction.EAST : Direction.SOUTH;
			return defaultBlockState().setValue(FACING, diag);
		}
		//orthogonal, like cut the block along the diagonals and assign the arrows intuitively
		else return defaultBlockState().setValue(FACING, Direction.getNearest(relX, 0, relZ));
	}

	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallHeight) {
		if (entity.isSuppressingBounce()) super.fallOn(world, state, pos, entity, fallHeight);
		else entity.causeFallDamage(fallHeight, 0, world.damageSources().fall());
	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
		if (!entity.isSteppingCarefully() && !entity.isSuppressingBounce()) bounce(entity, state, pos);
		super.stepOn(world, pos, state, entity);
	}

	protected void bounce(Entity entity, BlockState state, BlockPos pos) {
		PropulsionPadBlock.centerEntity(entity, pos);
		Direction dir = state.getValue(FACING);
		if (diagonal) {
			Direction second = dir.getCounterClockWise();
			entity.setDeltaMovement((dir.getStepX()+second.getStepX())*speedHorizontal*DIAGSQRT, speedVertical, (dir.getStepZ()+second.getStepZ())*speedHorizontal*DIAGSQRT);
		}
		else entity.setDeltaMovement(dir.getStepX()*speedHorizontal, speedVertical, dir.getStepZ()*speedHorizontal);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(Component.translatable("block.bypowderandsteel.gravitic_propulsion_pad.oriented.desc").withStyle(ChatFormatting.GRAY));
	}

}
