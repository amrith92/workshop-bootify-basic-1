package com.flipkart.workshop_todo.todo_item;

import com.flipkart.workshop_todo.user.User;
import com.flipkart.workshop_todo.user.UserRepository;
import com.flipkart.workshop_todo.util.CustomCollectors;
import com.flipkart.workshop_todo.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/todoItems")
public class TodoItemController {

    private final TodoItemService todoItemService;
    private final UserRepository userRepository;

    public TodoItemController(final TodoItemService todoItemService,
            final UserRepository userRepository) {
        this.todoItemService = todoItemService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("todoItems", todoItemService.findAll());
        return "todoItem/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("todoItem") final TodoItemDTO todoItemDTO) {
        return "todoItem/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("todoItem") @Valid final TodoItemDTO todoItemDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "todoItem/add";
        }
        todoItemService.create(todoItemDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("todoItem.create.success"));
        return "redirect:/todoItems";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("todoItem", todoItemService.get(id));
        return "todoItem/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("todoItem") @Valid final TodoItemDTO todoItemDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "todoItem/edit";
        }
        todoItemService.update(id, todoItemDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("todoItem.update.success"));
        return "redirect:/todoItems";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        todoItemService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("todoItem.delete.success"));
        return "redirect:/todoItems";
    }

}
