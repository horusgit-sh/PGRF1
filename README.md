# 3D zobrazení scény (PGRF1)

Java aplikace pro vykreslování 3D drátových modelů, křivek a ploch
s podporou kamery a perspektivní projekce.

Projekt nyní pracuje plně ve 3D prostoru a rozšiřuje předchozí 2D úlohy.

---

## Funkce aplikace

### 3D scéna
Aplikace zobrazuje následující objekty:
- krychle
- víceboký hranol (aproximace válce)
- jehlan
- parametrickou plochu – sedlovou plochu (hyperbolický paraboloid)
- osy souřadnicového systému (X, Y, Z)

### Transformace a zobrazení
- modelové transformace (posunutí, rotace, změna měřítka)
- pohledová transformace (kamera)
- perspektivní projekce
- dehomogenizace a viewportová transformace
- zobrazení ve formě drátového modelu (wireframe)

### Kamera a ovládání
- rotace kamery pomocí myši
- pohyb kamery pomocí kláves W, A, S, D

---

## Ovládání
- myš – změna směru pohledu
- W / A / S / D – pohyb kamery v prostoru
- okno aplikace lze libovolně měnit velikost

---

## Použité principy
- maticové 3D transformace (Model · View · Projection)
- homogenní souřadnice
- dehomogenizace
- perspektivní projekce
- parametrické křivky a plochy
- rasterizace úseček
