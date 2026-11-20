# El Torneo de los Inmortales
# grupo-delta-tp2
Trabajo Práctico N.º 2 de Paradigmas de Programación.

> **Grupo Delta - 2.º cuatrimestre 2026**

## Integrantes

- Pablo Alfredo Battiston
- Elián Ariel Módica
- Gustavo Gabriel Ayala Bustos
- Keila Mariel Almada
- Maria Celeste Torres Moran
- Lucas Leonel Gutierrez

# Consigna

Repositorio para la resolución del Trabajo Práctico N.º 2 de la materia **Paradigmas de Programación** de la Universidad Nacional de La Matanza.

## Fecha de entrega

17 o 20 de noviembre de 2026, según la comisión.

## Objetivo general

Aplicar los conceptos, principios y características de la programación orientada a objetos —clases, objetos, métodos, interfaces, herencia, polimorfismo y colecciones— para resolver un problema real.

El trabajo debe incluir el análisis de la problemática, el diseño de la solución mediante un diagrama de clases y la construcción del software.

## Temática

**El Torneo de los Inmortales: la elección de los Dos Santos Supremos**

## Objetivos del trabajo

- Modelar un sistema de combate por parejas entre cultivadores de distintas escuelas.
- Implementar técnicas y equipamientos que modifiquen las capacidades de los combatientes.
- Gestionar parejas, cruces y rondas de un torneo eliminatorio.
- Utilizar colecciones apropiadas y patrones de diseño.
- Aplicar herencia y polimorfismo para representar distintos tipos de combatientes.
- Permitir que cada escuela prepare a sus combatientes según su filosofía.

## Descripción general

Las parejas de combatientes se enfrentan en rondas eliminatorias hasta que queda una sola pareja. Sus integrantes son proclamados los **Dos Santos Supremos**.

Cada combatiente pertenece a una escuela de cultivación que determina su preparación inicial. El sistema debe ser flexible para incorporar nuevos combatientes, técnicas, escuelas y equipamientos.

## 1. Combatientes

`Combatiente` debe ser una clase abstracta con:

- identificador único e inmutable;
- nombre;
- vida actual y máxima;
- energía actual y máxima;
- equipamiento;
- técnicas disponibles.

Dos combatientes son iguales si tienen el mismo identificador, sin importar su nombre.

### Tipos iniciales

- **Cultivador de la Espada:** potencia el daño provocado.
- **Monje del Cuerpo Celestial:** reduce el daño recibido.
- **Hechicero del Vacío:** gasta menos energía al ejecutar técnicas.

Las diferencias deben surgir mediante polimorfismo. No se debe consultar el tipo concreto mediante `if` o `instanceof`. Una misma técnica debe producir un daño diferente según quién la ejecute, sin conocer la técnica el tipo del combatiente.

Debe ser posible agregar nuevos tipos sin modificar el torneo ni las técnicas existentes.

## 2. Técnicas

Una técnica es únicamente una definición compuesta por:

- nombre;
- costo de energía;
- efecto.

Los usos se registran en cada combatiente, porque una misma técnica puede pertenecer a varios combatientes.

Ejemplos:

- **Corte de los Mil Amaneceres:** daña al adversario.
- **Muro del Cuerpo Dorado:** reduce el daño recibido durante el combate.
- **Dominio de la Nada:** daña al adversario y le quita energía.
- **Rugido de la Bestia Primordial:** ataque poderoso.
- **Meditación del Inmortal:** recupera vida.

El grupo debe definir, documentar y justificar los valores de daño y costo.

## 3. Escuelas y creación de combatientes

Las escuelas preparan combatientes de cualquier tipo asignándoles técnicas y equipamiento iniciales.

- **Escuela de la Espada Celestial:** ataques físicos y armas.
- **Palacio del Vacío:** técnicas especiales y dominio de la energía.
- **Monasterio del Cuerpo Dorado:** resistencia, defensa y recuperación.

Una escuela debe ser un objeto que contenga nombre, técnicas iniciales y equipamientos iniciales. Agregar una escuela debe consistir en crear otro objeto, sin escribir código nuevo.

La creación debe estar centralizada: crear un Hechicero del Vacío preparado por el Palacio del Vacío no debe requerir conocer su clase concreta, sus atributos iniciales ni el proceso de preparación.

## 4. Equipamiento

El equipamiento modifica las capacidades del combatiente mientras lo lleva.

Ejemplos:

- **Espada Celestial:** aumenta el daño.
- **Armadura Dorada:** reduce el daño recibido.
- **Anillo Espiritual:** aumenta la energía máxima.
- **Sello del Fénix:** aumenta la vida máxima.

Reglas:

