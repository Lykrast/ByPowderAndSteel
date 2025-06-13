package lykrast.bypowderandsteel.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import lykrast.bypowderandsteel.entity.CowbonesPistoleroEntity;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CowbonesPistoleroItemLayer extends ItemInHandLayer<CowbonesPistoleroEntity, CowbonesPistoleroModel> {
	//private in ItemInHandLayer so I have to save it a fucking second time
	protected final ItemInHandRenderer itemInHandRenderer;

	public CowbonesPistoleroItemLayer(RenderLayerParent<CowbonesPistoleroEntity, CowbonesPistoleroModel> parent, ItemInHandRenderer renderer) {
		super(parent, renderer);
		this.itemInHandRenderer = renderer;
	}

	@Override
	protected void renderArmWithItem(LivingEntity entity, ItemStack stack, ItemDisplayContext context, HumanoidArm arm, PoseStack posestack, MultiBufferSource buffer, int p_117191_) {
		//copy pasting this entire thing from the super
		if (!stack.isEmpty()) {
			posestack.pushPose();
			getParentModel().translateToHand(arm, posestack);
			posestack.mulPose(Axis.XP.rotationDegrees(-90));
			posestack.mulPose(Axis.YP.rotationDegrees(180));
			boolean left = arm == HumanoidArm.LEFT;
			posestack.translate((float) (left ? -1 : 1) / 16.0F, 0.125F, -0.625F);
			//juuuust to put the rotation there
			posestack.mulPose(Axis.XP.rotationDegrees(left ? getParentModel().gunSpinL : getParentModel().gunSpinR));
			itemInHandRenderer.renderItem(entity, stack, context, left, posestack, buffer, p_117191_);
			posestack.popPose();
		}
	}

}
