package io.github.westbot.craftsaber.client.render.feature_renderers;

import io.github.westbot.craftsaber.ModComponents;
import io.github.westbot.craftsaber.ModItems;
import io.github.westbot.craftsaber.client.render.SaberTrailRenderer;
import io.github.westbot.craftsaber.data.Stash;
import io.github.westbot.craftsaber.mixin_data.ItemStackWithStash;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Pair;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.vivecraft.api_beta.client.VivecraftClientAPI;
import org.vivecraft.client.VRPlayersClient;

public class SaberFeatureRenderer<T extends PlayerEntity, M extends EntityModel<T> & ModelWithArms & ModelWithHead> extends FeatureRenderer<T, M> {

    ItemRenderer itemRenderer;

    public SaberFeatureRenderer(FeatureRendererContext<T, M> context, ItemRenderer itemRenderer) {
        super(context);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        ItemStack stack1 = entity.getMainHandStack();
        ItemStack stack2 = entity.getOffHandStack();

        if (!(stack1.isOf(ModItems.SABER) || stack2.isOf(ModItems.SABER))) return;

        Arm mainArm = entity.getMainArm();

        if (mainArm == Arm.LEFT) {
            ItemStack temp = stack2;
            stack2 = stack1;
            stack1 = temp;
        }

        VivecraftClientAPI instance = VivecraftClientAPI.getInstance();
        VRPlayersClient vrClient = VRPlayersClient.getInstance();

        if (vrClient.isVRPlayer(entity)) {

            VRPlayersClient.RotInfo playerData = vrClient.getRotationsForPlayer(entity.getUuid());

            MatrixStack matrix = new MatrixStack();

            Vec3d playerPos = entity.getPos();

            if (stack1.isOf(ModItems.SABER)) { // right hand VR
                matrix.push();
                matrix.translate(
                    (float) playerPos.x, (float) playerPos.y, (float) playerPos.z
                );
                matrix.multiply(RotationAxis.POSITIVE_Y.rotation(entity.getBodyYaw()));

                matrix.translate(
                    playerData.rightArmPos.x,
                    playerData.rightArmPos.y,
                    playerData.rightArmPos.z
                );
                matrix.multiply((new Quaternionf()).rotationXYZ(
                    (float) playerData.rightArmRot.x,
                    (float) playerData.rightArmRot.y,
                    (float) playerData.rightArmRot.z
                ));


                matrix.push();
                BakedModel bakedModel = itemRenderer.getModel(stack1, entity.getWorld(), null, entity.getId());
                matrix.push();
                itemRenderer.renderItem(stack1, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrix, vertexConsumers, light, 0xFFFFFF, bakedModel);
                matrix.pop();

                bakedModel.getTransformation().getTransformation(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND).apply(false, matrix);



                matrix.push();
                matrix.translate(0, (6/8f) * 0.6, 0);
                Vector3f blade_base = matrix.peek().getPositionMatrix().getTranslation(new Vector3f());
                matrix.pop();

                matrix.push();
                matrix.translate(0, (28/8f) * 0.6, 0);
                Vector3f blade_tip = matrix.peek().getPositionMatrix().getTranslation(new Vector3f());
                matrix.pop();
                matrix.pop();
                matrix.pop();

                Stash<Pair<Vector3f, Vector3f>> stash = ((ItemStackWithStash) ((Object) stack1)).craftSaber$getStash();
                SaberTrailRenderer.queueRender(blade_base, blade_tip, stash, stack1.getOrDefault(ModComponents.SABER_COLOR, 0));

            }

            if (stack2.isOf(ModItems.SABER)) { // left hand VR

                matrix.push();
                matrix.translate(
                    (float) playerPos.x, (float) playerPos.y, (float) playerPos.z
                );
                matrix.multiply(RotationAxis.POSITIVE_Y.rotation(entity.getBodyYaw()));

                matrix.translate(
                    playerData.leftArmPos.x,
                    playerData.leftArmPos.y,
                    playerData.leftArmPos.z
                );
                matrix.multiply((new Quaternionf()).rotationXYZ(
                    (float) playerData.leftArmRot.x,
                    (float) playerData.leftArmRot.y,
                    (float) playerData.leftArmRot.z
                ));

                matrix.push();
                BakedModel bakedModel = itemRenderer.getModel(stack2, entity.getWorld(), null, entity.getId());

                matrix.push();
                itemRenderer.renderItem(stack2, ModelTransformationMode.THIRD_PERSON_LEFT_HAND, false, matrix, vertexConsumers, light, 0xFFFFFF, bakedModel);
                matrix.pop();

                bakedModel.getTransformation().getTransformation(ModelTransformationMode.THIRD_PERSON_LEFT_HAND).apply(false, matrix);

                matrix.push();
                matrix.translate(0, (6/8f) * 0.6, 0);
                Vector3f blade_base = matrix.peek().getPositionMatrix().getTranslation(new Vector3f());
                matrix.pop();

                matrix.push();
                matrix.translate(0, (28/8f) * 0.6, 0);
                Vector3f blade_tip = matrix.peek().getPositionMatrix().getTranslation(new Vector3f());
                matrix.pop();
                matrix.pop();
                matrix.pop();

                Stash<Pair<Vector3f, Vector3f>> stash = ((ItemStackWithStash) ((Object) stack2)).craftSaber$getStash();
                SaberTrailRenderer.queueRender(blade_base, blade_tip, stash, stack2.getOrDefault(ModComponents.SABER_COLOR, 0));

            }

        } else {

            // 0. copy translations from LivingEntityRenderer // may be unnecessary, this section seems to just be getting the matrices to the player position and accounting for scale/rotation
            // from Line 63-141
            // anyways that's the result we end up with here, as `matrices`.




            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

            if (stack1.isOf(ModItems.SABER)) { // right hand


            }

            if (stack2.isOf(ModItems.SABER)) { // left hand


            }
        }
    }
}


