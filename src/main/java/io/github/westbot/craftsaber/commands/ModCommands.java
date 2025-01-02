package io.github.westbot.craftsaber.commands;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.westbot.craftsaber.CraftSaber;
import io.github.westbot.craftsaber.ModComponents;
import io.github.westbot.craftsaber.ModEntities;
import io.github.westbot.craftsaber.ModItems;
import io.github.westbot.craftsaber.entities.LightDisplayEntity;
import io.github.westbot.craftsaber.entities.NoteEntity;
import io.github.westbot.craftsaber.lightsystems.Util;
import io.github.westbot.craftsaber.networking.StructureSyncPayload;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
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

public class ModCommands {

    public static int debugLightDisplay(CommandContext<ServerCommandSource> context) {

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

    public static int spawnNote(CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();

        if (!world.isClient) {
            Vec3d spawnPoint = Vec3ArgumentType.getVec3(context, "position");
            int color  = IntegerArgumentType.getInteger(context, "color");
            float rotation = FloatArgumentType.getFloat(context, "rotation");
            float yaw;
            boolean has_dot;

            try {
                has_dot = BoolArgumentType.getBool(context, "has_dot");
            } catch (Exception ignored) {
                has_dot = false;
            }

            try {
                yaw = FloatArgumentType.getFloat(context, "yaw");
            } catch (Exception ignored) {

                Entity entity = context.getSource().getEntity();
                if (entity == null) {
                    yaw = 0;
                } else {
                    yaw = entity.getYaw();
                }
            }

            NoteEntity note = new NoteEntity(ModEntities.NOTE_ENTITY, world);
            note.setYaw(yaw);
            note.setDotNote(has_dot);
            note.setRoll(rotation);
            note.setColor(color);
            note.setPosition(spawnPoint);
            world.spawnEntity(note);

        }

        return 1;
    }

    public static int snapNote(CommandContext<ServerCommandSource> context) {

        try {
            for (Entity entity : EntityArgumentType.getEntities(context, "entities")) {
                if (entity instanceof NoteEntity note) {
                    Vec3d pos = note.getPos();

                    note.setPos(
                        Math.floor(pos.x) + 0.5,
                        Math.floor(pos.y) + 0.25,
                        Math.floor(pos.z) + 0.5
                        );

                }
            }
        } catch (CommandSyntaxException e) {
            context.getSource().sendFeedback(() -> Text.literal(e.getMessage()), false);
        }

        return 1;
    }

    private static void giveSaberToEntity(CommandContext<ServerCommandSource> context, LivingEntity entity, int color) {

        ItemStack stack = new ItemStack(ModItems.SABER);
        stack.set(ModComponents.SABER_COLOR, color);

        if (entity instanceof ServerPlayerEntity player) {
            player.getInventory().insertStack(stack.copy());
        }

    }

    public static int giveSaber(CommandContext<ServerCommandSource> context) {

        int color = HexArgumentType.getHex(context, "color");

        try {
            for (Entity entity : EntityArgumentType.getEntities(context, "target")) {
                if (entity instanceof ServerPlayerEntity le) {
                    giveSaberToEntity(context, le, color);
                }
            }
        } catch (CommandSyntaxException e) {
            context.getSource().sendFeedback(() -> Text.literal(e.getMessage()), false);
        } catch (IllegalArgumentException e) {
            Entity entity = context.getSource().getEntity();

            if (entity != null) {
                if (entity instanceof ServerPlayerEntity le) {
                    giveSaberToEntity(context, le, color);

                }
            }
        }

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
                                                    .executes(ModCommands::debugLightDisplay)
                                            )
                                        )
                                    )
                                )
                            ).then(
                                CommandManager.literal("rotate").then(
                                    CommandManager.argument("entities", EntityArgumentType.entities()).then(
                                        CommandManager.argument("rotation", Vec3ArgumentType.vec3(false))
                                            .executes(ModCommands::rotateLightDisplays)
                                    )
                                )
                            )
                        ).then(
                            CommandManager.literal("note").then(
                                CommandManager.literal("spawn").then(
                                    CommandManager.argument("position", Vec3ArgumentType.vec3(true)).then(
                                        CommandManager.argument("color", new HexArgumentType()).then(
                                            CommandManager.argument("rotation", FloatArgumentType.floatArg()).then(
                                                    CommandManager.argument("yaw", FloatArgumentType.floatArg()).then(
                                                            CommandManager.argument("has_dot", BoolArgumentType.bool())
                                                                .executes(ModCommands::spawnNote)
                                                        )
                                                        .executes(ModCommands::spawnNote)
                                                )
                                                .executes(ModCommands::spawnNote)
                                        )
                                    )
                                )
                            ).then(
                                CommandManager.literal("snap").then(
                                    CommandManager.argument("entities", EntityArgumentType.entities())
                                        .executes(ModCommands::snapNote)
                                )
                            )
                        ).then(
                            CommandManager.literal("cache").then(
                                CommandManager.literal("structures")
                                    .executes(ModCommands::printCache)
                            )
                        )
                    ).then(
                        CommandManager.literal("saber").then(
                            CommandManager.argument("color", new HexArgumentType()).then(
                                    CommandManager.argument("target", EntityArgumentType.entities())
                                        .executes(ModCommands::giveSaber)
                                )
                                .executes(ModCommands::giveSaber)
                        )
                    )
                )
            )
        );


    }

}
