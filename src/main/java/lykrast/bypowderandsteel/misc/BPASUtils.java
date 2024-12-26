package lykrast.bypowderandsteel.misc;

import lykrast.gunswithoutroses.registry.GWRAttributes;
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
}
