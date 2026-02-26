# 3D zobrazení scény (PGRF1)

## Ovládání

### Kamera
- **W/S/A/D** - pohyb kamery (dopředu/dozadu/doleva/doprava)
- **Myš (tažení levým tlačítkem)** - rozhlížení (změna azimut/zenith)
- **R** - reset kamery do výchozí pozice

### Projekce a zobrazení
- **P** - přepínání perspektivní/ortogonální projekce
- **M** - přepínání režimu zobrazení (filled+wireframe / filled / wireframe)

### Manipulace s objekty
- **Tab** - výběr dalšího aktivního objektu (přeskakuje osy)
- **Šipky (←↑↓→)** - posun aktivního objektu v ose X a Z
- **PageUp/PageDown** - posun aktivního objektu v ose Y
- **Q/E** - rotace kolem osy Y
- **Z/X** - rotace kolem osy X
- **C/V** - rotace kolem osy Z
- **+/-** - zvětšení/zmenšení aktivního objektu
- **T** - zapnutí/vypnutí textury na aktivním objektu

---

# 3D zobrazení scény (PGRF1)

Java aplikace pro vykreslování jednoduché 3D drátové grafické scény.
Projekt je zaměřen na implementaci zobrazovacího řetězce, 3D transformací,
kamerového systému, projekcí a parametrických křivek a ploch.

Aplikace vychází ze struktury úloh modulu PGRF1 a rozšiřuje předchozí 2D řešení
na plnohodnotné 3D zobrazení.


## Funkce aplikace

### 3D scéna
Scéna obsahuje několik 3D objektů s trojúhelníkovými plochami a wireframe reprezentací:
- **Krychle** (Cube) - protíná se s hranolem (demonstrace Z-buffer)
- **Osmiúhelníkový hranol** (Hranol) - protíná se s krychlí
- **Jehlan** (Pyramid)
- **Sféra** - reprezentuje zdroj světla (žlutá), lze vybrat a posouvat
- **Osy souřadnicového systému** (X-červená, Y-zelená, Z-modrá) - s trojúhelníkovými šipkami

### Vertex a Index Buffer
- Každé těleso obsahuje vertex buffer (seznam vrcholů) a dva index buffery:
  - **Line indices** - pro drátový model (wireframe)
  - **Triangle indices** - pro vyplněné plochy
- Dodatečné buffery: **normals** (osvětlení), **UV** (texturování)

### Transformace
- **Modelovací transformace** - posun, rotace (kolem všech os), změna měřítka
- **Pohledová transformace** - kamera ovládaná klávesnicí (WASD) a myší
- **Projekce** - perspektivní a ortogonální (přepínání klávesou P)

### Ořezání (Clipping)
- **Rychlé ořezání** podle zobrazovacího objemu (near plane test `w < 0.1`)
- **Ořezání x,y** při rasterizaci (bounding box v screen space)

### Z-Buffer algoritmus viditelnosti
- Plná implementace Z-buffer algoritmu pro správné řešení viditelnosti
- **Demonstrace**: krychle a hranol se vzájemně protínají, Z-buffer správně zobrazuje nejbližší fragmenty

### Režimy zobrazení
- **Wireframe** - pouze hrany
- **Filled** - vyplněné plochy
- **Both** - wireframe + filled současně
- Přepínání klávesou **M**

### Barvy
- Každé těleso má vlastní barvu (MAGENTA, ORANGE, YELLOW)
- Barvy se kombinují s osvětlením (ambient + diffuse)

### Texturování
- Každé těleso má přiřazenou procedurální texturu:
  - Cube - černobílá šachovnice
  - Hranol - červeno-modrý gradient
  - Pyramid - žluto-zelená šachovnice
- Přepínání textury na aktivním objektu klávesou **T**
- UV souřadnice pro každý vrchol
- Nearest-neighbor texture sampling

### Osvětlení (Phongův model)
- **Ambient složka** (0.2) - základní osvětlení
- **Diffuse složka** (0.8) - závisí na úhlu mezi normálou a směrem světla
- Interpolace normál pomocí barycentrických souřadnic
- Zdroj světla reprezentován jako žlutá sféra - lze vybrat (Tab) a posouvat
- Barva difuzní složky odpovídá barvě sféry (žlutá)
- Pozice světla se dynamicky aktualizuje při pohybu sféry

### Souřadnicový systém
- Tři barevně odlišené osy (X-červená, Y-zelená, Z-modrá)
- Každá osa obsahuje:
  - 1 hrana (čára)
  - 1 trojúhelník (šipka na konci)

---

## Technická implementace

### Datové struktury
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