package br.com.fiap.calendario.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "calendar_events")
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Tipo é obrigatório")
    private String type;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDateTime start;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "user_id")  
    private User user;

    public CalendarEvent() {}

    
    public CalendarEvent(Long id, String name, String type, LocalDateTime start, LocalDateTime endTime, User user) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.start = start;
        this.endTime = endTime;
        this.user = user;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
