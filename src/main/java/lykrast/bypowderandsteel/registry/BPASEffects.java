package lykrast.bypowderandsteel.registry;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BPASEffects {
	public static RegistryObject<MobEffect> marked;
	public static final DeferredRegister<MobEffect> REG = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ByPowderAndSteel.MODID);
	
	static {
		marked = REG.register("marked", () -> new Effect(true, 0xFF0606));
	}
	
	public static class Effect extends MobEffect {
		//WHAT DO YOU MEAN THE CONSTRUCTOR IS PROTECTED???
		public Effect(boolean good, int color) {
			super(good ? MobEffectCategory.BENEFICIAL : MobEffectCategory.HARMFUL, color);
		}
		
	}
}
