package lykrast.bypowderandsteel.renderer;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.CowbonesBuckarooEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CowbonesBuckarooRenderer extends HumanoidMobRenderer<CowbonesBuckarooEntity, CowbonesModel<CowbonesBuckarooEntity>> {
	//putting them here so that I can recycle the model between cowbones
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones_buckaroo"), "main");
	public static final ModelLayerLocation OVERLAY = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones_buckaroo"), "overlay");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones_buckaroo"), "inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("cowbones_buckaroo"), "outer_armor");
	
	private static final ResourceLocation TEXTURE = ByPowderAndSteel.rl("textures/entity/cowbones.png");
	private static final ResourceLocation TEXTURE_OVERLAY = ByPowderAndSteel.rl("textures/entity/cowbones_buckaroo.png");

	//That's a bit all over the place cause I normally put this in the model files
	//eeeh whatever
	//it's to get the sombrero
	public static LayerDefinition createOverlayLayer(CubeDeformation deform) {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(deform, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.getChild("head");

		//Blockbenched brim
		head.addOrReplaceChild("brim", CubeListBuilder.create().texOffs(30, 47).addBox(-8, -8, -6, 16, 16, 1, deform), PartPose.offsetAndRotation(0, 0, 0, -Mth.HALF_PI, 0, 0));
		
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public CowbonesBuckarooRenderer(EntityRendererProvider.Context context) {
		super(context, new CowbonesModel<>(context.bakeLayer(MODEL)), 0.5f);
		addLayer(new CowbonesOverlayLayer<>(this, context.getModelSet(), OVERLAY, TEXTURE_OVERLAY));
		addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel<>(context.bakeLayer(INNER_ARMOR)), new SkeletonModel<>(context.bakeLayer(OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(CowbonesBuckarooEntity entity) {
		return TEXTURE;
	}
}
