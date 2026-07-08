# 🎬 Galería de Ejemplos de PkCinematics

En este documento encontrarás ejemplos completos y listos para copiar y pegar en tu servidor. Hemos cubierto los casos de uso más comunes e incluido ejemplos avanzados que utilizan **todas las funciones disponibles**.

---

## 1. El Tutorial Épico (Para servidores SIN Texture Pack)
Este ejemplo es ideal para servidores clásicos. Inicia apenas el jugador entra por primera vez y muestra una exhibición de todos los efectos disponibles (clima, tiempo, sonidos, partículas).

### El Trigger (`triggers/tutorial_basico.yml`)
```yaml
id: tutorial_basico

trigger:
  type: first_join

actions:
  - type: cinematic
    id: intro_completa
  - type: message
    text: "&eDisfruta de la introducción, %player_name%."
```

### La Cinemática (`cinematics/intro_completa.yml`)
```yaml
duration: 400 # 20 Segundos en total

camera:
  '0':
    world: world
    x: 0.0
    y: 150.0
    z: 0.0
    yaw: 0.0
    pitch: 90.0
    fov: 90.0
    interpolation: LINEAR
  '100': # Movimiento rápido hacia abajo (5 segundos)
    world: world
    x: 0.0
    y: 80.0
    z: 0.0
    yaw: 0.0
    pitch: 20.0
    fov: 70.0
    interpolation: LINEAR
  '200': # Efecto FREEZE: La cámara se queda quieta aquí por 5 segundos
    world: world
    x: 0.0
    y: 80.0
    z: 0.0
    yaw: 0.0
    pitch: 20.0
    fov: 70.0
    interpolation: LINEAR
  '400': # Paneado final lento (10 segundos)
    world: world
    x: 100.0
    y: 75.0
    z: 100.0
    yaw: 90.0
    pitch: 0.0
    fov: 70.0
    interpolation: LINEAR

actions:
  '0': 
    - type: potion_effect
      effect: BLINDNESS
      duration: 60
    - type: time
      time: 18000 # Noche
    - type: weather
      weather: DOWNFALL # Lluvia

  '40': 
    - type: title
      title: "&6&lBIENVENIDO"
      subtitle: "&eEl mejor servidor de supervivencia"
      fadeIn: 20
      stay: 100
      fadeOut: 20
    - type: sound
      sound: UI_TOAST_CHALLENGE_COMPLETE
      volume: 1.0
      pitch: 1.0

  '100':
    - type: actionbar
      text: "&bExplorando el mapa..."
    - type: time
      time: 0 # Amanece dramáticamente
    - type: weather
      weather: CLEAR # Se despeja la lluvia
    - type: particle
      particle: FIREWORKS_SPARK
      count: 100
      offsetX: 5.0
      offsetY: 5.0
      offsetZ: 5.0
      speed: 0.2

  '200':
    - type: command
      command: "say ¡%player_name% está viendo el tutorial!"
      console: true
      
  '400': 
    - type: reset_environment # CRÍTICO: Limpiar el clima y hora al final
```

---

## 2. El Tutorial Seguro (Para servidores CON Texture Pack)
**⚠️ MUY IMPORTANTE:** Si tu servidor obliga o sugiere usar un Resource Pack, **NO uses el evento `first_join`**. Si lo haces, la cinemática se reproducirá *mientras* el jugador tiene la pantalla tapada descargando las texturas, y se perderá todo el show.

La forma correcta es usar el evento `resource_pack_loaded` junto con la condición `played_before: false`. Así, el tutorial solo iniciará cuando el jugador sea nuevo **Y** haya terminado de cargar las texturas exitosamente.

### El Trigger (`triggers/tutorial_texturas.yml`)
```yaml
id: tutorial_texturas

trigger:
  type: resource_pack_loaded

conditions:
  # Verifica que sea su primera vez en el servidor.
  # Si pones true, se reproducirá cada vez que entren y carguen el pack.
  - type: played_before
    value: false

actions:
  - type: cinematic
    id: intro_completa # (Puedes usar la misma cinemática del Ejemplo 1)
```
*(No necesitas cambiar la cinemática, puedes usar la misma `intro_completa.yml` del ejemplo anterior).*

---

## 3. Cinemática de Muerte (Drama y Castigo)
Un efecto de cámara de 5 segundos mostrando el lugar de muerte en cámara lenta y con oscuridad.

### El Trigger (`triggers/muerte.yml`)
```yaml
id: muerte_dramatica

trigger:
  type: respawn # Se lanza justo cuando le dan a "Reaparecer"

actions:
  - type: cinematic
    id: efecto_muerte
```

### La Cinemática (`cinematics/efecto_muerte.yml`)
```yaml
duration: 100 # 5 Segundos

camera:
  '0':
    world: world
    x: 0.0
    y: 100.0
    z: 0.0
    yaw: 45.0
    pitch: 90.0 # Mirando al suelo
    fov: 90.0
    interpolation: LINEAR
  '100':
    world: world
    x: 0.0
    y: 80.0
    z: 0.0
    yaw: 45.0
    pitch: 45.0
    fov: 30.0 # Zoom dramático
    interpolation: LINEAR

actions:
  '0': 
    - type: time
      time: 18000
    - type: weather
      weather: DOWNFALL
    - type: potion_effect
      effect: DARKNESS
      duration: 100
    - type: title
      title: "&4&lHAS MUERTO"
      subtitle: "&cHas perdido tu inventario..."
      fadeIn: 10
      stay: 80
      fadeOut: 10
    - type: sound
      sound: ENTITY_WITHER_SPAWN
      volume: 1.0
      pitch: 0.5
      
  '100':
    - type: reset_environment
```
