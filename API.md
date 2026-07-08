# 💻 API para Desarrolladores (PkCinematics)

PkCinematics proporciona una API robusta y fácil de usar para que otros plugins (o los tuyos propios) puedan reproducir, detener y crear cinemáticas, así como registrar acciones personalizadas.

Para acceder a la API en cualquier momento, utiliza:
```java
import com.pumpkiiiings.pkcinematics.api.PkCinematics;

PkCinematics api = PkCinematics.getApi();
```

---

## 1. Reproducir y Detener Cinemáticas (`PlaybackManager`)

El `PlaybackManager` controla las sesiones activas de cámara. Te permite iniciar, detener o pausar la cinemática de un jugador.

```java
import com.pumpkiiiings.pkcinematics.api.PlaybackManager;
import com.pumpkiiiings.pkcinematics.api.CinematicManager;
import com.pumpkiiiings.pkcinematics.model.Cinematic;

// 1. Obtener los managers
PlaybackManager playback = PkCinematics.getApi().getPlaybackManager();
CinematicManager cinManager = PkCinematics.getApi().getCinematicManager();

// 2. Buscar la cinemática por ID (nombre del archivo sin .yml)
Cinematic cinematic = cinManager.getCinematic("intro_tutorial");

if (cinematic != null) {
    // 3. Reproducirla al jugador
    playback.play(player, cinematic);
}

// Para detener una cinemática forzosamente:
playback.stop(player);

// Para comprobar si un jugador está viendo una cinemática:
boolean isPlaying = playback.isPlaying(player);
```

---

## 2. Buscar e Interactuar con Cinemáticas (`CinematicManager`)

El `CinematicManager` contiene todas las cinemáticas cacheadas en memoria y te permite administrarlas.

```java
import com.pumpkiiiings.pkcinematics.api.CinematicManager;

CinematicManager manager = PkCinematics.getApi().getCinematicManager();

// Obtener una cinemática específica
Cinematic cinematic = manager.getCinematic("nombre");

// Obtener TODAS las cinemáticas cargadas
for (Cinematic c : manager.getAllCinematics()) {
    System.out.println("Cargada: " + c.getId() + " con duración de " + c.getDuration() + " ticks.");
}
```

---

## 3. Crear Acciones Propias (`ActionRegistry`)

Puedes extender PkCinematics registrando tus propias "Actions". Por ejemplo, si quieres crear la acción `dar_dinero` para usarla en tus archivos `.yml`:

```yaml
actions:
  '40':
    - type: dar_dinero
      cantidad: 500
```

### Paso 1: Crear la clase de la Acción
Debes crear una clase que implemente `PkAction`.

```java
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import org.bukkit.entity.Player;
import java.util.Map;

public class DineroAction implements PkAction {

    private int cantidad = 0;

    // Se ejecuta al cargar el YAML
    @Override
    public void deserialize(Map<String, Object> data) {
        if (data.containsKey("cantidad")) {
            this.cantidad = (int) data.get("cantidad");
        }
    }

    // Se ejecuta en el momento exacto (tick) durante la cinemática o trigger
    @Override
    public void execute(Player player) {
        // Tu lógica aquí (ej. Vault API)
        player.sendMessage("¡Has recibido " + cantidad + " monedas!");
    }

    // El ID que se usará en el YAML
    @Override
    public String getType() {
        return "dar_dinero";
    }
}
```

### Paso 2: Registrarla en PkCinematics
En el `onEnable` de tu plugin, una vez que PkCinematics esté cargado, registras tu acción:

```java
import com.pumpkiiiings.pkcinematics.api.PkCinematics;

@Override
public void onEnable() {
    PkCinematics.getApi().getActionRegistry().registerAction("dar_dinero", DineroAction.class);
    getLogger().info("¡Acción dar_dinero registrada en PkCinematics!");
}
```

---

## 4. Crear Triggers o Condiciones Propias (`ConditionRegistry` & `TriggerRegistry`)

De la misma manera que las Acciones, puedes registrar Condiciones y Triggers.

### Ejemplo: Condición Personalizada
Para crear `- type: nivel_minimo`:

```java
import com.pumpkiiiings.pkcinematics.api.condition.Condition;
import org.bukkit.entity.Player;
import java.util.Map;

public class NivelCondition implements Condition {
    private int nivel;

    @Override
    public void deserialize(Map<String, Object> data) {
        this.nivel = (int) data.getOrDefault("nivel", 1);
    }

    @Override
    public boolean test(Player player) {
        return player.getLevel() >= this.nivel;
    }

    @Override
    public String getType() {
        return "nivel_minimo";
    }
}
```
**Registro:**
```java
PkCinematics.getApi().getConditionRegistry().registerCondition("nivel_minimo", NivelCondition.class);
```

### Eventos de Bukkit
PkCinematics usa llamadas manuales al `TriggerManager` en los Eventos de Bukkit. Si quieres crear tu propio Evento Trigger:

```java
@EventHandler
public void onPlayerJump(PlayerJumpEvent event) {
    // Busca si hay algún trigger YAML configurado como `type: jump` y lo ejecuta
    PkCinematics.getApi().getTriggerManager().fire("jump", event.getPlayer());
}
```
**Y lo registras con:**
```java
PkCinematics.getApi().getTriggerRegistry().registerTriggerType("jump");
```
