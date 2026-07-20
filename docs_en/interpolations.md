# Camera Movement: Easing and FOV

With PkCinematics, movements aren't robotic. You can mathematically configure how you want the camera to travel between *Point A* and *Point B*.

There are two key properties for each point: **Interpolation (Path)** and **Easing (Acceleration)**. You can edit both in-game using `/cinematic point edit <name> <index> interp <type>` or `easing <type>`.

## 1. Interpolation (Movement Path)

Defines the path the camera will take in 3D space.

* **`LINEAR` (Default):** The camera will travel in a perfect straight line from Point A to Point B. Useful for corridor movements or direct reveals.
* **`CATMULL_ROM`:** Mathematical magic! Instead of going in a straight line, the engine analyzes the previous and next points to create a **perfect, smooth curve** in the air. It is vital to use this when making camera turns or circling structures, otherwise the turn will look clunky.

## 2. Easing (Acceleration and Speed)

Defines the speed at which the camera will move during the journey.

* **`LINEAR` (Default):** Constant robotic speed. It starts abruptly and stops abruptly.
* **`EASE_IN`:** The camera starts very slowly, gradually gaining maximum speed until it abruptly reaches the next point.
* **`EASE_OUT`:** It starts very fast and, as it approaches the end point, slowly brakes like a car.
* **`SMOOTH`:** *The best option.* It starts slowly, accelerates in the middle of the path, and slowly brakes before arriving. It creates the perfect cinematic look.

## 3. FOV (Zoom)

When adding a point, it will save your FOV (Field of View) level. If you create Point A with normal FOV (70), and Point B with a lot of zoom (FOV 30), the plugin **will create a fluid zoom effect** perfectly synced with your Easing while traveling from Point A to B.

* You can use the zoom to give dramatic impact to a distant castle (`Ease-In` with FOV reduction).
* Edit the FOV of an existing point using: `/cinematic point edit <name> <index> fov <number>`.
