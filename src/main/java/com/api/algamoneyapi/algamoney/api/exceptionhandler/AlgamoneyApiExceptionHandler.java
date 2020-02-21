package com.api.algamoneyapi.algamoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class AlgamoneyApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    // erro quando mensagem de erro não tem leitura
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String body = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());

        String messageDevelop = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

        List<Erro> erros = Arrays.asList(new Erro(body, messageDevelop));
        return handleExceptionInternal( ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    // erro quando os argumentos nao estao validos
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Erro> erros = this.criarListaErros(ex.getBindingResult());
        return handleExceptionInternal( ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }
    
    // Excpetion personalizada
    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException (EmptyResultDataAccessException ex, WebRequest request) {
    	
    	String body = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());

        String messageDevelop = ex.toString();

        List<Erro> erros = Arrays.asList(new Erro(body, messageDevelop));
    	return handleExceptionInternal( ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // capturando lista de erros
    private List<Erro> criarListaErros(BindingResult result) {
        List<Erro> erros = new ArrayList<>();

        for(FieldError fieldError: result.getFieldErrors()){
            String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            String mensagemDesenvolvedor = fieldError.toString();
            erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
        }

        return erros;
    }

    // class erro para encapsular
    public static class Erro {
        private String mensagemUsuario;
        private String mensagemDesenvolvedor;

        public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
            this.mensagemDesenvolvedor = mensagemDesenvolvedor;
            this.mensagemUsuario = mensagemUsuario;
        }
        public String getMensagemUsuario() {
            return mensagemUsuario;
        }
        public String getMensagemDesenvolvedor() {
            return mensagemDesenvolvedor;
        }
    }
}
