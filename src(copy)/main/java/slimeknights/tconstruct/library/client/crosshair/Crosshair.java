package slimeknights.tconstruct.library.client.crosshair;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class Crosshair implements ICrosshair {

    private final Identifier texture;
    private final int size;

    public Crosshair(Identifier texture) {
        this(texture, 16);
    }

    public Crosshair(Identifier texture, int size) {
        this.texture = texture;
        this.size = size;
    }

    @Override
    public void render(float charge, float width, float height, float partialTicks) {
        MinecraftClient mc = MinecraftClient.getMinecraft();

        mc.getTextureManager().bindTexture(texture);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableAlpha();
        float spread = (1.0f - charge) * 25f;

        drawCrosshair(spread, width, height, partialTicks);
    }

    protected void drawCrosshair(float spread, float width, float height, float partialTicks) {
        drawSquareCrosshairPart(width / 2f - spread, height / 2f - spread, 0);
        drawSquareCrosshairPart(width / 2f + spread, height / 2f - spread, 1);
        drawSquareCrosshairPart(width / 2f - spread, height / 2f + spread, 2);
        drawSquareCrosshairPart(width / 2f + spread, height / 2f + spread, 3);
    }

    protected void drawSquareCrosshairPart(double x, double y, int part) {
        int s = size / 4;

        double z = -90;

        double u1 = 0;
        double v1 = 0;

        switch (part) {
            // top left
            case 0:
                x -= s;
                y -= s;
                break;
            case 1:
                u1 = 0.5;
                x += s;
                y -= s;
                break;
            case 2:
                v1 = 0.5;
                x -= s;
                y += s;
                break;
            case 3:
                u1 = 0.5;
                v1 = 0.5;
                x += s;
                y += s;
                break;
        }

        double u2 = u1 + 0.5;
        double v2 = v1 + 0.5;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vb = tessellator.getBuffer();
        vb.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV);
        vb.pos(x - s, y - s, z).tex(u1, v1).endVertex();
        vb.pos(x - s, y + s, z).tex(u1, v2).endVertex();
        vb.pos(x + s, y + s, z).tex(u2, v2).endVertex();
        vb.pos(x + s, y - s, z).tex(u2, v1).endVertex();
        tessellator.draw();
    }
}
