package com.fun.LogicApp;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
public class Addition extends LogicUnit<Addition.Input, Addition.Output> {

    
    @Override
    public Class<Input> getInputClass() {
        return Addition.Input.class;
    }

    @Override
    protected Output initOutput() {
        return new Output();
    }

    @Data
    public static class Input {
        private String text;
    }

    @Data
    public static class Output {
        private String text;
    }

    @Override
    public void run() {
        output.setText("Hello " + input.getText());
    }


    
}