package br.com.fiap.calendario.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fiap.calendario.model.Calendar;
import br.com.fiap.calendario.model.User;
import br.com.fiap.calendario.repository.CalendarRepository;
import br.com.fiap.calendario.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DatabaseSeeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @PostConstruct
    public void init() {
        
        if (userRepository.count() == 0) {
            User user1 = new User(null, "Admin", "admin@example.com", "admin123");
            userRepository.save(user1);

            User user2 = new User(null, "Thamires Ribeiro", "thamires@example.com", "123456");
            userRepository.save(user2);
        }

        if (calendarRepository.count() == 0) {
            Calendar calendar1 = new Calendar(null, "Festa de Aniversário", "Social");
            calendarRepository.save(calendar1);

            Calendar calendar2 = new Calendar(null, "Reunião de Trabalho", "Corporativo");
            calendarRepository.save(calendar2);
        }
    }
}
