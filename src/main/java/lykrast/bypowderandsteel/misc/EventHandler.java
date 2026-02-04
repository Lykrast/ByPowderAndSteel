package lykrast.bypowderandsteel.misc;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.item.MarksbladeItem;
import lykrast.bypowderandsteel.registry.BPASEffects;
import lykrast.bypowderandsteel.registry.BPASSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ByPowderAndSteel.MODID)
public class EventHandler {
	private static final TagKey<DamageType> BULLET_DMG = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ByPowderAndSteel.GWR_MODID, "is_bullet"));

	@SubscribeEvent
	public static void entityDamage(final LivingHurtEvent event) {
		if (event.isCanceled()) return;
		//Marked bonus damage
		LivingEntity attacked = event.getEntity();
		if (attacked.hasEffect(BPASEffects.marked.get()) && event.getSource().is(BULLET_DMG)) {
			event.setAmount(event.getAmount() + MarksbladeItem.MARKED_BASE_DMG + attacked.getEffect(BPASEffects.marked.get()).getAmplifier());
			attacked.removeEffect(BPASEffects.marked.get());
			Entity attacker = event.getSource().getEntity();
			if (attacker != null) attacker.level().playSound(null, attacked.blockPosition(), BPASSounds.marksbladeProc.get(), attacker.getSoundSource(), 1, 1);
			else attacked.level().playSound(null, attacked.blockPosition(), BPASSounds.marksbladeProc.get(), attacked.getSoundSource(), 1, 1);
		}
	}
	
}
