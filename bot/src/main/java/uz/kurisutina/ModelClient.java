package uz.kurisutina;

public interface ModelClient {
    Prediction predict(String text) throws Exception;
}