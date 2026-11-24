package pl.kosmetyki;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KosmetykiMod implements ClientModInitializer {
    private static KeyBinding openKey;

    @Override
    public void onInitializeClient() {
        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.kosmetyki.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, "category.kosmetyki"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.wasPressed())
                if (client.currentScreen == null) client.setScreen(new KosmetykiScreen());
        });

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, helper, ctx) -> {
            if (renderer instanceof PlayerEntityRenderer pr) helper.register(new CosmeticFeatureRenderer(pr));
        });
    }
}
