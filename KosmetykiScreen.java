package pl.kosmetyki;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.List;

/** Menu jak na filmie: Paleta | Twórcy | Kosmetyki | Ustawienia + boczne kategorie + siatka 3 kolumny. */
public class KosmetykiScreen extends Screen {
    private static final String[] TABS = {"PALETA", "TWÓRCY", "KOSMETYKI", "USTAWIENIA"};
    private static final int W = 420, H = 250;

    private int tab = 2;
    private Category category = Category.RECE;
    private double scroll = 0;
    private TextFieldWidget search;
    private boolean draggingSlider = false;

    private int left, top;

    public KosmetykiScreen() { super(Text.literal("Kosmetyki")); }

    private int accent() { return Theme.ALL[Config.INSTANCE.theme].accent(); }

    @Override
    protected void init() {
        left = (width - W) / 2;
        top = (height - H) / 2;
        search = new TextFieldWidget(textRenderer, left + W - 215, top + 36, 120, 14, Text.literal("Szukaj"));
        search.setPlaceholder(Text.literal("Szukaj kosmetyki"));
        search.setChangedListener(s -> scroll = 0);
        addDrawableChild(search);
        search.setVisible(tab == 2);
    }

    // ---------- rysowanie ----------
    @Override
    public void render(DrawContext g, int mx, int my, float delta) {
        int acc = accent();
        int glow = (acc & 0x00FFFFFF) | 0x66000000;

        // ramka z poswiata w kolorze motywu i ciemne tlo panelu
        g.fill(left - 4, top - 4, left + W + 4, top + H + 4, glow);
        g.fill(left, top, left + W, top + H, 0xF0101820);

        // zakladki
        int tw = (W - 20) / 4;
        for (int i = 0; i < 4; i++) {
            int x = left + 8 + i * tw;
            boolean sel = i == tab;
            g.fill(x, top + 8, x + tw - 4, top + 24, sel ? (acc & 0x00FFFFFF) | 0xFF000000 : 0xFF0B1A28);
            g.drawCenteredTextWithShadow(textRenderer, TABS[i], x + (tw - 4) / 2, top + 12, sel ? 0xFF000000 : 0xFFFFFFFF);
        }

        switch (tab) {
            case 0 -> renderPalette(g, mx, my);
            case 1 -> renderCreators(g);
            case 2 -> renderCosmetics(g, mx, my);
            case 3 -> renderSettings(g);
        }
        search.setVisible(tab == 2);
        super.render(g, mx, my, delta);
    }

    private void renderPalette(DrawContext g, int mx, int my) {
        g.drawTextWithShadow(textRenderer, "Paleta kolorów", left + 12, top + 34, 0xFFFFFFFF);
        g.drawTextWithShadow(textRenderer, "Zmień kolor wyglądu BosCosmetics", left + 12, top + 46, 0xFF9AA5B1);
        for (int i = 0; i < Theme.ALL.length; i++) {
            int[] r = paletteCell(i);
            boolean sel = i == Config.INSTANCE.theme;
            boolean hov = mx >= r[0] && mx < r[0] + r[2] && my >= r[1] && my < r[1] + r[3];
            g.fill(r[0], r[1], r[0] + r[2], r[1] + r[3], sel ? 0xFF2A3A4A : hov ? 0xFF1E2C3A : 0xFF16212C);
            if (sel) g.drawBorder(r[0], r[1], r[2], r[3], Theme.ALL[i].accent());
            g.fill(r[0] + 8, r[1] + 8, r[0] + r[2] - 8, r[1] + 22, Theme.ALL[i].accent());
            g.drawTextWithShadow(textRenderer, Theme.ALL[i].name(), r[0] + 8, r[1] + 30, 0xFFFFFFFF);
            g.drawTextWithShadow(textRenderer, sel ? "Wybrany motyw" : "Kliknij, aby zmienić", r[0] + 8, r[1] + 44, 0xFF9AA5B1);
        }
    }

    private int[] paletteCell(int i) {
        int cw = 92, ch = 62, gap = 8;
        return new int[]{left + 12 + (i % 4) * (cw + gap), top + 66 + (i / 4) * (ch + gap), cw, ch};
    }

    private void renderCreators(DrawContext g) {
        g.drawTextWithShadow(textRenderer, "Twórcy", left + 12, top + 34, 0xFFFFFFFF);
        g.drawTextWithShadow(textRenderer, "Tu wpisz autorów modeli i tekstur kosmetyk.", left + 12, top + 50, 0xFF9AA5B1);
    }

    private void renderSettings(DrawContext g) {
        g.drawTextWithShadow(textRenderer, "Ustawienia", left + 12, top + 34, 0xFFFFFFFF);
        int x = left + 20, y = top + 70, w = W - 60;
        g.fill(x - 8, y - 20, x + w + 8, y + 40, 0xFF0B1A28);
        g.drawBorder(x - 8, y - 20, w + 16, 60, 0xFFCCCCCC);
        g.drawTextWithShadow(textRenderer, String.format("Skala: %.2f", Config.INSTANCE.scale), x, y - 12, 0xFFFFFFFF);
        g.fill(x, y + 4, x + w, y + 6, 0xFF4A5A6A);
        float t = (Config.INSTANCE.scale - 0.5f) / 1.5f;
        g.fill(x + (int) (t * w) - 3, y - 2, x + (int) (t * w) + 3, y + 12, accent());
        g.drawTextWithShadow(textRenderer, "Rozmiar widoku twoich kosmetyk", x, y + 22, 0xFF9AA5B1);
    }

