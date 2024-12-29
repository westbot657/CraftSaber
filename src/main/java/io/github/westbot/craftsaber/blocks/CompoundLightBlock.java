package io.github.westbot.craftsaber.blocks;

import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;

// Used to have overlapping LightTiles, LaserReflectors, and non-full Mirror/Glass blocks.
public class CompoundLightBlock extends Block {
    public CompoundLightBlock() {
        super(Settings.create().slipperiness(0.98f).hardness(3f).resistance(5f).sounds(BlockSoundGroup.GLASS));
    }
}
