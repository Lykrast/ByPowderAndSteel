package lykrast.bypowderandsteel.registry;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BPASSounds {
	//Guns
	public static final DeferredRegister<SoundEvent> REG = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ByPowderAndSteel.MODID);
	//public static RegistryObject<SoundEvent> peashooter = initSound("item.peashooter.shoot"), cornGatling = initSound("item.corn_gatling.shoot");
	public static RegistryObject<SoundEvent> desertRevolver = initSound("item.desert_revolver.shoot"), desertShotgun = initSound("item.desert_shotgun.shoot");
	public static RegistryObject<SoundEvent> arcticPistol = initSound("item.arctic_pistol.shoot"), arcticSniper = initSound("item.arctic_sniper.shoot");
	public static RegistryObject<SoundEvent> buccaneerFlintlock = initSound("item.buccaneer_flintlock.shoot");// buccaneerCannon = initSound("item.buccaneer_cannon.shoot");
	//public static RegistryObject<SoundEvent> raygun = initSound("item.raygun.shoot");
	public static RegistryObject<SoundEvent> bloodfueledRevolver = initSound("item.bloodfueled_revolver.shoot");
	//Mobs

	public static RegistryObject<SoundEvent> initSound(String name) {
		return REG.register(name, () -> SoundEvent.createVariableRangeEvent(ByPowderAndSteel.rl(name)));
	}
}
