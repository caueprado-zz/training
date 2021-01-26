Serviço responsável por cadastrar pautas e associados. E realizar sessões de votos

* Stack

* Java 11
* Mongo
* Spring boot
* RabbitMq

Java é minha linguagem de domínio portanto foi selecionada para o desenvolvimento junto com Spring boot
A utilização do mongo por se tratar de requisições que não existem uma estrutura complexa
de um banco relacional.

Por sua facilidade de implantação e leveza para o ambiente de desenvolvimento local, o serviço
de mensageria escolhido foi o RabbitMQ.

Associado - Person
Uma pessoa com documento

Schedule - Pauta
Uma pauta tem uma sessão de votação

Sessão - Session
Possuí votos de associados

Foi construido um front end simples para a realização de testes e visualização do funcionamento do serviço

o serviço ficou com 74% de cobertura de testes pois algumas classes não haviam necessidades de serem testadas
pois os testes de integração realizavam a maior parte da validação. Evitando assim um Tautological TDD

O Rest Assured não foi utilizado, usando a chamada direta ao controller da aplicação nesse caso
um exemplo com uso feito por mim, pode ser visualizado nesse projeto:
https://github.com/mcorreiab/teste-desenvolvedor-java/pull/1

Versionamento da API

