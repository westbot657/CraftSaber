package io.github.westbot.craftsaber.blocks;

import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;

// has a 16x16 grid for selecting tiles that are either: Off, Color1, or Color2
public class LightTileBlock extends Block {
    public LightTileBlock() {
        super(Settings.create().slipperiness(0.98f).hardness(3f).resistance(5f).sounds(BlockSoundGroup.GLASS));
    }
}
