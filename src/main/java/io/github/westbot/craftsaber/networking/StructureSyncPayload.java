package io.github.westbot.craftsaber.networking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record StructureSyncPayload(NbtCompound blockdata) implements CustomPayload {
    public static final CustomPayload.Id<StructureSyncPayload> ID = new CustomPayload.Id<>(ServerNetworking.STRUCTURE_SYNC);
    public static final PacketCodec<RegistryByteBuf, StructureSyncPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.NBT_COMPOUND, StructureSyncPayload::blockdata,
        StructureSyncPayload::new
    );


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
