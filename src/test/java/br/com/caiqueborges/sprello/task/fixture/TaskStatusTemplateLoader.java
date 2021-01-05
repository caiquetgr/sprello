package br.com.caiqueborges.sprello.task.fixture;

import br.com.caiqueborges.sprello.task.repository.entity.TaskStatus;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class TaskStatusTemplateLoader implements TemplateLoader {

    public static final String TASK_STATUS_TO_DO = "TASK_STATUS_TO_DO";
    public static final String TASK_STATUS_DONE = "TASK_STATUS_DONE";

    @Override
    public void load() {
        addTemplateTaskStatusToDo();
    }

    private void addTemplateTaskStatusToDo() {

        Fixture.of(TaskStatus.class).addTemplate(TASK_STATUS_TO_DO, new Rule() {{
            add("id", 1L);
            add("name", "To do");
            add("description", "Task is awaiting to be started");
        }});

        Fixture.of(TaskStatus.class).addTemplate(TASK_STATUS_DONE, new Rule() {{
            add("id", 4L);
            add("name", "Done");
            add("description", "Task has already been finished");
        }});

    }

}
