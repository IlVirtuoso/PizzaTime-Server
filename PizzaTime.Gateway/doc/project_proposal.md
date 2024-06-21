# Pizza Time!

## idea

realizzare un app di delivery per la pizza, gli utenti ordinano una pizza di una determinata dimensione e con degli ingredienti. La pizzeria che può accogliere l'ordine (ha gli ingredienti riportati da qualche parte) e che si trova nelle condizioni di servire il cliente (scheduling per esempio considerando il numero totale degli ordini effettuati, quanto è lontano dalla casa del cliente) prepara la pizza e lo spedisce con il nostro servizio di trasporti (formato da rider e corrieri assunti , noi siamo per un salario equo). 

## cliente

Requisiti:
* il cliente deve registrarsi tramite Sito/App o usando google e account affini
* il cliente deve fornire una carta di credito, un account paypal o satispay

il cliente può :

* Ordinare una o più pizze scegliendo dimensione , ingredienti, impasto e altre robe
* Ordinare una o più pizze da un menù precompilato di una pizzeria
* Salvare il suo ordine in una lista privata o pubblica
* Indire un pizza party e aspettare che altri utenti si colleghino 
* Collegarsi a un pizza party
* Votare una pizza pubblicata (dagli ordini salvati da altri utenti che hanno deciso di pubblicare la pizza)

Il pizza party è un pool di persone che si riuniscono in una determinata location e possono ordinare ognuno con il proprio account, alla fine dell'ordine mandato da tutti quanti il direttore del pizza party manda l'ordine totale in modo da considerarlo come cliente unico (per motivi d i efficienza nello scheduling). Ma comunque permette di tenere le spese separate



## pizzeria 

Requisiti:
* la pizzeria si registra con account business fornendo partita iva e cose aziendali per l'identificazione
* la pizzeria fornisce un conto su cui versare il denaro del cliente che ha acquistato la pizza

la pizzeria può:
* Dichiararsi aperta o chiusa agli ordini in un determinato istante
* Fornire la lista degli ingredienti a sua disposizione o collegare il gestore con il loro magazzino
* Prendere in carico un ordine o rifiutarlo, l'app quindi lo rischedula a una pizzeria che probabilmente può gestire l'ordine
* Fornire un menu precompilato con le loro pizze
* Fornire un menu con il costo delle personalizzazioni che possono essere richieste

## riders e corrieri

Requisiti:
* i corrieri e i rider possono fare richiesta di registrazione tramite l'apposito portale, una volta accetata gli spedisci il porta pizze
* i corrieri e i rider registrati hanno un app che permette loro di prendere in carico gli ordini vicini e eseguirli

Il rider può:
* Accettare o rifiutare un incarico
* visualizzare la distanza dell'acquirente e il tempo stimato al raggiungimento con il suo mezzo (maps)
* Rendersi disponibile o meno ad accettare incarichi in un determinato istante

