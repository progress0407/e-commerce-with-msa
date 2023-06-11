package msa.with.ddd.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {

    public static <T> Executable assertEquality(T actual, T expected) {

        return () -> assertThat(actual).isEqualTo(expected);
    }

    public static <T> Executable assertNotNull(T actual) {

        return () -> assertThat(actual).isNotNull();
    }
}
