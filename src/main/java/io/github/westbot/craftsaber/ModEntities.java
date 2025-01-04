package io.github.westbot.craftsaber;

import io.github.westbot.craftsaber.entities.LightDisplayEntity;
import io.github.westbot.craftsaber.entities.NoteEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<LightDisplayEntity> LIGHT_DISPLAY = Registry.register(
        Registries.ENTITY_TYPE, Identifier.of(CraftSaber.MOD_ID, "light_display"),
        EntityType.Builder.create(LightDisplayEntity::new, SpawnGroup.MISC)
            .dimensions(0.1f, 0.1f).build()
    );

    public static final EntityType<NoteEntity> NOTE_ENTITY = Registry.register(
        Registries.ENTITY_TYPE, Identifier.of(CraftSaber.MOD_ID, "note_entity"),
        EntityType.Builder.create(NoteEntity::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f).build()
    );

    public static void init() {

    }

}
