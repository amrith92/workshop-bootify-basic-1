package com.flipkart.workshop_todo.todo_item;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/todoItems", produces = MediaType.APPLICATION_JSON_VALUE)
public class TodoItemResource {

    private final TodoItemService todoItemService;

    public TodoItemResource(final TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @GetMapping
    public ResponseEntity<List<TodoItemDTO>> getAllTodoItems() {
        return ResponseEntity.ok(todoItemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoItemDTO> getTodoItem(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(todoItemService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTodoItem(@RequestBody @Valid final TodoItemDTO todoItemDTO) {
        final Long createdId = todoItemService.create(todoItemDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTodoItem(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TodoItemDTO todoItemDTO) {
        todoItemService.update(id, todoItemDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable(name = "id") final Long id) {
        todoItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
