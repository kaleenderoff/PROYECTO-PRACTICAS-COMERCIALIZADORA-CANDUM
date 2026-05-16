export interface DetalleSimulacionSkuResponse {

    idSku: number;

    codigoSku: string;

    descripcionSku: string;

    pesoNetoGr: number;

    unidades: number;

    kilosProductoTerminado: number;

    kgBatchNecesario: number;

    numeroBatch: number;
}

export interface SimularProgramacionResponse {

    idProducto: number;

    producto: string;

    totalKgProductoTerminado: number;

    totalKgBatch: number;

    totalBatches: number;

    detalle: DetalleSimulacionSkuResponse[];
}
