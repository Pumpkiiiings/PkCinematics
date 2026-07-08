# Documentación Oficial de PkCinematics

PkCinematics es un motor de cinemáticas avanzado impulsado por paquetes (PacketEvents). A continuación se detalla técnicamente la creación y configuración de **Triggers** y **Cinemáticas**, incluyendo todos los eventos y acciones disponibles.

---

## 1. Sistema de Triggers (`/triggers/`)

Los **Triggers** son los escuchadores de eventos (Event Listeners) que determinan **cuándo** y bajo qué **condiciones** se debe ejecutar una acción (como iniciar una cinemática o enviar un mensaje).

Un archivo de trigger tiene la siguiente estructura base:

```yaml
id: identificador_unico

trigger:
  type: TIPO_DE_EVENTO

conditions:
  - type: TIPO_DE_CONDICION
    # Parametros de la condicion...

actions:
  - type: TIPO_DE_ACCION
    # Parametros de la accion...
```

### 1.1 Eventos Disponibles (`trigger.type`)
Los eventos que el plugin puede interceptar en el servidor son:
* `first_join`: Se ejecuta única y exclusivamente la primera vez que un jugador entra al servidor (útil para tutoriales).
* `join`: Se ejecuta cada vez que un jugador entra al servidor.
* `quit`: Se ejecuta cuando el jugador se desconecta.
* `respawn`: Se ejecuta en el instante en que el jugador reaparece tras morir.
* `death`: Se ejecuta en el momento exacto en que la vida del jugador llega a 0.
* `world_change`: Se ejecuta cuando el jugador cambia de mundo (ej. entra a un portal del Nether/End o usa un comando de teletransporte entre mundos).
* `resource_pack_loaded`: Se ejecuta cuando el jugador acepta y descarga exitosamente (`SUCCESSFULLY_LOADED`) el Resource Pack del servidor.
* `resource_pack_declined`: Se ejecuta si el jugador rechaza o falla la descarga del Resource Pack.

### 1.2 Condiciones (`conditions`)
Las condiciones actúan como filtros. Si una condición no se cumple, las acciones del trigger no se ejecutarán.
* **`gamemode`**: Verifica el modo de juego del jugador.
  ```yaml
  - type: gamemode
    value: SURVIVAL # Puede ser SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR
  ```
* **`permission`**: Verifica si el jugador tiene (o no tiene) un permiso específico.
  ```yaml
  - type: permission
    permission: pkcinematics.admin
    has: false # true = debe tenerlo | false = NO debe tenerlo
  ```
* **`world`**: Verifica en qué mundo se encuentra el jugador actualmente.
  ```yaml
  - type: world
    world: world_the_end
  ```
* **`played_before`**: Verifica si el jugador ya se había unido al servidor en el pasado.
  ```yaml
  - type: played_before
    value: false # true = ya había entrado antes | false = es su primera vez en el servidor
  ```

---

## 2. Sistema de Cinemáticas (`/cinematics/`)

Las **Cinemáticas** son secuencias programadas basadas en un sistema de **Ticks** (1 segundo = 20 Ticks). En ellas se define la ruta de la cámara y las acciones sincronizadas.

Estructura base de una cinemática:

```yaml
duration: DURACION_TOTAL_EN_TICKS

camera:
  'TICK':
    world: nombre_del_mundo
    x: 0.0
    y: 0.0
    z: 0.0
    yaw: 0.0
    pitch: 0.0
    fov: 70.0
    interpolation: LINEAR

actions:
  'TICK':
    - type: TIPO_DE_ACCION
      # Parametros...
```

### 2.1 Puntos de Cámara (`camera`) y Velocidad
Define los fotogramas clave (keyframes) de la cámara. El motor interpolará (suavizará) el movimiento entre el punto `A` y el punto `B`. El `yaw` es la rotación horizontal y el `pitch` es la rotación vertical (arriba/abajo).

