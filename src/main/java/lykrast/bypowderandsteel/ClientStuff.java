package lykrast.bypowderandsteel;

import lykrast.bypowderandsteel.entity.SlowBulletEntity;
import lykrast.bypowderandsteel.registry.BPASEntities;
import lykrast.bypowderandsteel.renderer.*;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ByPowderAndSteel.MODID, value = Dist.CLIENT)
public class ClientStuff {

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		//forest
		event.registerEntityRenderer(BPASEntities.gunome.get(), (context) -> new GunomeRenderer(context));
		event.registerEntityRenderer(BPASEntities.shrubhulk.get(), (context) -> new ShrubhulkRenderer(context));
		event.registerEntityRenderer(BPASEntities.shrubsnapper.get(), (context) -> new ShrubsnapperRenderer(context));
		//desert
		event.registerEntityRenderer(BPASEntities.cowbones.get(), (context) -> new CowbonesRenderer(context));
		//tundra
		event.registerEntityRenderer(BPASEntities.zombieSeal.get(), (context) -> new ZombieSealRenderer(context));
		//underground
		event.registerEntityRenderer(BPASEntities.sabersentry.get(), (context) -> new SaberSentryRenderer(context));
		event.registerEntityRenderer(BPASEntities.blastersentry.get(), (context) -> new BlasterSentryRenderer(context));
		//nether
		//end
		//mob projectiles
		event.registerEntityRenderer(BPASEntities.shrubsnapperFang.get(), (context) -> new ShrubsnapperFangRenderer(context));
		//custom bullets
		event.registerEntityRenderer(BPASEntities.slowBullet.get(), (context) -> new ThrownItemRenderer<SlowBulletEntity>(context));
	}

	@SubscribeEvent
	public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(GunomeModel.MODEL, () -> GunomeModel.createBodyLayer());
		event.registerLayerDefinition(ShrubhulkModel.MODEL, () -> ShrubhulkModel.createBodyLayer());
		event.registerLayerDefinition(ShrubsnapperModel.MODEL, () -> ShrubsnapperModel.createBodyLayer());
		event.registerLayerDefinition(CowbonesModel.MODEL, () -> CowbonesModel.createBodyLayer());
    	event.registerLayerDefinition(CowbonesModel.OVERLAY, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25f), 0), 64, 32));
    	event.registerLayerDefinition(CowbonesModel.INNER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
    	event.registerLayerDefinition(CowbonesModel.OUTER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(ZombieSealModel.MODEL, () -> ZombieSealModel.createBodyLayer());
    	event.registerLayerDefinition(ZombieSealModel.INNER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
    	event.registerLayerDefinition(ZombieSealModel.OUTER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(SaberSentryModel.MODEL, () -> SaberSentryModel.createBodyLayer());
		event.registerLayerDefinition(BlasterSentryModel.MODEL, () -> BlasterSentryModel.createBodyLayer());
    	//projectiles
		event.registerLayerDefinition(ShrubsnapperFangRenderer.MODEL, () -> EvokerFangsModel.createBodyLayer());
	}
}
