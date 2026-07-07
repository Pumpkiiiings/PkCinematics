# PkCinematics 🎬
El motor de cinemáticas definitivo para servidores de Minecraft (Paper 1.21.1).

PkCinematics es un plugin avanzado de cámaras y secuencias narrativas diseñado para ofrecer experiencias inmersivas a 60FPS constantes sin los molestos tirones de Minecraft Vanilla. 

Con un completo **Motor de Eventos (Triggers)**, puedes desencadenar espectáculos audiovisuales cuando el jugador entra al servidor, cambia de mundo, reaparece y más.

---

## 🌟 Características Principales
* **100% Fluidez (Paquetes Nativos)**: Utiliza `PacketEvents` para crear una cámara fantasma por paquetes. El jugador nunca experimenta los "tirones" típicos de la teletransportación de Bukkit.
* **Carga de Chunks Inteligente**: El plugin engaña al servidor simulando un movimiento físico real para asegurar que el mundo siempre cargue, mientras el cliente bloquea el temblor de la cámara usando el `CameraPacketListener`.
* **Sistema de Acciones por Ticks**: Sincroniza climas, pantallas de ceguera, textos y sonidos exactamente en el momento (tick) que desees.
* **Soporte de Splines & Interpolación**: Suavizado de cámara con curvas lineales y soporte para matemáticas avanzadas en el futuro.
* **Sistema de Triggers Dinámico**: Activa cinemáticas en eventos específicos (First Join, Respawn, etc.) con soporte para validación de condiciones (Permisos, Modos de Juego, etc).

---

## 🛠️ Comandos y Permisos

El comando principal es `/cinematic` (Aliases: `/pkcinematics`, `/cinematics`, `/pkc`).
**Permiso requerido para administrar**: `pkcinematics.admin`

* `/pkc play <nombre>` - Reproduce una cinemática.
* `/pkc stop` - Detiene la cinemática actual y restaura tu estado original.
* `/pkc debug` - Activa/Desactiva el modo desarrollador para ver en tiempo real los ticks y acciones que se ejecutan.
* `/pkc reload <cinematics|triggers|messages|all>` - Recarga los archivos de configuración sin reiniciar el servidor.

---

## 📖 Documentación de la API (Configuración)

PkCinematics utiliza dos carpetas principales dentro de `plugins/PkCinematics/`:
1. `cinematics/` (Archivos que definen la ruta de la cámara y los efectos).
2. `triggers/` (Archivos que definen *cuándo* y a *quién* se le reproducen las cinemáticas).

### 1. Creación de una Cinemática (`cinematics/intro.yml`)

Las cinemáticas están compuestas por una duración (en ticks), la ruta de la `camera` y las `actions`.

```yaml
duration: 1500

camera:
  '0':
    world: world
    x: 10.5
    y: 70.0
    z: 20.0
    yaw: 90.0
    pitch: 0.0
    fov: 70.0
    interpolation: LINEAR
  '100':
    # (...) Siguiente punto a los 5 segundos (100 ticks)

actions:
  '0': 
    - type: potion_effect
      effect: BLINDNESS
      duration: 60
    - type: time
      time: 18000
    - type: title
      title: "&6BIENVENIDO"
      subtitle: "&ePrepárate"
      fadeIn: 20
      stay: 100
      fadeOut: 20
  '1500': 
    - type: reset_environment # Restaura el clima y tiempo normal
```

### 2. Creación de un Trigger (`triggers/bienvenida.yml`)

Los triggers sirven como "escuchadores" de eventos del servidor.

```yaml
id: bienvenida_épica

trigger:
  type: first_join # Tipos: first_join, join, quit, respawn, death, world_change

conditions:
  - type: gamemode
    value: SURVIVAL
  - type: permission
    permission: pkcinematics.skip
    has: false # Solo ejecuta si NO tiene este permiso

actions:
  - type: cinematic
    id: intro # Reproduce la cinemática 'intro.yml'
  - type: message
    text: "&a¡Comenzando tu aventura, %player_name%!"
```

### 3. Tipos de Acciones Disponibles
Las acciones se pueden colocar tanto en **Triggers** como dentro de la línea de tiempo de una **Cinemática**:

* `title`: Muestra un título (title, subtitle, fadeIn, stay, fadeOut).
* `actionbar`: Muestra un texto arriba del inventario (text).
* `message`: Mensaje en el chat (text).
* `command`: Ejecuta un comando (command, console: true/false).
* `cinematic`: Inicia otra cinemática (id).
* `sound`: Reproduce un sonido (sound, volume, pitch).
* `particle`: Genera partículas (particle, count, offsetX, offsetY, offsetZ, speed).
* `potion_effect`: Da un efecto de poción (effect, duration, amplifier).
* `time`: Cambia la hora de forma personal para el jugador (time).
* `weather`: Cambia el clima personal del jugador (weather: DOWNFALL/CLEAR).
* `reset_environment`: Devuelve el clima y hora a los valores reales del servidor.

*(Nota: En todas las acciones de texto puedes usar la variable `%player_name%`)*.

---

## 🏗️ Construcción para Desarrolladores

El proyecto utiliza **Gradle** y la API de desarrollo **Paperweight**.

Para compilar el `.jar` listo para el servidor:
```bash
./gradlew jar
```
El archivo compilado se encontrará en `build/libs/PkCinematics-1.0-SNAPSHOT.jar`.
