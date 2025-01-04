package io.github.westbot.craftsaber.data;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.List;

public class StructureData {

    private BlockPos controllerPos;
    private HashMap<Vec3d, BlockState> structureData;
    private HashMap<Integer, BlockPos> blockEntities;
    private List<BlockPos> children;
    private String id;

    public StructureData(BlockPos controllerPos, String id, HashMap<Vec3d, BlockState> structure_data, HashMap<Integer, BlockPos> block_entities, List<BlockPos> children) {
        this.controllerPos = controllerPos;
        this.id = id;
        structureData = structure_data;
        blockEntities = block_entities;
        this.children = children;
    }




}
