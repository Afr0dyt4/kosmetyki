package pl.kosmetyki;

/** Jedna kosmetyka. color = kolor ARGB uzywany do podgladu i renderu (do czasu dodania wlasnych modeli/tekstur). */
public record Cosmetic(String id, String name, Category category, int color) {}
