package lykrast.bypowderandsteel.entity;

import lykrast.gunswithoutroses.item.IBullet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface GunMob {
	//This lets me just point to the item and be done with it
	<T extends Item & IBullet> T getBullet();
	default ItemStack getBulletStack() {
		//what do you mean it's ambiguous?? well that's why there's an "asItem" here
		return new ItemStack(getBullet().asItem());
	}
	double getAddedSpread();
}