    private void renderCosmetics(DrawContext g, int mx, int my) {
        g.drawTextWithShadow(textRenderer, category.label, left + 12, top + 38, 0xFFFFFFFF);

        // kategorie po prawej
        for (Category c : Category.values()) {
            int[] r = catBtn(c);
            boolean sel = c == category;
            g.fill(r[0], r[1], r[0] + r[2], r[1] + r[3], sel ? 0xFF2A3A4A : 0xFF0B1A28);
            if (sel) g.drawBorder(r[0], r[1], r[2], r[3], accent());
            g.drawTextWithShadow(textRenderer, c.label, r[0] + 8, r[1] + 4, 0xFFFFFFFF);
        }

        // siatka (3 kolumny) z przycinaniem
        List<Cosmetic> list = CosmeticRegistry.of(category, search.getText());
        int gx = left + 12, gy = top + 58, gw = W - 105, gh = H - 68;
        g.enableScissor(gx, gy, gx + gw, gy + gh);
        Cosmetic hovered = null;
        for (int i = 0; i < list.size(); i++) {
            int[] r = cell(i, gx, gy);
            r[1] -= (int) scroll;
            Cosmetic c = list.get(i);
            boolean eq = Config.INSTANCE.equipped(category) == c;
            boolean hov = mx >= r[0] && mx < r[0] + r[2] && my >= Math.max(r[1], gy) && my < Math.min(r[1] + r[3], gy + gh)
                && mx < gx + gw;
            g.fill(r[0], r[1], r[0] + r[2], r[1] + r[3], hov ? 0xFF2A3541 : 0xFF16212C);
            g.drawBorder(r[0], r[1], r[2], r[3], eq ? accent() : 0xFF3A4654);
            g.fill(r[0] + 22, r[1] + 10, r[0] + r[2] - 22, r[1] + r[3] - 10, c.color());
            if (hov) hovered = c;
        }
        g.disableScissor();
        if (hovered != null)
            g.drawTooltip(textRenderer, Text.literal(hovered.name() + " · " + hovered.category().label), mx, my);
    }

    private int[] cell(int i, int gx, int gy) {
        int cw = (W - 105 - 16) / 3, ch = 56;
        return new int[]{gx + (i % 3) * (cw + 8), gy + (i / 3) * (ch + 8), cw, ch};
    }

    private int[] catBtn(Category c) {
        return new int[]{left + W - 88, top + 58 + c.ordinal() * 24, 78, 18};
    }

    // ---------- wejscie ----------
    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (super.mouseClicked(mx, my, button)) return true;

        int tw = (W - 20) / 4;
        for (int i = 0; i < 4; i++) {
            int x = left + 8 + i * tw;
            if (in(mx, my, x, top + 8, tw - 4, 16)) { tab = i; scroll = 0; return true; }
        }
        switch (tab) {
            case 0 -> {
                for (int i = 0; i < Theme.ALL.length; i++) {
                    int[] r = paletteCell(i);
                    if (in(mx, my, r[0], r[1], r[2], r[3])) { Config.INSTANCE.theme = i; Config.INSTANCE.save(); return true; }
                }
            }
            case 2 -> {
                for (Category c : Category.values()) {
                    int[] r = catBtn(c);
                    if (in(mx, my, r[0], r[1], r[2], r[3])) { category = c; scroll = 0; return true; }
                }
                List<Cosmetic> list = CosmeticRegistry.of(category, search.getText());
                int gx = left + 12, gy = top + 58, gw = W - 105, gh = H - 68;
                if (in(mx, my, gx, gy, gw, gh))
                    for (int i = 0; i < list.size(); i++) {
                        int[] r = cell(i, gx, gy);
                        if (in(mx, my, r[0], r[1] - (int) scroll, r[2], r[3])) { Config.INSTANCE.toggle(list.get(i)); return true; }
                    }
            }
            case 3 -> {
                int x = left + 20, w = W - 60, y = top + 70;
                if (in(mx, my, x - 4, y - 6, w + 8, 24)) { draggingSlider = true; setScale(mx, x, w); return true; }
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mx, double my, int b, double dx, double dy) {
        if (draggingSlider) { setScale(mx, left + 20, W - 60); return true; }
        return super.mouseDragged(mx, my, b, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int b) {
        if (draggingSlider) { draggingSlider = false; Config.INSTANCE.save(); }
        return super.mouseReleased(mx, my, b);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double hAmount, double vAmount) {
        if (tab == 2) {
            int rows = (CosmeticRegistry.of(category, search.getText()).size() + 2) / 3;
            double max = Math.max(0, rows * 64 - (H - 68));
            scroll = Math.max(0, Math.min(max, scroll - vAmount * 24));
            return true;
        }
        return super.mouseScrolled(mx, my, hAmount, vAmount);
    }

    private void setScale(double mx, int x, int w) {
        double t = Math.max(0, Math.min(1, (mx - x) / w));
        Config.INSTANCE.scale = (float) (0.5 + t * 1.5);
    }

    private static boolean in(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    @Override public boolean shouldPause() { return false; }
}
