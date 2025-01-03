package io.github.westbot.craftsaber.mixin_data;

import io.github.westbot.craftsaber.data.Stash;
import net.minecraft.util.Pair;
import org.joml.Vector3f;

public interface ItemStackWithStash {

    Stash<Pair<Vector3f, Vector3f>> craftSaber$getStash();


}
