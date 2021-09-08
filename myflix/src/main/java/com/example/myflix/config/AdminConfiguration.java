package com.example.myflix.config;

import com.example.myflix.dao.ViewerRepository;
import com.example.myflix.model.Viewer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConfigurationProperties("app")
public class AdminConfiguration {

    private Viewer admin;

    public void setAdmin(Viewer admin) {
        this.admin = admin;
    }

    @Bean
    ApplicationRunner adminUser(ViewerRepository viewerRepository, PasswordEncoder passwordEncoder){
        return args -> {
            if (viewerRepository.existsByUsername(admin.getUsername())){
                return;
            }
            String password = admin.getPassword();
            String encoded = passwordEncoder.encode(password);
            admin.setPassword(encoded);
            viewerRepository.save(admin);
        };
    }
}
