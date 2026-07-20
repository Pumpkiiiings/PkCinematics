# Empezando: Tu primera cinemática

¡Bienvenido a PkCinematics! Crear cinemáticas espectaculares en Minecraft nunca ha sido tan fácil. Todo se puede hacer desde dentro del juego. Sigue estos pasos para crear tu primera obra de arte.

## Paso 1: Crear una cinemática vacía

Entra al juego y ejecuta el siguiente comando para crear una nueva cinemática llamada "mi_intro":

```
/cinematic create mi_intro
```

Inmediatamente entrarás en el **Modo Editor**. Cuando estás en este modo, todas tus acciones se guardan en la cinemática temporal.

## Paso 2: Añadir Puntos Clave (Keyframes)

Una cinemática se basa en "Puntos Clave" (`Keyframes`). La cámara volará de un punto clave al siguiente.

1. Ve a la posición donde quieres que empiece la cinemática.
2. Mira hacia donde quieres que la cámara mire.
3. Escribe el comando:
   ```
   /cinematic point
   ```
   ¡Acabas de añadir tu primer punto (el Punto 0)! Por defecto, estará en el Tick 0 (el inicio).

4. Muévete a la siguiente posición.
5. Vuelve a escribir `/cinematic point`.
   ¡Has añadido el Punto 1! Por defecto, el plugin lo colocará 60 ticks (3 segundos) después del punto anterior.

Añade tantos puntos como quieras.

## Paso 3: Ajustar Tiempos y Posiciones

¿Quieres que el segundo punto tarde más tiempo en llegar? Puedes editar los puntos que ya creaste.
Por ejemplo, si quieres que la cámara llegue al Punto 1 en el Tick 100 (5 segundos) en lugar de en el 60:

```
/cinematic point edit mi_intro 1 time 100
```

> **Nota sobre el Tiempo:** En Minecraft, el tiempo se mide en "Ticks". 20 ticks equivalen a 1 segundo. Por lo tanto, 100 ticks = 5 segundos.

## Paso 4: Probar la cinemática

Mientras estás en el Modo Editor, puedes probar cómo está quedando tu cinemática en cualquier momento sin tener que guardarla:

```
/cinematic play mi_intro
```

Disfruta del viaje. Si algo no te gusta, simplemente usa `/cinematic point edit` o edita el YAML más tarde.

## Paso 5: Guardar los cambios

¡Es vital guardar tu progreso! Si cierras el servidor o te desconectas sin guardar, perderás todo.

```
/cinematic save
```

¡Felicidades! Has creado y guardado tu primera cinemática.

## Siguientes Pasos

Ahora que sabes lo básico, te recomendamos leer:
* **[El Editor de Acciones](editor.md)** para añadir mensajes y partículas en momentos exactos.
* **[Guía de Movimiento y Cámara](interpolations.md)** para hacer que la cámara haga giros curvos y aceleraciones fluidas.
