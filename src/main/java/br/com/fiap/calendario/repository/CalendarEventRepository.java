package br.com.fiap.calendario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.fiap.calendario.model.CalendarEvent;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long>, JpaSpecificationExecutor<CalendarEvent> {
}
