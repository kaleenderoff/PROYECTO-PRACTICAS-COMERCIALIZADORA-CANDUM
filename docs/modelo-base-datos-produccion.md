# Modelo de base de datos de produccion

Este modelo reemplaza la logica operativa antigua sin borrar las migraciones historicas de Flyway. La nueva logica debe usar las tablas conectadas a los catalogos nuevos:

- `catalogo_linea`
- `catalogo_producto`
- `catalogo_sku`
- `turno`
- `usuario`
- `insumo`

## Decision de redisenio

Se mantiene la base tecnica del proyecto:

- arquitectura hexagonal
- Spring Boot
- Spring Security con JWT
- usuarios y roles
- excepciones globales
- DTOs, mappers, services, ports y adapters
- Flyway
- Docker y configuracion por perfiles

Se redisenia el nucleo del negocio. La nueva logica no debe usar como centro las tablas antiguas `produccion`, `detalle_produccion`, `consumo_insumo` ni `validacion`.

## Flujo principal nuevo

1. `programacion_produccion`
   Define que se va a producir por fecha, linea y turno.

2. `programacion_produccion_detalle`
   Lista los SKUs programados y sus cantidades. Representa el concepto de `ProgramacionSku`.

3. `orden_produccion`
   Convierte una programacion en una orden ejecutable para el jefe de linea.

4. `orden_produccion_detalle`
   Define cada SKU a producir dentro de la orden, su lote, formula y vencimiento.

5. `ejecucion_produccion`
   Guarda el inicio, pausas, cierre y estado operativo de la orden.

6. `lote_produccion`
   Identifica los lotes producidos por SKU.

7. `medicion_bache`
   Guarda mediciones reales por bache: kilos, temperatura, pH, grados brix y tiempos.

8. `produccion_real`
   Consolida lo producido en la ejecucion completa.

9. `produccion_real_sku`
   Guarda lo realmente producido: kilos, unidades, cajas, tiempos y rendimiento.

10. `validacion_produccion`
   Permite validaciones de calidad, produccion e inventario sobre cada detalle de orden.

11. `novedad_produccion`
   Registra paradas, mermas, problemas de calidad, equipo, personal o material.

## Insumos e inventario

- `insumo`
  Catalogo de materias primas, empaques, aditivos u otros insumos.

- `inventario_insumo`
  Existencias por insumo, lote, vencimiento, ubicacion y estado.

- `movimiento_inventario_insumo`
  Kardex de entradas, salidas, ajustes, bloqueos y liberaciones.

- `formula`
  Formula principal asociada a un SKU.

- `formula_version`
  Versiones controladas de una formula.

- `formula_detalle`
  Insumos requeridos por version de formula.

- `registro_insumo`
  Consumo real de insumos durante la ejecucion. Puede vincular lote de inventario y movimiento de kardex.

- `consumo_insumo_orden`
  Tabla creada en V11. Puede mantenerse como soporte tecnico, pero el lenguaje nuevo del negocio debe priorizar `registro_insumo`.

## Nucleo lacteos

- `recepcion_leche`
  Registra recepcion de leche por proveedor, litros, temperatura, acidez, densidad y estado.

- `descremado`
  Registra el proceso de descremado dentro de una ejecucion de produccion.

- `descremado_recepcion`
  Relaciona las recepciones de leche usadas en un proceso de descremado.

- `subproducto_produccion`
  Guarda subproductos como crema, leche descremada, suero o merma, con cantidad y destino.

## Criterio de escalabilidad

El piloto inicia con `Lacteos`, pero las tablas no estan amarradas a lacteos directamente. La linea se controla con `catalogo_linea`, por eso el mismo modelo puede crecer luego a mayonesa, salsas, aceite, margarina, azucar u otras lineas.

## Tablas antiguas

Las tablas antiguas como `produccion`, `detalle_produccion`, `producto`, `linea_produccion`, `producto_terminado`, `empaque`, `validacion` y `consumo_insumo` quedan como parte de la historia de Flyway. La nueva logica del backend no debe conectarse a esas tablas.

## Mapa practico

Se queda:

- `usuario`
- `log_auditoria`
- `catalogo_linea`
- `catalogo_producto`
- `catalogo_sku`
- `marca`
- `proveedor`
- `turno`
- `insumo`
- `inventario_insumo`
- `movimiento_inventario_insumo`

Se congela como legado:

- `linea_produccion`
- `producto`
- `producto_terminado`
- `produccion`
- `detalle_produccion`
- `empaque`
- `validacion`
- `consumo_insumo`

Se crea como nucleo nuevo:

- `programacion_produccion`
- `programacion_produccion_detalle`
- `orden_produccion`
- `orden_produccion_detalle`
- `ejecucion_produccion`
- `lote_produccion`
- `medicion_bache`
- `registro_insumo`
- `novedad_produccion`
- `produccion_real`
- `produccion_real_sku`
- `validacion_produccion`
- `formula`
- `formula_version`
- `formula_detalle`
- `recepcion_leche`
- `descremado`
- `descremado_recepcion`
- `subproducto_produccion`
