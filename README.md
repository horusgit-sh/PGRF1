# 3D zobrazení scény (PGRF1)

Java aplikace pro vykreslování jednoduché 3D drátové grafické scény.
Projekt je zaměřen na implementaci zobrazovacího řetězce, 3D transformací,
kamerového systému, projekcí a parametrických křivek a ploch.

Aplikace vychází ze struktury úloh modulu PGRF1 a rozšiřuje předchozí 2D řešení
na plnohodnotné 3D zobrazení.


## Funkce aplikace

### 3D scéna
Scéna obsahuje několik drátových 3D objektů:
- krychle
- víceboký hranol (aproximace válce)
- jehlan
- osy souřadnicového systému (X, Y, Z)

### Křivky a plochy
- kubická Bézierova křivka
- Fergusonova (Hermitova) kubická křivka
- Coonsova kubická křivka
- parametrická plocha – sedlová plocha (hyperbolický paraboloid)

### Transformace a projekce
- modelové transformace objektů (posunutí, rotace, změna měřítka)
- pohledová transformace (kamera)
- perspektivní projekce
- paralelní (ortografická) projekce
- skládání transformací pomocí matic (Model · View · Projection)
- dehomogenizace a převod do souřadnic okna
- vykreslení ve formě drátového modelu (wireframe)

---

## Ovládání
- myš – změna směru pohledu kamery
- W / A / S / D – pohyb kamery (dopředu, dozadu, vlevo, vpravo)
- P – přepnutí mezi perspektivní a paralelní projekcí

---

## Použité principy
- homogenní souřadnice
- maticové 3D transformace
- kamerový model
- perspektivní a ortografická projekce
- parametrické křivky a plochy
- rasterizace úseček pomocí index a vertex bufferů

---