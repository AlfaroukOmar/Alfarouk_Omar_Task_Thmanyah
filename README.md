# Thmanyah — Production Audio Discovery (sample)
<img width="1080" height="2400" alt="Screenshot_20260511_011736" src="https://github.com/user-attachments/assets/bb241b05-9cb2-40ee-ae99-03a284543491" />
<img width="1080" height="2400" alt="Screenshot_20260511_011750" src="https://github.com/user-attachments/assets/67997994-4a0a-49cf-8708-50fc154b3c39" />
<img width="1080" height="2400" alt="Screenshot_20260511_011745" src="https://github.com/user-attachments/assets/871d6fc4-013b-4bfa-b56a-b40b48f8967a" />
<img width="1080" height="2400" alt="Screenshot_20260511_011803" src="https://github.com/user-attachments/assets/2cb08f7e-8229-4fac-9128-4a40b4400979" />

Arabic-first RTL sample app: **home feed** with custom pagination, **debounced search**, Material 3 + dynamic color, MVVM + Hilt. **Settings** (theme + language) use **DataStore**; **mini-player** and notification UI stubs were removed.

## Architecture

| Layer | Modules |
|--------|---------|
| **App** | `:app` — `Application`, `MainActivity`, `NavHost` (`home` / `search` / `settings`), Hilt |
| **Features** | `:feature:home`, `:feature:search`, `:feature:settings` — ViewModels + Compose routes |
| **Domain** | `:domain` — entities, `DarkModeOption` / `AppLocale`, `HomeRepository` / `SearchRepository`, `AppResult` / `AppError` |
| **Data** | `:data:remote` (Retrofit + DTOs + `SectionMapper`), `:data:repository` (`HomeRepositoryImpl`, `HomePaginator`, search impl) |
| **Core** | `:core:common` (`UiText`, `SettingsRepository`, DataStore, dispatchers, bilingual strings), `:core:network`, `:core:designsystem`, `:core:ui` |

Dependency direction: **features → domain + core**; **data → domain + core**; **domain** has no Android deps.

See **[CONTRIBUTING.md](CONTRIBUTING.md)** for renderer file layout, `:core:fixtures`, previews, and locale rules.

## Home pagination (custom `HomePaginator`)

- Loads page `1` on initial open; appends `page + 1` when scrolling near the end.
- Stops when `page >= total_pages` from the API (`AppendLoadState.EndReached`). **`next_page` from JSON is not trusted** for looping.
- Each page still returns the **same seven section shells**; **`content[]` is merged** across pages: items are **deduped by `ContentItem.id`** within each section (stable section `id` / order).
- **Pull-to-refresh** bumps a `refreshGeneration` so the UI can reset scroll; repository/cache coordinates with the paginator.
- **SWR-style freshness**: in-memory snapshot can be shown while a network refresh runs (see `HomeRepositoryImpl` + `HomeMemoryCache`).
- **Offline / reconnect**: connectivity observer can auto-retry append if the last append ended in error.

## Defensive JSON → domain mapping

`SectionMapper` and `ParseExtensions` aim to avoid crashes on messy payloads:

- Layout aliases (e.g. `big square` vs `big_square`) normalized to `SectionLayout`.
- Unknown layouts → `SectionLayout.Unknown` (renderer fallback).
- Unknown `content_type` → safe defaults / dropped item via `runCatching` per element.
- Durations / dates / ints: **strict** numeric strings only (`toIntSafe` / `toLongSafe` — no digit-stripping from strings like `order3`).
- `ErrorMapper` maps I/O, timeouts, serialization, and HTTP 5xx to `AppError`.

## Dynamic section rendering (“add a new section type”)

1. Add / map a `SectionLayout` value in the domain if needed.
2. Extend `SectionMapper` to produce that layout from the API.
3. In `:core:ui`, implement a `SectionRenderer` composable.
4. Register it in `SectionRendererRegistry` (`register` or update the default map).
5. Optionally add strings / a11y in resources.

Unknown layouts already route to `UnknownLayoutRenderer`.

## Search

- `SearchViewModel`: `debounce(200)` → `distinctUntilChanged()` on `(query, retryEpoch)` → `flatMapLatest` (cancels in-flight search).
- **Retry**: `retry()` increments `retryEpoch` so a **repeat of the same query** still triggers the network call (plain `distinctUntilChanged` on text alone would drop it).
- UI keeps **previous results** during `Loading` / recoverable errors (`SearchUiState.Loading.previous`, `SearchUiState.Error.previous`).

## Category filter (home)

- **For you** plus chips for each **distinct `ContentType`** from loaded home sections (API-shaped). Selection filters which **`HomeSection`** rows render; empty result uses `home_empty_filter`.

## Image cache keys

- `ContentImage` accepts optional **`cacheKey`** — renderers pass **`ContentItem.id`** so Coil memory/disk cache lines up across lists.

