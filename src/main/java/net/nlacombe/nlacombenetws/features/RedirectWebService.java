package net.nlacombe.nlacombenetws.features;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class RedirectWebService {

    @RequestMapping("/")
    public ResponseEntity<?> redirectRootToSummary() {
        var headers = new HttpHeaders();
        headers.setLocation(URI.create("/summary.html"));

        return new ResponseEntity<>(null, headers, HttpStatus.TEMPORARY_REDIRECT);
    }
}
