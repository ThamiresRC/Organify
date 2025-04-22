package br.com.fiap.calendario.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import br.com.fiap.calendario.model.CalendarEvent;

public class CalendarEventSpecification {

    public static Specification<CalendarEvent> nameContains(String name) {
        return (root, query, cb) -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<CalendarEvent> typeEquals(String type) {
        return (root, query, cb) -> type == null ? null : cb.equal(cb.lower(root.get("type")), type.toLowerCase());
    }

    public static Specification<CalendarEvent> startDateAfter(LocalDateTime start) {
        return (root, query, cb) -> start == null ? null : cb.greaterThanOrEqualTo(root.get("start"), start);
    }

    public static Specification<CalendarEvent> endDateBefore(LocalDateTime end) {
        return (root, query, cb) -> end == null ? null : cb.lessThanOrEqualTo(root.get("endTime"), end);
    }
}
