package io.github.felipeboaventura.REST.controller;

import io.github.felipeboaventura.REST.ApiErros;
import io.github.felipeboaventura.exception.PedidoNaoEncontradoException;
import io.github.felipeboaventura.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleRegrasNegocioException(RegraNegocioException ex) {
        String messageErro = ex.getMessage();
        return new ApiErros(messageErro);
    }

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErros handlePedidoNotFoundException(PedidoNaoEncontradoException ex) {
        return new ApiErros(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleMethodNotValidException(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErros(erros);
    }
}
