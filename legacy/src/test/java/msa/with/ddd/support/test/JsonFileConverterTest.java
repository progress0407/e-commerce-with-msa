package msa.with.ddd.support.test;

import msa.with.ddd.support.JsonFileConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonFileConverterTest {

    @Nested
    class fromJsonFile_메서드는 {

        @Nested
        class JSON_파일경로를_입력받을때 {

            private String jsonFilePath = "test/reading-json-test.json";

            @Test
            void 변환된_DTO를_반환한다() {

                List<JsonResponse> jsonResponses = JsonFileConverter.fromJsonFile(jsonFilePath, JsonResponse.class);
                JsonResponse jsonResponse = jsonResponses.get(0);

                Assertions.assertThat(jsonResponses).hasSize(1);
                assertThat(jsonResponse.getId()).isEqualTo(1);
                assertThat(jsonResponse.getName()).isEqualTo("JSON 데이터 전송 테스트");
            }
        }
    }
}