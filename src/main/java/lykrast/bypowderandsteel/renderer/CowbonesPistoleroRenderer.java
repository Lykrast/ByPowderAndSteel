package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.CowbonesPistoleroEntity;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class CowbonesPistoleroRenderer extends HumanoidMobRenderer<CowbonesPistoleroEntity, CowbonesModel<CowbonesPistoleroEntity>> {
	//putting them here so that I can recycle the model between cowbones
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones_pistolero"), "main");
	public static final ModelLayerLocation OVERLAY = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones_pistolero"), "overlay");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones_pistolero"), "inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones_pistolero"), "outer_armor");
	
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/cowbones.png");
	private static final ResourceLocation TEXTURE_OVERLAY = ByPowderAndSteel.rl("textures/entity/cowbones_pistolero.png");

	public CowbonesPistoleroRenderer(EntityRendererProvider.Context context) {
		super(context, new CowbonesModel<>(context.bakeLayer(MODEL)), 0.5f);
		addLayer(new CowbonesOverlayLayer<>(this, context.getModelSet(), OVERLAY, TEXTURE_OVERLAY));
		addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel<>(context.bakeLayer(INNER_ARMOR)), new SkeletonModel<>(context.bakeLayer(OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(CowbonesPistoleroEntity entity) {
		return TEXTURE;
	}
}
