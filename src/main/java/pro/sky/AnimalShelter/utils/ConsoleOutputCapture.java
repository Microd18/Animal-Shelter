package pro.sky.AnimalShelter.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ConsoleOutputCapture {
    private final ByteArrayOutputStream outputStream;
    private final PrintStream originalOut;

    public ConsoleOutputCapture() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    public String getCapturedOutput() {
        return outputStream.toString().split("-")[1].trim();
    }

    public void stopCapture() {
        System.setOut(originalOut);
    }
}
