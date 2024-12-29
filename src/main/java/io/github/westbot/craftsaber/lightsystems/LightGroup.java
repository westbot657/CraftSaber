package io.github.westbot.craftsaber.lightsystems;

import java.util.ArrayList;
import java.util.List;

public class LightGroup {

    private int lightCount;
    private ArrayList<LightObject> lights;

    public LightGroup() {
        this.lightCount = 0;
        this.lights = new ArrayList<>();
    }

    public int getLightCount() {
        return this.lightCount;
    }

    public void addLight(LightObject light) {
        this.lights.add(light);
        this.lightCount++;
    }

    public void removeLight(LightObject light) {
        this.lights.remove(light);
        this.lightCount--;
    }

    public List<LightObject> selectLights(LightSelector selector) {
        ArrayList<LightObject> lights = new ArrayList<>();




        return lights;
    }

}
