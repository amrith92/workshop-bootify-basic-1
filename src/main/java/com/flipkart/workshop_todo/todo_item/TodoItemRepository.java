package com.flipkart.workshop_todo.todo_item;

import com.flipkart.workshop_todo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    TodoItem findFirstByUser(User user);

}
