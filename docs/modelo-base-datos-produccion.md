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

Desde `V13__alinear_modelo_oficial_mvp.sql`, el modelo oficial del MVP queda definido como:

- sistema de produccion
- registro de consumo real por ejecucion
- reporte/exportacion de consumo

El sistema no debe comportarse como inventario oficial. El inventario formal, kardex, reservas, costos y saldos quedan en el software externo de inventario.

Desde `V14__cerrar_modelo_mvp_sin_legacy.sql`, las tablas legacy y las tablas de inventario operativo se retiran del esquema activo. La base queda enfocada en produccion, trazabilidad de ejecucion y consumo reportado.

Desde `V15__ajustes_finales_modelo_mvp.sql`, se cierran los ajustes finales antes de congelar el modelo MVP: reglas `NOT NULL`, indices utiles, unicidad por SKU producido, validacion fechada y eliminacion de redundancias menores como `ejecucion_produccion.id_turno`.

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
   Identifica los lotes producidos por SKU.

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

- `inventario_insumo`
  Retirada del esquema activo en V14. El MVP no administra stock.

- `movimiento_inventario_insumo`
  Retirada del esquema activo en V14. El MVP no administra kardex.

- `formula`
  Formula principal asociada a un producto base de `catalogo_producto`, no a una presentacion/SKU.

- `formula_version`
  Versiones controladas de una formula.

- `formula_detalle`
  Insumos requeridos por version de formula.

- `registro_insumo`
  Consumo real de insumos durante la ejecucion. Guarda lote de insumo, cantidad usada, unidad, responsable y observaciones. Es la base para reportes de consumo.

- `consumo_insumo_orden`
  Retirada del esquema activo en V14. El MVP reporta consumo con `registro_insumo`.

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

Se congela como legado:

- `linea_produccion`
- `producto`
- `producto_terminado`
- `produccion`
- `detalle_produccion`
- `empaque`
- `inventario_insumo`
- `movimiento_inventario_insumo`
- `receta_sku`
- `receta_sku_detalle`
- `consumo_insumo_orden`
- `validacion`
- `consumo_insumo`

Estas tablas quedan retiradas del esquema activo desde V14. No deben tener entidades, repositorios ni endpoints nuevos.

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
