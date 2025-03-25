package lykrast.bypowderandsteel.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BilkshakeItem extends DrinkItem {

	public BilkshakeItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
		//so ok now there's a fucking curative items thing
		//I'm not messing around with that so I'm passing some fake milk to get the desired effect
		if (!world.isClientSide) entity.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
		return super.finishUsingItem(stack, world, entity);
	}

}
