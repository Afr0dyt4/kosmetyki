package pl.kosmetyki;

/** Motywy z zakladki "Paleta". */
public record Theme(String name, int accent) {
    public static final Theme[] ALL = {
        new Theme("Lód", 0xFF38D0F0),      new Theme("Zieleń", 0xFF5CE08A),
        new Theme("Fiolet", 0xFFB890F8),   new Theme("Róż", 0xFFF470C0),
        new Theme("Czerwień", 0xFFF06470), new Theme("Złoto", 0xFFF8D060),
        new Theme("Turkus", 0xFF40E0D0),   new Theme("Pomarańcz", 0xFFF89850)
    };
}
