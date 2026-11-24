package pl.kosmetyki;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.*;
import java.util.*;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("kosmetyki.json");

    public int theme = 7;                 // domyslnie Pomaranczowy jak na filmie
    public float scale = 1.0f;            // rozmiar kosmetyk (suwak w Ustawieniach)
    public Map<String, String> equipped = new HashMap<>(); // kategoria -> id kosmetyki

    public static Config INSTANCE = load();

    public static Config load() {
        try {
            if (Files.exists(FILE)) return GSON.fromJson(Files.readString(FILE), Config.class);
        } catch (Exception ignored) {}
        return new Config();
    }

    public void save() {
        try { Files.writeString(FILE, GSON.toJson(this)); } catch (Exception ignored) {}
    }

    public Cosmetic equipped(Category c) {
        String id = equipped.get(c.name());
        return id == null ? null : CosmeticRegistry.byId(id);
    }

    public void toggle(Cosmetic c) {
        if (c.id().equals(equipped.get(c.category().name()))) equipped.remove(c.category().name());
        else equipped.put(c.category().name(), c.id());
        save();
    }
}
