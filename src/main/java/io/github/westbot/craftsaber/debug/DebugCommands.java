package io.github.westbot.craftsaber.debug;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;

public class DebugCommands {

    public static int debugLightDisplay(CommandContext<ServerCommandSource> context) {

        BlockPos pos1 = context.getArgument("pos1", BlockPos.class);
        BlockPos pos2 = context.getArgument("pos2", BlockPos.class);
        String id = context.getArgument("structure_id", String.class);

        BlockPos min = BlockPos.min(pos1, pos2);
        BlockPos max = BlockPos.max(pos1, pos2);

        // TODO: iterate and add blocks to the structure

        return 1;
    }


    public static void init() {

        CommandRegistrationCallback.EVENT.register(
            (
                (dispatcher,
                 commandRegistryAccess,
                 registrationEnvironment
                ) -> {

                    dispatcher.register(
                        CommandManager.literal("craftsaber")
                            .then(
                                CommandManager.literal("debug")
                                    .then(
                                        CommandManager.literal("light_display")
                                            .then(
                                                CommandManager.argument("pos1", BlockPosArgumentType.blockPos())
                                                    .then(
                                                        CommandManager.argument("pos2", BlockPosArgumentType.blockPos())
                                                            .then(
                                                                CommandManager.argument("structure_id", StringArgumentType.string())
                                                                    .executes(DebugCommands::debugLightDisplay)
                                                            )
                                                    )
                                            )

                                    )
                            )
                    );

                }
            )
        );


    }

}
