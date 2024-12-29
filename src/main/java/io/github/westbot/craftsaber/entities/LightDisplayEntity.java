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
    private String structure_id;

    private final static TrackedData<NbtCompound> ROTATION;
    public Vec3d rotation;

    public LightDisplayEntity(EntityType<?> type, World world) {
        super(type, world);
        structure_id = "";
        rotation = new Vec3d(0, 0, 0);
    }

    public void setStructureId(String id) {
        this.structure_id = id;
    }

    public String getStructureId() {
        return this.structure_id;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(STRUCTURE_ID, "");
        builder.add(ROTATION, new NbtCompound());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("structure_id", NbtElement.STRING_TYPE)) {
            this.structure_id = nbt.getString("structure_id");
        }

        if (nbt.contains("rot", NbtElement.COMPOUND_TYPE)) {
            NbtCompound rot = nbt.getCompound("rot");
            this.rotation = new Vec3d(
                rot.getDouble("x"),
                rot.getDouble("y"),
                rot.getDouble("z")
            );
        } else {
            this.rotation = new Vec3d(0, 0, 0);
        }

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putString("structure_id", this.structure_id);
        NbtCompound rot = new NbtCompound();
        rot.putDouble("x", this.rotation.x);
        rot.putDouble("y", this.rotation.y);
        rot.putDouble("z", this.rotation.z);
    }

    static {
        STRUCTURE_ID = DataTracker.registerData(LightDisplayEntity.class, TrackedDataHandlerRegistry.STRING);
        ROTATION = DataTracker.registerData(LightDisplayEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    }

}
