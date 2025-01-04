package io.github.westbot.craftsaber.client.render.entity;

import io.github.westbot.craftsaber.CraftSaber;
import io.github.westbot.craftsaber.entities.NoteEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.specialty.DynamicGeoEntityRenderer;

public class NoteEntityRenderer extends DynamicGeoEntityRenderer<NoteEntity> {

    private static final String ARROW = "arrow";
    private static final String NOTE = "note";

    public NoteEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NoteEntityModel());

        //addRenderLayer(new AutoGlowingGeoLayer<>(this));

    }

    @Override
    public Identifier getTextureLocation(NoteEntity animatable) {
        if (animatable.isDotNote()) {
            return Identifier.of(CraftSaber.MOD_ID, "textures/entity/notes/template_dot_note.png");
        } else {
            return Identifier.of(CraftSaber.MOD_ID, "textures/entity/notes/template_arrow_note.png");
        }
    }

    @Override
    protected boolean boneRenderOverride(MatrixStack poseStack, GeoBone bone, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {

        super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, bone.getName().equals("note") ? this.animatable.getColor() + 0xFF000000 : 0xFFFFFFFF);

        return true;
    }

    @Override
    public void preRender(MatrixStack poseStack, NoteEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        float entityYaw = animatable.getYaw();
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, 0xF00000, packedOverlay, colour);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotation(-entityYaw * MathHelper.RADIANS_PER_DEGREE));
    }

    @Override
    public void render(NoteEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {

        poseStack.push();

        poseStack.scale(0.666666666f, 0.666666666f, 0.666666666f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, 0xF00000);
        poseStack.pop();
    }
}
