package io.github.westbot.craftsaber.data;

public class PixelRange {

    public Integer x1;
    public Integer y1;
    public Integer x2;
    public Integer y2;
    public LightColor color;

    public PixelRange(Integer x1, Integer y1, Integer x2, Integer y2, LightColor color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    public PixelRange copy() {
        return new PixelRange(this.x1, this.y1, this.x2, this.y2, this.color);
    }

}
