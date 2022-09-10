package me.whiteship.inflearnthejavatest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Target(ElementType.METHOD) // 애노테이션을 어디에 쓸 수 있는가?
@Retention(RetentionPolicy.RUNTIME) // 애노테이션 정보를 런타임까지 유지한다
@Test
@Tag("slow")
public @interface SlowTest {
}
