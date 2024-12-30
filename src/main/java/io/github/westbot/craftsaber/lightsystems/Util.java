package io.github.westbot.craftsaber.lightsystems;

import com.mojang.serialization.DataResult;
import io.github.westbot.craftsaber.CraftSaber;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static double easeInSine(double dt) {
        return 1 - Math.cos((dt * Math.PI) / 2.0);
    }

    public static double easeOutSine(double dt) {
        return Math.sin((dt * Math.PI) / 2.0);
    }

    public static double easeInOutSine(double dt) {
        return -(Math.cos(Math.PI * dt) - 1.0) / 2.0;
    }


    private static final double BACK_C1 = 1.70158;
    private static final double BACK_C2 = BACK_C1 * 1.525;
    private static final double BACK_C3 = BACK_C1 + 1;

    public static double easeInBack(double dt) {

        return BACK_C3 * dt * dt * dt - BACK_C1 * dt * dt;
    }

    public static double easeOutBack(double dt) {

        return 1 + BACK_C3 * Math.pow(dt - 1, 3) + BACK_C1 * Math.pow(dt - 1, 2);
    }

    public static double easeInOutBack(double dt) {
        return dt < 0.5
            ? (Math.pow(2.0 * dt, 2.0) * ((BACK_C2 + 1) * 2 * dt - BACK_C2)) / 2.0
            : (Math.pow(2.0 * dt - 2.0, 2.0) * ((BACK_C2 + 1) * (dt * 2.0 - 2.0) + BACK_C2) + 2.0) / 2.0;
    }

    /*
    Lightshow serialization:
    NBTComp{
        "lightshow_id": "Lightshow 1",
        "light_structures": NBTComp{
            "1": NBTComp{
                "offset": Vec3{...},
                "blockdata": ???
            },
            "2": ...
        },
        "lights": NBTComp{
            "category name 1": NBTComp{
                "group name 1.1": NBTComp{
                    "lights": "1",
                    "count": 16,
                    "position": Vec3{x:?, y:?, z:?},
                    "rotation": Vec3{x:?, y:?, z:?}
                }
            }
        }
    }
     */

    /// Deserializes an NBT Compound and updates lightshow data accordingly
    public static void deserializeLights(NbtCompound data) {

        String id = data.getString("lightshow_id");

        LightController controller;
        if (CraftSaber.lightShows.containsKey(id)) {
            controller = CraftSaber.lightShows.get(id);
        } else {
            controller = new LightController(id);
            CraftSaber.lightShows.put(id, controller);
        }

    }

    public static NbtCompound serializeLights(LightController controller) {
        NbtCompound dataOut = new NbtCompound();

        dataOut.putString("lightshow_id", controller.getId());

        return dataOut;
    }

    public static NbtCompound serialize_structure(String structure_id, Pair<Vec3d, List<Pair<Vec3d, BlockState>>> block_data) {
        NbtCompound out = new NbtCompound();

        out.putString("structure_id", structure_id);
        NbtCompound offset_nbt = new NbtCompound();
        offset_nbt.putDouble("x", block_data.getLeft().x);
        offset_nbt.putDouble("y", block_data.getLeft().y);
        offset_nbt.putDouble("z", block_data.getLeft().z);
        out.put("offset", offset_nbt);
        NbtList nbt_pairs = new NbtList();

        for (Pair<Vec3d, BlockState> pair : block_data.getRight()) {
            Vec3d pos = pair.getLeft();
            BlockState state = pair.getRight();

            DataResult<NbtElement> result = BlockState.CODEC.encodeStart(NbtOps.INSTANCE, state);
            NbtCompound compound = new NbtCompound();
            compound.putDouble("x", pos.x);
            compound.putDouble("y", pos.y);
            compound.putDouble("z", pos.z);
            compound.put("state", result.getOrThrow());

            nbt_pairs.add(compound);
        }

        out.put("block_data", nbt_pairs);

        return out;
    }

    public static Pair<Vec3d, List<Pair<Vec3d, BlockState>>> deserialize_structure(NbtCompound data) {
        List<Pair<Vec3d, BlockState>> out = new ArrayList<>();

        // String id = data.getString("structure_id");
        NbtCompound nbt_offset = data.getCompound("offset");
        Vec3d offset = new Vec3d(nbt_offset.getDouble("x"), nbt_offset.getDouble("y"), nbt_offset.getDouble("z"));
        NbtList nbt_pairs = data.getList("block_data", NbtElement.COMPOUND_TYPE);

        for ( NbtElement comp : nbt_pairs ) {
            if (comp instanceof NbtCompound compound) {

                double x = compound.getDouble("x");
                double y = compound.getDouble("y");
                double z = compound.getDouble("z");
                NbtElement block_state = compound.get("state");

                Vec3d pos = new Vec3d(x, y, z);

                DataResult<BlockState> result = BlockState.CODEC.parse(NbtOps.INSTANCE, block_state);

                if (result.isSuccess()) {
                    BlockState blockState = result.getOrThrow(); // In theory this should never throw

                    out.add(new Pair<>(pos, blockState));

                } else {
                    CraftSaber.LOGGER.error("Failed to load light structure");
                    return null;
                }

            }
        }

        return new Pair<>(offset, out);
    }

    public static Pair<Vec3d, List<Pair<Vec3d, BlockState>>> collect_blocks(BlockPos pos1, BlockPos pos2, Vec3d offset, ServerWorld world) {

        ArrayList<Pair<Vec3d, BlockState>> data = new ArrayList<>();

        BlockPos min = BlockPos.min(pos1, pos2);
        BlockPos max = BlockPos.max(pos1, pos2);

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    BlockState state = world.getBlockState(new BlockPos(x, y, z));

                    data.add(new Pair<>(new Vec3d(x - min.getX(), y - min.getY(), z - min.getZ()), state));
                }
            }
        }

        return new Pair<>(offset, data);

    }



}
