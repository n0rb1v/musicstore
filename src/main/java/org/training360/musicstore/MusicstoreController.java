package org.training360.musicstore;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/instruments")
public class MusicstoreController {
    private MusicstoreService musicstoreService;

    public MusicstoreController(MusicstoreService musicstoreService) {
        this.musicstoreService = musicstoreService;
    }

    @GetMapping
    public List<InstrumentDTO> listInstruments(@RequestParam Optional<String> search) {
        return musicstoreService.listAll(search);
    }
    @GetMapping("/{id}")
    public InstrumentDTO findInstrumentById(@PathVariable("id") long id){
        return musicstoreService.findInstrumentById(id);
    }
    @PutMapping("/{id}")
    public InstrumentDTO updateInstrument(@Valid @PathVariable("id") long id, @RequestBody UpdatePriceCommand command){
        return musicstoreService.updateInstrument(id,command);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInstrument(@PathVariable("id") long id){
        musicstoreService.deleteInstrument(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstrumentDTO createInstrument(@Valid @RequestBody CreateInstrumentCommand command) {
        return musicstoreService.createInstrument(command);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Problem> handleNotFound(IllegalArgumentException iae) {
        Problem problem = Problem.builder()
                .withType(URI.create("instruments/not-found"))
                .withTitle("Not found")
                .withStatus(Status.NOT_FOUND)
                .withDetail(iae.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
