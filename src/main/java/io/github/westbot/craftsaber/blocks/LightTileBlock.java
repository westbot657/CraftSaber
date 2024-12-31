package io.github.westbot.craftsaber.blocks;

import io.github.westbot.craftsaber.CraftSaber;
import io.github.westbot.craftsaber.data.LightTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

// has a 16x16 grid for selecting tiles that are either: Off, Color1, or Color2   so... 3^256 possibilities... aka too many

// pre-defined light types:
// edges 2^4 = 16
// full
// half  7 (side halves, half but centered)

public class LightTileBlock extends Block implements LightTile {

    public LightTileBlock() {
        super(Settings.create().noCollision().hardness(3f).resistance(5f).sounds(BlockSoundGroup.GLASS));
    }

    public static VoxelShape getVoxel(BlockState state, DirectionProperty FACE, float thickness) {
        switch (state.get(FACE)) {
            case UP -> {
                return VoxelShapes.cuboid(
                    0, (16-thickness)/16, 0, 1, 1, 1
                );
            }
            case DOWN -> {
                return VoxelShapes.cuboid(
                    0, 0, 0, 1, thickness/16f, 1
                );
            }
            case NORTH -> {
                return VoxelShapes.cuboid(
                    0, 0, 0, 1, 1, thickness/16f
                );
            }
            case EAST -> {
                return VoxelShapes.cuboid(
                    (16-thickness)/16, 0, 0, 1, 1, 1
                );
            }
            case SOUTH -> {
                return VoxelShapes.cuboid(
                    0, 0, (16-thickness)/16, 1, 1, 1
                );
            }
            case WEST -> {
                return VoxelShapes.cuboid(
                    0, 0, 0, thickness/16f, 1, 1
                );
            }
            default -> {
                return VoxelShapes.fullCube();
            }
        }
    }

    public static Direction getPlaceOrientation(Direction face, Vec3d hit_pos) {

        double dx = (hit_pos.x % 1.0) - (0.5 * (hit_pos.x/Math.abs(hit_pos.x)));
        double dy = (hit_pos.y % 1.0) - (0.5 * (hit_pos.y/Math.abs(hit_pos.y)));
        double dz = (hit_pos.z % 1.0) - (0.5 * (hit_pos.z/Math.abs(hit_pos.z)));

        CraftSaber.LOGGER.info("xyz: {}, {}, {}", dx, dy, dz);

        Direction rotation;

        switch (face) {
            case NORTH:
            case SOUTH:
                if (Math.abs(dx) >= Math.abs(dy)) {
                    if (dx >= 0) {
                        rotation = Direction.EAST;
                    } else {
                        rotation = Direction.WEST;
                    }
                } else {
                    if (dy >= 0) {
                        rotation = Direction.UP;
                    } else {
                        rotation = Direction.DOWN;
                    }
                }
                break;
            case WEST:
            case EAST:
                if (Math.abs(dz) >= Math.abs(dy)) {
                    if (dz >= 0) {
                        rotation = Direction.SOUTH;
                    } else {
                        rotation = Direction.NORTH;
                    }
                } else {
                    if (dy >= 0) {
                        rotation = Direction.UP;
                    } else {
                        rotation = Direction.DOWN;
                    }
                }

                break;
            case DOWN:
            case UP:
            default:
                if (Math.abs(dx) >= Math.abs(dz)) {
                    if (dx >= 0) {
                        rotation = Direction.EAST;
                    } else {
                        rotation = Direction.WEST;
                    }
                } else {
                    if (dz >= 0) {
                        rotation = Direction.SOUTH;
                    } else {
                        rotation = Direction.NORTH;
                    }
                }

                break;
        }

        return rotation;

    }

}



