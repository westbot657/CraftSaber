package io.github.westbot.craftsaber;

import io.github.westbot.craftsaber.debug.DebugCommands;
import io.github.westbot.craftsaber.lightsystems.LightController;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class CraftSaber implements ModInitializer {
    public static final String MOD_ID = "craftsaber";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashMap<String, LightController> lightShows;

    @Override
    public void onInitialize() {

        lightShows = new HashMap<>();

        ModItems.init();
        ModBlocks.init();
        ModEntities.init();


        DebugCommands.init();

    }
}
