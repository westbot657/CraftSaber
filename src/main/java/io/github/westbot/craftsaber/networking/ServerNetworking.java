package io.github.westbot.craftsaber.networking;


import io.github.westbot.craftsaber.CraftSaber;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

// if packets need to be larger: mod "Robin's Unlimited Packets"
public class ServerNetworking {
    public static final Identifier LIGHT_INIT = Identifier.of(CraftSaber.MOD_ID, "light_init");

    public static void init() {

        PayloadTypeRegistry.playS2C().register(LightInitPayload.ID, LightInitPayload.CODEC);

    }

}
