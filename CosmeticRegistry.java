package pl.kosmetyki;

import java.util.*;

public class CosmeticRegistry {
    public static final List<Cosmetic> ALL = new ArrayList<>();

    private static void add(Category c, String id, String name, int color) {
        ALL.add(new Cosmetic(id, name, c, color));
    }

    static {
        add(Category.SKRZYDLA, "butterfly_wings", "Motyle skrzydła", 0xFF3FA9F5);
        add(Category.SKRZYDLA, "demon_wings", "Demoniczne skrzydła", 0xFFB02020);
        add(Category.SKRZYDLA, "angel_wings", "Anielskie skrzydła", 0xFFF5F5F5);
        add(Category.SKRZYDLA, "rainbow_wings", "Tęczowe skrzydła", 0xFFE0A030);
        add(Category.SKRZYDLA, "halloween_wings", "Halloweenowe skrzydła", 0xFFE07010);
        add(Category.PELERYNY, "pirate_cloak", "Piracka peleryna", 0xFF202020);
        add(Category.PELERYNY, "earth_cloak", "Peleryna Ziemi", 0xFF2060C0);
        add(Category.PELERYNY, "strength_cloak", "Peleryna siły", 0xFFC02030);
        add(Category.PELERYNY, "winter_cloak", "Zimowa peleryna", 0xFF9CD0F0);
        add(Category.HELMY, "flaming_crown", "Płonąca korona", 0xFFF07010);
        add(Category.HELMY, "gold_crown", "Złota korona", 0xFFF8D060);
        add(Category.HELMY, "santa_hat", "Czapka Mikołaja", 0xFFC02020);
        add(Category.HELMY, "top_hat", "Cylinder", 0xFF181818);
        add(Category.RECE, "boxing_gloves", "Rękawice bokserskie", 0xFFB02020);
        add(Category.RECE, "angel_gloves", "Anielskie rękawice", 0xFFF0E0B0);
        add(Category.RECE, "corrupted_fists", "Skażone pięści", 0xFF8030C0);
        add(Category.AURY, "sponge_aura", "Aura gąbki", 0xFF30B0C0);
        add(Category.AURY, "heart_aura", "Aura serc", 0xFFE03050);
        add(Category.PETY, "mini_wolf", "Mini wilk", 0xFF909090);
        add(Category.PETY, "mini_crab", "Mini krab", 0xFFD04030);
        add(Category.BUTY, "pirate_boots", "Pirackie buty", 0xFF6B3A1A);
        add(Category.BUTY, "crimson_boots", "Karmazynowe buty", 0xFF901020);
    }

    public static List<Cosmetic> of(Category c, String filter) {
        String f = filter == null ? "" : filter.toLowerCase(Locale.ROOT);
        List<Cosmetic> out = new ArrayList<>();
        for (Cosmetic x : ALL)
            if (x.category() == c && x.name().toLowerCase(Locale.ROOT).contains(f)) out.add(x);
        return out;
    }

    public static Cosmetic byId(String id) {
        for (Cosmetic x : ALL) if (x.id().equals(id)) return x;
        return null;
    }
}
