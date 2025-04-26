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
		event.registerEntityRenderer(BPASEntities.cowbonesPistolero.get(), (context) -> new CowbonesPistoleroRenderer(context));
		event.registerEntityRenderer(BPASEntities.cowbonesBuckaroo.get(), (context) -> new CowbonesBuckarooRenderer(context));
		//tundra
		event.registerEntityRenderer(BPASEntities.zombieSeal.get(), (context) -> new ZombieSealRenderer(context));
		//ocean
		event.registerEntityRenderer(BPASEntities.sunkenPirate.get(), (context) -> new SunkenPirateRenderer(context));
		//underground
		event.registerEntityRenderer(BPASEntities.sabersentry.get(), (context) -> new SaberSentryRenderer(context));
		event.registerEntityRenderer(BPASEntities.blastersentry.get(), (context) -> new BlasterSentryRenderer(context));
		//nether
		event.registerEntityRenderer(BPASEntities.gunnubusCrimson.get(), (context) -> new GunnubusCrimsonRenderer(context));
		//end
		event.registerEntityRenderer(BPASEntities.skybender.get(), (context) -> new SkybenderRenderer(context));
		//mob projectiles
		event.registerEntityRenderer(BPASEntities.shrubsnapperFang.get(), (context) -> new ShrubsnapperFangRenderer(context));
		//custom bullets
		event.registerEntityRenderer(BPASEntities.slowBullet.get(), (context) -> new ThrownItemRenderer<SlowBulletEntity>(context));
	}

	@SubscribeEvent
	public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
		//forest
		event.registerLayerDefinition(GunomeModel.MODEL, () -> GunomeModel.createBodyLayer());
		event.registerLayerDefinition(ShrubhulkModel.MODEL, () -> ShrubhulkModel.createBodyLayer());
		event.registerLayerDefinition(ShrubsnapperModel.MODEL, () -> ShrubsnapperModel.createBodyLayer());
		//desert
		event.registerLayerDefinition(CowbonesPistoleroRenderer.MODEL, () -> CowbonesModel.createBodyLayer());
    	event.registerLayerDefinition(CowbonesPistoleroRenderer.OVERLAY, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25f), 0), 64, 32));
    	event.registerLayerDefinition(CowbonesPistoleroRenderer.INNER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
    	event.registerLayerDefinition(CowbonesPistoleroRenderer.OUTER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		event.registerLayerDefinition(CowbonesBuckarooRenderer.MODEL, () -> CowbonesModel.createBodyLayer());
    	event.registerLayerDefinition(CowbonesBuckarooRenderer.OVERLAY, () -> CowbonesBuckarooRenderer.createOverlayLayer(new CubeDeformation(0.25f)));
    	event.registerLayerDefinition(CowbonesBuckarooRenderer.INNER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
    	event.registerLayerDefinition(CowbonesBuckarooRenderer.OUTER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
    	//tundra
		event.registerLayerDefinition(ZombieSealModel.MODEL, () -> ZombieSealModel.createBodyLayer());
    	event.registerLayerDefinition(ZombieSealModel.INNER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
    	event.registerLayerDefinition(ZombieSealModel.OUTER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
    	//ocean
		event.registerLayerDefinition(SunkenPirateRenderer.MODEL, () -> GunnubusModel.createBodyLayer());
    	event.registerLayerDefinition(SunkenPirateRenderer.INNER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
    	event.registerLayerDefinition(SunkenPirateRenderer.OUTER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
    	//underground
		event.registerLayerDefinition(SaberSentryModel.MODEL, () -> SaberSentryModel.createBodyLayer());
		event.registerLayerDefinition(BlasterSentryModel.MODEL, () -> BlasterSentryModel.createBodyLayer());
		//nether
		event.registerLayerDefinition(GunnubusCrimsonRenderer.MODEL, () -> GunnubusModel.createBodyLayer());
    	event.registerLayerDefinition(GunnubusCrimsonRenderer.INNER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
    	event.registerLayerDefinition(GunnubusCrimsonRenderer.OUTER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		//end
		event.registerLayerDefinition(SkybenderModel.MODEL, () -> SkybenderModel.createBodyLayer());
    	//projectiles
		event.registerLayerDefinition(ShrubsnapperFangRenderer.MODEL, () -> EvokerFangsModel.createBodyLayer());
	}
}
