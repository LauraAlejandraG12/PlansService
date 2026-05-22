package com.plan.qv_ms_plans.exception;


import org.springframework.http.HttpStatus;

/**
 * Excepción personalizada para errores de lógica de negocio del sistema.
 *
 * <p>Se lanza cuando se viola una regla de negocio, como intentar
 * eliminar un plan asignado o asignar un plan inactivo. Incluye
 * el código HTTP correspondiente para retornarlo al cliente.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
public class BusinessException extends RuntimeException {

    /**
     * Código HTTP asociado al error de negocio.
     */
    private final HttpStatus status;

    /**
     * Crea una nueva excepción de negocio con mensaje y código HTTP.
     *
     * @param message mensaje descriptivo del error
     * @param status código HTTP a retornar al cliente
     */
    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    /**
     * Retorna el código HTTP asociado al error.
     *
     * @return código HTTP del error
     */
    public HttpStatus getStatus() {
        return status;
    }
}
