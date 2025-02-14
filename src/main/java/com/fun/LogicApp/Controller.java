package com.fun.LogicApp;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> test() {
        Map<String, LogicUnit> logicUnits = applicationContext.getBeansOfType(LogicUnit.class);
        Map<String, String> result = logicUnits.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getClass().getSimpleName()));
        return result;
    }
}
