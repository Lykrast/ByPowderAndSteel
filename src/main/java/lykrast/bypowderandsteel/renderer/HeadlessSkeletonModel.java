package lykrast.bypowderandsteel.renderer;

import com.google.common.collect.ImmutableList;

import lykrast.bypowderandsteel.ByPowderAndSteel;
import lykrast.bypowderandsteel.entity.HeadlessSkeletonEntity;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HeadlessSkeletonModel extends SkeletonModel<HeadlessSkeletonEntity> {
	public static final ModelLayerLocation MODEL = new ModelLayerLocation(ByPowderAndSteel.rl("headless_skeleton"), "main");
	public static final ModelLayerLocation INNER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("headless_skeleton"), "inner_armor");
	public static final ModelLayerLocation OUTER_ARMOR = new ModelLayerLocation(ByPowderAndSteel.rl("headless_skeleton"), "outer_armor");
	
	public HeadlessSkeletonModel(ModelPart modelpart) {
		super(modelpart);
	}

	@Override
	public void setupAnim(HeadlessSkeletonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		ItemStack itemstack = entity.getMainHandItem();
		if (entity.isAggressive() && (itemstack.isEmpty() || !itemstack.is(Items.BOW))) {
			//overriding the default aggressive animation to be like pillagers
			rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
			rightArm.yRot = 0.0F;
			rightArm.zRot = 0.0F;
			leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
			leftArm.yRot = 0.0F;
			leftArm.zRot = 0.0F;
            AnimationUtils.swingWeaponDown(rightArm, leftArm, entity, attackTime, ageInTicks);
		}
	}

	//prevent the head from rendering at all
	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(body, rightArm, leftArm, rightLeg, leftLeg);
	}

}
