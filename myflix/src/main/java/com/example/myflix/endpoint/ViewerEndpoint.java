package com.example.myflix.endpoint;

import com.example.myflix.model.Viewer;
import com.example.myflix.service.ViewerService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/viewer")
public class ViewerEndpoint {

    private final ViewerService viewerService;

    public ViewerEndpoint(ViewerService viewerService) {
        this.viewerService = viewerService;
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    Viewer save(@RequestBody Viewer viewer){
        return viewerService.save(viewer);
    }

}

