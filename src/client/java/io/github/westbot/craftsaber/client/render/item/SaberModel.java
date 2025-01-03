package io.github.westbot.craftsaber.client.render.item;

import io.github.westbot.craftsaber.CraftSaber;
import io.github.westbot.craftsaber.items.Saber;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

public class SaberModel extends GeoModel<Saber> {
    @Override
    public Identifier getModelResource(Saber saber) {
        return Identifier.of(CraftSaber.MOD_ID, "geo/saber.geo.json");
    }

    @Override
    public Identifier getTextureResource(Saber saber) {
        return Identifier.of(CraftSaber.MOD_ID, "textures/item/template_saber.png");
    }

    @Override
    public Identifier getAnimationResource(Saber saber) {
        return Identifier.of(CraftSaber.MOD_ID, "animations/saber.animation.json");
    }

    @Override
    public @Nullable RenderLayer getRenderType(Saber animatable, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }

}
