package com.fun.LogicApp;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final ObjectFactory<Addition> additionObjectFactory;
    private final ApplicationContext applicationContext;

    @GetMapping("/logicUnits")
    @ResponseStatus(HttpStatus.OK)
    public List<String> returnAllLogicUnits() {
        Map<String, LogicUnit> logicUnits = applicationContext.getBeansOfType(LogicUnit.class);

        List<String> result = logicUnits.values()
            .stream()
            .map(entry -> entry.getClass().getSimpleName())
            .toList();
        return result;
    }

    @PostMapping("/addition")
    @ResponseStatus(HttpStatus.OK)
    public Addition.Output processJson(@RequestBody String jsonString) throws Exception {
        Addition instance = additionObjectFactory.getObject();
        instance.init(jsonString);
        instance.execute();
        return instance.getOutput();
    }
}
