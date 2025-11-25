package com.naapi.naapi.controllers.handlers;

import com.naapi.naapi.services.exceptions.BusinessException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException; // Import adicionado
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.List;
import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Recurso não encontrado")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> businessException(BusinessException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Violação de regra de negócio")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; // Código 422
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Erro de validação");
        err.setMessage("Dados inválidos enviados na requisição.");
        err.setPath(request.getRequestURI());

        // Pega cada erro de campo da exceção e adiciona na nossa lista personalizada
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError f : fieldErrors) {
            err.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(err);
    }

    // --- NOVO MÉTODO PARA TRATAR ERROS DE INTEGRIDADE DO BANCO ---
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT; // 409 Conflict
        String message = "Conflito de dados no banco.";

        // Tenta pegar a mensagem mais específica do erro (ex: do PostgreSQL)
        String rootMsg = e.getMostSpecificCause().getMessage();

        if (rootMsg != null) {
            // Caso 1: Duplicidade (Matrícula, CPF, etc.)
            if (rootMsg.contains("duplicate key") || rootMsg.contains("unique constraint")) {
                message = "Já existe um registro com este dado único (Matrícula, CPF, Nome ou Email).";
            } 
            // Caso 2: Violação de Chave Estrangeira (Tentar excluir algo em uso)
            else if (rootMsg.contains("foreign key") || rootMsg.contains("constraint")) {
                message = "Não é possível excluir este item pois ele está vinculado a outros registros (ex: Alunos ou Atendimentos que dependem dele).";
            }
        }

        StandardError err = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Integridade de Dados")
                .message(message)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(err);
    }
}