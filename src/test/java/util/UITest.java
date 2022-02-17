package util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class UITest {

    @Test
    void getOptionTest() {
        // Given
        String[] options = {
                "first option",
                "second option",
                "third option"
        };
        String console = "3";
        // When
        InputStream in = new ByteArrayInputStream(console.getBytes());
        System.setIn(in);
        int actual = UI.getOption("get option", options);
        // Then
        int expected = 3;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getInputTest() {
        // Given
        String console = "AA1234";
        // When
        InputStream in = new ByteArrayInputStream(console.getBytes());
        System.setIn(in);
        String actual = UI.getInput("get input", "^[A-Z0-9]{2}\\d{1,4}$");
        // Then
        String expected = "AA1234";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void pauseTest() {
        // Given

        // When

        // Then
    }
}