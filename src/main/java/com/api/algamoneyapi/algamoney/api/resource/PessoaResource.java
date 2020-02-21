package com.api.algamoneyapi.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.algamoneyapi.algamoney.api.event.RecursoCriadoEvent;
import com.api.algamoneyapi.algamoney.api.model.Pessoa;
import com.api.algamoneyapi.algamoney.api.repository.PessoaRepository;
import com.api.algamoneyapi.algamoney.api.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    @Autowired
    private PessoaRepository pessoaRepository;
    
    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getUsers(){
        List<Pessoa> pessoas = this.pessoaRepository.findAll();
        return !pessoas.isEmpty() ? ResponseEntity.ok(pessoas) : ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> savePessoa(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response){

        Pessoa pessoaSalva = this.pessoaRepository.save(pessoa);
        
        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers(@PathVariable("id") Long id){
        Optional<Pessoa> pessoa = this.pessoaRepository.findById(id);

        return pessoa.isPresent() ? ResponseEntity.ok(pessoa.get()) : ResponseEntity.notFound().build();
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE )
    @ResponseStatus(HttpStatus.NO_CONTENT) // pelo fato de não ter retorno - 204
    public void deleteUser(@PathVariable("id") Long id) {
    	pessoaRepository.deleteById(id);
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public ResponseEntity<Pessoa> update(@PathVariable("id") Long id, @Valid @RequestBody Pessoa pessoa){
    	
    	Pessoa pessoaSalva = this.pessoaService.atualizarPessoa(id, pessoa);
    	return ResponseEntity.ok(pessoaSalva);
    }
    
    @RequestMapping(value="/{id}/ativo", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarPropriedadeAtivo(@PathVariable Long id, @RequestBody Boolean ativo) {
    	this.pessoaService.atualizarPropriedadeAtivo(id, ativo);
    }
}