**¿Cómo hacer la cinemática más rápida o más lenta?**
La velocidad de la cámara está determinada por la distancia de los Ticks entre dos puntos de cámara (`'0'` y `'100'` por ejemplo). 
* Si quieres que vaya **rápido**, pon una diferencia de ticks pequeña (ej. de `'0'` a `'40'` para recorrer 100 bloques).
* Si quieres que vaya **lento**, pon una diferencia de ticks grande (ej. de `'0'` a `'200'` para recorrer la misma distancia, lo que le tomará 10 segundos).

### 2.2 Acciones por Ticks (`actions`)
Puedes ejecutar cualquiera de las siguientes funciones especificando el tick exacto en el que deseas que ocurran.

#### Mensajes y Textos (Variables soportadas: `%player_name%`)
* **`title`**: Envía un título y subtítulo en pantalla.
  ```yaml
  - type: title
    title: "&6Título Principal"
    subtitle: "&eSubtítulo"
    fadeIn: 20  # Ticks que tarda en aparecer
    stay: 60    # Ticks que se mantiene en pantalla
    fadeOut: 20 # Ticks que tarda en desaparecer
  ```
* **`actionbar`**: Mensaje sobre la barra de inventario.
  ```yaml
  - type: actionbar
    text: "&aMensaje de Actionbar"
  ```
* **`message`**: Mensaje estándar en el chat.
  ```yaml
  - type: message
    text: "&7Hola, %player_name%."
  ```

#### Efectos Visuales y Sonoros
* **`sound`**: Reproduce un sonido al jugador (Nombres oficiales de Bukkit).
  ```yaml
  - type: sound
    sound: ENTITY_ENDER_DRAGON_GROWL
    volume: 1.0
    pitch: 1.0 # 1.0 es normal, < 1.0 es más grave, > 1.0 es más agudo
  ```
* **`particle`**: Genera partículas en la posición de la cámara (Nombres oficiales de Bukkit).
  ```yaml
  - type: particle
    particle: FLAME
    count: 50
    offsetX: 1.0
    offsetY: 1.0
    offsetZ: 1.0
    speed: 0.1
  ```
* **`potion_effect`**: Aplica un efecto de poción al jugador (útil para dar `BLINDNESS` y ocultar tiempos de carga).
  ```yaml
  - type: potion_effect
    effect: BLINDNESS
    duration: 60 # Duración en ticks
  ```

#### Entorno (Paquetes Visuales)
* **`time`**: Cambia la hora (0 = Amanecer, 6000 = Mediodía, 18000 = Medianoche). Solo afecta visualmente al jugador de la cinemática.
  ```yaml
  - type: time
    time: 18000
  ```
* **`weather`**: Cambia el clima visual del jugador.
  ```yaml
  - type: weather
    weather: DOWNFALL # Opciones: CLEAR, DOWNFALL
  ```
* **`reset_environment`**: **CRÍTICO**. Restaura la hora y el clima a los valores reales del servidor. *Siempre* debe colocarse en el último tick si se modificaron con las acciones anteriores.
  ```yaml
  - type: reset_environment
  ```

#### Utilidades
* **`command`**: Ejecuta un comando.
  ```yaml
  - type: command
    command: "give %player_name% diamond 1"
    console: true # true = Lo ejecuta la consola, false = Lo ejecuta el jugador
  ```
* **`cinematic`**: Empalma y reproduce otra cinemática inmediatamente.
  ```yaml
  - type: cinematic
    id: nombre_del_archivo_sin_yml
  ```

---

## 3. Ejemplos Completos y Estructurados

### Ejemplo 1: Trigger de Primera Vez (First Join)
**Archivo:** `triggers/tutorial.yml`
```yaml
id: tutorial_inicio

trigger:
  type: first_join

conditions:
  # Solo ejecuta la cinemática si NO tiene el permiso "tutorial.saltar"
  - type: permission
    permission: tutorial.saltar
    has: false

actions:
  # Llama a la cinemática
  - type: cinematic
    id: intro_tutorial
  # Ejecuta un comando para darle un kit inicial
  - type: command
    command: "kit inicio %player_name%"
    console: true
```

