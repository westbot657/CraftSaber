package io.github.westbot.craftsaber;

import io.github.westbot.craftsaber.blocks.BlackMirrorBlock;
import io.github.westbot.craftsaber.blocks.BlackMirrorPanelBlock;
import io.github.westbot.craftsaber.blocks.BlackMirrorPillarBlock;
import io.github.westbot.craftsaber.blocks.BlackMirrorSlabBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final BlackMirrorBlock BLACK_MIRROR_BLOCK = (BlackMirrorBlock) register(new BlackMirrorBlock(), "black_mirror_block");
    public static final BlackMirrorSlabBlock BLACK_MIRROR_SLAB_BLOCK = (BlackMirrorSlabBlock) register(new BlackMirrorSlabBlock(), "black_mirror_slab");
    public static final BlackMirrorPillarBlock BLACK_MIRROR_PILLAR_BLOCK = (BlackMirrorPillarBlock) register(new BlackMirrorPillarBlock(), "black_mirror_pillar");
    public static final BlackMirrorPanelBlock BLACK_MIRROR_PANEL_BLOCK = (BlackMirrorPanelBlock) register(new BlackMirrorPanelBlock(), "black_mirror_panel");


    private static Block register(Block block, String path) {
        Registry.register(Registries.BLOCK, Identifier.of(CraftSaber.MOD_ID, path), block);
        Registry.register(Registries.ITEM, Identifier.of(CraftSaber.MOD_ID, path), new BlockItem(block, new Item.Settings()));
        return block;
    }

    public static void init() {

    }

}
