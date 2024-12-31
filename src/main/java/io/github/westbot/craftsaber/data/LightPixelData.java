package io.github.westbot.craftsaber.data;

import java.util.ArrayList;

public class LightPixelData {
    private ArrayList<PixelRange> pixel_ranges;

    public LightPixelData() {
        this.pixel_ranges = new ArrayList<>();
    }

    public LightPixelData(LightPixelData other) {
        this.pixel_ranges = new ArrayList<>();
        for (PixelRange range : other.pixel_ranges) {
            this.pixel_ranges.add(range.copy());
        }
    }

    public ArrayList<PixelRange> getPixelRanges() {
        return this.pixel_ranges;
    }

    public void setPixelRanges(ArrayList<PixelRange> ranges) {
        this.pixel_ranges = ranges;
    }

    public void addPixelRange(PixelRange range) {
        this.pixel_ranges.add(range);
    }

}
