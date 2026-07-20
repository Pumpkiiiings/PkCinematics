# Librería de Acciones (Actions)

Durante la línea de tiempo de tu cinemática, puedes hacer que sucedan eventos programados (Títulos, Sonidos, Efectos de Partículas). Puedes añadirlos fácilmente usando el menú GUI in-game (`/cinematic actions <tick>`) o editando directamente el archivo `.yml`.

A continuación, tienes una lista completa de las acciones soportadas.

---

### `title`
Muestra un título grande y un subtítulo en la pantalla del jugador.
* **title**: Texto principal. Soporta colores (`&a`, `&l`).
* **subtitle**: Texto inferior.
* **fadeIn**: Tiempo que tarda en aparecer (en ticks).
* **stay**: Tiempo que se queda en pantalla.
* **fadeOut**: Tiempo que tarda en desaparecer.

### `message`
Envía un mensaje de chat tradicional al jugador.
* **message**: El texto del mensaje (soporta colores).

### `actionbar`
Muestra un pequeño mensaje sobre la barra de acceso rápido (donde sale el nombre de los ítems).
* **message**: El texto.

### `sound`
Reproduce un sonido de Minecraft para el jugador.
* **sound**: Nombre oficial del sonido (ej. `ENTITY_ENDER_DRAGON_GROWL`).
* **volume**: Volumen (0.0 a 1.0+).
* **pitch**: Velocidad/tono del sonido (0.5 a 2.0).

### `particle`
Genera efectos de partículas frente a la cámara.
* **particle**: Nombre oficial de la partícula (ej. `FLAME`, `EXPLOSION`).
* **count**: Cantidad de partículas.
* **offsetX / offsetY / offsetZ**: Área de dispersión de las partículas.
* **speed**: Velocidad a la que salen disparadas.

### `potion_effect`
Aplica un efecto de poción al jugador (nota: el jugador real está invisible en el suelo durante la cinemática, así que efectos como ceguera (`BLINDNESS`) afectarán su cámara, ideal para transiciones).
* **effect**: Nombre de la poción (ej. `BLINDNESS`, `CONFUSION`).
* **duration**: Duración en ticks (ej. 100 para 5 segundos).
* **amplifier**: Nivel de la poción (ej. 1 para nivel 2).

### `command`
Ejecuta un comando en el servidor.
* **command**: El comando a ejecutar (sin la `/` inicial).
* **executor**: Quién lo ejecuta. Opciones: `PLAYER` (como si lo escribiera el jugador), o `CONSOLE` (lo ejecuta la consola).

### `time`
¡Altera el tiempo solo para el espectador!
* **time**: El valor del tiempo (ej. `0` para amanecer, `6000` para mediodía, `18000` para medianoche). Si quieres hacer que se haga de noche rápidamente para darle dramatismo a la escena.

### `weather`
¡Altera el clima solo para el espectador!
* **weather**: El clima deseado. Opciones: `CLEAR` (Soleado), `DOWNFALL` (Lluvia/Nieve).

### `reset_environment`
Devuelve el Tiempo y el Clima del jugador a la normalidad (lo sincroniza con el tiempo real del mundo del servidor). Útil al final de la cinemática.
* (No requiere parámetros extras).

### `play_cinematic`
Reproduce otra cinemática (funciona para enlazar varias escenas).
* **cinematic**: El ID/nombre de la cinemática que se va a reproducir a continuación.