- cada combatiente puede llevar hasta tres equipamientos;
- no puede llevar dos objetos de la misma categoría: arma, armadura, anillo o sello;
- los efectos se acumulan;
- se puede equipar y quitar antes del torneo y entre combates;
- un intento inválido debe rechazarse sin modificar el estado;
- aumentar un máximo no aumenta el valor actual;
- al quitar un equipamiento, el valor actual se recorta si supera el nuevo máximo;
- el combatiente debe poder consultar los objetos que lleva.

## 5. Parejas y torneo

Cada pareja tiene un identificador único asignado al crearse. El creador asigna los identificadores de los combatientes en orden de creación.

No se permite:

- inscribir un combatiente dos veces;
- registrar una pareja dos veces;
- iniciar el torneo con una cantidad distinta de cuatro u ocho parejas.

Los cruces se forman de la siguiente manera:

- primera ronda: por orden de inscripción;
- rondas siguientes: por el orden de los cruces de la ronda anterior.

Una pareja pierde cuando sus dos integrantes quedan fuera de combate. El torneo termina cuando queda una sola pareja.

Las colecciones deben seleccionarse según la necesidad: conservar orden, evitar duplicados, registrar usos de técnicas y ordenar la tabla de posiciones.

## 6. Reglas del combate

### Asaltos y turnos

El combate se desarrolla en asaltos. En cada asalto, cada pareja juega su turno completo y cada integrante en pie realiza una acción.

- comienza la pareja de menor identificador;
- dentro de la pareja actúa primero el combatiente de menor identificador;
- si una pareja queda sin integrantes en pie, el combate termina inmediatamente.

### Acciones

Una acción puede ser:

- ataque básico;
- técnica.

El ataque básico no consume energía, no tiene límite de usos y no participa de la regla del compañero.

Una técnica requiere energía suficiente y usos disponibles.

Criterio predeterminado:

1. técnica de recuperación si falta vida;
2. técnica defensiva si no hay una defensa activa;
3. técnica ofensiva con más usos restantes;
4. ataque básico si ninguna técnica es posible.

En caso de empate, se elige la primera técnica según el orden de la escuela. El grupo puede definir otro criterio, pero debe ser determinista y estar documentado.

### Objetivos

Los ataques básicos y las técnicas ofensivas apuntan al rival en pie con menor vida actual. En caso de empate, se elige al combatiente de menor identificador.

Las técnicas defensivas y de recuperación se aplican al ejecutor.

### Vida y energía

- al llegar a cero de vida, el combatiente queda fuera de combate;
- un combatiente caído no actúa ni puede ser objetivo;
- al comenzar cada asalto, los combatientes en pie recuperan una cantidad fija de energía;
- la energía no puede superar el máximo;
- al comenzar cada combate, todos recuperan vida y energía al máximo, incluso quienes habían caído en combates anteriores;
- el grupo debe definir los valores iniciales.

### Técnicas y regla del compañero

Cuando un integrante usa una técnica, su compañero no puede usarla mientras el primero conserve usos de esa técnica y siga en pie.

El compañero puede usarla si:

- el primer combatiente agota sus usos;
- el primer combatiente queda fuera de combate;
- comienza un nuevo combate.

Cada combatiente puede usar cada técnica cinco veces durante todo el torneo. Este registro no se reinicia entre combates.

### Duración y desempate

El grupo debe fijar un límite de asaltos por combate.

Si se alcanza ese límite, gana la pareja con mayor vida total. La vida total es la suma de la vida actual de sus integrantes; un caído aporta cero.

Si también hay empate, gana la pareja de menor identificador.

### Reproducibilidad

El combate debe ser determinista: ante el mismo estado inicial debe producir el mismo resultado e historial.

## 7. Acontecimientos y presentación

El combate y el torneo deben comunicar:

- inicio de combate;
- acción realizada;
- ataque básico o uso de técnica;
- eliminación;
- resultado del combate;
- ronda;
- pareja ganadora;
- vida total de cada pareja;
- proclamación del campeón.

Cada acontecimiento debe conservar los datos del momento en que ocurrió. Deben poder agregarse nuevos interesados sin modificar la lógica del torneo.

### Componentes obligatorios

#### Heraldo Imperial

Presenta los acontecimientos por pantalla. Es la única clase del sistema que escribe en pantalla, incluido lo que muestra `main`.

#### Cronista de la Secta

Registra los acontecimientos y permite consultar el historial. Con los resultados registrados arma la tabla de posiciones ordenada por:

1. última ronda alcanzada;
2. vida total final;
3. identificador de pareja.

Al finalizar el torneo, `main` debe solicitar la tabla al Cronista y pedir al Heraldo que la presente.

