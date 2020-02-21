package com.api.algamoneyapi.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.api.algamoneyapi.algamoney.api.model.Pessoa;
import com.api.algamoneyapi.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	PessoaRepository pessoaRepository;
	
	public Pessoa atualizarPessoa(Long id, Pessoa pessoa) {
		Pessoa pessoaSalva = findUserForId(id);
    	BeanUtils.copyProperties(pessoa, pessoaSalva, "id"); // copia as properties
    	return pessoaRepository.save(pessoaSalva);
	}

	public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
		
		Pessoa pessoaSalva = this.findUserForId(id);
		pessoaSalva.setAtivo(ativo);
		this.pessoaRepository.save(pessoaSalva);
	}
	
	private Pessoa findUserForId(Long id) {
		Pessoa pessoaSalva = this.pessoaRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		return pessoaSalva;
	}
}
