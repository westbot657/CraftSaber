package io.github.westbot.craftsaber.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.westbot.craftsaber.data.Stash;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class SaberTrailRenderer {

    public static void render(Vector3f blade_base, Vector3f blade_tip, Stash<Pair<Vector3f, Vector3f>> stash, int col) {

        Vector3f current_base = blade_base;
        Vector3f current_tip = blade_tip;
        Vec3d cam = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();

        if (!stash.isEmpty()) {
            var tessellator = Tessellator.getInstance();
            var trail_buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            int opacity = 0;
            float step = 0x7F / (float) stash.getSize();
            for (Pair<Vector3f, Vector3f> ab : stash) {
                Vector3f a = ab.getLeft();
                Vector3f b = ab.getRight();

                int op = (0x7F - ((int)(step * opacity))) << 24;
                opacity++;

                trail_buffer.vertex((float) (current_base.x - cam.x), (float) (current_base.y - cam.y), (float) (current_base.z - cam.z)).color(col + op);
                trail_buffer.vertex((float) (a.x - cam.x), (float) (a.y - cam.y), (float) (a.z - cam.z)).color(col + op);
                trail_buffer.vertex((float) (b.x - cam.x), (float) (b.y - cam.y), (float) (b.z - cam.z)).color(col + op);
                trail_buffer.vertex((float) (current_tip.x - cam.x), (float) (current_tip.y - cam.y), (float) (current_tip.z - cam.z)).color(col + op);

                current_base = a;
                current_tip = b;

            }
            ShaderProgram oldShader = RenderSystem.getShader();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            BufferRenderer.drawWithGlobalProgram(trail_buffer.end());
            RenderSystem.setShader(() -> oldShader);
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }

        stash.push(new Pair<>(blade_base, blade_tip));

    }

}
