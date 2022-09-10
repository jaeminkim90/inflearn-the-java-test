package me.whiteship.inflearnthejavatest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AppTest {

    @Test
    void create() {
        App app = new App();
        assertNotNull(app);
    }


    @Test
    @DisplayName("스터디 만들기 \uD83D")
    void create_new_study() {
        App app = new App();
        assertNotNull(app);
    }


}