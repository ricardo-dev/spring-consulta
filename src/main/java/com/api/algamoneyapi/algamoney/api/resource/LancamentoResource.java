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
import org.springframework.web.bind.annotation.RestController;

import com.api.algamoneyapi.algamoney.api.event.RecursoCriadoEvent;
import com.api.algamoneyapi.algamoney.api.model.Lancamento;
import com.api.algamoneyapi.algamoney.api.repository.LancamentoRepository;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
    private ApplicationEventPublisher publisher;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllLancamentos(){
		List<Lancamento> lancamentos = this.lancamentoRepository.findAll();
		return !lancamentos.isEmpty() ? ResponseEntity.ok(lancamentos) : ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getLancamentoById(@PathVariable("id") Long id){
		Optional<Lancamento> lancamentoSalvo = this.lancamentoRepository.findById(id);
		return lancamentoSalvo.isPresent() ? ResponseEntity.ok(lancamentoSalvo.get()) : ResponseEntity.notFound().build();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> inserirLancamento(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response ){
		Lancamento lancamentoSalvo = this.lancamentoRepository.save(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamento.getCodigo() ) );
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

}
