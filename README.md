# Super Power Mod (Fabric 1.21.1)

Skill:
- R = Fire Burst  (ledakan api melingkar, particle custom)
- Z = Energy Beam (laser lurus, model 3D GeckoLib)

## Download jar
Tab **Releases** -> **Latest build** -> download file `.jar`.
Jar otomatis di-build ulang setiap ada perubahan di repo ini.

Taruh di folder `mods` bareng: Fabric Loader, Fabric API, GeckoLib (Minecraft 1.21.1, Java 21).

## Edit
- Model:   src/main/resources/assets/supermod/geo/entity/energy_beam.geo.json (Blockbench)
- Tekstur: assets/supermod/textures/...
- Balance: src/main/java/com/supermod/ability/Ability.java (cost & cooldown)
- Skill baru: tambah di enum `Ability`, `AbilityManager.cast`, dan keybind di `SuperModClient`.
