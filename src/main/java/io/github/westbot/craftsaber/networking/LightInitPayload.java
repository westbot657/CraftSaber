package io.github.westbot.craftsaber.networking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record LightInitPayload(String network, NbtCompound build_data) implements CustomPayload {
    public static final CustomPayload.Id<LightInitPayload> ID = new CustomPayload.Id<>(ServerNetworking.LIGHT_INIT);
    public static final PacketCodec<RegistryByteBuf, LightInitPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, LightInitPayload::network,
        PacketCodecs.NBT_COMPOUND, LightInitPayload::build_data,
        LightInitPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
