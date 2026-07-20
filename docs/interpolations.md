# Movimiento de Cámara: Easing y FOV

Con PkCinematics, los movimientos no son robóticos. Puedes configurar matemáticamente cómo quieres que la cámara viaje entre el *Punto A* y el *Punto B*.

Existen dos propiedades clave para cada punto: **Interpolation (Trayectoria)** y **Easing (Aceleración)**. Puedes editar ambas desde el juego usando `/cinematic point edit <nombre> <index> interp <tipo>` o `easing <tipo>`.

## 1. Interpolation (Trayectoria de Movimiento)

Define por dónde viajará la cámara en el espacio 3D.

* **`LINEAR` (Por defecto):** La cámara viajará en línea recta y perfecta desde el Punto A al Punto B. Es útil para movimientos de pasillo o revelaciones directas.
* **`CATMULL_ROM`:** ¡Magia matemática! En lugar de ir en línea recta, el motor analiza el punto anterior y el punto siguiente, y crea una **curva perfecta y suave** en el aire. Es vital usar esto cuando haces giros de cámara o rodeas estructuras, de lo contrario el giro se verá tosco.

## 2. Easing (Aceleración y Velocidad)

Define a qué velocidad se moverá la cámara durante el trayecto.

* **`LINEAR` (Por defecto):** Velocidad robótica constante. Empieza de golpe y frena de golpe.
* **`EASE_IN`:** La cámara arranca muy lentamente, y poco a poco va ganando máxima velocidad hasta llegar de golpe al siguiente punto.
* **`EASE_OUT`:** Arranca muy rápido y, cuando se acerca al punto final, frena lentamente como un coche.
* **`SMOOTH`:** *La mejor opción.* Empieza lentamente, acelera en la mitad del camino, y frena lentamente antes de llegar. Crea el aspecto cinematográfico perfecto.

## 3. FOV (El Zoom)

Al añadir un punto, este guardará tu nivel de FOV (Field of View). Si creas el Punto A con FOV normal (70), y el Punto B con mucho zoom (FOV 30), el plugin **hará un efecto de zoom fluido** sincronizado perfectamente con tu Easing mientras viaja del Punto A al B.

* Puedes usar el zoom para darle impacto dramático a un castillo lejano (`Ease-In` con reducción de FOV).
* Edita el FOV de un punto ya creado usando: `/cinematic point edit <nombre> <index> fov <numero>`.
