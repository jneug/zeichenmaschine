# Changelog
Alle wichtigen Änderungen an diesem Projekt werden in dieser Datei dokumentiert.

Das Format basiert auf [Keep a Changelog](https://keepachangelog.com/de/1.0.0/)
und diese Projekt folgt [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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