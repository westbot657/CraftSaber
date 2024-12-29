package io.github.westbot.craftsaber.lightsystems;


import java.util.HashMap;

// The brain of any lightshow.
// responsible for loading, editing, saving, and playing light shows.
public class LightController {
    private String id;
    public HashMap<String, LightCategory> lightCategories;

    public LightController(String id) {
        this.id = id;
        this.lightCategories = new HashMap<>();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String new_id) {
        this.id = new_id;
    }

}
