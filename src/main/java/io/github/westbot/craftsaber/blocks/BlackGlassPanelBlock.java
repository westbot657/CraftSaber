package io.github.westbot.craftsaber.blocks;

import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;

public class BlackGlassPanelBlock extends Block {
    public BlackGlassPanelBlock() {
        super(Settings.create().slipperiness(0.98f).hardness(3f).resistance(5f).sounds(BlockSoundGroup.GLASS));
    }
}
