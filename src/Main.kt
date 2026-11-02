import java.time.LocalDate
import kotlin.math.round

// ==========================================
// MODELO DE CLASES (POO, Herencia y Polimorfismo)
// ==========================================
open class Usuario(
    val nombre: String,
    val contacto: String,
    val correo: String
)

class Cliente(
    nombre: String,
    contacto: String,
    correo: String,
    val direccion: String,
    val notas: String? // Manejo de nulos (campo opcional)
) : Usuario(nombre, contacto, correo)

class Repartidor(
    nombre: String,
    contacto: String,
    correo: String,
    val zona: String
) : Usuario(nombre, contacto, correo) {

    fun entregarPedido(pedido: Pedido) {
        println("🚚 [Repartidor $nombre] Entregando el pedido a ${pedido.cliente.nombre} en ${pedido.cliente.direccion}...")
        pedido.estado = "Entregado"
    }
}

class Producto(
    val nombre: String,
    val precio: Double,
    val ingredientesNecesarios: Map<String, Int> // Receta exacta por unidad
) {
    fun detallarProducto(): String {
        val ingStr = ingredientesNecesarios.entries.joinToString(", ") { "${it.key}: ${it.value} unid." }
        return "Pizza: $nombre | Precio: $$precio | Ingredientes: [$ingStr]"
    }
}

class Pedido(
    val fecha: String, // Registro por fecha para informes diarios (formato AAAA-MM-DD)
    val cliente: Cliente,
    val productos: List<Pair<Producto, Int>>, // Producto y cantidad
    var estado: String, // En preparación, Listo, En camino, Entregado
    val repartidor: Repartidor?
) {
    // Función para calcular totales con impuestos (IVA 19%) y descuentos (>3 pizzas)
    fun calcularTotales(): Map<String, Double> {
        var subtotal = 0.0
        var totalPizzas = 0

        for ((producto, cantidad) in productos) {
            subtotal += producto.precio * cantidad
            totalPizzas += cantidad
        }

        val descuento = if (totalPizzas > 3) subtotal * 0.10 else 0.0
        val subtotalConDescuento = subtotal - descuento
        val impuesto = subtotalConDescuento * 0.19
        val totalFinal = subtotalConDescuento + impuesto

        return mapOf(
            "subtotal" to subtotal,
            "descuento" to descuento,
            "impuesto" to impuesto,
            "totalFinal" to totalFinal
        )
    }
}

// ==========================================
// FUNCIONES DE VALIDACIÓN Y ENTRADA SEGURA
// ==========================================
fun leerCorreoValido(mensaje: String): String {
    print(mensaje)
    val correo = readlnOrNull()?.trim() ?: ""
    val esValido = correo.contains("@") && correo.contains(".") && correo.length > 5
    return if (esValido) correo else {
        println("⚠️ Correo no válido. Se asignará el valor predeterminado: correo@invalido.com")
        "correo@invalido.com"
    }
}

fun leerTelefonoValido(mensaje: String): String {
    print(mensaje)
    val telefono = readlnOrNull()?.trim() ?: ""
    val soloDigitos = telefono.filter { it.isDigit() }
    val esValido = soloDigitos.length >= 8 && soloDigitos.length == telefono.length
    return if (esValido) telefono else {
        println("⚠️ Teléfono no válido. Se asignará el valor predeterminado: (000) 000-0000")
        "(000) 000-0000"
    }
}

fun leerEnteroSeguro(mensaje: String, minimo: Int = 0): Int {
    while (true) {
        print(mensaje)
        try {
            val entrada = readlnOrNull()?.trim()?.toInt()
            if (entrada != null && entrada >= minimo) {
                return entrada
            }
            println("❌ Error: El valor debe ser mayor o igual a $minimo.")
        } catch (e: NumberFormatException) {
            println("❌ Error: Debe ingresar un número entero válido.")
        }
    }
}

