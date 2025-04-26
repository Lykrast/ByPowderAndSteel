package lykrast.bypowderandsteel.registry;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ByPowderAndSteel.MODID)
public class BPASEntities {
	public static final DeferredRegister<EntityType<?>> REG = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ByPowderAndSteel.MODID);
	//forest
	public static RegistryObject<EntityType<GunomeEntity>> gunome;
	public static RegistryObject<EntityType<ShrubhulkEntity>> shrubhulk;
	public static RegistryObject<EntityType<ShrubsnapperEntity>> shrubsnapper;
	//desert
	public static RegistryObject<EntityType<CowbonesPistoleroEntity>> cowbonesPistolero;
	public static RegistryObject<EntityType<CowbonesBuckarooEntity>> cowbonesBuckaroo;
	//tundra
	public static RegistryObject<EntityType<ZombieSealEntity>> zombieSeal;
	//ocean
	public static RegistryObject<EntityType<SunkenPirateEntity>> sunkenPirate;
	//underground
	public static RegistryObject<EntityType<SaberSentryEntity>> sabersentry;
	public static RegistryObject<EntityType<BlasterSentryEntity>> blastersentry;
	//nether
	public static RegistryObject<EntityType<GunnubusCrimsonEntity>> gunnubusCrimson;
	//end
	public static RegistryObject<EntityType<SkybenderEntity>> skybender;
	//mob projectiles
	public static RegistryObject<EntityType<ShrubsnapperFangEntity>> shrubsnapperFang;
	//custom bullets
	public static RegistryObject<EntityType<SlowBulletEntity>> slowBullet;
	
	static {
		//forest
		gunome = REG.register("gunome", () -> EntityType.Builder.of(GunomeEntity::new, MobCategory.MONSTER).sized(0.45F, 1.25F).clientTrackingRange(8).build(""));
		shrubhulk = REG.register("shrubhulk", () -> EntityType.Builder.of(ShrubhulkEntity::new, MobCategory.MONSTER).sized(0.99F, 2.4F).clientTrackingRange(8).build(""));
		shrubsnapper = REG.register("shrubsnapper", () -> EntityType.Builder.of(ShrubsnapperEntity::new, MobCategory.MONSTER).sized(0.85F, 2.8F).clientTrackingRange(8).build(""));
		//desert
		cowbonesPistolero = REG.register("cowbones_pistolero", () -> EntityType.Builder.of(CowbonesPistoleroEntity::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(""));
		cowbonesBuckaroo = REG.register("cowbones_buckaroo", () -> EntityType.Builder.of(CowbonesBuckarooEntity::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(""));
		//tundra
		zombieSeal = REG.register("zombie_seal", () -> EntityType.Builder.of(ZombieSealEntity::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(""));
		//ocean
		sunkenPirate = REG.register("sunken_pirate", () -> EntityType.Builder.of(SunkenPirateEntity::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(""));
		//underground
		sabersentry = REG.register("sabersentry", () -> EntityType.Builder.of(SaberSentryEntity::new, MobCategory.MONSTER).sized(0.9F, 3.3F).clientTrackingRange(8).build(""));
		blastersentry = REG.register("blastersentry", () -> EntityType.Builder.of(BlasterSentryEntity::new, MobCategory.MONSTER).sized(0.75F, 1.99F).clientTrackingRange(8).build(""));
		//nether
		gunnubusCrimson = REG.register("gunnubus_crimson", () -> EntityType.Builder.of(GunnubusCrimsonEntity::new, MobCategory.MONSTER).sized(1.2F, 3.99F).clientTrackingRange(8).build(""));
		//end
		skybender = REG.register("skybender", () -> EntityType.Builder.of(SkybenderEntity::new, MobCategory.MONSTER).sized(0.9F, 1.125F).clientTrackingRange(8).build(""));
		
		//mob projectiles
		//I dunno why this one bein dum dum with the types
		shrubsnapperFang = REG.register("shrubsnapper_fang", () -> EntityType.Builder.<ShrubsnapperFangEntity>of(ShrubsnapperFangEntity::new, MobCategory.MISC).sized(0.5F, 0.8F).clientTrackingRange(6).updateInterval(2).build(""));
		
		//custom bullets
		slowBullet = REG.register("bullet_slow", () -> EntityType.Builder.<SlowBulletEntity>of(SlowBulletEntity::new, MobCategory.MISC).sized(0.3125f, 0.3125f).setUpdateInterval(2).setTrackingRange(64).setShouldReceiveVelocityUpdates(true).build(""));
	}

	@SubscribeEvent
	public static void registerEntityAttributes(final EntityAttributeCreationEvent event) {
		event.put(gunome.get(), GunomeEntity.createAttributes().build());
		event.put(shrubhulk.get(), ShrubhulkEntity.createAttributes().build());
		event.put(shrubsnapper.get(), ShrubsnapperEntity.createAttributes().build());
		event.put(cowbonesPistolero.get(), CowbonesPistoleroEntity.createAttributes().build());
		event.put(cowbonesBuckaroo.get(), CowbonesBuckarooEntity.createAttributes().build());
		event.put(zombieSeal.get(), ZombieSealEntity.createAttributes().build());
		event.put(sunkenPirate.get(), SunkenPirateEntity.createAttributes().build());
		event.put(sabersentry.get(), SaberSentryEntity.createAttributes().build());
		event.put(blastersentry.get(), BlasterSentryEntity.createAttributes().build());
		event.put(gunnubusCrimson.get(), GunnubusCrimsonEntity.createAttributes().build());
		event.put(skybender.get(), SkybenderEntity.createAttributes().build());
	}
	
	@SubscribeEvent
	public static void registerSpawnPlacements(final SpawnPlacementRegisterEvent event) {
		event.register(sunkenPirate.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SunkenPirateEntity::spawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(sabersentry.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SaberSentryEntity::spawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(blastersentry.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlasterSentryEntity::spawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
	}

}
