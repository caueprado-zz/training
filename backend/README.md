Serviço responsável por cadastrar pautas e associados. E realizar sessões de votos pelos associados que obitiveram seu documento aprovado para votar.

## Stack

* Java 14
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

##Para Rodar

subir o ambiente docker localizado na pasta docker

Utilizar o profile dev

```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Considerações

Foi construido um front end simples para a realização de testes e visualização do funcionamento do serviço

o serviço ficou com 74% de cobertura de testes pois algumas classes não haviam necessidades de serem testadas
pois os testes de integração realizavam a maior parte da validação. Evitando assim um Tautological TDD

O Rest Assured não foi utilizado, usando a chamada direta ao controller da aplicação nesse caso
um exemplo com uso feito por mim, pode ser visualizado nesse projeto:
https://github.com/mcorreiab/teste-desenvolvedor-java/pull/1

O serviço é simples e escalável estratégias dentro de containers como Kubernetes permitem que possamos isolar serviços e workloads.
Para requisitos de performance poderia ter utilizado uma API gateway do front para o backend distribuindo as requisições entre uma ou mais instancias 
da aplicação, porém para evitar over engineering não foi implementado.

## Versionamento da API

O versionamento de api é um recurso importante durante o desenvolvimento de APIs, uma vez que dentro de um ambiente ágil estamos lidando a todo 
momento com mudanças, um aumento no número de integrações, para manter uma compatibilidade e o bom funcionamento é importante termos uma estratégia 
funcional de versionamento de nossas APIs.

Estratégias comuns para realizar tal requisito, poderiam ser adicionar um header contendo a versão da aplicação a qual deseja utilizar, ou então
utilizar como parte da URL da api.

Para esse projeto nós incorporamos o prefixo v1 ao path da api, sinalizando que é a nossa primeira versão de API. Caso precisassemos criar uma nova versão
para cumprir com novos requisitos, poderiamos manter essa api v1 e criar uma api com o path v2/, a exemplo :

V1 - O qual estará pronto para atender a quem já utiliza e não vê a necessidade imediata de atualizar a sua integração
```
v1/schedules
```


V2 - para novos clientes que adirão a nova regra de negócios e para clientes que irão atualizar para esse novo modelo de requisição

```
v2/schedules
```

Essa foi a estratégia escolhida pelo sua baixa complexidade e facilmente ser realizada.

poderíamos aperfeiçoar o design da api da seguinte maneira:


```
v1/schedules
```

Como a sessão está relacionada a uma schedule

```
v1/schedules/{schedule_id}/sessions/{session_id}
```