package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.GoalDto;
import com.claudioscagliotti.thesis.service.GoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Controller
@RequestMapping("/goals")
@Validated

public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<?> createGoal(@Valid @RequestBody GoalDto createGoalRequest) { //TODO ASSOCIATE USER
        GoalDto createdGoal = goalService.createGoal(createGoalRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdGoal.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdGoal);
    }//TODO exceptions
}
