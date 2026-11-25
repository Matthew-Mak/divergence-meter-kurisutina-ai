package uz.kurisutina;

import java.util.List;
import java.util.Random;

public class StubModelClient implements ModelClient {
    private final Random random = new Random();

    @Override
    public Prediction predict(String text) {
        double score = random.nextDouble();
        String verdict = score < 0.3 ? "safe" : score < 0.7 ? "suspicious" : "scam";
        return new Prediction(score, verdict, List.of("(заглушка)"));
    }
}