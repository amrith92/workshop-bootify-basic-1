package com.flipkart.workshop_todo.user;

import com.flipkart.workshop_todo.todo_item.TodoItem;
import com.flipkart.workshop_todo.todo_item.TodoItemRepository;
import com.flipkart.workshop_todo.util.NotFoundException;
import com.flipkart.workshop_todo.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final TodoItemRepository todoItemRepository;

    public UserService(final UserRepository userRepository,
            final TodoItemRepository todoItemRepository) {
        this.userRepository = userRepository;
        this.todoItemRepository = todoItemRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setName(userDTO.getName());
        return user;
    }

    public String getReferencedWarning(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final TodoItem userTodoItem = todoItemRepository.findFirstByUser(user);
        if (userTodoItem != null) {
            return WebUtils.getMessage("user.todoItem.user.referenced", userTodoItem.getId());
        }
        return null;
    }

}
