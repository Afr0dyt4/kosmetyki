package pl.kosmetyki;

public enum Category {
    RECE("Ręce"), SKRZYDLA("Skrzydła"), PELERYNY("Peleryny"), HELMY("Hełmy"),
    AURY("Aury"), PETY("Pety"), BUTY("Buty");

    public final String label;
    Category(String label) { this.label = label; }
}
