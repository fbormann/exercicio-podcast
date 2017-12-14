# Memória

Utilizando o Android Profiler foi possível observar a utilização de memória em alguns cenários. utilizei o link https://developer.android.com/studio/profile/memory-profiler.html como fonte de estudos para saber o que observar em alguns cenários dos dados de memórias.


## Início do App
No ínicio do app é esperado o pico encontrado dado que também houve um aumento do uso de CPU, é um aumento de memória crescente até que há uma estabilização.

Consumo de memória no ínicio
	![MEMORY_INICIO](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/memory_inicio.png)

Consumo de memória estabilizado:
	![MEMORY_OK](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/memory_estabilizado.png)
## Utilizando ListView
Ao utilizar a ListView, há um pico no uso de memória mas nada muito elevado, dá pra perceber que isto não é um grande problema dado que as views que compõe a ListView são somente com textos.

![MEMORY_LIST](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/memory_list_view.png)

## Tocando Episódio

## Realizando download de episódio
Ao realizar o download de episódio, é incrível perceber o aumento de memória e um padrão de picos, antes de o buffer utilizado no código de download ser esvaziado, por o ciclo de "pico máximo" e "pico mínimo" se repete por alguns momentos.

Começo do download:
	![MEMORY_DOWNLOAD_EPISODE](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/memory_download_episodio.png)

Durante o download (é possível perceber que o GC é chamado algumas vezes durante o download, justamente pela quantidade de dados necessária (o bufferArray) para realizar o download aumentar):
	![MEMORY_DOWNLOAD_EPISODE_PATTERN](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/MEMORY_PATTERN.png)


