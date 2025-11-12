# Interaktivní kreslení linií a polygonů (PGRF1 – Úloha 2)

Jednoduchá Java aplikace pro kreslení čar a polygonů, vyplňování oblastí a ořezávání tvarů.

## Funkce
- Kreslení úseček myší (algoritmus Bresenham).
- Tvorba polygonu z úseček, uzavření mezerníkem `SPACE`.
- Mazání plátna klávesou `C`.
- Vyplňování oblastí:
  - `B` – vyplnění po hranici (boundary fill)
  - `F` – vyplnění podle pozadí (flood fill)
- Kreslení obdélníku ze 3 bodů (`R`).
- Ořezání polygonů konvexním pětiúhelníkem (`V`).
- Vyplnění výsledného polygonu:
  - běžná barva (zelená)
  - nebo šachovnicový vzor (`P`).

## Ovládání
- Myš – kreslení čar.
- `SPACE` – uzavře aktuální polygon.
- `C` – smaže vše.
- `R` – kreslení obdélníku.
- `B` / `F` – vyplňování oblasti.
- `V` – ořezání polygonů pětiúhelníkem.
- `P` – aktivuje šachovnicové vyplnění.

## Algoritmy
- Bresenham – rasterizace úseček
- Flood/Boundary fill – vyplnění oblastí
- Scan-line – vyplnění polygonů
- Sutherland–Hodgman – ořezání polygonů
