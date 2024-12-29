package io.github.westbot.craftsaber.lightsystems;

import io.github.westbot.craftsaber.CraftSaber;
import net.minecraft.nbt.NbtCompound;

public class Util {

    public static double easeInSine(double dt) {
        return 1 - Math.cos((dt * Math.PI) / 2.0);
    }

    public static double easeOutSine(double dt) {
        return Math.sin((dt * Math.PI) / 2.0);
    }

    public static double easeInOutSine(double dt) {
        return -(Math.cos(Math.PI * dt) - 1.0) / 2.0;
    }


    private static final double BACK_C1 = 1.70158;
    private static final double BACK_C2 = BACK_C1 * 1.525;
    private static final double BACK_C3 = BACK_C1 + 1;

    public static double easeInBack(double dt) {

        return BACK_C3 * dt * dt * dt - BACK_C1 * dt * dt;
    }

    public static double easeOutBack(double dt) {

        return 1 + BACK_C3 * Math.pow(dt - 1, 3) + BACK_C1 * Math.pow(dt - 1, 2);
    }

    public static double easeInOutBack(double dt) {
        return dt < 0.5
            ? (Math.pow(2.0 * dt, 2.0) * ((BACK_C2 + 1) * 2 * dt - BACK_C2)) / 2.0
            : (Math.pow(2.0 * dt - 2.0, 2.0) * ((BACK_C2 + 1) * (dt * 2.0 - 2.0) + BACK_C2) + 2.0) / 2.0;
    }

    /*
    Lightshow serialization:
    NBTComp{
        "lightshow_id": "Lightshow 1",
        "light_structures": NBTComp{
            "1": NBTComp{
                "offset": Vec3{...},
                "blockdata": ???
            },
            "2": ...
        },
        "lights": NBTComp{
            "category name 1": NBTComp{
                "group name 1.1": NBTComp{
                    "lights": "1",
                    "count": 16,
                    "position": Vec3{x:?, y:?, z:?},
                    "rotation": Vec3{x:?, y:?, z:?}
                }
            }
        }
    }
     */

    /// Deserializes an NBT Compound and updates lightshow data accordingly
    public static void deserializeLights(NbtCompound data) {

        String id = data.getString("lightshow_id");

        LightController controller;
        if (CraftSaber.lightShows.containsKey(id)) {
            controller = CraftSaber.lightShows.get(id);
        } else {
            controller = new LightController(id);
            CraftSaber.lightShows.put(id, controller);
        }

    }

    public static NbtCompound serializeLights(LightController controller) {
        NbtCompound dataOut = new NbtCompound();

        dataOut.putString("lightshow_id", controller.getId());

        return dataOut;
    }

}
