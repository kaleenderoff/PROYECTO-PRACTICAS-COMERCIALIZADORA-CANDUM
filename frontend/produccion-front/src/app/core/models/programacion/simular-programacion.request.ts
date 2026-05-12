export interface SimularProgramacionRequest {
    idProducto: number;
    skus: SimularSkuRequest[];
}

export interface SimularSkuRequest {
    idSku: number;
    unidades: number;
}