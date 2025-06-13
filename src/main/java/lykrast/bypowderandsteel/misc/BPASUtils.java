package lykrast.bypowderandsteel.misc;

import java.util.EnumMap;
import java.util.UUID;

import lykrast.bypowderandsteel.registry.BPASItems;
import lykrast.gunswithoutroses.item.GunItem;
import lykrast.gunswithoutroses.registry.GWRAttributes;
import lykrast.gunswithoutroses.registry.GWRItems;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class BPASUtils {
	public static AttributeSupplier.Builder baseGunMobAttributes() {
		return Monster.createMonsterAttributes().add(GWRAttributes.dmgBase.get()).add(GWRAttributes.dmgTotal.get()).add(GWRAttributes.fireDelay.get()).add(GWRAttributes.spread.get())
				.add(GWRAttributes.knockback.get()).add(GWRAttributes.sniperMult.get()).add(GWRAttributes.shotgunProjectiles.get());
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
		return progress * progress;
	}

	public static float easeOutQuad(float progress) {
		progress = 1 - progress;
		return 1 - (progress * progress);
	}

	public static float easeInOut(float progress) {
		//https://math.stackexchange.com/questions/121720/ease-in-out-function/121755#121755
		//TODO hey maybe smoothstep is better? 3x^2-2x^3, less jagged and less maths but like dunno if I could spot the differences, will have to test when I use that
		float sq = progress * progress;
		return sq / (2 * (sq - progress) + 1);
	}

	//this is protected in humanoidmodel
	public static float rotlerpRad(float progress, float start, float end) {
		float f = (end - start) % Mth.TWO_PI;
		if (f < -Mth.PI) {
			f += Mth.TWO_PI;
		}

		if (f >= Mth.PI) {
			f -= Mth.TWO_PI;
		}

		return start + progress * f;
	}

	//ripped from ArmorItem cause it's static there
	private static final EnumMap<ArmorItem.Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266744_) -> {
		p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
		p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
		p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
		p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
	});

	public static UUID armorUUID(ArmorItem.Type slot) {
		return ARMOR_MODIFIER_UUID_PER_TYPE.get(slot);
	}

	public static void enchantOffhand(Mob mob, RandomSource random, float difficultyMult) {
		//mob's enchantSpawnedWeapon only does main hand but I got a few mobs with weapons in both hands, so this does offhand
		if (!mob.getOffhandItem().isEmpty() && random.nextFloat() < 0.25F * difficultyMult) {
			mob.setItemSlot(EquipmentSlot.OFFHAND, EnchantmentHelper.enchantItem(random, mob.getOffhandItem(), (int) (5 + difficultyMult * random.nextInt(18)), false));
		}
	}
}