## 8. Pruebas unitarias

Se debe crear una batería de pruebas con JUnit 5 para:

- técnicas;
- combatientes;
- escuelas;
- equipamiento;
- parejas;
- combate;
- torneo.

Cada regla de las secciones 4, 5 y 6 debe tener al menos una prueba asociada y el informe debe indicar qué prueba cubre cada regla.

Como mínimo se debe probar:

- una misma técnica daña diferente según el tipo del ejecutor;
- no se ejecuta una técnica sin energía o sin usos restantes;
- los usos se conservan entre combates, mientras vida y energía se recuperan;
- el compañero no puede usar una técnica mientras el primero conserve usos;
- equipar un cuarto objeto o repetir una categoría se rechaza sin cambios;
- quitar un equipamiento que aumenta la vida máxima recorta la vida actual;
- un caído no actúa ni puede ser objetivo;
- elección de objetivo y desempate al límite de asaltos;
- inscripciones duplicadas y cantidad inválida de parejas;
- tabla ordenada por última ronda y vida final;
- dos torneos iguales producen el mismo campeón e historial.

Las pruebas de combate y torneo deben verificar el estado de los objetos o el historial del Cronista, nunca leer la salida por pantalla.

## 9. Bonus

Se debe implementar al menos una de estas funcionalidades:

### Efectos temporales

Un efecto activo durante una cantidad definida de turnos del combatiente afectado, como veneno o regeneración.

Debe definirse:

- cuándo se aplica;
- cuánto dura;
- qué ocurre cuando termina.

### Combatiente reactivo

Un combatiente modifica su comportamiento ante un acontecimiento, por ejemplo potenciando su próximo ataque cuando cae su compañero.

Debe definirse cuándo se activa y cuándo termina.

## Interfaz y ejecución

El sistema se evaluará mediante dos enfoques simultáneos:

1. pruebas unitarias con JUnit;
2. un `main` que configure escuelas, cree combatientes preparados, forme parejas y ejecute el torneo completo mostrando los resultados mediante el Heraldo.

Ejemplo simplificado:

```java
public class TorneoDeLosInmortales {

    public static void main(String[] args) {

        Escuela escuelaDeLaEspada = new Escuela(
            "Escuela de la Espada Celestial",
            List.of(new CorteDeLosMilAmaneceres()),
            List.of(new EspadaCelestial())
        );

        Escuela palacioDelVacio = new Escuela(
            "Palacio del Vacío",
            List.of(
                new DominioDeLaNada(),
                new MeditacionDelInmortal()
            ),
            List.of(new AnilloEspiritual())
        );

        CreadorCombatientes creador = new CreadorCombatientes();

        Combatiente aurelio = creador.crear(
            "Cultivador de la Espada",
            "Aurelio",
            escuelaDeLaEspada
        );

        Combatiente nebula = creador.crear(
            "Hechicero del Vacío",
            "Nébula",
            palacioDelVacio
        );

        Combatiente kael = creador.crear(
            "Monje del Cuerpo Celestial",
            "Kael",
            palacioDelVacio
        );

        Pareja primera = new Pareja(1, aurelio, nebula);

        Torneo torneo = new Torneo();
        torneo.inscribir(primera);
        torneo.iniciar();
    }
}
```

Se recomienda preparar varios escenarios para mostrar las distintas características implementadas.

## Entrega

La entrega se realiza en grupos de cuatro a seis integrantes e incluye:

- proyecto con código fuente;
- pruebas unitarias;
- diagrama de clases;
- informe con carátula, índice, introducción, desarrollo, conclusiones y referencias en formato APA.

También habrá una defensa y presentación oral en la fecha designada.

Se valora la claridad del diseño acorde al nivel de la materia. La sobreingeniería no suma.

## Glosario

- **Escuela:** configuración que define técnicas y equipamiento iniciales; no determina el tipo de combatiente.
- **Técnica:** acción con efecto propio y costo de energía, con usos limitados por torneo.
- **Ataque básico:** acción sin costo de energía ni límite de usos; no es una técnica.
- **Pareja:** unidad formada por dos combatientes.
- **Ronda:** etapa del torneo en la que se juegan cruces y avanzan las parejas ganadoras.
- **Cruce:** enfrentamiento entre dos parejas dentro de una ronda.
- **Combate:** desarrollo de un cruce, dividido en asaltos.
- **Asalto:** unidad del combate en la que cada pareja realiza su turno completo.
- **Turno:** parte del asalto en la que los integrantes en pie de una pareja realizan una acción.
- **Acontecimiento:** hecho del combate o del torneo que se comunica a interesados como el Heraldo y el Cronista.
