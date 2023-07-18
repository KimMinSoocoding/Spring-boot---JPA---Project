package kr.co.sionms.demo;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@RequiredArgsConstructor
@ToString
public class DemoModel {
    @NonNull
    private String id;

    public static void main(String[] args) {
        DemoModel demoModel = DemoModel.builder().id("abcd").build();
        System.out.println(demoModel);

    }
}
