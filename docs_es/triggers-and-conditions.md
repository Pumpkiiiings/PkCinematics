# Triggers y Condiciones

No siempre quieres arrancar una cinemática escribiendo `/cinematic play <jugador>`. Con los **Triggers** (Eventos), puedes configurar el plugin para que lo haga solo cuando ocurra algo en el servidor.

Los Triggers se configuran mediante archivos `.yml` dentro de la carpeta `plugins/PkCinematics/triggers/`. Puedes crear tantos archivos como quieras.

## Estructura de un Trigger

Ejemplo de `mi_primer_join.yml`:

```yaml
type: first_join
cinematic: intro_epica
conditions:
  - type: gamemode
    gamemodes: [SURVIVAL, ADVENTURE]
```

* **`type`**: Cuál es el evento que iniciará la cinemática.
* **`cinematic`**: El nombre (id) de la cinemática que se reproducirá.
* **`conditions`**: (Opcional) Requisitos que el jugador debe cumplir para que la cinemática se inicie.

## Tipos de Triggers

Puedes usar cualquiera de estos eventos en la propiedad `type`:

* **`first_join`**: Cuando un jugador entra al servidor por *primera vez en su vida*.
* **`join`**: Cuando un jugador entra al servidor (cada vez).
* **`quit`**: Cuando un jugador sale del servidor (nota: la cinemática no se la puedes mostrar si ya salió, esto es más útil si quisieras hacer un comando global).
* **`respawn`**: Cuando el jugador revive después de morir.
* **`death`**: En el instante en el que muere.
* **`world_change`**: Cuando el jugador se teletransporta o pasa por un portal hacia otro mundo.
* **`resource_pack_loaded`**: Cuando el jugador termina de aceptar y descargar el texture pack del servidor.
* **`resource_pack_declined`**: Si el jugador rechaza descargar el texture pack del servidor.

## Condiciones Disponibles

Dentro de la lista `conditions`, puedes añadir bloqueos para que la cinemática no empiece si no se cumplen ciertas reglas.

**1. Por Permiso (`permission`)**
Exige que el jugador tenga un permiso específico.
```yaml
  - type: permission
    permission: "vip.ver_intro"
```

**2. Por Mundo (`world`)**
Exige que el evento haya ocurrido en un mundo específico.
```yaml
  - type: world
    worlds: ["world", "world_the_end"]
```

**3. Por Modo de Juego (`gamemode`)**
Bloquea la cinemática si el jugador está en creativo, por ejemplo.
```yaml
  - type: gamemode
    gamemodes: ["SURVIVAL", "ADVENTURE"]
```

**4. Ha Jugado Antes (`played_before`)**
Pregunta si el jugador es antiguo en el servidor (`true`) o completamente nuevo (`false`).
```yaml
  - type: played_before
    played_before: true
```
