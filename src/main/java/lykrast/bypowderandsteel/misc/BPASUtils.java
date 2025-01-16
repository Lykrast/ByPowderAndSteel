package lykrast.bypowderandsteel.misc;

import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.gunswithoutroses.item.GunItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
import lykrast.gunswithoutroses.registry.GWRItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;

public class BPASUtils {
	public static AttributeSupplier.Builder baseGunMobAttributes() {
		return Monster.createMonsterAttributes()
				.add(GWRAttributes.dmgBase.get())
				.add(GWRAttributes.dmgTotal.get())
				.add(GWRAttributes.fireDelay.get())
				.add(GWRAttributes.spread.get())
				.add(GWRAttributes.knockback.get())
				.add(GWRAttributes.sniperMult.get())
				.add(GWRAttributes.shotgunProjectiles.get());
	}
	
	/**
	 * Iron, Gold, and Gunsteel have same stats for mobs, so 45% iron, 45% gunsteel, 10% gold
	 */
	public static GunItem randomDefaultGun(RandomSource random) {
		if (random.nextFloat() < 0.1) return GWRItems.goldGun.get();
		else return random.nextBoolean() ? GWRItems.ironGun.get() : BPASItems.gunsteelGun.get();
	}
	
	//those take a float from 0 to 1, for animation smoothing
	public static float easeInQuad(float progress) {
		return progress*progress;
	}
	public static float easeOutQuad(float progress) {
		progress = 1-progress;
		return 1-(progress*progress);
	}
	public static float easeInOut(float progress) {
		//https://math.stackexchange.com/questions/121720/ease-in-out-function/121755#121755
		float sq = progress * progress;
		return sq / (2 * (sq - progress)+1);
	}
}
