package com.api.algamoneyapi.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.api.algamoneyapi.algamoney.api.model.Categoria;
import com.api.algamoneyapi.algamoney.api.repository.CategoriaRepository;


@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

    private static final Logger log =  LoggerFactory.getLogger(CategoriaResource.class);

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllCategorias(){
        List<Categoria> lista = this.categoriaRepository.findAll();

        return !lista.isEmpty() ? ResponseEntity.ok(lista) : ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveCategory(@Valid @RequestBody Categoria categoria, HttpServletResponse response){
        Categoria categoriaSalva = this.categoriaRepository.save(categoria);
        
        publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCategoryId(@PathVariable("id") Long id){

        Optional<Categoria> categoria = this.categoriaRepository.findById(id); // findById().orElse(null);
        return categoria.isPresent() ? ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();
    }
}
