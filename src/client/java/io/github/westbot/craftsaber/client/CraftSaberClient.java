package io.github.westbot.craftsaber.client;

import io.github.westbot.craftsaber.ModEntities;
import io.github.westbot.craftsaber.client.networking.ClientNetworking;
import io.github.westbot.craftsaber.client.render.LightDisplayEntityRenderer;
import io.github.westbot.craftsaber.lightsystems.Util;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.List;

public class CraftSaberClient implements ClientModInitializer {

    public static HashMap<String, Pair<Vec3d, List<Pair<Vec3d, BlockState>>>> structure_cache;

    @Override
    public void onInitializeClient() {
        structure_cache = new HashMap<>();

        EntityRendererRegistry.register(ModEntities.LIGHT_DISPLAY, LightDisplayEntityRenderer::new);

        ClientNetworking.init();

    }

    // Inverse function is found in the Util class
    public static void cache(NbtCompound data) {
        String id = data.getString("structure_id");
        Pair<Vec3d, List<Pair<Vec3d, BlockState>>> out = Util.deserialize_structure(data);

        structure_cache.put(id, out);

    }

}
