# Desafio para a empresa Meetime

## Sobre
Foi desenvolvida a aplicacao como proposta, todos os endpoints estão cobertos por testes e o serviço principal
também foi. 

As tecnologias utilizadas foram o pacote Spring (Mvc, Data) com bando da dados em memória H2.

## Instruções

Para compilar o programa basta executar o maven:
`mvn clean install`

Para subir o projeto
`java -jar target/desafio.jar`

### Opnião sobre a solução:
Fiz a solução toda e no final do timing fui para a integração o que acabou levando um pouco mais 
de tempo e acabou que não consegui caprichar tanto no código. Mas a linha de seguimento seria a mesma desenvolvida em
todo o resto da aplicação. 

A ideia seria separar os 'SAVES' no Pipedrive em cada classe separada e utilizar Objetos próprios para o 
retorno como DTO's e não o Map feínho que acabei utilizando. rsrs
