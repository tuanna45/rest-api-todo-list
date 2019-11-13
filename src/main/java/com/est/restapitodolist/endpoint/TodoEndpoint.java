package com.est.restapitodolist.endpoint;

import com.est.restapitodolist.model.Work;
import com.est.restapitodolist.repository.WorkRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/todo/works")
public class TodoEndpoint {
    @Resource
    private WorkRepository workRepository;

    @GetMapping
    public ResponseEntity<List<Work>> getWorks(@RequestParam(defaultValue = "startingDate") String sortBy,
                                               @RequestParam(defaultValue = "asc") String sortOrder,
                                               @RequestParam(defaultValue = "20") int limit,
                                               @RequestParam(defaultValue = "0") int offset) {
        try {
            var sort = Sort.by(Direction.fromString(sortOrder), sortBy);
            var pageable = PageRequest.of(offset, limit, sort);

            return ResponseEntity.ok(workRepository.findAll(pageable).getContent());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Work> addWork(@RequestBody Work workRequest) {
        var work = Work.builder()
                .name(workRequest.getName())
                .status(workRequest.getStatus())
                .startingDate(LocalDateTime.now())
                .build();
        return status(CREATED).body(workRepository.save(work));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Work> updateWork(@PathVariable Long id,
                                           @RequestBody Work workRequest) {
        return workRepository.findById(id)
                .map(work -> Work.builder()
                        .id(work.getId())
                        .name(nonNull(workRequest.getName()) ? workRequest.getName() : work.getName())
                        .status(nonNull(workRequest.getStatus()) ? workRequest.getStatus() : work.getStatus())
                        .startingDate(work.getStartingDate())
                        .endingDate(LocalDateTime.now())
                        .build())
                .map(workRepository::save)
                .map(ResponseEntity::ok)
                .orElseGet(status(NOT_FOUND)::build);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteNote(@PathVariable Long id) {
        return workRepository.findById(id)
                .map(work -> {
                    workRepository.delete(work);
                    return status(NO_CONTENT).build();
                }).orElseGet(status(NOT_FOUND)::build);
    }
}
