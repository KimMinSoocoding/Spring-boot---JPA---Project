package kr.co.sionms.demo.hello.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.sionms.demo.hello.dto.ResponseDTO;
import kr.co.sionms.demo.hello.dto.TodoDTO;
import kr.co.sionms.demo.hello.model.TodoEntity;
import kr.co.sionms.demo.hello.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {
    @Autowired
    public TodoService service;

    // @GetMapping
    public ResponseEntity<?> testTodo() {
        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto, @AuthenticationPrincipal String userid) {
        try {
            // String temporaryUserId = "temPorary-user"; // temporary user id

            // TodoEntity로 변환
            TodoEntity entity = TodoDTO.todoEntity(dto);

            // id를 null로 초기화 한다 생성 당시에는 id가 없어야 하기 때문
            entity.setId(null);

            // 임시 유저 아이디를 설정, 한 유저만 로그인 없이 사용 가능한 애플리케이션인 셈
            entity.setUserId(userid);

            // 서비스를 이용해 Todo 엔티티를 생성
            List<TodoEntity> entities = service.create(entity);

            // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // ResponseDTO를 리턴
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 예외가 나는 경우 dto eo대신 error에 메시지를 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userid) {

        // String temporaryUserId = "temPorary-user"; // temporary user id

        // 서비스 메서드의 retrieve 메서드를 사용 Todo 리스트를 가져온다
        List<TodoEntity> entities = service.retrieve(userid);

        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);

    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto, @AuthenticationPrincipal String userid) {
        // String temporaryUserId = "temPorary-user";

        // dto를 entity로 변환한다
        TodoEntity entity = TodoDTO.todoEntity(dto);

        // id를 temporaryuserid로 초기화한다
        entity.setUserId(userid);

        // 서비스를 이용해 entity를 업데이트한다
        List<TodoEntity> entities = service.updata(entity);

        // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // ResponseDTO를 리턴
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto, @AuthenticationPrincipal String userid) {
        try {
            // String temporaryUserId = "temPorary-user";

            // dto를 entity로 변환한다
            TodoEntity entity = TodoDTO.todoEntity(dto);

            // id를 temporaryuserid로 초기화한다
            entity.setUserId(userid);

            // 서비스를 이용해 entity를 업데이트한다
            List<TodoEntity> entities = service.delete(entity);

            // 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // ResponseDTO를 리턴
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 예외가 나는 경우 dto 대신 error에 메시지를 넣어 리턴한다
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
