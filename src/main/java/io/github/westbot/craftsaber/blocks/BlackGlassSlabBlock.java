package io.github.westbot.craftsaber.blocks;

import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;

public class BlackGlassSlabBlock  extends Block {
    public BlackGlassSlabBlock() {
        super(Settings.create().slipperiness(0.98f).hardness(3f).resistance(5f).sounds(BlockSoundGroup.GLASS));
    }
}
