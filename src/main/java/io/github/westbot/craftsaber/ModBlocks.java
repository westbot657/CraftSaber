package io.github.westbot.craftsaber;

import io.github.westbot.craftsaber.blocks.*;
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

    public static final BlackGlassBlock BLACK_GLASS_BLOCK = (BlackGlassBlock) register(new BlackGlassBlock(), "black_glass_block");
    public static final BlackGlassSlabBlock BLACK_GLASS_SLAB_BLOCK = (BlackGlassSlabBlock) register(new BlackGlassSlabBlock(), "black_glass_slab");
    public static final BlackGlassPillarBlock BLACK_GLASS_PILLAR_BLOCK = (BlackGlassPillarBlock) register(new BlackGlassPillarBlock(), "black_glass_pillar");
    public static final BlackGlassPanelBlock BLACK_GLASS_PANEL_BLOCK = (BlackGlassPanelBlock) register(new BlackGlassPanelBlock(), "black_glass_panel");

    public static final FilledLightTileBlock FILLED_LIGHT_TILE_BLOCK = (FilledLightTileBlock) register(new FilledLightTileBlock(), "filled_light_tile");
    public static final EdgeLightTileBlock EDGE_LIGHT_TILE_BLOCK = (EdgeLightTileBlock) register(new EdgeLightTileBlock(), "edge_light_tile");
    public static final CornerLightTileBlock CORNER_LIGHT_TILE_BLOCK = (CornerLightTileBlock) register(new CornerLightTileBlock(), "corner_light_tile");
    public static final ColumnLightTileBlock COLUMN_LIGHT_TILE_BLOCK = (ColumnLightTileBlock) register(new ColumnLightTileBlock(), "column_light_tile");
    public static final EndLightTileBlock END_LIGHT_TILE_BLOCK = (EndLightTileBlock) register(new EndLightTileBlock(), "end_light_tile");


    private static Block register(Block block, String path) {
        Registry.register(Registries.BLOCK, Identifier.of(CraftSaber.MOD_ID, path), block);
        Registry.register(Registries.ITEM, Identifier.of(CraftSaber.MOD_ID, path), new BlockItem(block, new Item.Settings()));
        return block;
    }

    public static void init() {

    }

}
