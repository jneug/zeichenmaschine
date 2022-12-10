# Changelog
Alle wichtigen Änderungen an diesem Projekt werden in dieser Datei dokumentiert.

Das Format basiert auf [Keep a Changelog](https://keepachangelog.com/de/1.0.0/)
und diese Projekt folgt [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## Version 0.0.34

### Added
- `Faker`-Klasse zur Erzeugung von Fake-Daten hinzugefügt.
- Dokumentation unter [zeichenmaschine.xyz](https://zeichenmaschine.xyz) mit 
  [MkDocs](https://www.mkdocs.org) und [Material for MkDocs](https://squidfunk.github.io/mkdocs-material/).
- Neue `image` methoden im `DrawingLayer`.

### Changed
- `FilledShape` und `StrokedShape` durch `Fillable` und `Strokeable` Interfaces ersetzt.
	- `Shape` erweitert nun `BasisDrawable` als abstrakte Grundlage.
- `io` Klassen nutzen nun mehr der `java.nio` Funktionen.
- Package-Struktur angepasst.

## Version 0.0.23

### Added
- System für EventListener.
  - `AudioListener` und `AnimationListener` als erste Anwendungsfälle.
- Pakete für Animationen und Maschinelles-Lernen.
- Farbverläufe als Füllung.

### Changed
- `update(double)` und `draw()` werden nun in einem eigenen Thread aufgerufen. 
- Die Standardwerte in `Constants` wurden mit dem Prefix `DEFAULT_` benannt (vorher `STD_`).
- Die Standardwerte sind nun nicht mehr `final` und können vom Nutzer manuell gesetzt werden.

## Version 0.0.22

### Added
- Interface `Audio` extrahiert, mit Basisfunktionen von `Sound` und `Music`.
- Klasse `Mixer` steuert mehrere Audio-Objekte gleichzeitig. 
- Klasse `tasks.RateLimitedTask`, `tasks.FramerateLimitedTask`, `tasks.FrameSynchronizedTask` und `tasks.DelayedTask`. 

### Changed
- Neue Package-Struktur:
  - `schule.ngb.zm.media` für Audio-Klassen (und ggf. zukünftig Video).
  - `schule.ngb.zm.util.tasks` für alles Rund um Parallelität.
- `Zeichenthread` und `TaskRunner` setzen die Namen der Threads für besseres Debugging.

### Removed
- Beispielprojekte in [eigenes Repository](https://github.com/jneug/zeichenmaschine-examples) verschoben.

## Version 0.0.21

### Added
- Parameter `stop_after_draw` im Konstruktor der `Zeichenmaschine` erlaubt es beim Erstellen festzulegen, ob nach dem ersten Frame die Zeichenmaschine gestoppt wird.
- `Picture.tint(Color)` färbt ein Bild ein.
- `Picture.flip(Options.Direction)` spiegelt ein Bild entlang einer Achse (`LEFT`/`RIGHT` für horizontal, `UP`/`DOWN` für vertikal).
- Abstrakte Klasse `Zeichenobjekt` als einheitliche Oberklasse für Objekte in Projekten. Die Klasse erbt von `Constants` und implementiert `Drawabale` und `Updatable` mit leeren Methoden.
- Klasse `java.util.Validator` übernimmt intern Parametervalidierung.
- Klasse `Log` implementiert eine einfache Logging-API über `java.util.logging`.
- Klasse `TaskRunner` führt parallele Prozesse aus.
- `Zeichenmaschine#scheduleTask(Runnable, int)` führt eine Aufgabe nach einer Wartezeit im Gameloop aus.
- Neue Klasse `util.ResourceStreamProvider` sucht Resourcen und öffnet `InputStream`s.

### Changed
- Objektvariablen der `Zeichenmaschine`, die von Unterklassen genutzt werden sollen, sind nun statisch in `Constants`. Dadurch können auch andere Klasse, die von `Constants` erben ohne Umwege auf diese Werte zugreifen (z.B. `width`/`height` der Zeichenleinwand).
- `ImageLoader` und `FontLoader` wurden überarbeitet.
  - Nutzung von `Log`
  - Nutzung von `ResourceStreamProvider`
- Verarbeitung von Swing `InputEvent`s in einer eigenen interne EventQueue synchron zur Framerate.
