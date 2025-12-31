package com.university.cyberwalk.config;

import com.university.cyberwalk.model.Option;
import com.university.cyberwalk.model.Scenario;
import com.university.cyberwalk.repository.ScenarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ScenarioRepository scenarioRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (scenarioRepository.count() > 0) {
            return;
        }
        
        // Create main scenario (video 1)
        Scenario scenario1 = new Scenario();
        scenario1.setVideoId("1");
        scenario1.setVideoPath("/video/1.mp4");
        scenario1.setDescription("Initial scenario - Choose your path wisely!");
        scenario1.setLeafNode(false);
        
        // Create options for scenario 1
        Option option1_1 = new Option();
        option1_1.setLabel("Choice A");
        option1_1.setTargetVideoId("1_1");
        option1_1.setScoreChange(10);
        option1_1.setPosition("bottom-left");
        option1_1.setInteractionType("click");
        option1_1.setScenario(scenario1);
        
        Option option1_2 = new Option();
        option1_2.setLabel("Choice B");
        option1_2.setTargetVideoId("1_2");
        option1_2.setScoreChange(-5);
        option1_2.setPosition("bottom-right");
        option1_2.setInteractionType("click");
        option1_2.setScenario(scenario1);
        
        scenario1.setOptions(Arrays.asList(option1_1, option1_2));
        scenarioRepository.save(scenario1);
        
        // Create scenario for video 1_1 (leaf node - no further options)
        Scenario scenario1_1 = new Scenario();
        scenario1_1.setVideoId("1_1");
        scenario1_1.setVideoPath("/video/1_1.mp4");
        scenario1_1.setDescription("You chose path A - Good choice!");
        scenario1_1.setLeafNode(true);
        scenario1_1.setOptions(new ArrayList<>());
        scenarioRepository.save(scenario1_1);
        
        // Create scenario for video 1_2 (leaf node - no further options)
        Scenario scenario1_2 = new Scenario();
        scenario1_2.setVideoId("1_2");
        scenario1_2.setVideoPath("/video/1_2.mp4");
        scenario1_2.setDescription("You chose path B - This path has consequences!");
        scenario1_2.setLeafNode(true);
        scenario1_2.setOptions(new ArrayList<>());
        scenarioRepository.save(scenario1_2);
        
        System.out.println("Database initialized with scenarios!");
    }
}
