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
        scenario1.setDescription("Office: Your phone is ringing. Caller ID says 'IT Helpdesk'.");
        scenario1.setLeafNode(false);

        // Create options for scenario 1
        Option option1_1 = new Option();
        option1_1.setLabel("Answer Call");
        option1_1.setTargetVideoId("1_1");
        option1_1.setDefenderScoreDelta(Option.SCORE_PLUS);
        option1_1.setAttackerScoreDelta(0);
        option1_1.setPosition("bottom-left");
        option1_1.setInteractionType("click");
        option1_1.setScenario(scenario1);

        Option option1_2 = new Option();
        option1_2.setLabel("Ignore Call");
        option1_2.setTargetVideoId("1_2");
        option1_2.setDefenderScoreDelta(Option.SCORE_MINUS);
        option1_2.setAttackerScoreDelta(10);
        option1_2.setPosition("bottom-right");
        option1_2.setInteractionType("click");
        option1_2.setScenario(scenario1);

        scenario1.setOptions(Arrays.asList(option1_1, option1_2));
        scenarioRepository.save(scenario1);

        // Create scenario for video 1_1
        Scenario scenario1_1 = new Scenario();
        scenario1_1.setVideoId("1_1");
        scenario1_1.setVideoPath("/video/1_1.mp4");
        scenario1_1.setDescription("You answered. The caller asks for your password.");
        scenario1_1.setLeafNode(true); // For prototype, end here
        scenario1_1.setOptions(new ArrayList<>());
        scenarioRepository.save(scenario1_1);

        // Create scenario for video 1_2
        Scenario scenario1_2 = new Scenario();
        scenario1_2.setVideoId("1_2");
        scenario1_2.setVideoPath("/video/1_2.mp4");
        scenario1_2.setDescription("You ignored the call. It might have been important.");
        scenario1_2.setLeafNode(true);
        scenario1_2.setOptions(new ArrayList<>());
        scenarioRepository.save(scenario1_2);

        // --- Scenario 2: Home (MFA Fatigue) ---
        Scenario scenario2 = new Scenario();
        scenario2.setVideoId("2");
        scenario2.setVideoPath("/video/1.mp4"); // Reusing video 1 for prototype
        scenario2.setDescription("Home: You get multiple MFA notifications on your phone.");
        scenario2.setLeafNode(false);

        Option option2_1 = new Option();
        option2_1.setLabel("Approve Login");
        option2_1.setTargetVideoId("1_2"); // Lead to "bad" ending
        option2_1.setDefenderScoreDelta(Option.SCORE_FAIL);
        option2_1.setAttackerScoreDelta(50);
        option2_1.setPosition("bottom-left");
        option2_1.setInteractionType("click");
        option2_1.setScenario(scenario2);

        Option option2_2 = new Option();
        option2_2.setLabel("Change Password");
        option2_2.setTargetVideoId("1_1"); // Lead to "good" ending
        option2_2.setDefenderScoreDelta(Option.SCORE_DOUBLE_PLUS);
        option2_2.setAttackerScoreDelta(0);
        option2_2.setPosition("bottom-right");
        option2_2.setInteractionType("click");
        option2_2.setScenario(scenario2);

        scenario2.setOptions(Arrays.asList(option2_1, option2_2));
        scenarioRepository.save(scenario2);

        // --- Scenario 3: Restaurant (Fake WiFi) ---
        Scenario scenario3 = new Scenario();
        scenario3.setVideoId("3");
        scenario3.setVideoPath("/video/1.mp4"); // Reusing video 1 for prototype
        scenario3.setDescription("Restaurant: You need internet. Which WiFi do you choose?");
        scenario3.setLeafNode(false);

        Option option3_1 = new Option();
        option3_1.setLabel("Free_Public_WiFi");
        option3_1.setTargetVideoId("1_2");
        option3_1.setDefenderScoreDelta(Option.SCORE_FAIL);
        option3_1.setAttackerScoreDelta(50);
        option3_1.setPosition("bottom-left");
        option3_1.setInteractionType("click");
        option3_1.setScenario(scenario3);

        Option option3_2 = new Option();
        option3_2.setLabel("Use 5G Hotspot");
        option3_2.setTargetVideoId("1_1");
        option3_2.setDefenderScoreDelta(Option.SCORE_PLUS);
        option3_2.setAttackerScoreDelta(0);
        option3_2.setPosition("bottom-right");
        option3_2.setInteractionType("click");
        option3_2.setScenario(scenario3);

        scenario3.setOptions(Arrays.asList(option3_1, option3_2));
        scenarioRepository.save(scenario3);

        System.out.println("Database initialized with scenarios!");
    }
}
