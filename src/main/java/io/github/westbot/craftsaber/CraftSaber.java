package io.github.westbot.craftsaber;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.westbot.craftsaber.debug.DebugCommands;
import io.github.westbot.craftsaber.lightsystems.LightController;
import io.github.westbot.craftsaber.lightsystems.Util;
import io.github.westbot.craftsaber.networking.ServerNetworking;
import io.github.westbot.craftsaber.networking.StructureSyncPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class CraftSaber implements ModInitializer {
    public static final String MOD_ID = "craftsaber";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Need to put this link somewhere unobtrusive, like a credits page somewhere in settings or as an item.
    public static final String KO_FI = "https://ko-fi.com/westbot";

    public static HashMap<String, Pair<Vec3d, List<Pair<Vec3d, BlockState>>>> structure_cache;
    public static HashMap<String, LightController> lightShows;


    @Override
    public void onInitialize() {

        LOGGER.info("Initializing CraftSaber");
        structure_cache = new HashMap<>();
        lightShows = new HashMap<>();

        ModItems.init();
        ModBlocks.init();
        ModEntities.init();
        ModItemGroup.init();

        ServerNetworking.init();

        DebugCommands.init();


        ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
            LOGGER.info("Loading Light Display structures");
            load_structures(minecraftServer);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(CraftSaber::save_structures);

        ServerPlayConnectionEvents.JOIN.register((serverPlayNetworkHandler, packetSender, minecraftServer) -> {
            LOGGER.info("Syncing Light displays with player...");
            if (structure_cache.isEmpty()) {
                load_structures(minecraftServer);
            }
            structure_cache.forEach((k, v) -> {
                packetSender.sendPacket(new StructureSyncPayload(Util.serialize_structure(k, v)));
            });
        });

    }

    public static void load_structures(MinecraftServer server) {
        Path folderPath = server.getPath("./craftsaber/");
        if (!folderPath.toFile().exists()) {
            boolean ignored = folderPath.toFile().mkdirs();
        }
        NbtCompound data = new NbtCompound();

        File structure_file = server.getPath("./craftsaber/structure_data.txt").toFile();
        if (structure_file.exists()) {
            try {
                String nbt = Files.readString(server.getPath("./craftsaber/structure_data.txt"));
                NbtHelper.fromNbtProviderString(nbt);
            } catch (IOException | CommandSyntaxException e) {
                LOGGER.error("Error loading light displays: ", e);

            }
        } else {
            try {
                boolean ignored = structure_file.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Error Creating light structure file.", e);
            }
        }


        for (String k : data.getKeys()) {
            NbtElement v = data.get(k);

            if (v instanceof NbtCompound comp) {
                Pair<Vec3d, List<Pair<Vec3d, BlockState>>> structure_data = Util.deserialize_structure(comp);
                structure_cache.put(k, structure_data);
            }

        }

    }

    public static void save_structures(MinecraftServer server) {
        Path folderPath = server.getPath("./craftsaber/");
        if (!folderPath.toFile().exists()) {
            boolean ignored = folderPath.toFile().mkdirs();
        }

        NbtCompound data = new NbtCompound();

        structure_cache.forEach((k, v) -> {
            data.put(k, Util.serialize_structure(k, v));
        });


        File structure_file = server.getPath("./craftsaber/structure_data.txt").toFile();
        if (structure_file.exists()) {
            try {
                Files.writeString(server.getPath("./craftsaber/structure_data.txt"), NbtHelper.toNbtProviderString(data));
            } catch (IOException e) {
                LOGGER.error("Error saving light structure file.", e);
            }
        } else {
            try {
                boolean ignored = structure_file.createNewFile();
                try {
                    Files.writeString(server.getPath("./craftsaber/structure_data.txt"), NbtHelper.toNbtProviderString(data));
                } catch (IOException e) {
                    LOGGER.error("Error saving light structure file.", e);
                }
            } catch (IOException e) {
                LOGGER.error("Error saving light structure file.", e);
            }
        }

    }

}
