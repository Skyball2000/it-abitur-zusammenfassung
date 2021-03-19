# IT Abitur-Zusammenfassung

Eine Website, auf der nützliches fürs IT Abitur in Baden Württemberg gesammelt wird!  
Eine [Live-Demo](http://yanwittmann.de/schule/site/) kann hier gefunden werden.

## Wie man eine Seite verfasst

Zuerst im `site/pages/` directory eine neue Textdatei (bzw. einen neuen Ordner mit einer Textdatei) erstellen.  
Die Textdatei mit dem Inhalt der Seite befüllen. Die Syntax:

- `# Main Title` - Setzt die Hauptüberschrift der Seite
- `# Main Title!!` - Setzt die Hauptüberschrift der Seite mit einer Warnung, dass die Seite
  eventuell Fehler enthalten oder ungenau sein könnte 
- `# Main Title!` - Setzt die Hauptüberschrift der Seite mit einer Warnung, dass die Seite
  nicht unseren Standards entspricht
- `> Keyword, Katze` - Setzt Keywords für die Seite, sodass man in der Suche auf der Hauptseite danach suchen kann
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
- `<table class="tg">` - Tabellen (muss Klasse `tg` sein, ansonsten eine normale HTML Tabelle mit `<tr>` und `<td>`)

## Neues

- Man kann jetzt mehrere Leertasten hintereinander verwenden
- Anker-Links zu Anschnitten oder Text-Snippets einer anderen Seite
- Keywords für eine Seite, nach denen in der Suche gesucht werden kann
- Man kann jetzt `<<` und `>>` eingeben, ohne, dass es die gesamte Formatierung der Seite ruiniert
  (was immer gut ist!)
- Code snippets können nun leer sein und eine Leertaste am Anfang enthalten
