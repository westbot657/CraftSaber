package io.github.westbot.craftsaber.client;

import com.mojang.serialization.DataResult;
import io.github.westbot.craftsaber.CraftSaber;
import io.github.westbot.craftsaber.ModEntities;
import io.github.westbot.craftsaber.client.render.LightDisplayEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CraftSaberClient implements ClientModInitializer {

    public static HashMap<String, List<Pair<Vec3d, BlockState>>> structure_cache;

    @Override
    public void onInitializeClient() {
        structure_cache = new HashMap<>();

        EntityRendererRegistry.register(ModEntities.LIGHT_DISPLAY, LightDisplayEntityRenderer::new);

    }

    // Inverse function is found in the Util class
    public static void cache(NbtCompound data) {
        List<Pair<Vec3d, BlockState>> out = new ArrayList<>();

        String id = data.getString("structure_id");
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
                    return;
                }

            }
        }

        structure_cache.put(id, out);

    }

}
