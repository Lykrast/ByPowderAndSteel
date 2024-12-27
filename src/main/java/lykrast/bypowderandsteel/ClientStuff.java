package lykrast.bypowderandsteel;

import lykrast.bypowderandsteel.registry.BPASEntities;
import lykrast.bypowderandsteel.renderer.CowbonesModel;
import lykrast.bypowderandsteel.renderer.CowbonesRenderer;
import lykrast.bypowderandsteel.renderer.GunomeModel;
import lykrast.bypowderandsteel.renderer.GunomeRenderer;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ByPowderAndSteel.MODID, value = Dist.CLIENT)
public class ClientStuff {

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(BPASEntities.gunome.get(), (context) -> new GunomeRenderer(context));
		event.registerEntityRenderer(BPASEntities.cowbones.get(), (context) -> new CowbonesRenderer(context));
	}

	@SubscribeEvent
	public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(GunomeModel.MODEL, () -> GunomeModel.createBodyLayer());
		event.registerLayerDefinition(CowbonesModel.MODEL, () -> CowbonesModel.createBodyLayer());
    	event.registerLayerDefinition(CowbonesModel.OVERLAY, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25f), 0), 64, 32));
    	event.registerLayerDefinition(CowbonesModel.INNER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
    	event.registerLayerDefinition(CowbonesModel.OUTER_ARMOR, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
	}
}
