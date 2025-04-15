package br.com.fiap.calendario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.calendario.model.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}