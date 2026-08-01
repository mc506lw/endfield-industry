# AGENTS.md

Minecraft Paper plugin "Endfield Industry" (终末地工业) built on the **Rebar framework** (io.github.pylonmc:rebar, compileOnly). Rebar and Paper API are compileOnly — the jar only runs on a server with Rebar installed.

## Build / dev commands (Windows)

- Build jar only: `.\gradlew.bat shadowJar` → `build/libs/Endfield-Industry-1.0.0.jar`
- **Gotcha**: `.\gradlew.bat build` auto-runs the `copyToServer` task which copies the jar to a hardcoded local server dir (`E:\Minecraft\开发\Pylon开发\plugins` in build.gradle.kts). It fails on machines without that path and always re-runs (`outputs.upToDateWhen { false }`). Use `shadowJar` to skip it.
- Run a dev server: `.\gradlew.bat runServer` (downloads the matching rebar jar from GitHub, MC version from `gradle.properties`).
- No tests, no CI, no lint config — `shadowJar` is the only verification.
- `gradle.properties` pins `org.gradle.java.home=D:\java\zulu25` (local-only path; adjust or remove if Gradle cannot find the JDK). UTF-8 encoding is forced repo-wide; keep it (Chinese strings everywhere).

## Version state

Working tree is mid-upgrade (uncommitted): Paper API / apiVersion **26.1.2**, Rebar **0.42.1-26.1**, Java 25 toolchain (Kotlin jvmTarget still 21). Committed HEAD targets MC 1.21 / Java 21. README describes the old state — trust build.gradle.kts + gradle.properties, not README.

## Architecture

- Entry `EndfieldIndustry.kt` (`EndfieldIndustry` class, `JavaPlugin` + `RebarAddon`). `onEnable` registers modules **in order**: PowerSystem → Items → Blocks → Entities → Fluids → Recipes → RecipeTypes → Pages → CloudStorage (+Gui, Command). New content modules must be added there.
- Top-level module objects (`EndfieldIndustryItems/Blocks/Entities/Fluids/Recipes/RecipeTypes/Pages/Keys`, `content/...` objects) each expose `initialize()`; content classes live in `content/{food,machines,materials,minerals,plants,potions,powersystem,cloudstorage}`.
- Use `EndfieldIndustry.key(name)` for NamespacedKeys; content ids are `endfield-industry:<id>`.
- Every item is research-gated: definition data lives in `src/main/resources/settings/*.yml`, unlocks in `researches.yml`, display text in `lang/en.yml` + `lang/zh_CN.yml` (add both).
- PowerSystem + CloudStorage use H2 (bundled via shadow). PowerGrid data persists via `PowerSystemStorage`; wire paths via `connection/wirepath`.

## Repo quirks

- `参考/` holds full source copies of the pylon and rebar repos as reference material. Untracked — never commit, and use it (or those upstream repos) to look up Rebar API since the dependency is compileOnly.
- Commit messages are in Chinese, conventional format: `feat(模块): 描述`.
- `.gitignore` excludes `rebarapi.md` (generated Rebar API doc) and `.trae/`.
