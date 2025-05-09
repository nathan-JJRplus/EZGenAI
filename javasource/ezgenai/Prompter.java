package genai;

public class Prompter {
    public static String prompt(String model, String prompt) {
        MxGenAI mxGenAI = getModel(model);
        return mxGenAI.getResponse(prompt);
    }

    private static MxGenAI getModel(String model) {
        if (model == null || model.isBlank()) {
            return ModelManager.getModel();
        } else {
            return ModelManager.getModel(model);
        }
    }
}
