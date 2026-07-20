# Configuración Avanzada: Formato YAML

Si eres un administrador avanzado que prefiere escribir en su editor de código favorito (como Visual Studio Code o Notepad++) en lugar de usar la interfaz dentro del juego, puedes abrir directamente los archivos guardados en `plugins/PkCinematics/cinematics/`.

> [!WARNING]
> Ten mucho cuidado con la indentación (espacios). Un mal espaciado en YAML corromperá el archivo y la cinemática no cargará.
> Después de editar un archivo, recuerda usar `/cinematic reload cinematics` en tu servidor.

## Estructura de `cinematics/mi_escena.yml`

A continuación se presenta un archivo de ejemplo que ilustra cada sección del formato.

```yaml
# Duración total de la cinemática en ticks. 
# Nota: Si pones un número menor al del último punto de cámara, el plugin lo corregirá automáticamente.
duration: 300 

# Si es true, el jugador podrá saltarse la escena usando 'Shift' (Agacharse)
skipeable-cinematic: true

# Sección de la pista de cámara. Cada clave es el número exacto del tick.
camera:
  '0':
    world: world_the_end
    x: 100.5
    y: 70.0
    z: 0.5
    yaw: 0.0
    pitch: 0.0
    fov: 90.0
    interpolation: LINEAR
    easing: EASE_IN
  '300':
    world: world_the_end
    x: 0.5
    y: 70.0
    z: 0.5
    yaw: 0.0
    pitch: 20.0
    fov: 70.0
    interpolation: LINEAR
    easing: EASE_OUT

# Sección de la pista de acciones. Cada clave es el tick en el que se ejecutan.
actions:
  '0': 
    # Hace de día al arrancar
    - type: time
      time: 6000 
    
    # Carga partículas en la pantalla
    - type: particle
      particle: FIREWORKS_SPARK
      count: 50
      offsetX: 1.0
      offsetY: 1.0
      offsetZ: 1.0
      speed: 0.1
      
  '40': # 2 segundos después, aparece un título.
    - type: title
      title: "&5&lEL FIN"
      subtitle: "&dPrepárate para la batalla final"
      fadeIn: 20
```

¡Eso es todo! Usar el formato YAML es ideal si necesitas copiar y pegar coordenadas exactas o añadir decenas de partículas de una sola vez.
