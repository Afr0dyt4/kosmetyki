package pl.kosmetyki;

import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

/**
 * Rysuje wybrane kosmetyki na graczu (na razie proste bryly w kolorze kosmetyki).
 * Zeby miec prawdziwe modele jak na filmie, podmien metody box()/wing() na wlasne modele + tekstury.
 */
public class CosmeticFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    private static final Identifier WHITE = Identifier.ofVanilla("textures/misc/white.png");

    public CosmeticFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> ctx) {
        super(ctx);
    }

    @Override
    public void render(MatrixStack m, VertexConsumerProvider vcp, int light,
                       PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        if (state.invisible) return;
        Config cfg = Config.INSTANCE;
        VertexConsumer vc = vcp.getBuffer(RenderLayer.getEntityTranslucent(WHITE));
        float s = cfg.scale;

        Cosmetic wings = cfg.equipped(Category.SKRZYDLA);
        if (wings != null) {
            for (int side = -1; side <= 1; side += 2) {
                m.push();
                m.translate(0, 0.1, 0.15);
                m.scale(s, s, s);
                m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(side * 25));
                m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(side * -10));
                box(m, vc, light, side > 0 ? 0.05f : -0.75f, -0.1f, 0f, 0.7f, 0.8f, 0.02f, wings.color());
                m.pop();
            }
        }

        Cosmetic cloak = cfg.equipped(Category.PELERYNY);
        if (cloak != null) {
            m.push();
            m.translate(0, 0, 0.16);
            m.scale(s, s, s);
            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6 + limbDistance * 20));
            box(m, vc, light, -0.28f, 0f, 0f, 0.56f, 1.0f, 0.03f, cloak.color());
            m.pop();
        }

        Cosmetic hat = cfg.equipped(Category.HELMY);
        if (hat != null) {
            m.push();
            getContextModel().head.rotate(m);
            m.scale(s, s, s);
            box(m, vc, light, -0.27f, -0.62f, -0.27f, 0.54f, 0.18f, 0.54f, hat.color());
            m.pop();
        }

        Cosmetic boots = cfg.equipped(Category.BUTY);
        if (boots != null) {
            for (int side = -1; side <= 1; side += 2) {
                m.push();
                (side < 0 ? getContextModel().rightLeg : getContextModel().leftLeg).rotate(m);
                box(m, vc, light, -0.14f, 0.75f, -0.14f, 0.28f, 0.2f, 0.28f, boots.color());
                m.pop();
            }
        }
    }

    /** Prostopadloscian (x,y,z = rog, w,h,d = rozmiar) w jednym kolorze. */
    private static void box(MatrixStack m, VertexConsumer vc, int light,
                            float x, float y, float z, float w, float h, float d, int argb) {
        Matrix4f p = m.peek().getPositionMatrix();
        float x2 = x + w, y2 = y + h, z2 = z + d;
        quad(p, vc, light, argb, x, y, z2, x2, y, z2, x2, y2, z2, x, y2, z2, 0, 0, 1);   // przod
        quad(p, vc, light, argb, x2, y, z, x, y, z, x, y2, z, x2, y2, z, 0, 0, -1);      // tyl
        quad(p, vc, light, argb, x, y, z, x, y, z2, x, y2, z2, x, y2, z, -1, 0, 0);      // lewo
        quad(p, vc, light, argb, x2, y, z2, x2, y, z, x2, y2, z, x2, y2, z2, 1, 0, 0);   // prawo
        quad(p, vc, light, argb, x, y, z, x2, y, z, x2, y, z2, x, y, z2, 0, -1, 0);      // dol
        quad(p, vc, light, argb, x, y2, z2, x2, y2, z2, x2, y2, z, x, y2, z, 0, 1, 0);   // gora
    }

    private static void quad(Matrix4f p, VertexConsumer vc, int light, int argb,
                             float x1, float y1, float z1, float x2, float y2, float z2,
                             float x3, float y3, float z3, float x4, float y4, float z4,
                             float nx, float ny, float nz) {
        v(p, vc, light, argb, x1, y1, z1, 0, 1, nx, ny, nz);
        v(p, vc, light, argb, x2, y2, z2, 1, 1, nx, ny, nz);
        v(p, vc, light, argb, x3, y3, z3, 1, 0, nx, ny, nz);
        v(p, vc, light, argb, x4, y4, z4, 0, 0, nx, ny, nz);
    }

    private static void v(Matrix4f p, VertexConsumer vc, int light, int argb,
                          float x, float y, float z, float u, float v, float nx, float ny, float nz) {
        vc.vertex(p, x, y, z).color(argb).texture(u, v)
          .overlay(OverlayTexture.DEFAULT_UV).light(light).normal(nx, ny, nz);
    }
}
