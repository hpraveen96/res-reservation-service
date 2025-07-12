package com.egov.projectservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@RequestMapping("api/v1")
public class MainRestController {
    private static final Logger logger = LoggerFactory.getLogger(MainRestController.class);

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    QuoteRepository quoteRepository;

    @PostMapping("float/project")
    public ResponseEntity<String> addProfile(@RequestHeader("Authorization") String token,
                                             @RequestBody Project project) {
        logger.info("Received parameter for profile"+ project.toString());
        String phone="";
        try {
            phone = tokenService.validateToken(token);
        }catch(WebClientResponseException e){
            logger.info("Token validation failed: " + e.getMessage());
            return ResponseEntity.status(401).body("Invalid token");
        }
        if(phone.equals(project.getPhone())) {
            logger.info("Phone Match, Saving Profile details");
            projectRepository.save(project);
            return ResponseEntity.ok("Project added Successfully");
        }

        return ResponseEntity.status(401).body("Invalid Phone Number");

    }

    @PostMapping("raise/quote")
    public ResponseEntity<String> addQuote(@RequestHeader("Authorization") String token,
                                           @RequestBody Quote quote){
        logger.info("Received parameter for quote"+ quote.toString());
        String phone = "";
        try {
            phone = tokenService.validateToken(token);
        }catch(WebClientResponseException e){
            logger.info("Token validation failed: " + e.getMessage());
        }

        if(phone.equals(quote.getContractorId())){
            logger.info("Contractor Matched, Saving Quote details");
            Quote savedQuote =  quoteRepository.save(quote);
            return ResponseEntity.ok("Quote added Successfully with Quote id "+savedQuote.getId());
        }

        return ResponseEntity.status(401).body("Invalid Contractor Id");
    }
}
