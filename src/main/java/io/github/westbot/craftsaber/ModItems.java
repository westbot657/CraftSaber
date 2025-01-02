package io.github.westbot.craftsaber;

import io.github.westbot.craftsaber.items.Saber;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Saber SABER = (Saber) register(new Saber(), "saber");

    public static Item register(Item item, String id) {

        return Registry.register(Registries.ITEM, Identifier.of(CraftSaber.MOD_ID, id), item);
    }

    public static void init() {
        
    }

}
