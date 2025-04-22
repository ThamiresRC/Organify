package br.com.fiap.calendario.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.fiap.calendario.model.CalendarEvent;
import br.com.fiap.calendario.repository.CalendarEventRepository;
import br.com.fiap.calendario.specification.CalendarEventSpecification;

@Service
public class CalendarEventService {

    @Autowired
    private CalendarEventRepository repository;

    public Page<CalendarEvent> findAll(String name, String type, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        Specification<CalendarEvent> spec = Specification.where(
            CalendarEventSpecification.nameContains(name))
            .and(CalendarEventSpecification.typeEquals(type))
            .and(CalendarEventSpecification.startDateAfter(start))
            .and(CalendarEventSpecification.endDateBefore(end));
        return repository.findAll(spec, pageable);
    }

    public Optional<CalendarEvent> findById(Long id) {
        return repository.findById(id);
    }

    public CalendarEvent save(CalendarEvent event) {
        return repository.save(event);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
