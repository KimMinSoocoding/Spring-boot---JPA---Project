package kr.co.sionms.demo.hello.dto;

import kr.co.sionms.demo.hello.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class TodoDTO {
    private String id;
    private String title;
    private boolean done;

    public TodoDTO(final TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
    }

    public static TodoEntity todoEntity(final TodoDTO dto) {
        return TodoEntity.builder().id(dto.id).title(dto.title).done(dto.done).build();
    }
}
