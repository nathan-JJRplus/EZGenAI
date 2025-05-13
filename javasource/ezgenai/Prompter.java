package ezgenai;

import java.util.Map;

public class Prompter {
    public static String prompt(String model, String prompt, Map<String, Object> searchParams) {
        MxGenAI mxGenAI = ModelManager.getModel(model);
        return mxGenAI.getResponse(prompt, searchParams);
    }
}
