package genai;

import ai.onnxruntime.genai.*;

import java.util.function.Consumer;

import com.mendix.core.Core;
import com.mendix.logging.ILogNode;

public class MxGenAI {
    private final SimpleGenAI simpleGenAI;
    private final ILogNode LOG;
    private final String name;

    public MxGenAI(String name, String absoluteModelDirectory) {
        this.LOG = Core.getLogger("GenAI - " + name);
        this.name = name;
        try {
            LOG.info(String.format("Initializing model %s on path %s", name, absoluteModelDirectory));
            this.simpleGenAI = new SimpleGenAI(absoluteModelDirectory);
        } catch (GenAIException gae) {
            this.LOG.error(gae);    
            throw new RuntimeException(gae);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getResponse(String prompt) {
        return this.getResponse(prompt, null);
    }

    public String getResponse(String prompt, Consumer<String> listener) {
        try {
            long startTime = System.currentTimeMillis();
            this.LOG.debug(String.format("Init inference using model %s with prompt%n%s", name, prompt));            
            GeneratorParams generatorParams = this.simpleGenAI.createGeneratorParams();
            /*
             * Potential feature: dynamic generator parameters using runtime config
             */
            String response = simpleGenAI.generate(generatorParams, prompt, listener);
            /*
             *  Potential feature: easy hook for configuring microflow as listener consumer
             */
            long executionTime = System.currentTimeMillis() - startTime;
            this.LOG.debug(String.format("Inference finished in %d seconds", Math.round(executionTime / 1000)));
            return response;
        } catch (GenAIException gae) {
            this.LOG.error(gae);
            throw new RuntimeException(gae);
        }

    }

}
