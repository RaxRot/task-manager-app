package com.raxrot.back.service.impl;

import com.raxrot.back.dto.TaskRequest;
import com.raxrot.back.dto.TaskResponse;
import com.raxrot.back.entity.Task;
import com.raxrot.back.entity.User;
import com.raxrot.back.enums.Priority;
import com.raxrot.back.exception.ApiException;
import com.raxrot.back.repository.TaskRepository;
import com.raxrot.back.service.TaskService;
import com.raxrot.back.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }
    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        User currentUser=userService.getCurrentLoggedUser();
        Task task=modelMapper.map(taskRequest, Task.class);
        task.setUser(currentUser);
        Task savedTask=taskRepository.save(task);
        return modelMapper.map(savedTask, TaskResponse.class);
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        User currentUser=getCurrentLoggedUser();
        List<Task>allTasks=taskRepository.findByUser(currentUser, Sort.by(Sort.Direction.ASC, "id"));
        List<TaskResponse>allTaskResponses=mapTasks(allTasks);
        return allTaskResponses;
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        User currentUser=getCurrentLoggedUser();
        Task task=taskRepository.findById(id).orElseThrow(()->new ApiException("Task not found"));
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new ApiException("You are not allowed to view this task");
        }

        return modelMapper.map(task, TaskResponse.class);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        User currentUser=getCurrentLoggedUser();
        Task task=taskRepository.findById(id).orElseThrow(()->new ApiException("Task not found"));
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new ApiException("You are not allowed to update this task");
        }
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setCompleted(taskRequest.getCompleted());
        task.setPriority(taskRequest.getPriority());
        task.setDueDate(taskRequest.getDueDate());
        Task savedTask=taskRepository.save(task);
        return modelMapper.map(savedTask, TaskResponse.class);
    }

    @Override
    public void deleteTask(Long id) {
        User currentUser=getCurrentLoggedUser();
        Task task=taskRepository.findById(id).orElseThrow(()->new ApiException("Task not found"));
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new ApiException("You are not allowed to delete this task");
        }
        taskRepository.delete(task);
    }

    @Override
    public List<TaskResponse> getTasksByCompletedStatus(boolean completed) {
        User currentUser=getCurrentLoggedUser();
        List<Task>tasks=taskRepository.findByCompletedAndUser(completed, currentUser);
        List<TaskResponse>taskResponses=mapTasks(tasks);
        return taskResponses;
    }

    @Override
    public List<TaskResponse> getTasksByPriority(String priority) {
        User currentUser = userService.getCurrentLoggedUser();

        Priority enumPriority;
        try {
            enumPriority = Priority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException("Invalid priority value: " + priority);
        }

        List<Task> tasks = taskRepository.findByPriorityAndUser(enumPriority, currentUser, Sort.by(Sort.Direction.ASC, "id"));
        List<TaskResponse>taskResponses=mapTasks(tasks);
        return taskResponses;
    }

    private User getCurrentLoggedUser() {
        return userService.getCurrentLoggedUser();
    }

    private List<TaskResponse>mapTasks(List<Task> tasks) {
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskResponse.class))
                .collect(Collectors.toList());
    }
}
