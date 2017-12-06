# Setup

*  adicionei testes utilizando Espresso, o primeiro teste adicionado foi para checar se o Settings continha uma textview contendo a fonte dos dados esperada, a classe gerada SettingsValueActivityTest.

*  o próximo teste foi para testar de o ViewModel/objeto de LiveData, infelizmente, não consegui rodar uma thread secundária de forma que o acesso ao DB fosse garantido até agora.