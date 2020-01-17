# A

[x] Beschränkung der Empfangsrate 1024kbit/s

[ ] Verzögerung

[ ] Paketfehler

Die Zeit bis Empfangsrate stabil ist: 2 sekunden

# B

[x] beschränkung der empfangsrate 1024kbit/s

[x] verzögerung 500ms

[ ] paketfehler

Beobachtungen:

* ping brauchen 500 ms länger
* DNS resolve dauert länger
* TCP connect dauert länger
* HTTP response braucht länger
* Empfangsrate ist erst nach ca 10 sekunden stabil

# C


[x] beschränkung der empfangsrate 1024 kbit/s

[ ] verzögerung 500ms

[x] paketfehler

Übertragungsrate in kB/s

Messung | 2% | 5% | 10%
--------|----|----|-----------
1|110|102|90
2|109|101|67
3|103|103|84
4|107|103|68
5|106|101|80

Beobachtung:
Der Verbindungsaufbau dauert bei 5% und 10% länger und je höher die Paketfehler sind, desto häufiger und länger wird die Übertragungsrate kleiner. Sie fällt meist auf 80 - 90 kB/s ab. Bei 10% auch mal auf nur 50 kB/s

Erklärung:
Nach einem Paketfehler geht TCP mit der Senderate runter, da angenommen wird, dass die Fehler durch zu viele Pakete entstehen. Es wird danach wieder versucht die Senderate zu steigern, wenn dann aber immer noch Fehler auftreten geht TCP weiter runter bis die Anzahl der Fehler stabil bleibt und sich eine geringere Rate einstellt.

# D

[x] beschränkung der empfangsrate 128 kbit/s

[x] verzögerung 200ms

[ ] Paketfehler

Beobachtung
Der Verbindungsaufbau dauert wieder etwas länger, ähnlich zu B. Auch die empfangsrate ist langsamer (ca 12kB/s)

# E

[x] beschränkung der empfangsrate 1024 kbit/s

[ ] verzögerung 0ms

[x] Paketfehler im output 10%

Datenrate: 115KB/s

Der Aufbau der Verbindung dauert länger. Speziell das DNS resolving.

Aber die Datenrate während der Verbindung wird nicht beeinflusst. Dies erklärt sich dadurch, dass TCP ein pipelining verwendet.

# F

Um ein Verlustszenario sehr schnell aufzeichnen zu können, wurde die loss rate auf 50% gesetzt.

Im folgendenden wird bezug auf den Screenshot `tcp_50_percent_loss.png` genommen.

Client: 10.181.110.148
Server: 129.187.208.9

Packet | Sender | Empfänger | Typ  | Erläuterung
-------|--------|-----------|------|-------------
202    | Client | Server    | SYNC | Zum Verbindungsaufbau
203    | Server | Client    | SYNC | ACK des Verbindungsaufbaus
670    | Client | Server    | GET  | HTTP GET Request
671    | Server | Client    | ACK  | ACK für 670
672    | Server | Client    | PSH ACK | Push: Daten müssen an die nächste schicht weiter gegeben werden
673    | Server | Client    | ACK  | zweites Paket, deutlich größer als 672
709    | Server | Client    | RTM  | Retransmission von 672
761    | Server | Client    | RTM  | Retransmission von 672
762    | Client | Server    | ACK  | Ack zu 672
763    | Server | Client    | RTM  | Retransmission von 673
