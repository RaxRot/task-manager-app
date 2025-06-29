package com.raxrot.back.controller;

import com.raxrot.back.dto.TaskRequest;
import com.raxrot.back.dto.TaskResponse;
import com.raxrot.back.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest) {
       TaskResponse taskResponse = taskService.createTask(taskRequest);
       return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse>allTasks = taskService.getAllTasks();
        return new ResponseEntity<>(allTasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse taskResponse = taskService.getTaskById(id);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<List<TaskResponse>> getTasksByCompletedStatus(@RequestParam boolean completed) {
        List<TaskResponse>tasks=taskService.getTasksByCompletedStatus(completed);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/priority")
    public ResponseEntity<List<TaskResponse>> getTasksByPriority(@RequestParam String priority) {
        List<TaskResponse>tasks=taskService.getTasksByPriority(priority);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,@Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = taskService.updateTask(id, taskRequest);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
