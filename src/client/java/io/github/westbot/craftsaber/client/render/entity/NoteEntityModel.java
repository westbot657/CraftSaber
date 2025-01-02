package io.github.westbot.craftsaber.client.render.entity;

import io.github.westbot.craftsaber.CraftSaber;
import io.github.westbot.craftsaber.entities.NoteEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.Optional;

public class NoteEntityModel extends GeoModel<NoteEntity> {

    @Override
    public Identifier getModelResource(NoteEntity noteEntity) {
        return Identifier.of(CraftSaber.MOD_ID, "geo/bsnote.geo.json");
    }

    @Override
    public Identifier getTextureResource(NoteEntity noteEntity) {
        if (noteEntity.isDotNote()) {
            return Identifier.of(CraftSaber.MOD_ID, "textures/entity/notes/template_dot_note.png");
        } else {
            return Identifier.of(CraftSaber.MOD_ID, "textures/entity/notes/template_arrow_note.png");
        }
    }

    @Override
    public Identifier getAnimationResource(NoteEntity noteEntity) {
        return Identifier.of(CraftSaber.MOD_ID, "animations/bsnote.animation.json");
    }

    @Override
    public void setCustomAnimations(NoteEntity animatable, long instanceId, AnimationState<NoteEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone block = getAnimationProcessor().getBone("note");
        GeoBone arrow = getAnimationProcessor().getBone("arrow");

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (block != null) {
            block.setRotZ(animatable.getRoll() * MathHelper.RADIANS_PER_DEGREE);
            block.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }

        if (arrow != null) {
            arrow.setRotZ(animatable.getRoll() * MathHelper.RADIANS_PER_DEGREE);
            arrow.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }

    }
}
