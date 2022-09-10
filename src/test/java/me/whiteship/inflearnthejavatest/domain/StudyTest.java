package me.whiteship.inflearnthejavatest.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import me.whiteship.inflearnthejavatest.FastTest;
import me.whiteship.inflearnthejavatest.SlowTest;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 클래스 단위로 인스턴스를 생성한다.(= 클래스 내에서 하나의 인스턴스를 공유함)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    // 특정 순서대로 테스트를 실행하고 싶을 때s는 @TestInstance(Lifecycle.PER_CLASS)와 @TestMethodOrder를 사용한다.
class StudyTest {

    // @BeforeAll이나 AfterAll을 정의할 때는 반드시 static을 사용해야 한다
    // 하지만 클래스 단위로 인스턴스를 생성하면 static일 필요가 없다
    @BeforeAll
    static void beforeAll() {
        System.out.println("befor All");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("after All");
    }

    int value = 1;

    @FastTest
    @Disabled
    void create_new_study() {
        Study study = new Study(value++);
        // assertAll을 사용하면 깨지는 테스트를 한 번에 찾을 수 있다
        assertAll(
            () -> assertNotNull(study),
            () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 DRAFT 상태다."),
            // 람다로 넘겨주면 테스트가 실패 했을 때만 연산을 한다. 문자열만 넣어두면 테스트 실패/성공 여부와 상관없이 연산한다.
            () -> assertTrue(1 < 2),
            () -> assertTrue(study.getLimitCount() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다.")
        );
    }

    @SlowTest
    @Order(1)
        // Jnunit이 제공하는 Order를 사용해야 한다.
    void wrong_limit_test() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> new Study(-10));

        String message = exception.getMessage();
        assertEquals("limit은 0보다 커야 한다.", exception.getMessage());
    }

    @Test
    void timeout_test() {
        assertTimeout(Duration.ofMillis(400), () -> {
            new Study(10);
            Thread.sleep(300);
        });
        // TODO ThreadLocal을 사용할 경우 예상치 못한 결과가 나올 수 있다

        // 타임아웃되면 바로 테스트를 종료하고 싶을 경우 assertTimeoutPreemptively를 사용할 수 있다
        assertTimeoutPreemptively(Duration.ofMillis(300), () -> {
            new Study(10);
            Thread.sleep(100);

        });
    }

    @Test
    void assert_That_test() {
        System.out.println("this = " + this);
        System.out.println("value ++ = " + value++);
        // AssertJ 라이브러리를 사용하는 방법
        Study actual = new Study(value++);
        System.out.println("now value = " + value);
        assertThat(actual.getLimitCount()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    void value_test() {
        System.out.println("this = " + this);
        System.out.println("value ++ = " + value++);
        // AssertJ 라이브러리를 사용하는 방법
        Study actual = new Study(value++);
        System.out.println("now value = " + value);
        assertThat(actual.getLimitCount()).isGreaterThan(0);
    }

    @Test
    void assume_test() {
        // 조건을 만족할 때만 테스트를 실행하길 원한다면 assumeTrue을 사용한다
        String test_env = System.getenv("TEST_ENV");
        System.out.println("what : " + test_env);

        assumeTrue("LOCAL".equalsIgnoreCase(System.getenv("TEST_ENV")));
    }

    @DisplayName("스터디 만들기6")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    void repeat(RepetitionInfo repetitionInfo) {
        System.out.println(
            "test " + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions());
    }

    @DisplayName("스터디 만들기5")
    @ParameterizedTest(name = "{index} {displayName}  message = {0}") // 몇 번째 파라미터를 사용할 지 지정할 수 있다
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요."})
    @NullAndEmptySource
    void parameterizedTest(String message) {
        System.out.println(message);
    }

    @DisplayName("스터디 만들기4")
    @ParameterizedTest(name = "{index} {displayName}  message = {0}") // 몇 번째 파라미터를 사용할 지 지정할 수 있
    @ValueSource(ints = {10, 20, 30})
    @Order(3)
    void convert_Test(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println(study.getLimitCount());
    }

    @DisplayName("스터디 만들기3")
    @ParameterizedTest(name = "{index} {displayName}  message = {0}") // 몇 번째 파라미터를 사용할 지 지정할 수 있
    @CsvSource({"10, '자바 스터디'", "20, '스프링 스터디'"})
    void csv_Test(Integer limit, String name) {
        Study study = new Study(limit, name);
        System.out.println(study);
    }

    @DisplayName("스터디 만들기2")
    @ParameterizedTest(name = "{index} {displayName}  message = {0}") // 몇 번째 파라미터를 사용할 지 지정할 수 있
    @CsvSource({"10, '자바 스터디'", "20, '스프링 스터디'"})
    void argumentAccessor_Test(ArgumentsAccessor argumentsAccessor) {
        // 0번 인덱스에서 Integer 값을 참조하고, 1번 인덱스에서 String 값을 참조한다.
        Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        System.out.println(study);
    }

    @DisplayName("스터디 만들기1")
    @ParameterizedTest(name = "{index} {displayName}  message = {0}") // 몇 번째 파라미터를 사용할 지 지정할 수 있
    @CsvSource({"10, '자바 스터디'", "20, '스프링 스터디'"})
    void studyAggregator_Test(@AggregateWith(StudyAggregator.class) Study study) {
        System.out.println(study);
    }

    static class StudyAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws
            ArgumentsAggregationException {
            Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
            return study;
        }
    }

    static class StudyConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class targetType) throws ArgumentConversionException {
            assertEquals(Study.class, targetType, "Can only convert to Study");
            return new Study(Integer.parseInt(source.toString()));
        }
    }
}

