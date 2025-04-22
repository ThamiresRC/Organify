package br.com.fiap.calendario.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.calendario.model.CalendarEvent;
import br.com.fiap.calendario.repository.CalendarEventRepository;
import br.com.fiap.calendario.specification.CalendarEventSpecification;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class CalendarEventController {

    @Autowired
    private CalendarEventRepository repository;

    @GetMapping("/search")
    public Page<CalendarEvent> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Specification<CalendarEvent> spec = Specification
                .where(CalendarEventSpecification.nameContains(name))
                .and(CalendarEventSpecification.typeEquals(type))
                .and(CalendarEventSpecification.startDateAfter(start))
                .and(CalendarEventSpecification.endDateBefore(end));

        Sort sorting = Sort.by(Sort.Order.by(sort[0]));
        if (sort.length > 1 && sort[1].equalsIgnoreCase("desc")) {
            sorting = sorting.descending();
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        return repository.findAll(spec, pageable);
    }

    @GetMapping("/{id}")
    public CalendarEvent getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CalendarEvent create(@Valid @RequestBody CalendarEvent event) {
        return repository.save(event);
    }

    @PutMapping("/{id}")
    public CalendarEvent update(@PathVariable Long id, @Valid @RequestBody CalendarEvent updated) {
        CalendarEvent event = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));

        event.setName(updated.getName());
        event.setType(updated.getType());
        event.setStart(updated.getStart());
        event.setEndTime(updated.getEndTime());

        return repository.save(event);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        CalendarEvent event = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));
        repository.delete(event);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        repository.deleteAll();
    }
}
