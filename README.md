# Kosmetyki (Fabric 1.21.4)

Klawisz **K** otwiera menu: Paleta | Twórcy | Kosmetyki | Ustawienia.

## Budowanie
1. Zainstaluj JDK 21.
2. W folderze projektu: `gradle wrapper` (jednorazowo), potem `./gradlew build`
3. Gotowy mod: `build/libs/kosmetyki-1.0.0.jar` -> do folderu `mods` (potrzebny Fabric Loader + Fabric API 1.21.4).

## Jak dodać własną kosmetykę
- Dopisz wpis w `CosmeticRegistry.java`.
- Wygląd na graczu rysuje `CosmeticFeatureRenderer.java` (na razie bryły w kolorze) – tu podłącz własne modele/tekstury.
