package kr.co.sionms.demo.hello.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.sionms.demo.hello.dto.ResponseDTO;
import kr.co.sionms.demo.hello.dto.TestRequestBody;

@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping
    public String testController() {
        return "hello world";
    }

    @GetMapping("/testGetMapping")
    public String testControllerWithPath() {
        return "hello world testGetMapping";
    }

    @GetMapping("/{id}")
    public String testControllerWithPathVariables(@PathVariable(required = false) String id) {
        return "hello World Id " + id;
    }

    @GetMapping("/testRequestParam")
    public String testControllerRequestParam(@RequestParam(required = false) int id) {
        return "hello World Id : " + id;
    }

    @GetMapping("/requestBody")
    public String testControllerRequestBody(@RequestBody TestRequestBody testRequestBody) {
        return "hello World id: " + testRequestBody.getId() + ", message : " + testRequestBody.getMessage();
    }

    @GetMapping("/requestDTO")
    public String testControllerDTO(TestRequestBody testRequestBody) {
        return "hello World id: " + testRequestBody.getId() + ", message : " + testRequestBody.getMessage();
    }

    // 반환테스트
    @GetMapping("testResponseBody")
    public ResponseDTO<String> testControllerRequestBody() {
        List<String> list = new ArrayList<>();
        list.add("hello world i'm responseDTO");
        ResponseDTO<String> responseDTO = ResponseDTO.<String>builder().data(list).build();
        return responseDTO;
    }

    @GetMapping("testResponseEntity")
    public ResponseEntity<?> testControllerRequestEntity() {
        List<String> list = new ArrayList<>();
        list.add("hello world i'm responseDTO And you got 400!");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.badRequest().body(response);
    }
}
