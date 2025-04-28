package lykrast.bypowderandsteel.registry;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class BPASDamage {
	//Copying Alex's caves again for this https://github.com/AlexModGuy/AlexsCaves/blob/main/src/main/java/com/github/alexmodguy/alexscaves/server/misc/ACDamageTypes.java
	public static final ResourceKey<DamageType> BLOODFUELED = ResourceKey.create(Registries.DAMAGE_TYPE, ByPowderAndSteel.rl("bloodfueled"));
	
	public static DamageSource bloodfueledDamage(RegistryAccess ra) {
		return new DamageSource(ra.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(BLOODFUELED));
	}
}
