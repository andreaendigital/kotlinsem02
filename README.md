# 🍕 Sistema de Gestión de Pizzería - Kotlin (Semana 2)

Sistema de consola desarrollado en **Kotlin** para la gestión integral de una pizzería, aplicando Programación Orientada a Objetos (POO), colecciones avanzadas, control de inventario interactivo, validaciones robustas y reportes diarios.

---

## 🚀 Características Principales del Sistema

1. **Gestión de Inventario Interactiva:**
   - Control estricto de ingredientes mediante colecciones `Map`.
   - Verificación previa de stock antes de aceptar un pedido (con catálogo simplificado: *Pizza de Carnes* y *Pizza Hawaiana*).
   - Sistema de compra y reabastecimiento en tiempo real cuando algún ingrediente es insuficiente.

2. **Programación Orientada a Objetos (POO):**
   - Uso de clases base (`Usuario`) y especializadas (`Cliente`, `Repartidor`) aplicando herencia y polimorfismo.
   - Modelado modular de `Producto` y `Pedido` con métodos encapsulados para el cálculo de totales (descuentos e impuestos).

3. **Flujos de Control y Colecciones:**
   - Menú interactivo principal con opciones estructuradas mediante `when`.
   - Almacenamiento de pedidos en arreglos/listas y filtrado avanzado (`filter`) por estados (`En preparación`, `Listo`, `En camino`, `Entregado`).
   - Reporte diario de pedidos utilizando iteración por ciclos `for`.

4. **Manejo de Errores y Nulos:**
   - Validación segura de entradas numéricas con bloques `try-catch`.
   - Uso de operadores de seguridad (`?`, `?:`, `let`) para campos opcionales como las notas del cliente.

---

## 📷 Evidencias de Ejecución 

### 1. Menú Principal del Sistema

<img width="431" height="217" alt="Captura de pantalla 2026-11-02 170636" src="https://github.com/user-attachments/assets/21f3ac15-0811-4470-8a99-5560b9c314c2" />


### 2. Ingreso de Nuevo Pedido y Reabastecimiento de Inventario

<img width="1084" height="517" alt="Captura de pantalla 2026-11-02 163912" src="https://github.com/user-attachments/assets/397faa88-cea2-45b8-a53b-fcb2e4e99d44" />


### 3. Gestión y Filtrado de Estados de Pedidos

<img width="743" height="447" alt="Captura de pantalla 2026-11-02 165554" src="https://github.com/user-attachments/assets/64810393-e68a-4185-9c19-5af06109d280" />

<img width="765" height="329" alt="Captura de pantalla 2026-11-02 165627" src="https://github.com/user-attachments/assets/794e003e-03fc-4d53-a522-ea0ccb86d3d0" />


### 4. Reporte Diario de Pedidos

<img width="674" height="721" alt="Captura de pantalla 2026-11-02 171128" src="https://github.com/user-attachments/assets/3bea231a-581c-4228-bb82-659364b8a802" />

### 4. Gestión de Inventario

<img width="497" height="466" alt="Captura de pantalla 2026-11-02 162417" src="https://github.com/user-attachments/assets/fbb172b3-110b-4659-9361-329b59089e80" />

<img width="627" height="329" alt="Captura de pantalla 2026-11-02 162440" src="https://github.com/user-attachments/assets/3ba8761d-0017-4c58-9d62-a780e6e6629d" />

---

## 🛠️ Instrucciones de Ejecución

1. Asegúrate de tener instalado **IntelliJ IDEA** y el entorno de ejecución de **Kotlin**.
2. Crea un archivo `.kt` (por ejemplo, `Main.kt`) dentro de tu proyecto.
3. Copia el código fuente del sistema en el archivo.
4. Ejecuta la aplicación haciendo clic en el botón de reproducción verde (`▶`) al lado de la función `main()`.
