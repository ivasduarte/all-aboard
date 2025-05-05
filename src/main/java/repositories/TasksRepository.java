package repositories;

import domain.TaskCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TasksRepository {
    private final List<TaskCategory> taskCategories;
    public TasksRepository() {
        taskCategories = new ArrayList<>();
    }

    public TaskCategory getTaskCategoryByDescription(String taskCategoryDescription) {
        TaskCategory newTaskCategory = new TaskCategory(taskCategoryDescription);
        TaskCategory taskCategory = null;
        if (taskCategories.contains(newTaskCategory)) {
            taskCategory = taskCategories.get(taskCategories.indexOf(newTaskCategory));
        }
        if (taskCategory == null) {
            throw new IllegalArgumentException(
                    "Task Category requested for [" + taskCategoryDescription + "] does not exist.");
        }
        return taskCategory;
    }

    public Optional<TaskCategory> add(TaskCategory taskCategory) {
        Optional<TaskCategory> newTaskCategory = Optional.empty();
        boolean operationSuccess = false;

        if (validateTaskCategory(taskCategory)) {
            newTaskCategory = Optional.of(taskCategory);
            operationSuccess = taskCategories.add(newTaskCategory.get());
        }

        if (!operationSuccess) {
            newTaskCategory = Optional.empty();
        }

        return newTaskCategory;
    }

    private boolean validateTaskCategory(TaskCategory taskCategory) {
        boolean isValid = !taskCategories.contains(taskCategory);
        return isValid;
    }

    public List<TaskCategory> getTaskCategories() {
        return List.copyOf(taskCategories);
    }
}
