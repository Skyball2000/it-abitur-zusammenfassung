# IT Abitur-Zusammenfassung

Eine Website, auf der nützliches fürs IT Abitur in Baden Württemberg gesammelt wird!  
Eine [Live-Demo](http://yanwittmann.de/schule/site/) kann hier gefunden werden.

![res/site/img/itabiicon.png](res/site/img/itabiicon.png)


## Wie man eine Seite verfasst

Zuerst im `site/pages/` directory eine neue Textdatei (bzw. einen neuen Ordner mit einer Textdatei) erstellen.  
Die Textdatei mit dem Inhalt der Seite befüllen. Die Syntax:

- `# Main Title` - Setzt die Hauptüberschrift der Seite
- `# Main Title!!` - Setzt die Hauptüberschrift der Seite mit einer Warnung, dass die Seite
  eventuell Fehler enthalten oder ungenau sein könnte 
- `# Main Title!` - Setzt die Hauptüberschrift der Seite mit einer Warnung, dass die Seite
- `# Main Title!Nachricht` - Warnung mit einer Nachricht
- `> Keyword, Katze` - Setzt Keywords für die Seite, sodass man in der Suche auf der Hauptseite danach suchen kann
- `> hidden` - Versteckt eine Seite von der Hauptübersicht (nicht von der Suche)
- `## Text Title` - Erstellt eine Überschrift im Text
- `img https://picsum.photos/450/250` - Fügt ein Bild ein
- `Simple Text` - Fügt einen Text hinzu
- `[[href=http://yanwittmann.de|Text with link]]` - Ein klickbarer Link
- `[Marker]` - Platziert einen Anker an der Stelle mit dem gegebenen Text
- `[Klick mich!|Assembler]` - Ein klickbarer Link zur Seite `Assembler` mit dem Text `Klick mich!`
- `[Klick mich!|Assembler|Marker]` - Ein klickbarer Link zur Seite `Assembler` der direkt zum Anker
  `Marker` springt mit dem Text `Klick mich!`
- `<b>, <i>, ...` - HTML Formatierungszeichen
- ``` `code` ``` - Code, also mit backticks umschließen
- `- Text` für eine ungeordnete Liste (mehrere `-` für Einrückungen)
- `~ Text` für eine geordnete Liste (mehrere `~` für Einrückungen)
- `$code$` - Spoiler
- `$$code$$` - Spoiler mit schwarzem Hintergrund bei hover
- `$$ Frage 1` - Text in einer kleinen Box
- `$$ Frage 2:Was ist 4 + 6?` - Text in einer kleinen Box mit Titel
- `$$ Frage 3:Was ist 7 - 3?->Lösung` - Text in einer kleinen Box mit Titel und spoiler box reveal (Ende des Spoilers mit `$$$` markieren)
- `$$ Was ist 7 - 3?->Lösung` - Text in einer kleinen Box und spoiler box reveal (Ende des Spoilers mit `$$$` markieren)
- `$$$ Lösungen`  
  `Dies ist eine Lösung`  
  `$$$` - Spoiler-box mit reveal/hide button
- `$$$$`  
  `Text in einem Kasten`  
  `$$$$` - Box mit Text
- `<table class="tg">` - Tabellen (muss Klasse `tg` sein, ansonsten eine normale HTML Tabelle mit `<tr>` und `<td>`)
- `<center>` - Zentriert Elemente auf der Seite


## Neues

- Man kann jetzt mehrere Leertasten hintereinander verwenden
- Anker-Links zu Anschnitten oder Text-Snippets einer anderen Seite
- Keywords für eine Seite, nach denen in der Suche gesucht werden kann
- Man kann jetzt `<<` und `>>` eingeben, ohne, dass es die gesamte Formatierung der Seite ruiniert (was immer gut ist!)
- Code snippets können nun leer sein und eine Leertaste am Anfang enthalten
- Mit dem Keyword `hidden` können Seiten nun von der Hauptübersicht versteckt werden
- Es können nun Nachrichten mit den Warnungen angezeigt werden: `!!Nachricht`
- Eine Überschrift mit `##` erzeugt nun einen Zeilenabstand zum vorherigen Absatz
- Neues Icon!
- `[]^<>` können nun mit einem \ escaped werden: `\[ \] \^ \< \>`
- Bilder, die direkt nach einem anderen Bild oder einer Liste kommen, haben jetzt einen kleineren Abstand zu diesen
- Die Suchleiste zeigt jetzt zufällige Suchbegriffe basierend auf den vorhandenen Seiten an
- Die Suchleiste wählt nun alle 10 Sekunden einen neuen Suchbegriff
- Haupt/Startseiten-Texte endlich geschrieben
- Seiten mit Zahlen >= 10 wurden alphabetisch sortiert und daher direkt nach der Seite 1 platziert. Diese werden nun richtig sortiert.
- Elemente können nun auf der Seite zentriert werden: `<center> ... </center>`
- Tabellen haben jetzt nicht mehr einen so großen Abstand oben zum Text
- Spoiler in text & Spoiler boxen
- Fragen mit spoiler Antworten
- Out dir zur `.gitignore` hinzugefügt
- Multiline code (+ warnings, wenn diese nicht verwendet werden)

## Todo

- Luca Bild für Hauptseite unten
- Mehr Beispiele
- Aufgaben zu Themen (Lösungen mit spoiler-boxen)
- Seiten fertig importieren
- Seiten überarbeiten
- Keywords für Seiten erstellen