## i18n & RTL

- **`ThmanyahApp`** reads persisted locale on cold start; **Settings** can switch Arabic/English via `AppCompatDelegate.setApplicationLocales`.
- Bilingual copy lives under `values` / **`values-ar`** (`:core:common`, `:feature:home`, `:feature:settings`).
- Compose **RTL** follows locale; verify chips and top bar in **Layout direction RTL**.
- **IBM Plex Sans Arabic:** `ThmanyahTypography` in `:core:designsystem` uses embedded **IBM Plex Sans Arabic** (`.otf` under `core/designsystem/src/main/res/font/`; upstream [IBM Plex](https://github.com/IBM/plex), SIL Open Font License). After switching to **Arabic** in Settings, spot-check **greeting, section titles, and body** for correct weight, no clipping on stacked diacritics, and comfortable **line height**; tweak `TextStyle.lineHeight` in `Typography.kt` if a screen looks tight.

## Typography

- Material 3 text styles are centralized in `ThmanyahTypography` and use **`FontFamily` weights** Thin → Bold mapped to the bundled OTFs. **Text** vs **Regular** (both UI-weight “book” in the Plex package): only **Regular** is registered at `FontWeight.Normal` so `FontFamily` resolution stays deterministic.

## Testing

- **Unit tests**: `ErrorMapperTest`, `SectionMapperTest`, `ParseExtensionsTest`, `MergeItemsTest` (dedupe idea), **`SearchViewModelTest`** (Turbine + `StandardTestDispatcher`: debounce, distinct, flatMapLatest, error + retry).
- Note: **`stateIn` exposes `StateFlow`**, which can **conflate** rapid `Loading` → `Success`; tests avoid relying on every intermediate emission.
- **Instrumented smoke**: `HomeSmokeTest` (`:app` androidTest) checks that the home scaffold is shown (`home_route` test tag). Run on a device/emulator: `./gradlew :app:connectedDebugAndroidTest`.
- Commands: `./gradlew test`, `./gradlew :app:assembleDebug`.

## Challenges & solutions

- **Messy or evolving JSON** from `home_sections`: map defensively in `SectionMapper` and related helpers so unknown layouts/types degrade instead of crashing; unit tests lock in edge cases.
- **Pagination vs repeated section shells**: do not trust `next_page` alone for termination; merge `content[]` with **stable dedupe** and stop append using **`total_pages`** from the API.
- **Search cancellation and duplicate queries**: `debounce` + `flatMapLatest` for stale cancellation; a **`retryEpoch`** (or similar) so “same query” retries still hit the network after errors.
- **RTL + brand font**: locale-driven `AppCompatDelegate` plus embedded IBM Plex Arabic keeps LTR/RTL consistent; verify Arabic screens manually (see i18n section).
- **Optional UI instrumentation**: minimal Hilt-aware smoke test proves the default **NavHost** route renders; broader flows remain manual or future work.

## Assignment alignment (Android scope)

| Topic | In repo |
|--------|---------|
| MVVM + Compose + Kotlin | Yes (`ViewModel`s, feature routes). |
| Home feed from API | `HomeApi` `GET home_sections` with `page`. |
| Search + debounce ~200ms | Search feature; `SearchViewModel` debounce + `flatMapLatest`. |
| Defensive parsing / sections | `SectionMapper`, tests, paginator merge/dedupe. |
| Pull-to-refresh | `HomeRoute` `PullToRefreshBox`. |
| Unit tests | Network/mapper/search VM tests (not every line). |
| UI tests | Optional in brief; `HomeSmokeTest` provides a minimal path. |
| IBM Plex Arabic | Bundled OTFs + `ThmanyahTypography`. |
| README / assumptions | This file + challenges section. |

## Assumptions & future work

- API base URLs / keys are wired for dev; replace with build flavors or remote config for production.
- No real **ExoPlayer** / downloads.
- Optional: shared `:core:testing` (MainDispatcherRule, fake APIs), screenshot tests, and broader `HomePaginator` integration tests with fakes.

## Manual smoke checklist

- [ ] Cold start: home loads, skeleton → content.
- [ ] Scroll to bottom: append spinner / end reached at `total_pages`.
- [ ] Airplane mode: error + banner; reconnect retry.
- [ ] Pull-to-refresh: content updates without crash.
- [ ] Search: debounce feels right; retry after failure; RTL layout.
- [ ] Arabic + IBM Plex: greeting and feed text weights/readability; adjust `Typography.kt` line heights if needed.
- [ ] Settings: theme (system/light/dark) and language persist across restarts.
- [ ] Category chips filter sections; **For you** shows all section types.
- [ ] Toggle light/dark + dynamic color on supported devices (when theme = System).

---

*Internal exercise — not affiliated with a production store listing.*
