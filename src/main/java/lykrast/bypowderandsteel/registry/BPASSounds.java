package lykrast.bypowderandsteel.registry;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BPASSounds {
	//TODO missing sounds
	//Guns
	public static final DeferredRegister<SoundEvent> REG = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ByPowderAndSteel.MODID);
	public static RegistryObject<SoundEvent> peashooter = initSound("item.peashooter.shoot"), cornGatling = initSound("item.corn_gatling.shoot");
	public static RegistryObject<SoundEvent> desertRevolver = initSound("item.desert_revolver.shoot"), desertShotgun = initSound("item.desert_shotgun.shoot");
	public static RegistryObject<SoundEvent> arcticPistol = initSound("item.arctic_pistol.shoot"), arcticSniper = initSound("item.arctic_sniper.shoot");
	public static RegistryObject<SoundEvent> buccaneerFlintlock = initSound("item.buccaneer_flintlock.shoot");// buccaneerCannon = initSound("item.buccaneer_cannon.shoot");
	//public static RegistryObject<SoundEvent> raygun = initSound("item.raygun.shoot");
	public static RegistryObject<SoundEvent> bloodfueledRevolver = initSound("item.bloodfueled_revolver.shoot");
	//Mobs
	//forest
	//public static RegistryObject<SoundEvent> gunomeIdle = initSound("entity.gunome.idle"), gunomeHurt = initSound("entity.gunome.hurt"), gunomeDeath = initSound("entity.gunome.death");
	public static RegistryObject<SoundEvent> shrubIdle = initSound("entity.shrub.idle"), shrubHurt = initSound("entity.shrub.hurt"), shrubDeath = initSound("entity.shrub.death");
	//desert
	//public static RegistryObject<SoundEvent> cowbonesIdle = initSound("entity.cowbones.idle"), cowbonesHurt = initSound("entity.cowbones.hurt"), cowbonesDeath = initSound("entity.cowbones.death");
	//tundra
	//public static RegistryObject<SoundEvent> sealIdle = initSound("entity.zombie_seal.idle"), sealHurt = initSound("entity.zombie_seal.hurt"), sealDeath = initSound("entity.zombie_seal.death");
	//ocean
	//public static RegistryObject<SoundEvent> pirateIdle = initSound("entity.sunken_pirate.idle"), pirateHurt = initSound("entity.sunken_pirate.hurt"), pirateDeath = initSound("entity.sunken_pirate.death");
	//undeground
	public static RegistryObject<SoundEvent> sentryHurt = initSound("entity.sentry.hurt"), sentryDeath = initSound("entity.sentry.death");
	//public static RegistryObject<SoundEvent> sabersentryIdle = initSound("entity.sabersentry.idle"), blastersentryIdle = initSound("entity.blastersentry.idle");
	//nether
	//public static RegistryObject<SoundEvent> gunnubusIdle = initSound("entity.gunnubus.idle"), gunnubusHurt = initSound("entity.gunnubus.hurt"), gunnubusDeath = initSound("entity.gunnubus.death");
	//end
	//public static RegistryObject<SoundEvent> skybenderIdle = initSound("entity.skybender.idle"), skybenderHurt = initSound("entity.skybender.hurt"), skybenderDeath = initSound("entity.skybender.death");

	public static RegistryObject<SoundEvent> initSound(String name) {
		return REG.register(name, () -> SoundEvent.createVariableRangeEvent(ByPowderAndSteel.rl(name)));
	}
}
