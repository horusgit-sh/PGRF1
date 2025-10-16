
# Interaktivní kreslení úseček a polygonů

Tento projekt je úloha 1 z předmětu **PGRF1**. 

## Funkce aplikace
- Interaktivní zadávání bodů myší
- Kreslení úseček (Bresenhamův algoritmus)
- Režim **SHIFT** – zarovnání na vodorovnou, svislou nebo úhlopříčnou úsečku
- Barevný přechod (gradient) mezi dvěma koncovými body
- Uzavření polygonu klávesou **SPACE**
- Vymazání plátna a dat klávesou **C**

## Struktura projektu
- `rasterize/` – práce s rastrem a rasterizace úseček
- `polygon/` – ukládání a vykreslování polygonů
- `controller/` – uživatelská interakce (myš a klávesnice)
- `view/` – vykreslovací panel a okno aplikace

## Spuštění
Projekt lze spustit v prostředí **Java 17+**:
1. Zkompilujte projekt
2. Spusťte třídu `Main`