### Ejemplo 2: Cinemática Sincronizada (Intro Tutorial)
**Archivo:** `cinematics/intro_tutorial.yml`
```yaml
duration: 200 # Total 10 segundos

camera:
  '0':
    world: world
    x: 0.0
    y: 100.0
    z: 0.0
    yaw: 0.0
    pitch: 20.0
    fov: 70.0
    interpolation: LINEAR
  '200':
    world: world
    x: 100.0
    y: 80.0
    z: 100.0
    yaw: 90.0
    pitch: 0.0
    fov: 70.0
    interpolation: LINEAR

actions:
  '0': 
    # Oscurecemos la pantalla para esconder la generación del terreno (Descargando Terreno...)
    - type: potion_effect
      effect: BLINDNESS
      duration: 60
    # Forzamos noche para un efecto dramático
    - type: time
      time: 18000
    - type: weather
      weather: DOWNFALL

  '40': # Tick 40 (2 Segundos después, para asegurar que el cliente ha cargado)
    - type: title
      title: "&4&lBIENVENIDO"
      subtitle: "&7Sobrevive si puedes..."
      fadeIn: 20
      stay: 60
      fadeOut: 20
    - type: sound
      sound: ENTITY_LIGHTNING_BOLT_THUNDER
      volume: 1.0
      pitch: 0.8

  '100':
    - type: actionbar
      text: "&e¡Mirando el horizonte!"

  '200': 
    # Restauramos TODO a la normalidad al terminar la cinemática
    - type: reset_environment
```

### Ejemplo 3: Cinemática esperando al Texture Pack
Para servidores que requieren Resource Pack, es vital esperar a que cargue antes de iniciar la cinemática de bienvenida.

**Archivo:** `triggers/tutorial_texture_pack.yml`
```yaml
id: tutorial_con_texturas

trigger:
  # Se ejecuta únicamente cuando el texture pack está SUCCESSFULLY_LOADED
  type: resource_pack_loaded

conditions:
  # Filtramos para que SOLO suceda si es la primera vez que entran al servidor
  - type: played_before
    value: false

actions:
  - type: cinematic
    id: intro_tutorial
```

---

## 4. Preguntas Frecuentes (FAQ)

### ¿Puedo pausar la cámara o dejarla estática ("freeze") en una ubicación?
**Sí**. No se utilizan triggers para esto, se hace directamente repitiendo la ubicación en la cámara.
Si quieres crear un efecto de "pausa", simplemente repite las mismas coordenadas exactas en dos fotogramas (ticks) distintos. 
Por ejemplo: si pones un punto en `'40'` y el siguiente en `'100'` con **las mismas posiciones** de `x, y, z, yaw, pitch`, la cámara se quedará completamente quieta durante esos 60 ticks (3 segundos) contemplando el paisaje, y luego desde el `'100'` reanudará su viaje rápido hacia tu siguiente punto. 
¡Esto te permite combinar viajes muy rápidos con pausas dramáticas en la misma cinemática!

### ¿Cómo elimino o borro un trigger/cinemática de prueba que ya no quiero?
Es completamente manejado por archivos. 
1. Ve a la carpeta `plugins/PkCinematics/cinematics/` o `plugins/PkCinematics/triggers/` de tu servidor.
2. Elimina el archivo `.yml` que ya no quieras (ej. `test_intro.yml`).
3. Entra al juego (o desde la consola) y ejecuta el comando `/pkc reload all`. ¡Y listo, borrado para siempre!

### ¿Cuál es la diferencia entre las `actions` del Trigger y las `actions` de la Cinemática?
Tienen el mismo nombre pero un propósito distinto dependiendo de dónde las pongas:
* **En un Trigger:** Son las consecuencias instantáneas de presionar "El Botón Rojo". Por ejemplo: *"Cuando muera (trigger), reproduce la película y dale un ítem (actions)"*.
* **En una Cinemática:** Son los efectos especiales dentro de la línea de tiempo. Ocurren en un Tick específico. Por ejemplo: *"En el tick '40' (segundo 2 de la película), haz que caiga un rayo visual (actions)"*.
