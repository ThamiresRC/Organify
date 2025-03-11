package br.com.fiap.calendario.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.calendario.model.Calendar;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private Logger log = LoggerFactory.getLogger(getClass());
 
    private List<Calendar> repository = new ArrayList<>();
    
    //Listar todos os eventos
    //Get localhost:8080/calendar
    
    @GetMapping
    public List<Calendar> index(){
        return repository;
    }

    //Cadastrar um evento
   
    
    @PostMapping
    public ResponseEntity<Calendar> create(@RequestBody Calendar calendar){
        repository.add(calendar);
        log.info("Cadastrando evento: " + calendar.getName());
        return ResponseEntity.status(201).body(calendar);
    }

    //Buscar evento

    @GetMapping("{id}")
    public Calendar get(@PathVariable Long id){
        log.info("Buscando evento " + id);
        return getCalendar(id);
    }

    
    private Calendar getCalendar(long id) {
        return repository.stream()
        .filter(c -> c.getId().equals(id))
        .findFirst()
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento " + id + " não encontrado" )
        );
    }
}
