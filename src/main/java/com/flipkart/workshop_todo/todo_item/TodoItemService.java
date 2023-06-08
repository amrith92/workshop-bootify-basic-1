package com.flipkart.workshop_todo.todo_item;

import com.flipkart.workshop_todo.user.User;
import com.flipkart.workshop_todo.user.UserRepository;
import com.flipkart.workshop_todo.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;
    private final UserRepository userRepository;

    public TodoItemService(final TodoItemRepository todoItemRepository,
            final UserRepository userRepository) {
        this.todoItemRepository = todoItemRepository;
        this.userRepository = userRepository;
    }

    public List<TodoItemDTO> findAll() {
        final List<TodoItem> todoItems = todoItemRepository.findAll(Sort.by("id"));
        return todoItems.stream()
                .map(todoItem -> mapToDTO(todoItem, new TodoItemDTO()))
                .toList();
    }

    public TodoItemDTO get(final Long id) {
        return todoItemRepository.findById(id)
                .map(todoItem -> mapToDTO(todoItem, new TodoItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TodoItemDTO todoItemDTO) {
        final TodoItem todoItem = new TodoItem();
        mapToEntity(todoItemDTO, todoItem);
        return todoItemRepository.save(todoItem).getId();
    }

    public void update(final Long id, final TodoItemDTO todoItemDTO) {
        final TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(todoItemDTO, todoItem);
        todoItemRepository.save(todoItem);
    }

    public void delete(final Long id) {
        todoItemRepository.deleteById(id);
    }

    private TodoItemDTO mapToDTO(final TodoItem todoItem, final TodoItemDTO todoItemDTO) {
        todoItemDTO.setId(todoItem.getId());
        todoItemDTO.setTitle(todoItem.getTitle());
        todoItemDTO.setIsDone(todoItem.getIsDone());
        todoItemDTO.setUser(todoItem.getUser() == null ? null : todoItem.getUser().getId());
        return todoItemDTO;
    }

    private TodoItem mapToEntity(final TodoItemDTO todoItemDTO, final TodoItem todoItem) {
        todoItem.setTitle(todoItemDTO.getTitle());
        todoItem.setIsDone(todoItemDTO.getIsDone());
        final User user = todoItemDTO.getUser() == null ? null : userRepository.findById(todoItemDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        todoItem.setUser(user);
        return todoItem;
    }

}
