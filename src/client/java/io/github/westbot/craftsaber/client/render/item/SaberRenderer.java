package io.github.westbot.craftsaber.client.render.item;

import io.github.westbot.craftsaber.ModComponents;
import io.github.westbot.craftsaber.items.Saber;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer;

public class SaberRenderer extends DynamicGeoItemRenderer<Saber> {
    public SaberRenderer() {
        super(new SaberModel());
    }



    @Override
    protected boolean boneRenderOverride(MatrixStack poseStack, GeoBone bone, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {

        int color = currentItemStack.getOrDefault(ModComponents.SABER_COLOR, 0);

        super.renderCubesOfBone(
            poseStack, bone, buffer, packedLight, packedOverlay,
            (bone.getName().equals("blade") || bone.getName().equals("hilt_color"))
                ? color + 0xFF000000
                : 0xFFFFFFFF
        );

        return true;
    }

    @Override
    public void renderFinal(MatrixStack poseStack, Saber animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {

        this.model.getBone("blade_base").flatMap(base -> this.model.getBone("blade_tip")).ifPresent(tip -> {

        });

        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
    }
}
