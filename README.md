﻿# PagueloFacilChallenge

## El proyecto cuenta a nivel UI/UX con:
- Un campo para realizar una búsqueda de una transacción por código de operación, se consulta contra la API aquella que coincida.
- En caso de realizar la búsqueda con el campo vacío comienza el paginado de transacciones, el tamaño de la página es de 5 items a la vez a medida que se scrollea hacia abajo.
- La posibilidad de filtrar los resultados por medio de pago (visa, mastercard, efectivo, clave), y por orden ascendente o descendente según la fecha.Cada ítem puede expandirse para brindar más detalles relevantes sobre la transacción, como el estado, el monto, el medio de pago o la referencia.
- Ícono, paleta de colores y navegación inferior inspirados en la app original

## El proyecto cuenta a nivel código/técnico con:
- Arquitectura Clean con patrón MVVM en Kotlin.
- Arquitectura Clean multi modular del tipo "layered-feature".
- Remote Config para evitar exponer el access token en el proyecto.
- Uso de Material & Navigation Components.String resources internacionalizados.