// ==========================================
// FUNCIÓN PRINCIPAL Y MENÚ INTERACTIVO
// ==========================================
fun main() {
    // Inventario inicial en un Map
    val inventario = mutableMapOf(
        "carne" to 10,
        "salchicha" to 10,
        "salame" to 10,
        "queso" to 25,
        "piña" to 15,
        "jamon" to 15,
        "aceitunas" to 15
    )

    // Lista de repartidores disponibles
    val repartidoresDisponibles = listOf(
        Repartidor("Carlos Pérez", "912345678", "carlos@pizzeria.cl", "Zona Norte"),
        Repartidor("Ana Gómez", "987654321", "ana@pizzeria.cl", "Zona Centro"),
        Repartidor("Mateo Soto", "955554433", "mateo@pizzeria.cl", "Zona Sur")
    )

    // Catálogo con las 2 pizzas solicitadas (Carnes y Hawaiana)
    val catalogoPizzas = listOf(
        Producto(
            "Pizza de Carnes",
            9990.0,
            mapOf("carne" to 1, "salchicha" to 1, "salame" to 1, "queso" to 1)
        ),
        Producto(
            "Pizza Hawaiana",
            8990.0,
            mapOf("queso" to 1, "piña" to 1, "jamon" to 1, "aceitunas" to 1)
        )
    )

    // Arreglo/Lista mutable para almacenar los pedidos del día
    val historialPedidos = mutableListOf<Pedido>()

    // Bucle principal del menú
    while (true) {
        println("\n==================================================")
        println("          SISTEMA DE GESTIÓN - PIZZERIA")
        println("==================================================")
        println("[1] Ingresar nuevo pedido")
        println("[2] Revisión y gestión de stock de inventario")
        println("[3] Filtrar pedidos por estado y actualizar")
        println("[4] Generar reporte por día (Informe diario)")
        println("[5] Salir del sistema")
        print("Seleccione una opción: ")

        when (readlnOrNull()?.trim()) {
            "1" -> {
                println("\n--- NUEVO PEDIDO ---")
                print("Ingrese la fecha del pedido (AAAA-MM-DD o presione Enter para hoy): ")
                val fechaInput = readlnOrNull()?.trim()
                val fechaPedido = if (fechaInput.isNullOrEmpty()) LocalDate.now().toString() else fechaInput

                print("Ingrese el nombre del cliente: ")
                val nombreCliente = readlnOrNull()?.trim()?.takeIf { it.isNotEmpty() } ?: "Cliente General"

                val telefonoCliente = leerTelefonoValido("Ingrese el teléfono del cliente (solo números): ")
                val correoCliente = leerCorreoValido("Ingrese el correo electrónico del cliente: ")

                print("Ingrese la dirección de entrega: ")
                val direccionCliente = readlnOrNull()?.trim()?.takeIf { it.isNotEmpty() } ?: "Dirección por definir"

                // Manejo de nulos para notas opcionales
                print("Ingrese notas adicionales para el pedido (opcional): ")
                val notasInput = readlnOrNull()?.trim()
                val notasFinales = notasInput?.takeIf { it.isNotEmpty() } ?: "Sin notas"

                notasFinales.let {
                    if (it != "Sin notas") println("💡 [Nota registrada]: $it")
                }

                val cliente = Cliente(nombreCliente, telefonoCliente, correoCliente, direccionCliente, notasFinales)

                // Mostrar menú de pizzas
                println("\n--- Menú de Pizzas Disponibles ---")
                catalogoPizzas.forEachIndexed { index, p -> println("[${index + 1}] ${p.detallarProducto()}") }

                val opcionPizza = leerEnteroSeguro("\nSeleccione la pizza (1: Carnes, 2: Hawaiana): ", 1)
                val pizzaSeleccionada = if (opcionPizza == 1) catalogoPizzas[0] else catalogoPizzas[1]

                val cantidadPizzas = leerEnteroSeguro("Ingrese la cantidad de pizzas: ", 1)

                // ==========================================
                // VERIFICACIÓN Y REABASTECIMIENTO DE INVENTARIO
                // ==========================================
                var ingredientesValidos = false

                while (!ingredientesValidos) {
                    var faltanIngredientes = false
                    val faltantes = mutableMapOf<String, Pair<Int, Int>>() // ingrediente -> (necesario, disponible)

                    for ((ingrediente, reqUnidad) in pizzaSeleccionada.ingredientesNecesarios) {
                        val necesarioTotal = reqUnidad * cantidadPizzas
                        val disponible = inventario[ingrediente] ?: 0

                        if (disponible < necesarioTotal) {
                            faltanIngredientes = true
                            faltantes[ingrediente] = Pair(necesarioTotal, disponible)
                        }
                    }

                    if (faltanIngredientes) {
                        println("\n⚠️ ¡Atención! Stock insuficiente detectado:")
                        for ((ing, par) in faltantes) {
                            println("   • Ingrediente '$ing': Se necesitan ${par.first} unidades, pero quedan ${par.second} en inventario.")

                            print("   ¿Desea reabastecer '$ing'? (s/n): ")
                            val respuestaCompra = readlnOrNull()?.trim()?.lowercase()
                            if (respuestaCompra == "s") {
                                val cantidadAComprar = leerEnteroSeguro("   Ingrese cuántas unidades más comprar de '$ing': ", 1)
                                val stockActual = inventario[ing] ?: 0
                                inventario[ing] = stockActual + cantidadAComprar
                                println("   ✅ Se han repuesto $cantidadAComprar unidades de '$ing'. Compra realizada con éxito.\n")
                            } else {
                                println("   ❌ No se reabasteció '$ing'. No se puede avanzar con el pedido.")
                            }
                        }
                    } else {
                        // Descontar automáticamente del inventario
                        for ((ingrediente, reqUnidad) in pizzaSeleccionada.ingredientesNecesarios) {
                            val necesarioTotal = reqUnidad * cantidadPizzas
                            inventario[ingrediente] = inventario[ingrediente]!! - necesarioTotal
                        }
                        ingredientesValidos = true
                    }
                }

                // Asignar repartidor usando find()
                val repartidorAsignado = repartidoresDisponibles.find { it.zona == "Zona Centro" } ?: repartidoresDisponibles.first()
                val estadoInicial = "En preparación"

                val pedido = Pedido(
                    fecha = fechaPedido,
                    cliente = cliente,
                    productos = listOf(Pair(pizzaSeleccionada, cantidadPizzas)),
                    estado = estadoInicial,
                    repartidor = repartidorAsignado
                )

                historialPedidos.add(pedido)

                // Resumen del pedido generado
                val totales = pedido.calcularTotales()
                println("\n==================================================")
                println("        PEDIDO REGISTRADO EXITOSAMENTE")
                println("==================================================")
                println("📅 Fecha: ${pedido.fecha}")
                println("👤 Cliente: ${cliente.nombre} | Tel: ${cliente.contacto}")
                println("🍕 Producto: ${pizzaSeleccionada.nombre} x $cantidadPizzas")
                println("💵 Subtotal: $${round(totales["subtotal"]!!)}")
                println("🎁 Descuento (>3 pizzas): $${round(totales["descuento"]!!)}")
                println("🧾 IVA (19%): $${round(totales["impuesto"]!!)}")
                println("💰 Total Final: $${round(totales["totalFinal"]!!)}")
                println("📌 Estado: ${pedido.estado}")
                println("🛵 Repartidor asignado: ${repartidorAsignado.nombre}")
                println("==================================================")
            }
            "2" -> {
                println("\n--- INVENTARIO ACTUAL DE INGREDIENTES ---")
                for ((ingrediente, cantidad) in inventario) {
                    println("   • $ingrediente: $cantidad unidades disponibles")
                }
                print("\n¿Desea reabastecer algún ingrediente manualmente? (s/n): ")
                if (readlnOrNull()?.trim()?.lowercase() == "s") {
                    print("Ingrese el nombre exacto del ingrediente a reabastecer: ")
                    val ingBuscado = readlnOrNull()?.trim() ?: ""
                    if (inventario.containsKey(ingBuscado)) {
                        val extra = leerEnteroSeguro("Ingrese la cantidad a comprar: ", 1)
                        inventario[ingBuscado] = inventario[ingBuscado]!! + extra
                        println("✅ Se han repuesto $extra unidades de '$ingBuscado'. Stock actual: ${inventario[ingBuscado]}")
                    } else {
                        println("❌ El ingrediente no existe en el inventario.")
                    }
                }
            }
            "3" -> {
                println("\n--- FILTRAR ESTADO DE PEDIDOS ---")
                println("[1] En preparación")
                println("[2] Listo")
                println("[3] En camino")
                println("[4] Entregado")
                println("[5] Ver todos los pedidos")
                print("Elija el filtro de estado (1-5): ")

                val filtroOpcion = readlnOrNull()?.trim()
                val estadoBuscado = when (filtroOpcion) {
                    "1" -> "En preparación"
                    "2" -> "Listo"
                    "3" -> "En camino"
                    "4" -> "Entregado"
                    "5" -> "TODOS"
                    else -> null
                }

                if (estadoBuscado == null) {
                    println("❌ Opción no válida.")
                } else if (historialPedidos.isEmpty()) {
                    println("ℹ️ No hay pedidos registrados todavía.")
                } else {
                    val pedidosFiltrados = if (estadoBuscado == "TODOS") {
                        historialPedidos
                    } else {
                        historialPedidos.filter { it.estado.equals(estadoBuscado, ignoreCase = true) }
                    }

                    println("\n--- REPORTE DE PEDIDOS ($estadoBuscado) ---")
                    if (pedidosFiltrados.isEmpty()) {
                        println("ℹ️ No se encontraron pedidos con este estado.")
                    } else {
                        pedidosFiltrados.forEachIndexed { idx, p ->
                            val tot = p.calcularTotales()["totalFinal"]!!
                            println("[$idx] Fecha: ${p.fecha} | Cliente: ${p.cliente.nombre} | Estado: ${p.estado} | Total: $$tot")
                        }

                        // RESTAURADO: Opción para cambiar el estado de los pedidos filtrados
                        print("\n¿Desea cambiar el estado de alguno de estos pedidos? (s/n): ")
                        if (readlnOrNull()?.trim()?.lowercase() == "s") {
                            val indexPedido = leerEnteroSeguro("Ingrese el índice [0-${pedidosFiltrados.size - 1}] del pedido: ", 0)
                            if (indexPedido < pedidosFiltrados.size) {
                                val pSeleccionado = pedidosFiltrados[indexPedido]
                                println("Estado actual: ${pSeleccionado.estado}")
                                println("Seleccione nuevo estado: [1] En preparación | [2] Listo | [3] En camino | [4] Entregado")
                                val nuevoEstOpc = readlnOrNull()?.trim()
                                pSeleccionado.estado = when (nuevoEstOpc) {
                                    "1" -> "En preparación"
                                    "2" -> "Listo"
                                    "3" -> "En camino"
                                    "4" -> "Entregado"
                                    else -> pSeleccionado.estado
                                }
                                println("✅ ¡Estado actualizado exitosamente a '${pSeleccionado.estado}'!")
                            } else {
                                println("❌ Índice fuera de rango.")
                            }
                        }
                    }
                }
            }
            "4" -> {
                // ==========================================
                // REPORTE DIARIO DE PEDIDOS (Uso de ciclo for)
                // ==========================================
                println("\n--- REPORTE POR DÍA ---")
                print("Ingrese la fecha a consultar (AAAA-MM-DD o presione Enter para la fecha de hoy): ")
                val fechaConsultaInput = readlnOrNull()?.trim()
                val fechaConsultada = if (fechaConsultaInput.isNullOrEmpty()) LocalDate.now().toString() else fechaConsultaInput

                if (historialPedidos.isEmpty()) {
                    println("ℹ️ No hay pedidos registrados en el sistema.")
                } else {
                    println("\n==================================================")
                    println("       REPORTE DE PEDIDOS PARA LA FECHA: $fechaConsultada")
                    println("==================================================")

                    var contadorPedidosDia = 0
                    var recaudacionTotalDia = 0.0

                    // Ciclo for para recorrer y generar el reporte requerido
                    for (pedido in historialPedidos) {
                        if (pedido.fecha == fechaConsultada) {
                            contadorPedidosDia++
                            val totales = pedido.calcularTotales()
                            recaudacionTotalDia += totales["totalFinal"]!!

                            println("Pedido #${contadorPedidosDia}:")
                            println("  • Cliente: ${pedido.cliente.nombre}")
                            println("  • Pizzas pedidas:")
                            for ((prod, cant) in pedido.productos) {
                                println("     - ${prod.nombre} x $cant unidades")
                            }
                            println("  • Total del pedido: $${round(totales["totalFinal"]!!)}")
                            println("  • Estado: ${pedido.estado}")
                            println("--------------------------------------------------")
                        }
                    }

                    if (contadorPedidosDia == 0) {
                        println("ℹ️ No se encontraron pedidos registrados para la fecha $fechaConsultada.")
                    } else {
                        println("📊 Total de pedidos en el día: $contadorPedidosDia")
                        println("💰 Recaudación total de la jornada: $$recaudacionTotalDia")
                    }
                    println("==================================================")
                }
            }
            "5" -> {
                println("👋 Saliendo del sistema de pizzería. ¡Hasta luego!")
                break
            }
            else -> {
                println("❌ Opción no válida, intente de nuevo.")
            }
        }
    }
}