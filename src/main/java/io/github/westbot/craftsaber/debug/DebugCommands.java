package io.github.westbot.craftsaber.debug;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.westbot.craftsaber.CraftSaber;
import io.github.westbot.craftsaber.ModEntities;
import io.github.westbot.craftsaber.entities.LightDisplayEntity;
import io.github.westbot.craftsaber.lightsystems.Util;
import io.github.westbot.craftsaber.networking.StructureSyncPayload;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;
import java.util.List;

public class DebugCommands {

    public static int debugLightDisplay(CommandContext<ServerCommandSource> context) {

        CraftSaber.LOGGER.info("Called debug light display");

        ServerWorld world = context.getSource().getWorld();

        if (!world.isClient) {
            BlockPos pos1 = BlockPosArgumentType.getBlockPos(context, "pos1");
            BlockPos pos2 = BlockPosArgumentType.getBlockPos(context, "pos2");
            Vec3d offset = Vec3ArgumentType.getVec3(context, "offset");
            String id = context.getArgument("structure_id", String.class);

            Pair<Vec3d, List<Pair<Vec3d, BlockState>>> block_data = Util.collect_blocks(pos1, pos2, offset, world);
            CraftSaber.structure_cache.put(id, block_data);
            CraftSaber.save_structures(context.getSource().getServer());

            Vec3d pos = context.getSource().getPosition();

            LightDisplayEntity entity = new LightDisplayEntity(ModEntities.LIGHT_DISPLAY, world);
            entity.setPosition(pos);
            entity.setStructureId(id);
            world.spawnEntity(entity);

            for (ServerPlayerEntity player : world.getPlayers()) {
                ServerPlayNetworking.send(player, new StructureSyncPayload(Util.serialize_structure(id, block_data)));
            }
        }

        return 1;
    }

    public static int rotateLightDisplays(CommandContext<ServerCommandSource> context) {

        try {
            Collection<? extends Entity> entities = EntityArgumentType.getEntities(context, "entities");
            Vec3d rotation = Vec3ArgumentType.getVec3(context, "rotation");

            for (Entity entity : entities) {
                if (entity instanceof LightDisplayEntity lightDisplayEntity) {
                    lightDisplayEntity.setRotation(rotation.multiply(MathHelper.RADIANS_PER_DEGREE));
                }
            }

        } catch (CommandSyntaxException e) {
            context.getSource().sendError(Text.literal(e.getMessage()));
        }

        return 1;
    }

    public static int printCache(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() ->
            Text.literal("Cache: " + CraftSaber.structure_cache.toString()),
            false
        );

        return 1;
    }


    public static void init() {

        CommandRegistrationCallback.EVENT.register(
            (
                (dispatcher,
                 commandRegistryAccess,
                 registrationEnvironment
                ) -> dispatcher.register(
                    CommandManager.literal("craftsaber").then(
                        CommandManager.literal("debug").then(
                            CommandManager.literal("light_display").then(
                                CommandManager.literal("spawn").then(
                                    CommandManager.argument("pos1", BlockPosArgumentType.blockPos()).then(
                                        CommandManager.argument("pos2", BlockPosArgumentType.blockPos()).then(
                                            CommandManager.argument("offset", Vec3ArgumentType.vec3(false)).then(
                                                CommandManager.argument("structure_id", StringArgumentType.string())
                                                    .executes(DebugCommands::debugLightDisplay)
                                            )
                                        )
                                    )
                                )
                            ).then(
                                CommandManager.literal("rotate").then(
                                    CommandManager.argument("entities", EntityArgumentType.entities()).then(
                                        CommandManager.argument("rotation", Vec3ArgumentType.vec3(false))
                                            .executes(DebugCommands::rotateLightDisplays)
                                    )
                                )
                            )
                        ).then(
                            CommandManager.literal("cache").then(
                                CommandManager.literal("structures")
                                    .executes(DebugCommands::printCache)
                            )
                        )
                    )
                )
            )
        );


    }

}
