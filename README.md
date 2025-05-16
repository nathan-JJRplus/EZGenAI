# EZGenAI

Run open-source or custom built LLMs directly inside the Mendix runtime. No external hosting and/or webservices necessary.

Powered by [ONNXRuntime-GenAI](https://github.com/microsoft/onnxruntime-genai) (experimental).

## Features

- ✅ Include 1 or multiple LLM(s) on a plug-and-play basis

- ✅ Prompt your LLM using a single microflow action

- ✅ Retrieve default hyperparameters from the genai configuration file

- ✅ Override default hyperparameters on a per-prompt basis using the SearchParameter Mendix objects


## Prerequisites

### Bring your own LLM
This module requires you to bring your own LLM, compatible with the ONNXRuntime-GenAI library.

Either
-  build your own using the [ONNXRuntime-GenAI model builder](https://onnxruntime.ai/docs/genai/howto/build-model.html) or [Olive auto-opt](https://onnxruntime.ai/docs/genai/tutorials/deepseek-python.html)
- Download one of the pre-built models I supplied [here](https://github.com/nathan-JJRplus/Mendix-ONNXGenAI-Models).

Once built or downloaded, place the "model" folder inside `/resources/genai/models/`. You can rename the folder however you want. The name of this folder will be what your model is referred to inside the module.

### Initialize the module 
Place the **"Init EZGenAI"** microflow action inside your After-startup flow.

This will initialize the native dependencies as well as load your LLM(s).

## Usage

### Prompting

Once the prerequisites are in place, you can prompt your LLM using the **"Prompt"** microflow action.

In case you are using multiple models, provide the Model parameter with the name of the folder your model resides in, leave empty otherwise.

### Hyperparameters

If you want to override your models hyperparameters, you can do so by passing a list of SearchParameter objects into the Prompt action. 

The default hyperparameters are the attributes of the `"search"` element inside the `genai_config.json` file. 

These can also be retrieved using the **"Retrieve Search Parameters"** microflow action provided by the module.

### Testing

This module comes with a handy test snippet **"SNIP_EZGenAI_Test"**. Use this to fine-tune both your prompts, as well as your hyperparameters.

This testsnippet relies on the constant Modelname_Testpage. Fill out this constant in case you are using multiple models.

## Limitations

- Running LLM inference is very resource-heavy, generally performed by GPUs. The Mendix runtime will infer using the CPU. Make sure your LLM is optimized for this. All my pre-built models are INT4 quantized.
- The entire LLM will be loaded into RAM during inference. Consider this when choosing your LLM. 
- Tweak the max_tokens hyperparameter to make sure you don't run out of RAM during inference.
- Beamsearch seems to consistently return only 1 token, which probably has to do with my next point.
- ONNXRuntime-GenAI is still under heavy development. Issues can arise.

## Issues, suggestions and feature requests

[GitHub Issues page](https://github.com/nathan-JJRplus/EZGenAI/issues)
