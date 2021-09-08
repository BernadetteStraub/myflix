package com.example.myflix.service;

import com.example.myflix.dao.ViewerRepository;
import com.example.myflix.model.Viewer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ViewerService {

    private final ViewerRepository viewerRepository;
    private final PasswordEncoder passwordEncoder;
    private final Set<String> viewerAuthorities;

    public ViewerService(ViewerRepository viewerRepository, PasswordEncoder passwordEncoder,
                       @Value("${app.authorities}") Set<String> viewerAuthorities) {
        this.viewerRepository = viewerRepository;
        this.passwordEncoder = passwordEncoder;
        this.viewerAuthorities = viewerAuthorities;
    }

    public Viewer save(Viewer viewer){
        Optional<Viewer> oViewer = viewerRepository.findOneByUsername(viewer.getUsername());
        if (oViewer.isPresent()){
            return oViewer.get();
        }
        String password = viewer.getPassword();
        String encoded = passwordEncoder.encode(password);
        viewer.setPassword(encoded);
        viewer.setAuthorities(viewerAuthorities);
        return viewerRepository.save(viewer);
    }
}

