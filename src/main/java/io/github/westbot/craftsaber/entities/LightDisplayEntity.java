package io.github.westbot.craftsaber.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LightDisplayEntity extends Entity {

    private static final TrackedData<String> STRUCTURE_ID;

    private final static TrackedData<NbtCompound> ROTATION;

    public LightDisplayEntity(EntityType<?> type, World world) {
        super(type, world);
        ignoreCameraFrustum = true;

    }

    public void setStructureId(String id) {
        this.dataTracker.set(STRUCTURE_ID, id);
    }

    public String getStructureId() {
        return this.dataTracker.get(STRUCTURE_ID);
    }

    public void setRotation(Vec3d rot) {
        NbtCompound rotation = new NbtCompound();
        rotation.putDouble("x", rot.x);
        rotation.putDouble("y", rot.y);
        rotation.putDouble("z", rot.z);
        this.dataTracker.set(ROTATION, rotation);
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    public Vec3d getRotation() {
        NbtCompound rot = this.dataTracker.get(ROTATION);
        return new Vec3d(rot.getDouble("x"), rot.getDouble("y"), rot.getDouble("z"));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(STRUCTURE_ID, "");
        builder.add(ROTATION, new NbtCompound());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("structure_id", NbtElement.STRING_TYPE)) {
            this.dataTracker.set(STRUCTURE_ID, nbt.getString("structure_id"));
        }

        if (nbt.contains("rotation", NbtElement.COMPOUND_TYPE)) {
            NbtCompound rot = nbt.getCompound("rotation");
            this.dataTracker.set(ROTATION, rot);
        } else {
            NbtCompound compound = new NbtCompound();
            compound.putDouble("x", 0);
            compound.putDouble("y", 0);
            compound.putDouble("z", 0);
            this.dataTracker.set(ROTATION, compound);
        }

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putString("structure_id", this.dataTracker.get(STRUCTURE_ID));
        nbt.put("rotation", this.dataTracker.get(ROTATION));
    }

    static {
        STRUCTURE_ID = DataTracker.registerData(LightDisplayEntity.class, TrackedDataHandlerRegistry.STRING);
        ROTATION = DataTracker.registerData(LightDisplayEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    }

}
