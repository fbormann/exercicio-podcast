# Descrição
A rede só é consumida em *dois momentos*: 

*  Quando o app inicializa
*  Quando um episódio é baixado


No primeiro o consumo de rede é mínimo dado que somente informações mínimas são baixadas, como o nome, data de publicação, etc..

![NETWORK_COMECO_DA_APLICACAO](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/NETWORK_COMECO.png)

No segundo momento:

ele é relativamente alto, quase sempre 0.7MB/s e os dados enviados são 0,01MB/s, sendo os pacotes acks enviados pela própria rede para validar que recebeu os dados enviados pelo servidor dos podcasts.

Em nenhum outro momento observei consumo de rede.

![NETWORK_DOWNLOAD_EPISODIO](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/network_download_episodio.png)


Outra coisa que me chamou a atenção, após uma análise mais profunda através do Android Profiler foi que mesmo após a atividade na rede acabar próximo aos 2m de aplicação rodando, o app manteve as conexões abertas até 6,8m. O que são 4 minutos de inatividade na rede e conexões abertas.

![NETWORK_LEAK](https://github.com/fbormann/exercicio-podcast/blob/master/Podcast/report_images/NETWORK_LEAK.png)

## Boas práticas

Guardar os dados em um DB para reutilização e a checagem de se eles forem baixados, provavelmente garantiriam um menor uso da rede ao inicializar o app uma segunda vez.


