package io.github.westbot.craftsaber.blocks;


import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;

// place to have lasers reflect off an adjacent surface
// should have a setting to not bounce if a light is in the path of reflection
public class LaserReflectorBlock extends Block {
    public LaserReflectorBlock() {
        super(Settings.create().slipperiness(0.98f).hardness(3f).resistance(5f).sounds(BlockSoundGroup.GLASS));
    }
}