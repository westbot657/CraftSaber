package io.github.westbot.craftsaber.blocks;

import io.github.westbot.craftsaber.data.LightTile;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.IntProperty;

// has a 16x16 grid for selecting tiles that are either: Off, Color1, or Color2   so... 3^256 possibilities... aka too many

// pre-defined light types:
// edges 2^4 = 16
// full
// half  7 (side halves, half but centered)

public class LightTileBlock extends Block implements LightTile {

    public static final IntProperty X_POS = IntProperty.of("x_pos", Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final IntProperty Y_POS = IntProperty.of("y_pos", Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final IntProperty Z_POS = IntProperty.of("z_pos", Integer.MIN_VALUE, Integer.MAX_VALUE);

    public LightTileBlock() {
        super(Settings.create().slipperiness(0.98f).hardness(3f).resistance(5f).sounds(BlockSoundGroup.GLASS));
    }
}



