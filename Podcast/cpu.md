# Android Profiler

Ao utilizar android profiler para analisar a CPU, percebi que haviam picos de memória em dois cenários exclusivamente:

*  Na mudança de orientação da tela
*  Ao percorrer a lista de objetos


Na primeira, em alguns casos, o uso de CPU alcançou 48%, o máximo que registrei. E na segunda, o pico foi bem menor, em torno de 13%.

# Boas práticas

Não consegui identificar boas práticas que diminuiram o uso de CPU no meu app.
