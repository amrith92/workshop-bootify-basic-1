package com.flipkart.workshop_todo.model;

import com.flipkart.workshop_todo.user.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AllUserTodos {

    @NotNull
    private List<String> titles;

    @NotNull
    @Valid
    private UserDTO user;

}
