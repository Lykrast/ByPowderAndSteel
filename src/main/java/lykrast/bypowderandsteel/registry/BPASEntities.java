package lykrast.bypowderandsteel.registry;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ByPowderAndSteel.MODID)
public class BPASEntities {
	public static final DeferredRegister<EntityType<?>> REG = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ByPowderAndSteel.MODID);
	public static RegistryObject<EntityType<GunomeEntity>> gunome;
	public static RegistryObject<EntityType<ShrubhulkEntity>> shrubhulk;
	public static RegistryObject<EntityType<ShrubsnapperEntity>> shrubsnapper;
	public static RegistryObject<EntityType<CowbonesEntity>> cowbones;
	public static RegistryObject<EntityType<SaberSentryEntity>> sabersentry;
	public static RegistryObject<EntityType<MechaminatorEntity>> mechaminator;
	//mob projectiles
	public static RegistryObject<EntityType<ShrubsnapperFangEntity>> shrubsnapperFang;
	//custom bullets
	public static RegistryObject<EntityType<SlowBulletEntity>> slowBullet;
	
	static {
		gunome = REG.register("gunome", () -> EntityType.Builder.of(GunomeEntity::new, MobCategory.MONSTER).sized(0.45F, 1.25F).clientTrackingRange(8).build(""));
		shrubhulk = REG.register("shrubhulk", () -> EntityType.Builder.of(ShrubhulkEntity::new, MobCategory.MONSTER).sized(0.99F, 2.4F).clientTrackingRange(8).build(""));
		shrubsnapper = REG.register("shrubsnapper", () -> EntityType.Builder.of(ShrubsnapperEntity::new, MobCategory.MONSTER).sized(0.85F, 2.8F).clientTrackingRange(8).build(""));
		cowbones = REG.register("cowbones", () -> EntityType.Builder.of(CowbonesEntity::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(""));
		sabersentry = REG.register("sabersentry", () -> EntityType.Builder.of(SaberSentryEntity::new, MobCategory.MONSTER).sized(0.9F, 3.3F).clientTrackingRange(8).build(""));
		mechaminator = REG.register("mechaminator", () -> EntityType.Builder.of(MechaminatorEntity::new, MobCategory.MONSTER).sized(0.75F, 1.99F).clientTrackingRange(8).build(""));
		
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
		event.put(cowbones.get(), CowbonesEntity.createAttributes().build());
		event.put(sabersentry.get(), SaberSentryEntity.createAttributes().build());
		event.put(mechaminator.get(), MechaminatorEntity.createAttributes().build());
	}

}
