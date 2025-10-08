package com.project.user.business;

import com.project.user.infrastructure.clients.ViaCepClient;
import com.project.user.infrastructure.clients.ViaCepDTO;
import com.project.user.infrastructure.exception.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor

public class ViaCepService {

    private final ViaCepClient client;

    public ViaCepDTO buscarDadosEndereco(String cep) {
        return client.buscaDadosEndereco(processarCep(cep));
    }

//    public ViaCepDTO buscarDadosEndereco(String cep) {
//        try {
//            return client.buscaDadosEndereco(processarCep(cep));
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("Erro: ", e);
//        }
//        catch (Exception e) {
//            throw new ErroInternoException()
//        }
//    }

    private String processarCep(String cep) {
        String cepFormatado = cep.replace(" ", "").
                replace("-", "");

        if (!cepFormatado.matches("\\d+")
                || !Objects.equals(cepFormatado.length(), 8)) {
            throw new IllegalArgumentException("O CEP contém caracteres inválidos, favor verificar.");
        }

        return cepFormatado;
    }
}
