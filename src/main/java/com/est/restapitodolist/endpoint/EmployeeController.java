package com.est.restapitodolist.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @PostMapping
    public ResponseEntity<?> createEmployeeHierarchy(@RequestBody Map<String, String> requestEmployees) {
        Map<String, List<String>> supervisorHierarchy = requestEmployees.entrySet().stream()
                .collect(groupingBy(Map.Entry::getValue, mapping(Map.Entry::getKey, toList())));
        List<String> topSupervisors = supervisorHierarchy.keySet().stream()
                .filter(supervisor -> Objects.isNull(requestEmployees.get(supervisor)))
                .collect(toList());
        if (topSupervisors.size() > 2) {
            throw new RuntimeException("Multiple roots found");
        } else if (topSupervisors.size() == 1) {
            String topSupervisor = topSupervisors.get(0);
            Map<String, ?> finalHierarchy = new HashMap<>();
//            finalHierarchy.put(topSupervisor, supervisorHierarchy.get(topSupervisor));
        } else {
            throw new RuntimeException("No top supervisor found");
        }
        return ResponseEntity.ok("topSupervisor");
    }
}
