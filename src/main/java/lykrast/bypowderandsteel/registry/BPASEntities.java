package lykrast.bypowderandsteel.registry;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.CowbonesEntity;
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
	public static RegistryObject<EntityType<CowbonesEntity>> cowbones;
	
	static {
		//match skeleton
		cowbones = REG.register("cowbones", () -> EntityType.Builder.of(CowbonesEntity::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(""));
	}

	@SubscribeEvent
	public static void registerEntityAttributes(final EntityAttributeCreationEvent event) {
		event.put(cowbones.get(), CowbonesEntity.createAttributes().build());
	}

}
