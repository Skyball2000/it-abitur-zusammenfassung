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
- ````` ````Java `````  
  `Java code`  
  ````` ```` ````` - Code blocks mit Sprache des Codes (Sprachlabel um eins nach oben verschieben: `Sprache<br>&nbsp;`)
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
- Progress bar, wie weit der Website-Generierungsprozess ist
- Für multiline code muss nun kein `<br>` mehr am Ende der Zeile platziert werden
- Mehr & bessere warnings
- Code blocks in spoiler boxen werden nun richtig angezeigt
- Code blocks haben nun einen 📋 button neben sich, mit dem man den code block kopieren kann
- Code blocks können nun eine Sprache angeben, indem man die Sprache hinter die Anfangs-Backticks schreibt
- Favicon
- Click counter pro Seite
- Google AdSense (?)
- Click counter wird nun auf yanwittmann.de gehostet (darum müssen die Seiten nun auch `.php` sein)
- Code blocks fügen jetzt automatisch Leerstellen ans Ende der Zeile hinzu, wenn es sich um die bisher
  längste Zeile im code block handelt, damit die Sprache richtig dargestellt werden kann
- Code blocks copy kopiert nun keine automatisch generierten Leerstellen am Ende einer Zeile
- Wenn man nun die Variable `BUILD_SITE_FOR_WEB` auf `true` setzt, wird die Dateiendung nicht mehr an Links
  angehängt, somit ist es egal, welche Dateiendung die aufzurufende Seite hat  
  Zudem: `true` erstellt `php`-Seiten mit Aufruferzähler, `false` `html` ohne Zähler
- Hier hatte ich gerade meinen ersten `BSOD` (mfg Yan)
- utilityClasses updated auf neueste Version
- Code blocks copy kopiert auf Chrome nun die 📋 nicht mehr mit
- Habe mal noch wegen den Werbungen ein paar Sachen getestet und ich bin mir unsicher, was damit jetzt ist.
- Neue Warnings bezüglich Code `` ` ` `` und unescaped `[]`
- BlurNotification wird nun angezeigt, wenn die Site generiert wurde
- Code blocks können nun Leerzeilen enthalten
- Automatisch generierte Keywords enthalten nun keine Keywords mehr, die ein `;` enthalten würden
- Code snippets haben nun abgerundete Ecken
- Code blocks wechseln jetzt pro Zeile ihre Farbe ab
- `-->` wird nun automatisch mit `🠚` ersetzt
- Abstände zwischen Elementen auf der Hauptseite sind nun kleiner
- Seitenleiste hinzugefügt, die nun auf jeder Seite erscheint und einen zu jeder Seite bringen kann
- HTML und CSS auf allen Seiten etwas angepasst und CSS-Dateien umbenannt
- Code blocks nach spoiler boxen werden nun richtig dargestellt
- Die automatisch platzierten `<br>` in code blocks in spoiler boxen werden nun vor dem `<code>` platziert,
  darum werden jetzt keine komischen Zeilenumbrüche mehr angezeigt

## Todo

- Luca Bild für Hauptseite unten
- Mehr Beispiele
- Aufgaben zu Themen (Lösungen mit spoiler-boxen)
- Seiten nochmal überarbeiten
- Keywords für Seiten erstellen
- Kapitel Java/OOP fertig schreiben
- Werbung (AdSense)  
  Site Behavior: Navigation  
  Users should be able to easily navigate through the site or app to find what products, goods, or services are promised  
  Ich hab jetzt meine gesamte yanwittmann.de Domain viel verknüpfter gemacht mit mehr Links die einen hin-
  und herspringen lassen (unter anderem die Seitenleiste mit allen Seiten)