# Android Profiler

Ao utilizar android profiler para analisar a CPU, percebi que haviam picos de memória em alguns cenários:

*  Na mudança de orientação da tela
*  Ao percorrer a lista de objetos
* No carregamento da MainActivity


Na primeira, em alguns casos, o uso de CPU alcançou 48%, o máximo que registrei, que foi no começo da aplicação. E na segunda, o pico foi bem menor, em torno de 13%.

# início da aplicação

Percebe-se um pico no começo da aplicação, como foi falado anteriormente, provavelmente isto se deve à criação da ListView. E de cada view presente na tela sendo carregada pelo ArrayAdapter.

![CPU1](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/cpu_inicio.png)

# início de download de episódio



# Boas práticas

Não consegui identificar boas práticas que diminuiram o uso de CPU no meu app.
