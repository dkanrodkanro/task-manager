package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskCreateDTO;
import com.example.taskmanager.dto.TaskUpdateDTO;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("findOne: 존재하는 id로 조회하면 task를 반환함")
    void findOne_success() {
        // given
        Task task = new Task();
        task.setTitle("할 일");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // when
        Task result = taskService.findOne(1L);

        // then
        assertThat(result.getTitle()).isEqualTo("할 일");
        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("findOne: 존재하지 않는 id로 조회하면 예외가 발생한다.")
    void findOne_notFound() {
        // given
        when(taskRepository.findById(9999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> taskService.findOne(9999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("할 일을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("makeTask - 정상 등록")
    void makeTask_success() {
        // given
        User user = new User();
        user.setId(1L);

        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTitle("새 할 일");
        dto.setDescription("설명");

        // when
        taskService.makeTask(dto, user);

        // then
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("updateTask - 본인 할 일 수정 성공")
    void updateTask_success() {
        // given
        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setUser(user);
        task.setTitle("기존 제목");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskUpdateDTO dto = new TaskUpdateDTO();
        dto.setTitle("수정된 제목");
        dto.setDescription("수정된 설명");

        // when
        taskService.updateTask(dto, user, 1L);

        // then
        assertThat(task.getTitle()).isEqualTo("수정된 제목");
        assertThat(task.getDescription()).isEqualTo("수정된 설명");
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("updateTask - 다른 사용자의 할 일 수정 시 예외 발생")
    void updateTask_notOwner() {
        // given
        User owner = new User();
        owner.setId(1L);

        User other = new User();
        other.setId(2L);

        Task task = new Task();
        task.setUser(owner);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskUpdateDTO dto = new TaskUpdateDTO();
        dto.setTitle("수정 시도");

        // when & then
        assertThatThrownBy(() -> taskService.updateTask(dto, other, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("권한이 없습니다.");
    }

    @Test
    @DisplayName("toggleComplete - 정상 토글")
    void toggleComplete_success() {
        // given
        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setUser(user);
        task.setCompleted(false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // when
        taskService.toggleComplete(user, 1L);

        // then
        assertThat(task.isCompleted()).isTrue();
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("toggleComplete - 다른 사용자의 할 일 토글 시 예외 발생")
    void toggleComplete_notOwner() {
        // given
        User owner = new User();
        owner.setId(1L);

        User other = new User();
        other.setId(2L);

        Task task = new Task();
        task.setUser(owner);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // when & then
        assertThatThrownBy(() -> taskService.toggleComplete(other, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("작성자가 아닙니다.");
    }

    @Test
    @DisplayName("deleteTask - 정상 삭제")
    void deleteTask_success() {
        // given
        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setUser(user);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // when
        taskService.deleteTask(user, 1L);

        // then
        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("deleteTask - 다른 사용자의 할 일 삭제 시 예외 발생")
    void deleteTask_notOwner() {
        // given
        User owner = new User();
        owner.setId(1L);

        User other = new User();
        other.setId(2L);

        Task task = new Task();
        task.setUser(owner);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // when & then
        assertThatThrownBy(() -> taskService.deleteTask(other, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("작성자가 아닙니다.");
    }
}
