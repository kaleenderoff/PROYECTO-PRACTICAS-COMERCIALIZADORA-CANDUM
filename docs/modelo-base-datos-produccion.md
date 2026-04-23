# Modelo de base de datos de produccion

Este modelo reemplaza la logica operativa antigua. Como el proyecto todavia esta en etapa de desarrollo, las migraciones se compactaron en una secuencia limpia y reconstruible desde cero. La nueva logica debe usar las tablas conectadas a los catalogos nuevos:

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

Se redisenia el nucleo del negocio. La nueva logica no crea ni usa como centro las tablas antiguas `produccion`, `detalle_produccion`, `consumo_insumo`, `validacion`, `producto`, `producto_terminado`, `linea_produccion` ni `empaque`.

La secuencia oficial de Flyway queda ordenada de `V1` a `V18` por bloques funcionales:

- `V1__crear_usuarios_y_auditoria.sql`
- `V2__crear_turno.sql`
- `V3__crear_catalogos_base.sql`
- `V4__crear_catalogo_producto.sql`
- `V5__crear_catalogo_sku.sql`
- `V6__crear_insumo.sql`
- `V7__crear_formula_y_version.sql`
- `V8__crear_formula_detalle.sql`
- `V9__crear_programacion_produccion.sql`
- `V10__crear_programacion_sku.sql`
- `V11__crear_orden_produccion.sql`
- `V12__crear_orden_produccion_detalle.sql`
- `V13__crear_ejecucion_produccion.sql`
- `V14__crear_medicion_bache_y_novedad.sql`
- `V15__crear_registro_insumo.sql`
- `V16__crear_lote_y_produccion_real.sql`
- `V17__crear_validacion_produccion.sql`
- `V18__crear_lacteos_recepcion_descremado.sql`

El modelo oficial del MVP queda definido como:

- sistema de produccion
- registro de consumo real por ejecucion
- reporte/exportacion de consumo

El sistema no debe comportarse como inventario oficial. El inventario formal, kardex, reservas, costos y saldos quedan en el software externo de inventario.

La base queda enfocada en produccion, trazabilidad de ejecucion y consumo reportado. El modelo logico queda congelado para empezar entidades JPA y servicios: toda version de formula exige `id_creado_por` y todo lote exige `id_ejecucion`.

## Flujo principal nuevo

1. `programacion_produccion`
   Define que se va a producir por fecha, linea y turno.

2. `programacion_sku`
   Lista los SKUs programados y sus unidades objetivo. Representa el detalle planeado de la programacion.

3. `orden_produccion`
   Convierte una programacion en una orden ejecutable para el jefe de linea.

4. `orden_produccion_detalle`
   Define cada SKU a producir dentro de la orden y su cantidad programada. La trazabilidad de lote queda en el resultado real.

5. `ejecucion_produccion`
   Guarda el inicio, pausas, cierre y estado operativo de la orden.

6. `lote_produccion`
   Identifica los lotes producidos por SKU. Todo lote debe quedar ligado a una ejecucion real.

7. `medicion_bache`
   Guarda mediciones reales por bache: kilos, temperatura, pH, grados brix y tiempos.

8. `produccion_real`
   Consolida lo producido en la ejecucion completa.

9. `produccion_real_sku`
   Guarda lo realmente producido: kilos, unidades, cajas, tiempos y rendimiento.

10. `validacion_produccion`
   Registra la validacion formal de Astrid sobre la ejecucion y la produccion real.

11. `novedad_produccion`
   Registra paradas, mermas, problemas de calidad, equipo, personal o material.

## Insumos e inventario

- `insumo`
  Catalogo de materias primas, empaques, aditivos u otros insumos.

- `formula`
  Formula principal asociada a un producto base de `catalogo_producto`, no a una presentacion/SKU.

- `formula_version`
  Versiones controladas de una formula. Toda version debe registrar el usuario que la crea.

- `formula_detalle`
  Insumos requeridos por version de formula.

- `registro_insumo`
  Consumo real de insumos durante la ejecucion. Guarda lote de insumo, cantidad usada, unidad, responsable y observaciones. Es la base para reportes de consumo.

## Nucleo lacteos

- `recepcion_leche`
  Registra recepcion de leche por proveedor, litros segun remision, litros reales, pesos, grasa, temperatura, placa, conductor y hora de llegada.

- `descremado`
  Registra el proceso de descremado como flujo independiente de la ejecucion de produccion.

- `descremado_recepcion`
  Relaciona las recepciones de leche usadas en un proceso de descremado.

- `subproducto_produccion`
  Guarda subproductos derivados del descremado, como crema u otros resultados asociados al proceso.

## Criterio de escalabilidad

El piloto inicia con `Lacteos`, pero las tablas no estan amarradas a lacteos directamente. La linea se controla con `catalogo_linea`, por eso el mismo modelo puede crecer luego a mayonesa, salsas, aceite, margarina, azucar u otras lineas.

## Tablas antiguas

Las tablas antiguas como `produccion`, `detalle_produccion`, `producto`, `linea_produccion`, `producto_terminado`, `empaque`, `validacion` y `consumo_insumo` no forman parte de la secuencia limpia de Flyway. La nueva logica del backend no debe conectarse a esas tablas.

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

Se crea como nucleo nuevo:

- `programacion_produccion`
- `programacion_sku`
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
