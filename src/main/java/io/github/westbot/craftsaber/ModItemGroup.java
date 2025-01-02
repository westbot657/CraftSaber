package io.github.westbot.craftsaber;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {


    public static final RegistryKey<ItemGroup> GROUP_KEY = RegistryKey.of(
        Registries.ITEM_GROUP.getKey(), Identifier.of(CraftSaber.MOD_ID, "item_group")
    );
    public static final ItemGroup GROUP = FabricItemGroup.builder().icon(
        () -> new ItemStack(ModBlocks.BLACK_MIRROR_BLOCK)
    ).displayName(
        Text.translatable("itemGroup.craftsaber.group")
    ).build();

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, GROUP_KEY, GROUP);

        ItemGroupEvents.modifyEntriesEvent(GROUP_KEY).register(group -> {
            group.add(ModItems.SABER);
            group.add(ModBlocks.BLACK_MIRROR_BLOCK);
            group.add(ModBlocks.BLACK_MIRROR_SLAB_BLOCK);
            group.add(ModBlocks.BLACK_MIRROR_PANEL_BLOCK);
            group.add(ModBlocks.BLACK_MIRROR_PILLAR_BLOCK);

            group.add(ModBlocks.BLACK_GLASS_BLOCK);
            group.add(ModBlocks.BLACK_GLASS_SLAB_BLOCK);
            group.add(ModBlocks.BLACK_GLASS_PANEL_BLOCK);
            group.add(ModBlocks.BLACK_GLASS_PILLAR_BLOCK);

            group.add(ModBlocks.FILLED_LIGHT_TILE_BLOCK);
            group.add(ModBlocks.EDGE_LIGHT_TILE_BLOCK);
            group.add(ModBlocks.CORNER_LIGHT_TILE_BLOCK);
            group.add(ModBlocks.COLUMN_LIGHT_TILE_BLOCK);
            group.add(ModBlocks.END_LIGHT_TILE_BLOCK);
        });

    }
}
