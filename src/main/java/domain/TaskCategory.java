package domain;

import java.util.Objects;

public class TaskCategory {
    private final String description;

    public TaskCategory(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof domain.TaskCategory)) {
            return false;
        }
        domain.TaskCategory that = (domain.TaskCategory) o;
        return description.equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    public String getDescription() {
        return description;
    }

    public TaskCategory clone() {
        return new domain.TaskCategory(this.description);
    }
}
