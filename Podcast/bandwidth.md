# Descrição
A rede só é consumida em *dois momentos*: 

*  Quando o app inicializa
*  Quando um episódio é baixado


No primeiro o consumo de rede é mínimo dado que somente informações mínimas são baixadas, como o nome, data de publicação, etc..

No segundo momento:

ele é relativamente alto, quase sempre 0.7MB/s e os dados enviados são 0,01MB/s, sendo os pacotes acks enviados pela própria rede para validar que recebeu os dados enviados pelo servidor dos podcasts.

Em nenhum outro momento observei consumo de rede.


# Boas práticas



