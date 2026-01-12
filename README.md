# El Problema de la Estación de Montaje de Drones

## 1. Introducción

Esta práctica simula un entorno de producción real donde varios hilos (operarios) compiten por recursos limitados (herramientas). El objetivo es aplicar los conceptos de exclusión mutua y sincronización para garantizar que la cadena de montaje funcione de forma fluida y segura, evitando situaciones de interbloqueo (deadlock) e inanición (starvation).

## 2. Descripción del Problema

Una empresa de tecnología dispone de una mesa circular con **5 puestos de trabajo**. En cada puesto hay un **Operario**. Para realizar el ensamblaje de un dron, se requieren obligatoriamente dos herramientas que se encuentran sobre la mesa:

- **Soldador**: situado a la izquierda de cada operario
- **Destornillador**: situado a su derecha

### Problema Técnico

Las herramientas son **compartidas**. El soldador de la izquierda de un operario es, al mismo tiempo, el destornillador de la derecha del operario contiguo. Por tanto, **dos operarios adyacentes no pueden trabajar al mismo tiempo**.

Esto crea una situación donde múltiples hilos compiten por recursos compartidos, lo que puede llevar a condiciones de carrera, deadlock o inanición si no se gestiona correctamente.

## 3. Estados del Hilo (Operario)

Cada hilo "Operario" ejecuta un **ciclo infinito** con los siguientes pasos:

1. **Estado "Preparando"**: El operario organiza sus piezas. Se simula mediante un `sleep()` de tiempo aleatorio entre 500ms y 2000ms.
2. **Estado "Solicitando herramientas"**: El operario intenta adquirir primero una herramienta y luego la otra.
3. **Estado "Ensamblando"**: Una vez conseguidas ambas herramientas, el operario trabaja. Se simula con un `sleep()` aleatorio entre 300ms y 1000ms.
4. **Estado "Liberando"**: El operario suelta ambas herramientas en el orden inverso al que las cogió y vuelve al estado 1.

## 4. Requisitos de la Implementación

### Lenguaje
- **Java 17** o superior

### Mecanismo de Sincronización
Se ha utilizado la clase **Semaphore** (Semáforos) de Java para controlar el acceso a las herramientas compartidas.

### Evitar el Deadlock

Se ha implementado una solución técnica que impide que todos los operarios cojan una herramienta y bloqueen el sistema mediante la **ruptura de la espera circular**.

#### Estrategia Implementada: Ordenamiento de Recursos

La solución utiliza la técnica de **ordenamiento global de recursos** para evitar deadlock:

1. **Semáforos para Exclusión Mutua**: Cada herramienta está protegida por un `Semaphore` con 1 permiso, garantizando que solo un operario puede usarla a la vez.
2. **Ordenamiento Consistente**: Cada operario **siempre adquiere las herramientas en un orden consistente** basado en el ID de la herramienta:
   - Primero adquiere la herramienta con **menor ID**
   - Luego adquiere la herramienta con **mayor ID**
3. **Liberación en Orden Inverso**: Las herramientas se liberan en el orden inverso al de adquisición (última adquirida, primera liberada).

#### ¿Por qué funciona esta estrategia?

- **Rompe la espera circular**: Al establecer un orden global de adquisición, eliminamos la posibilidad de que todos los operarios esperen circularmente. Si todos intentan adquirir primero la herramienta de menor ID, solo uno podrá tenerla, y los demás esperarán. Esto garantiza que siempre habrá progreso.
- **Garantiza progreso**: Si un operario tiene la herramienta de menor ID, eventualmente podrá adquirir la segunda, o liberará la primera permitiendo que otros progresen.
- **Previene inanición**: Los semáforos garantizan que los recursos se asignen de forma justa (FIFO).

### Salida por Pantalla

El programa muestra de forma clara qué está ocurriendo. Ejemplo de salida:

```
[Operario 2] - Preparando piezas...
[Operario 2] - Intentando coger herramientas...
[Operario 2] - Herramienta 2 adquirida.
[Operario 2] - Herramienta 3 adquirida.
[Operario 2] - Herramientas adquiridas.
[Operario 2] - Ensamblando dron nº 1...
[Operario 2] - Dron nº 1 finalizado.
[Operario 2] - Finalizado. Soltando herramientas.
[Operario 2] - Herramienta 3 liberada.
[Operario 2] - Herramienta 2 liberada.
```

## 5. Estructura del Proyecto

```
src/
├── Main.java              # Clase principal que arranca la simulación
├── EstacionMontaje.java   # Gestiona la mesa circular y los operarios
├── Operario.java          # Clase que extiende Thread, representa cada operario
└── Herramienta.java       # Recurso compartido protegido con semáforo
```

### Descripción de las Clases

- **Main**: Punto de entrada del programa. Crea la estación, inicia el trabajo y lo detiene después de 30 segundos.
- **EstacionMontaje**: Gestiona la configuración circular de la mesa. Crea las 5 herramientas y los 5 operarios, asignando a cada operario sus herramientas correspondientes.
- **Operario**: Extiende `Thread` y representa un operario. Implementa el ciclo infinito de trabajo con los 4 estados descritos.
- **Herramienta**: Representa una herramienta compartida. Utiliza un `Semaphore` para controlar el acceso exclusivo.

## 6. Compilación y Ejecución

### Requisitos
- Java 17 o superior
- Compilador Java (javac)

### Compilación
```bash
javac src/*.java
```

### Ejecución
```bash
java -cp src Main
```

El programa ejecutará la simulación durante **30 segundos** y mostrará:
- Los estados de cada operario en tiempo real
- Las herramientas que adquieren y liberan
- Las estadísticas finales de drones ensamblados por cada operario

## 7. Objetivos de Aprendizaje (Criterios de Evaluación)

### CE a): Conceptos de hilo y proceso
 Se han identificado los conceptos de hilo y proceso, así como sus estados de ejecución.
- La clase `Operario` extiende `Thread`, representando un hilo.
- Cada operario pasa por los estados: Preparando, Solicitando herramientas, Ensamblando, Liberando.

### CE b): Creación y control de hilos
 Se han utilizado clases para la creación y control de hilos.
- La clase `Operario` extiende `Thread` y sobrescribe el método `run()`.
- Se utilizan métodos como `start()`, `interrupt()`, `join()` para controlar los hilos.

### CE c): Mecanismos de sincronización
 Se han aplicado mecanismos de sincronización para proteger el acceso a recursos compartidos.
- Se utiliza la clase `Semaphore` para proteger cada herramienta.
- Cada herramienta tiene un semáforo con 1 permiso, garantizando exclusión mutua.

### CE d): Soluciones para evitar bloqueo mutuo
 Se han diseñado soluciones para evitar situaciones de bloqueo mutuo.
- Implementación de ordenamiento global de recursos para evitar deadlock.
- Cada operario adquiere siempre primero la herramienta con menor ID, rompiendo la espera circular.

## 8. Documentación

El código está completamente documentado con **JavaDoc** en español. Cada clase y método público tiene su documentación correspondiente explicando su funcionalidad.

## Autor

**Alejandro Ramon Rolon Vazquez**

---

**Fecha límite de entrega**: 12 de enero de 2026
```
