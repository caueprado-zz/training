## Java 11:
[JEP 323]

readString() 
writeString()


## Update Java 14:

Pattern Matching for instanceof (Preview) [JEP 305]

Helpful NullPointerExceptions [JEP 358]

Records (Preview) [JEP 359]

Record é uma classe abstrata e todas as classes que estenderem Record, devem possuir obrigatoriamente os seguintes membros:

- um atributo final e private para cada componente declarado, com o mesmo nome e mesmo tipo;

- um construtor público, cuja lista de argumentos é idêntica - ordem, nomes e tipos - a lista declarada como componente no Record. A implementação inicializa cada atributo do Record;

- um método de acesso público com o mesmo nome declarado e retorna o mesmo tipo para cada componente.

- um método hashCode implementado usando a combinação do valor de código hash de todos os componentes;

- um método toString que retorna o nome da classe, o nome e o valor de cada componente.

- um método equals que obedece um contrato especial, onde:

R copy = new R(r.c1(), r.c2(), ..., r.cn());

isso significa que r.equals(copy) será verdadeiro;

Packaging Tool (Incubator) [JEP 343]

- https://openjdk.java.net/jeps/343

- https://www.baeldung.com/java14-jpackage

Switch Expressions [JEP 361]

```java
private String enhancedSwitchCase(DAY_OF_WEEK dayOfWeek) {
    return switch (dayOfWeek) {
        case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
        case SATURDAY, SUNDAY -> "Weekend";
    };
}
```

Java Flight Record

https://docs.oracle.com/javacomponents/jmc-5-4/jfr-runtime-guide/run.htm#JFRUH164

java -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=myrecording.jfr MyApp
