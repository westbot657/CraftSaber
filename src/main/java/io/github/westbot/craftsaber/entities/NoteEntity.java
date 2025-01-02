package io.github.westbot.craftsaber.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NoteEntity extends Entity implements GeoEntity {

    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final TrackedData<Integer> COLOR;
    private static final TrackedData<Float> ROTATION;
    private static final TrackedData<Boolean> DOT_NOTE;



    public NoteEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(COLOR, 0x000000);
        builder.add(ROTATION, 0.0f);
        builder.add(DOT_NOTE, false);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("color", NbtElement.INT_TYPE)) {
            dataTracker.set(COLOR, nbt.getInt("color"));
        } else {
            dataTracker.set(COLOR, 0x000000);
        }

        if (nbt.contains("rotation", NbtElement.FLOAT_TYPE)) {
            dataTracker.set(ROTATION, nbt.getFloat("rotation"));
        } else {
            dataTracker.set(ROTATION, 0.0f);
        }

        if (nbt.contains("dot_note")) {
            dataTracker.set(DOT_NOTE, nbt.getBoolean("dot_note"));
        } else {
            dataTracker.set(DOT_NOTE, false);
        }

    }

    public void setColor(int color) {
        dataTracker.set(COLOR, color);
    }

    public void setRoll(float rotation) {
        dataTracker.set(ROTATION, rotation);
    }

    public void setDotNote(boolean has_dot) {
        dataTracker.set(DOT_NOTE, has_dot);
    }

    public int getColor() {
        return dataTracker.get(COLOR);
    }

    public float getRoll() {
        return dataTracker.get(ROTATION);
    }

    public boolean isDotNote() {
        return dataTracker.get(DOT_NOTE);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("color", dataTracker.get(COLOR));
        nbt.putFloat("rotation", dataTracker.get(ROTATION));
        nbt.putBoolean("dot_note", dataTracker.get(DOT_NOTE));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<NoteEntity> eAnimationState) {

        eAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.bsnote.idle", Animation.LoopType.LOOP));

        //if (eAnimationState.isMoving()) {
        //    eAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.living_potato.walk", Animation.LoopType.LOOP));
        //} else {
        //    eAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.living_potato.idle", Animation.LoopType.LOOP));
        //}
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    static {
        COLOR = DataTracker.registerData(NoteEntity.class, TrackedDataHandlerRegistry.INTEGER);
        ROTATION = DataTracker.registerData(NoteEntity.class, TrackedDataHandlerRegistry.FLOAT);
        DOT_NOTE = DataTracker.registerData(NoteEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

}
