package uz.kurisutina;

import java.util.List;

public record Prediction(double score, String verdict, List<String> topWords) {}