package br.com.fiap.calendario.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.fiap.calendario.model.CalendarEvent;
import br.com.fiap.calendario.model.User;
import br.com.fiap.calendario.repository.CalendarEventRepository;
import br.com.fiap.calendario.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DatabaseSeeder {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarEventRepository calendarRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            User user1 = new User(null, "Admin", "admin@example.com", "admin123");
            user1 = userRepository.save(user1);

            User user2 = new User(null, "Thamires Ribeiro", "thamires@example.com", "123456");
            user2 = userRepository.save(user2);

            if (calendarRepository.count() == 0) {
                CalendarEvent event1 = new CalendarEvent(null, "Meeting", "Work",
                        LocalDateTime.parse("2023-10-01T10:00:00"),
                        LocalDateTime.parse("2023-10-01T11:00:00"), user1);
                calendarRepository.save(event1);

                CalendarEvent event2 = new CalendarEvent(null, "Doctor's Appointment", "Personal",
                        LocalDateTime.parse("2023-10-02T14:00:00"),
                        LocalDateTime.parse("2023-10-02T15:00:00"), user1);
                calendarRepository.save(event2);
            }
        }
    }
}



