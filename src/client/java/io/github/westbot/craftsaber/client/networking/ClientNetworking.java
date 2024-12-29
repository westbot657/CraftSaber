package io.github.westbot.craftsaber.client.networking;

import io.github.westbot.craftsaber.networking.LightInitPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.world.ClientWorld;

public class ClientNetworking {


    public static void init() {

        ClientPlayNetworking.registerGlobalReceiver(LightInitPayload.ID, (payload, context) -> {

            context.client().execute(() -> {
                ClientWorld world = context.client().world;
                if (world == null) return;

            });

        });

    }


}
