# Modelo de base de datos de produccion

Este modelo reemplaza la logica operativa antigua sin borrar las migraciones historicas de Flyway. La nueva logica debe usar las tablas conectadas a los catalogos nuevos:

- `catalogo_linea`
- `catalogo_producto`
- `catalogo_sku`
- `turno`
- `usuario`
- `insumo`

## Flujo principal

1. `programacion_produccion`
   Define que se va a producir por fecha, linea y turno.

2. `programacion_produccion_detalle`
   Lista los SKUs programados y sus cantidades.

3. `orden_produccion`
   Convierte una programacion en una orden ejecutable para el jefe de linea.

4. `orden_produccion_detalle`
   Define cada SKU a producir dentro de la orden, su lote, receta y vencimiento.

5. `produccion_real_sku`
   Guarda lo realmente producido: kilos, unidades, cajas, tiempos y rendimiento.

6. `validacion_produccion`
   Permite validaciones de calidad, produccion e inventario sobre cada detalle de orden.

## Insumos e inventario

- `insumo`
  Catalogo de materias primas, empaques, aditivos u otros insumos.

- `inventario_insumo`
  Existencias por insumo, lote, vencimiento, ubicacion y estado.

- `movimiento_inventario_insumo`
  Kardex de entradas, salidas, ajustes, bloqueos y liberaciones.

- `receta_sku`
  Versiones de receta por SKU.

- `receta_sku_detalle`
  Insumos requeridos por SKU y cantidad por unidad.

- `consumo_insumo_orden`
  Consumo real de insumos por detalle de orden. Puede vincularse al lote de inventario usado.

## Criterio de escalabilidad

El piloto inicia con `Lacteos`, pero las tablas no estan amarradas a lacteos directamente. La linea se controla con `catalogo_linea`, por eso el mismo modelo puede crecer luego a mayonesa, salsas, aceite, margarina, azucar u otras lineas.

## Tablas antiguas

Las tablas antiguas como `produccion`, `detalle_produccion`, `producto`, `linea_produccion`, `producto_terminado`, `empaque`, `validacion` y `consumo_insumo` quedan como parte de la historia de Flyway. La nueva logica del backend no debe conectarse a esas tablas.
