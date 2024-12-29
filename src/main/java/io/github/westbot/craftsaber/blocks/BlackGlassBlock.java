package io.github.westbot.craftsaber.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;

// Material properties: Lets light glow through it but physical objects are not visible through it...
// Lasers however do not glow through the glass
public class BlackGlassBlock extends Block {
    public BlackGlassBlock() {
        super(Settings.create().slipperiness(0.98f).hardness(3f).resistance(5f).sounds(BlockSoundGroup.GLASS));
    }
}
