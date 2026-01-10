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
                // Clear existing data to ensure a fresh start
                scenarioRepository.deleteAll();

                // --- SCENE 1: IT SPAM MAIL ---
                Scenario s1 = new Scenario();
                s1.setVideoId("1");
                s1.setVideoPath("/video/university_student_scenerio/spam_mail/spam_mail_coming.mp4");
                s1.setDescription("IT Spam: You received a suspicious email.");
                s1.setLeafNode(false);
                s1.setNextScenarioId("2");

                Option o1_1 = createOption("Mail to CITS", "1_1", Option.SCORE_PLUS, 0, "bottom-right", 5.0, s1);
                Option o1_2 = createOption("Ignore Mail", "1_2", Option.SCORE_MINUS, 10, "bottom-left", 5.0, s1);
                s1.setOptions(Arrays.asList(o1_1, o1_2));
                scenarioRepository.save(s1);

                Scenario s1_1 = createLeaf("1_1", "/video/university_student_scenerio/spam_mail/send_mail_to_cits.mp4",
                                "Reported to CITS.", "2");
                Scenario s1_2 = createLeaf("1_2", "/video/university_student_scenerio/spam_mail/ignore_spam_mail.mp4",
                                "Ignored the mail.", "2");
                scenarioRepository.saveAll(Arrays.asList(s1_1, s1_2));

                // --- SCENE 2: WIFI PROBLEM ---
                Scenario s2 = new Scenario();
                s2.setVideoId("2");
                s2.setVideoPath("/video/university_student_scenerio/wifi_issue/wifi_problem.mp4");
                s2.setDescription("WiFi Issue: Suspicious WiFi connection attempt.");
                s2.setLeafNode(false);
                s2.setNextScenarioId("3");

                Option o2_1 = createOption("Connect WiFi", "2_1", Option.SCORE_FAIL, 20, "bottom-left", 5.0, s2);
                Option o2_2 = createOption("Choose Mobile Data", "2_2", Option.SCORE_PLUS, 0, "bottom-right", 5.0, s2);
                s2.setOptions(Arrays.asList(o2_1, o2_2));
                scenarioRepository.save(s2);

                Scenario s2_1 = createLeaf("2_1", "/video/university_student_scenerio/wifi_issue/accept_fake_wifi.mp4",
                                "Connected to risky WiFi.", "3");
                Scenario s2_2 = createLeaf("2_2",
                                "/video/university_student_scenerio/wifi_issue/choose_mobile_data.mp4",
                                "Used safe mobile data.", "3");
                scenarioRepository.saveAll(Arrays.asList(s2_1, s2_2));

                // --- SCENE 3: FAKE CALL ---
                Scenario s3 = new Scenario();
                s3.setVideoId("3");
                s3.setVideoPath("/video/university_student_scenerio/fake_call/fake_call_appear.mp4");
                s3.setDescription("Fake Call: Incoming call from IT department.");
                s3.setLeafNode(false);
                s3.setNextScenarioId("4");

                Option o3_1 = createOption("Accept Call", "3_1", Option.SCORE_FAIL, 30, "bottom-left", 5.0, s3);
                Option o3_2 = createOption("Ignore Call", "3_2", Option.SCORE_PLUS, 0, "bottom-right", 5.0, s3);
                s3.setOptions(Arrays.asList(o3_1, o3_2));
                scenarioRepository.save(s3);

                Scenario s3_1 = createLeaf("3_1", "/video/university_student_scenerio/fake_call/accept_call.mp4",
                                "Accepted a fraudulent call.", "4");
                Scenario s3_2 = createLeaf("3_2", "/video/university_student_scenerio/fake_call/ignore_call.mp4",
                                "Properly ignored the fake call.", "4");
                scenarioRepository.saveAll(Arrays.asList(s3_1, s3_2));

                // --- SCENE 4: PHISHING LINK ---
                Scenario s4 = new Scenario();
                s4.setVideoId("4");
                s4.setVideoPath("/video/university_student_scenerio/fake_link/appear_fake_link_in_groupchat.mp4");
                s4.setDescription("Phishing Link: A suspicious link appeared in the group chat.");
                s4.setLeafNode(false);
                s4.setNextScenarioId(null); // Round ends after choice videos

                Option o4_1 = createOption("Open Link", "4_1", Option.SCORE_FAIL, 40, "bottom-left", 5.0, s4);
                Option o4_2 = createOption("Simply Ignore", "4_2", Option.SCORE_PLUS, 0, "bottom-right", 5.0, s4);
                s4.setOptions(Arrays.asList(o4_1, o4_2));
                scenarioRepository.save(s4);

                Scenario s4_1 = createLeaf("4_1",
                                "/video/university_student_scenerio/fake_link/open_fake_link_in_groupchat.mp4",
                                "Clicked on a phishing link.", "GAMEOVER");
                Scenario s4_2 = createLeaf("4_2",
                                "/video/university_student_scenerio/fake_link/ignore_fake_link_in_groupchat.mp4",
                                "Ignored a malicious link.", "GAMEOVER");
                scenarioRepository.saveAll(Arrays.asList(s4_1, s4_2));

                System.out.println("Database initialized with the 4-scene interactive story flow.");
        }

        private Option createOption(String label, String target, int defScore, int atkScore, String pos, Double time,
                        Scenario s) {
                Option o = new Option();
                o.setLabel(label);
                o.setTargetVideoId(target);
                o.setDefenderScoreDelta(defScore);
                o.setAttackerScoreDelta(atkScore);
                o.setPosition(pos);
                o.setInteractionType("click");
                o.setAppearTime(time);
                o.setScenario(s);
                return o;
        }

        private Scenario createLeaf(String id, String path, String desc, String next) {
                Scenario s = new Scenario();
                s.setVideoId(id);
                s.setVideoPath(path);
                s.setDescription(desc);
                s.setLeafNode(true);
                s.setNextScenarioId(next);
                s.setOptions(new ArrayList<>());
                return s;
        }
}
