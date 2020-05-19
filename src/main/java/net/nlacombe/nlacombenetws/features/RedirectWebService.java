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
        headers.setLocation(URI.create("/summary"));

        return new ResponseEntity<>(null, headers, HttpStatus.TEMPORARY_REDIRECT);
    }

    @RequestMapping("/summary.html")
    public ResponseEntity<?> redirectSummaryHtmlExtensionPageToNewUri() {
        var headers = new HttpHeaders();
        headers.setLocation(URI.create("/summary"));

        return new ResponseEntity<>(null, headers, HttpStatus.TEMPORARY_REDIRECT);
    }

    @RequestMapping("/privacy.html")
    public ResponseEntity<?> redirectPrivacyHtmlExtensionPageToNewUri() {
        var headers = new HttpHeaders();
        headers.setLocation(URI.create("/privacy"));

        return new ResponseEntity<>(null, headers, HttpStatus.TEMPORARY_REDIRECT);
    }

    @RequestMapping("/compatibility.html")
    public ResponseEntity<?> redirectCompatibilityHtmlExtensionPageToNewUri() {
        var headers = new HttpHeaders();
        headers.setLocation(URI.create("/compatibility"));

        return new ResponseEntity<>(null, headers, HttpStatus.TEMPORARY_REDIRECT);
    }

    @RequestMapping("/accounts.html")
    public ResponseEntity<?> redirectAccountsHtmlExtensionPageToNewUri() {
        var headers = new HttpHeaders();
        headers.setLocation(URI.create("/accounts"));

        return new ResponseEntity<>(null, headers, HttpStatus.TEMPORARY_REDIRECT);
    }
}
