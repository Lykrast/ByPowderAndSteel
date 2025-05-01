package lykrast.bypowderandsteel.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PropulsionPadBlock extends Block {
	protected double speed;

	public PropulsionPadBlock(Properties prop, double speed) {
		super(prop);
		this.speed = speed;
	}

	//bounce logic based on slime blocks
	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallHeight) {
		if (entity.isSuppressingBounce()) super.fallOn(world, state, pos, entity, fallHeight);
		else entity.causeFallDamage(fallHeight, 0, world.damageSources().fall());
	}

	//that's where the slime block bounces, but I'm missing the blockstate context
//	@Override
//	public void updateEntityAfterFallOn(BlockGetter world, Entity entity) {
//		if (entity.isSuppressingBounce()) super.updateEntityAfterFallOn(world, entity);
//		else bounceUp(entity);
//	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
		if (!entity.isSteppingCarefully() && !entity.isSuppressingBounce()) bounce(entity, pos);
		super.stepOn(world, pos, state, entity);
	}

	protected void bounce(Entity entity, BlockPos pos) {
		centerEntity(entity, pos);
		entity.setDeltaMovement(0, speed, 0);
	}
	
	public static void centerEntity(Entity entity, BlockPos pos) {
		entity.setPos(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}

}
