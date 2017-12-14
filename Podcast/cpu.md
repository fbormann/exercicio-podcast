# Android Profiler

Ao utilizar android profiler para analisar a CPU, percebi que haviam picos de memória em alguns cenários:

*  Na mudança de orientação da tela
*  Ao percorrer a lista de objetos
* No carregamento da MainActivity


Na primeira, em alguns casos, o uso de CPU alcançou 48%, o máximo que registrei, que foi no começo da aplicação. E na segunda, o pico foi bem menor, em torno de 13%.

## início da aplicação

Percebe-se um pico no começo da aplicação, como foi falado anteriormente, provavelmente isto se deve à criação da ListView. E de cada view presente na tela sendo carregada pelo ArrayAdapter.

![CPU1](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/cpu_inicio.png)

## início de download de episódio

Uma conexão entre um pico no uso de rede no começo do download e o pico do uso de CPU é interessante, apesar de eles não ocorrerem exatamente ao mesmo tempo pode ser explicado pelo fato de que a CPU só é utilizada para processar os dados baixados pelo Service um pouco após dos dados de fato serem consumidos na rede.

Consumo de CPU:
![CPU_DOWNLOAD_EPISODE](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/cpu_download_inicio.png)

Consumo de Rede (perceba que a atividade começa alguns momentos antes):
![NETWORK_DOWNLOAD_EPISODE](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/network_download_episodio.png)


## Percorrendo lista de objetos

Ao percorrer uma lista de objetos utilizando ListView, sabemos que não é ótima a utilização de Garbage Collector para reutilizar as views já percorridas, o que acarreta em um aumento de CPU, como mostrado abaixo. Seria interessante refatorar o código para utilizar RecyclerView e perceber se haveria melhora, ou seja a diminuição dos níveis de uso de CPU.

![CPU_LIST_VIEW](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/cpu_list_view.png)


# Boas práticas

Não consegui identificar boas práticas que diminuiram o uso de CPU no meu app.
