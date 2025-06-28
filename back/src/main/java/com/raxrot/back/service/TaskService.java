package com.raxrot.back.service;

import com.raxrot.back.dto.TaskRequest;
import com.raxrot.back.dto.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest taskRequest);
    List<TaskResponse> getAllTasks();
    TaskResponse getTaskById(Long id);
    TaskResponse updateTask(Long id, TaskRequest taskRequest);
    void deleteTask(Long id);
    List<TaskResponse> getTasksByCompletedStatus(boolean completed);
    List<TaskResponse> getTasksByPriority(String priority);
}
