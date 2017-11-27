A primeira parte foi remodelar as classes, fazia mais sentido utilizar o nome Podcast dado que os dados baixados eram somente áudios de Podcast.

# Setup Inicial

Baseado em aplicações fornecidas pelo google no repositório https://github.com/googlesamples/android-architecture-components/tree/master/BasicSample eu separei as classes dentro do package "db" entre os packages menores:

*  "dao" onde está localizado o PodcastDao, responsável pelas operações com o DB, fornecendo entidades e persistindo as mudanças para o DB.
*  "entities" onde está localizado a classe Podcast, responsável por representar a tabela de podcast no DB
*  "viewmodels" onde está localizada a classe ListPodcastViewModel, responsável pelo binding com a MainActivity, onde são listados os Podcasts

Além disto, removi as classes auxiliares *PodcastDBHelper*, *PodcastProvider*, *PodcastProviderContract* que serão substítuidas pelas classes do Room.

