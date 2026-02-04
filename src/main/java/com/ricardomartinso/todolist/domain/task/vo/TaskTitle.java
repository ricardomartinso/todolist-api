/* (C)2026 */
package com.ricardomartinso.todolist.domain.task.vo;

import com.ricardomartinso.todolist.domain.task.exceptions.InvalidTaskTitleException;

public record TaskTitle(String value) {

  public TaskTitle {
    if (value == null || value.isBlank()) {
      throw new InvalidTaskTitleException("Task title cannot be null or blank.");
    }

    if (value.length() > 100) {
      throw new InvalidTaskTitleException("Task title cannot exceed 100 characters.");
    }

    value = value.trim();
  }
}
