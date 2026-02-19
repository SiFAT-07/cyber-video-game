package com.university.cyberwalk.config;

import com.university.cyberwalk.model.*;
import com.university.cyberwalk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

        @Autowired
        private LevelRepository levelRepository;

        @Autowired
        private DefenderProfileRepository defenderProfileRepository;

        @Autowired
        private AttackScenarioRepository attackScenarioRepository;

        @Autowired
        private AttackOptionRepository attackOptionRepository;

        @Autowired
        private DefenderChoiceRepository defenderChoiceRepository;

        @Override
        public void run(String... args) throws Exception {
                // Only initialize data if database is empty (first run)
                if (levelRepository.count() > 0) {
                        System.out.println("Database already contains data. Skipping initialization.");
                        return;
                }

                System.out.println("Initializing database with comprehensive cyber attack simulation levels...");

                createLevel1_UniversityHostelLife();
                createLevel2_CampusWideAttacks();
                createLevel3_ExamStressPeriod();
                createLevel4_SummerInternship();
                createLevel5_OnlineShoppingSpree();

                System.out.println("Database initialized successfully with 5 comprehensive levels!");
        }

        // ========== LEVEL 1: University Hostel Life ==========
        private void createLevel1_UniversityHostelLife() {
                Level level = new Level();
                level.setName("University Hostel Life");
                level.setDescription(
                                "You're a student living in a hostel away from home. Navigate common cyber threats targeting students.");
                level.setDifficulty("EASY");
                level.setEnabled(true);
                level.setOrderIndex(1);
                level.setMaxAttacks(5);
                level = levelRepository.save(level);

                // === DEFENDER PROFILE: Sifat ===
                DefenderProfile sifat = new DefenderProfile();
                sifat.setName("Sifat");
                sifat.setDescription(
                                "Computer Science student living in hostel, away from family in Dhaka. Tech-savvy but emotionally vulnerable when family is mentioned.");
                sifat.setAge(21);
                sifat.setAgeGroup("YOUNG");
                sifat.setOccupation("Computer Science Student");
                sifat.setTechSavviness("HIGH");
                sifat.setMentalState("STRESSED");
                sifat.setFinancialStatus("STRUGGLING");
                sifat.setAvatarIcon("👨‍🎓");
                sifat.setLevel(level);
                sifat = defenderProfileRepository.save(sifat);

                // === DEFENDER PROFILE: Riya ===
                DefenderProfile riya = new DefenderProfile();
                riya.setName("Riya");
                riya.setDescription(
                                "Business major from local area. Lives close to campus, family-oriented, moderate tech skills.");
                riya.setAge(20);
                riya.setAgeGroup("YOUNG");
                riya.setOccupation("Business Student");
                riya.setTechSavviness("MEDIUM");
                riya.setMentalState("CALM");
                riya.setFinancialStatus("STABLE");
                riya.setAvatarIcon("👩‍🎓");
                riya.setLevel(level);
                riya = defenderProfileRepository.save(riya);

                // === ATTACK SCENARIO 1: Fake Call from Family ===
                AttackScenario fakeCallScenario = new AttackScenario();
                fakeCallScenario.setAttackType("FAKE_CALL");
                fakeCallScenario.setName("Family Emergency Call");
                fakeCallScenario.setDescription("Scammer pretends to be a family member in distress");
                fakeCallScenario.setAttackerNarrative(
                                "Target students away from home. Use emotional manipulation about family emergencies to extract money or information.");
                fakeCallScenario.setLevel(level);
                fakeCallScenario = attackScenarioRepository.save(fakeCallScenario);

                // Attack Option 1.1: Call as Mother
                AttackOption callAsMother = new AttackOption();
                callAsMother.setLabel("Call as Mother");
                callAsMother.setDescription("Impersonate the student's mother claiming medical emergency");
                callAsMother.setAttackerMessage(
                                "Beta, I'm in the hospital! I need money urgently for treatment. Please send 50,000 Taka immediately!");
                callAsMother.setImpersonatedEntity("Mother");
                callAsMother.setBaseAttackerPoints(30);
                callAsMother.setRiskLevel(4);
                callAsMother.setCriticalRisk(true);
                callAsMother.setAttackScenario(fakeCallScenario);
                callAsMother = attackOptionRepository.save(callAsMother);

                // Sifat's choices for Mother call (VULNERABLE)
                DefenderChoice sifatMotherTrust = new DefenderChoice();
                sifatMotherTrust.setLabel("Send money immediately");
                sifatMotherTrust.setDescription("Panic and transfer money without verification");
                sifatMotherTrust.setOutcome(
                                "Sifat sends 50,000 Taka. Later discovers mother was never in hospital. Life savings gone.");
                sifatMotherTrust.setDefenderScoreDelta(-40);
                sifatMotherTrust.setAttackerScoreDelta(30);
                sifatMotherTrust.setChoiceType("WRONG");
                sifatMotherTrust.setEducationalNote(
                                "VULNERABILITY: Sifat lives far from home and can't verify in person. Always call back on known numbers or video call before sending money.");
                sifatMotherTrust.setAttackOption(callAsMother);
                defenderChoiceRepository.save(sifatMotherTrust);

                DefenderChoice sifatMotherVerify = new DefenderChoice();
                sifatMotherVerify.setLabel("Call father to verify");
                sifatMotherVerify.setDescription("Stay calm and cross-check with another family member");
                sifatMotherVerify.setOutcome("Father confirms mother is safe at home. Scam prevented!");
                sifatMotherVerify.setDefenderScoreDelta(25);
                sifatMotherVerify.setAttackerScoreDelta(-15);
                sifatMotherVerify.setChoiceType("CORRECT");
                sifatMotherVerify.setEducationalNote(
                                "STRENGTH: Always verify emergency calls through alternative contact. Scammers rely on panic.");
                sifatMotherVerify.setAttackOption(callAsMother);
                defenderChoiceRepository.save(sifatMotherVerify);

                DefenderChoice sifatMotherVideo = new DefenderChoice();
                sifatMotherVideo.setLabel("Demand video call first");
                sifatMotherVideo.setDescription("Ask for video proof before taking action");
                sifatMotherVideo.setOutcome("Scammer hangs up immediately. Crisis averted!");
                sifatMotherVideo.setDefenderScoreDelta(30);
                sifatMotherVideo.setAttackerScoreDelta(-20);
                sifatMotherVideo.setChoiceType("CORRECT");
                sifatMotherVideo.setEducationalNote(
                                "BEST PRACTICE: Video calls are hard to fake. Use them to verify identity in emergencies.");
                sifatMotherVideo.setAttackOption(callAsMother);
                defenderChoiceRepository.save(sifatMotherVideo);

                // Attack Option 1.2: Call as Best Friend
                AttackOption callAsFriend = new AttackOption();
                callAsFriend.setLabel("Call as Best Friend Rahim");
                callAsFriend.setDescription("Impersonate best friend needing urgent help");
                callAsFriend.setAttackerMessage(
                                "Bro, I'm stuck! Got arrested, need bail money. Don't tell anyone, just send 30,000 now!");
                callAsFriend.setImpersonatedEntity("Best Friend");
                callAsFriend.setBaseAttackerPoints(15);
                callAsFriend.setRiskLevel(2);
                callAsFriend.setCriticalRisk(false);
                callAsFriend.setAttackScenario(fakeCallScenario);
                callAsFriend = attackOptionRepository.save(callAsFriend);

                // Sifat's choices for Friend call (LESS VULNERABLE - knows friend well)
                DefenderChoice sifatFriendTrust = new DefenderChoice();
                sifatFriendTrust.setLabel("Send money to help friend");
                sifatFriendTrust.setDescription("Believe the story and transfer money");
                sifatFriendTrust.setOutcome("Money sent to scammer. Real Rahim is confused when asked about it later.");
                sifatFriendTrust.setDefenderScoreDelta(-25);
                sifatFriendTrust.setAttackerScoreDelta(15);
                sifatFriendTrust.setChoiceType("WRONG");
                sifatFriendTrust.setEducationalNote(
                                "Even urgent requests from friends should be verified. Scammers clone phone numbers.");
                sifatFriendTrust.setAttackOption(callAsFriend);
                defenderChoiceRepository.save(sifatFriendTrust);

                DefenderChoice sifatFriendDetect = new DefenderChoice();
                sifatFriendDetect.setLabel("Ask verification question");
                sifatFriendDetect.setDescription("Ask something only real Rahim would know");
                sifatFriendDetect.setOutcome(
                                "Sifat asks about their shared project. Scammer fumbles. Sifat hangs up and calls real Rahim.");
                sifatFriendDetect.setDefenderScoreDelta(35);
                sifatFriendDetect.setAttackerScoreDelta(-25);
                sifatFriendDetect.setChoiceType("CORRECT");
                sifatFriendDetect.setEducationalNote(
                                "STRENGTH: Sifat knows his best friend well. Personal verification questions expose imposters.");
                sifatFriendDetect.setAttackOption(callAsFriend);
                defenderChoiceRepository.save(sifatFriendDetect);

                // === ATTACK SCENARIO 2: Fake WiFi Network ===
                AttackScenario fakeWifiScenario = new AttackScenario();
                fakeWifiScenario.setAttackType("FAKE_WIFI");
                fakeWifiScenario.setName("Evil Twin WiFi");
                fakeWifiScenario.setDescription("Fake WiFi network mimicking legitimate hostel WiFi");
                fakeWifiScenario.setAttackerNarrative(
                                "Set up rogue WiFi with similar name to hostel network. Capture credentials and browsing data.");
                fakeWifiScenario.setLevel(level);
                fakeWifiScenario = attackScenarioRepository.save(fakeWifiScenario);

                // Attack Option 2.1: Hostel_WiFi_Free
                AttackOption fakeHostelWifi = new AttackOption();
                fakeHostelWifi.setLabel("Create 'Hostel_WiFi_Free' network");
                fakeHostelWifi.setDescription("Mimic official network name with slight variation");
                fakeHostelWifi.setAttackerMessage("WiFi Network Available: Hostel_WiFi_Free - No password required!");
                fakeHostelWifi.setImpersonatedEntity("University IT Department");
                fakeHostelWifi.setBaseAttackerPoints(20);
                fakeHostelWifi.setRiskLevel(3);
                fakeHostelWifi.setCriticalRisk(false);
                fakeHostelWifi.setAttackScenario(fakeWifiScenario);
                fakeHostelWifi = attackOptionRepository.save(fakeHostelWifi);

                // Sifat's choices (HIGH tech savviness - should detect)
                DefenderChoice sifatWifiConnect = new DefenderChoice();
                sifatWifiConnect.setLabel("Connect immediately - free WiFi!");
                sifatWifiConnect.setDescription("Jump on free WiFi without checking");
                sifatWifiConnect.setOutcome("Connected to evil twin. Attacker intercepts passwords and banking info.");
                sifatWifiConnect.setDefenderScoreDelta(-30);
                sifatWifiConnect.setAttackerScoreDelta(20);
                sifatWifiConnect.setChoiceType("WRONG");
                sifatWifiConnect.setEducationalNote(
                                "Free public WiFi is tempting but dangerous. Always verify network authenticity.");
                sifatWifiConnect.setAttackOption(fakeHostelWifi);
                defenderChoiceRepository.save(sifatWifiConnect);

                DefenderChoice sifatWifiSuspicious = new DefenderChoice();
                sifatWifiSuspicious.setLabel("Check with hostel admin first");
                sifatWifiSuspicious.setDescription("Verify if this is legitimate network");
                sifatWifiSuspicious.setOutcome(
                                "Admin confirms official network is 'Hostel_Secure_2024'. Fake network reported.");
                sifatWifiSuspicious.setDefenderScoreDelta(30);
                sifatWifiSuspicious.setAttackerScoreDelta(-20);
                sifatWifiSuspicious.setChoiceType("CORRECT");
                sifatWifiSuspicious.setEducationalNote(
                                "STRENGTH: High tech savviness helps Sifat question suspicious networks. Always verify with authorities.");
                sifatWifiSuspicious.setAttackOption(fakeHostelWifi);
                defenderChoiceRepository.save(sifatWifiSuspicious);

                DefenderChoice sifatWifiMobile = new DefenderChoice();
                sifatWifiMobile.setLabel("Use mobile data instead");
                sifatWifiMobile.setDescription("Avoid untrusted networks entirely");
                sifatWifiMobile.setOutcome("Uses secure mobile data. No risk of interception.");
                sifatWifiMobile.setDefenderScoreDelta(25);
                sifatWifiMobile.setAttackerScoreDelta(-15);
                sifatWifiMobile.setChoiceType("CORRECT");
                sifatWifiMobile.setEducationalNote(
                                "When in doubt, mobile data or VPN on trusted networks is safest option.");
                sifatWifiMobile.setAttackOption(fakeHostelWifi);
                defenderChoiceRepository.save(sifatWifiMobile);

                // Riya's choices (MEDIUM tech savviness - mixed vulnerability)
                DefenderChoice riyaWifiTrust = new DefenderChoice();
                riyaWifiTrust.setLabel("Connect - seems legitimate");
                riyaWifiTrust.setDescription("Trust the network name");
                riyaWifiTrust.setOutcome("Riya connects. Credentials compromised.");
                riyaWifiTrust.setDefenderScoreDelta(-25);
                riyaWifiTrust.setAttackerScoreDelta(20);
                riyaWifiTrust.setChoiceType("WRONG");
                riyaWifiTrust.setEducationalNote("Network names can be easily spoofed. Don't rely on names alone.");
                riyaWifiTrust.setAttackOption(fakeHostelWifi);
                defenderChoiceRepository.save(riyaWifiTrust);

                DefenderChoice riyaWifiAsk = new DefenderChoice();
                riyaWifiAsk.setLabel("Ask roommate if they use this");
                riyaWifiAsk.setDescription("Seek peer verification");
                riyaWifiAsk.setOutcome("Roommate says they never heard of it. Riya avoids the trap.");
                riyaWifiAsk.setDefenderScoreDelta(20);
                riyaWifiAsk.setAttackerScoreDelta(-10);
                riyaWifiAsk.setChoiceType("CORRECT");
                riyaWifiAsk.setEducationalNote(
                                "When uncertain about tech, asking knowledgeable peers is smart strategy.");
                riyaWifiAsk.setAttackOption(fakeHostelWifi);
                defenderChoiceRepository.save(riyaWifiAsk);

                // === ATTACK SCENARIO 3: Phishing Email ===
                AttackScenario phishingScenario = new AttackScenario();
                phishingScenario.setAttackType("PHISHING_EMAIL");
                phishingScenario.setName("Scholarship Scam");
                phishingScenario.setDescription("Fake scholarship opportunity email targeting struggling students");
                phishingScenario.setAttackerNarrative(
                                "Target students with financial struggles. Offer too-good-to-be-true scholarship requiring personal information.");
                phishingScenario.setLevel(level);
                phishingScenario = attackScenarioRepository.save(phishingScenario);

                // Attack Option 3.1: Government Scholarship Email
                AttackOption scholarshipEmail = new AttackOption();
                scholarshipEmail.setLabel("Gov Scholarship - Apply Now!");
                scholarshipEmail.setDescription("Fake government scholarship with urgent deadline");
                scholarshipEmail.setAttackerMessage(
                                "Congratulations! You're pre-selected for 100,000 Taka scholarship. Submit bank details within 24 hours to claim.");
                scholarshipEmail.setImpersonatedEntity("Government Education Ministry");
                scholarshipEmail.setBaseAttackerPoints(25);
                scholarshipEmail.setRiskLevel(4);
                scholarshipEmail.setCriticalRisk(true);
                scholarshipEmail.setAttackScenario(phishingScenario);
                scholarshipEmail = attackOptionRepository.save(scholarshipEmail);

                // Sifat's choices (STRUGGLING financially - vulnerable)
                DefenderChoice sifatScholarApply = new DefenderChoice();
                sifatScholarApply.setLabel("Submit details immediately");
                sifatScholarApply.setDescription("Jump at opportunity due to financial need");
                sifatScholarApply.setOutcome("Sifat provides bank account, ID copy. Identity stolen, account drained.");
                sifatScholarApply.setDefenderScoreDelta(-45);
                sifatScholarApply.setAttackerScoreDelta(25);
                sifatScholarApply.setChoiceType("WRONG");
                sifatScholarApply.setEducationalNote(
                                "VULNERABILITY: Financial desperation makes Sifat susceptible. Legitimate scholarships never ask for bank details upfront.");
                sifatScholarApply.setAttackOption(scholarshipEmail);
                defenderChoiceRepository.save(sifatScholarApply);

                DefenderChoice sifatScholarVerify = new DefenderChoice();
                sifatScholarVerify.setLabel("Check official ministry website");
                sifatScholarVerify.setDescription("Verify through official channels");
                sifatScholarVerify
                                .setOutcome("No such scholarship exists on official site. Email reported as phishing.");
                sifatScholarVerify.setDefenderScoreDelta(35);
                sifatScholarVerify.setAttackerScoreDelta(-20);
                sifatScholarVerify.setChoiceType("CORRECT");
                sifatScholarVerify.setEducationalNote(
                                "STRENGTH: Despite need, Sifat's tech knowledge prompts verification. Always check official sources.");
                sifatScholarVerify.setAttackOption(scholarshipEmail);
                defenderChoiceRepository.save(sifatScholarVerify);

                DefenderChoice sifatScholarAnalyze = new DefenderChoice();
                sifatScholarAnalyze.setLabel("Analyze email headers and links");
                sifatScholarAnalyze.setDescription("Use technical skills to inspect email");
                sifatScholarAnalyze.setOutcome(
                                "Sifat spots fake sender domain and suspicious links. Reports to cybersecurity authority.");
                sifatScholarAnalyze.setDefenderScoreDelta(40);
                sifatScholarAnalyze.setAttackerScoreDelta(-25);
                sifatScholarAnalyze.setChoiceType("CORRECT");
                sifatScholarAnalyze.setEducationalNote(
                                "HIGH TECH SAVVINESS WIN: Technical analysis reveals phishing attempt. Email headers often expose fraud.");
                sifatScholarAnalyze.setAttackOption(scholarshipEmail);
                defenderChoiceRepository.save(sifatScholarAnalyze);

                System.out.println("✓ Level 1 created: University Hostel Life");
        }

        // ========== LEVEL 2: Campus Wide Attacks ==========
        private void createLevel2_CampusWideAttacks() {
                Level level = new Level();
                level.setName("Campus Wide Cyber Attack");
                level.setDescription(
                                "Multiple coordinated attacks targeting different campus demographics during exam week.");
                level.setDifficulty("MEDIUM");
                level.setEnabled(true);
                level.setOrderIndex(2);
                level.setMaxAttacks(6);
                level = levelRepository.save(level);

                // === DEFENDER PROFILE: Professor Rahman ===
                DefenderProfile prof = new DefenderProfile();
                prof.setName("Dr. Rahman");
                prof.setDescription(
                                "Senior professor, 58 years old. Respected academic but struggles with modern technology. Often stressed during exam periods.");
                prof.setAge(58);
                prof.setAgeGroup("MIDDLE_AGED");
                prof.setOccupation("University Professor");
                prof.setTechSavviness("LOW");
                prof.setMentalState("STRESSED");
                prof.setFinancialStatus("STABLE");
                prof.setAvatarIcon("👨‍🏫");
                prof.setLevel(level);
                prof = defenderProfileRepository.save(prof);

                // === DEFENDER PROFILE: Lab Assistant Nadia ===
                DefenderProfile nadia = new DefenderProfile();
                nadia.setName("Nadia");
                nadia.setDescription(
                                "Computer lab assistant, 24. Tech-savvy and calm, handles campus IT issues daily.");
                nadia.setAge(24);
                nadia.setAgeGroup("YOUNG");
                nadia.setOccupation("Lab Assistant");
                nadia.setTechSavviness("HIGH");
                nadia.setMentalState("CALM");
                nadia.setFinancialStatus("STABLE");
                nadia.setAvatarIcon("👩‍💻");
                nadia.setLevel(level);
                nadia = defenderProfileRepository.save(nadia);

                // === ATTACK SCENARIO 1: Fake University Portal ===
                AttackScenario fakePortal = new AttackScenario();
                fakePortal.setAttackType("FAKE_WEBSITE");
                fakePortal.setName("Cloned University Portal");
                fakePortal.setDescription("Fake exam grade portal targeting professors and students");
                fakePortal.setAttackerNarrative(
                                "During exam week, send emails with links to fake portal. Capture university credentials for grade manipulation later.");
                fakePortal.setLevel(level);
                fakePortal = attackScenarioRepository.save(fakePortal);

                // Attack Option: Urgent Grade Submission
                AttackOption gradePortalLink = new AttackOption();
                gradePortalLink.setLabel("Email: Submit Grades Urgently");
                gradePortalLink.setDescription("Fake urgent email from registrar office");
                gradePortalLink.setAttackerMessage(
                                "Dear Faculty, Grade submission deadline extended 24hrs. Login now: univ-portal-grades.com/submit");
                gradePortalLink.setImpersonatedEntity("University Registrar");
                gradePortalLink.setBaseAttackerPoints(40);
                gradePortalLink.setRiskLevel(5);
                gradePortalLink.setCriticalRisk(true);
                gradePortalLink.setAttackScenario(fakePortal);
                gradePortalLink = attackOptionRepository.save(gradePortalLink);

                // Dr. Rahman's choices (LOW tech, STRESSED - very vulnerable)
                DefenderChoice profLogin = new DefenderChoice();
                profLogin.setLabel("Login quickly to submit grades");
                profLogin.setDescription("Click link and enter credentials in panic");
                profLogin.setOutcome(
                                "Professor's credentials stolen. Attacker changes grades system-wide. Academic integrity compromised.");
                profLogin.setDefenderScoreDelta(-60);
                profLogin.setAttackerScoreDelta(40);
                profLogin.setChoiceType("WRONG");
                profLogin.setEducationalNote(
                                "CRITICAL VULNERABILITY: Low tech skills + high stress = perfect target. Always verify emails, especially urgent ones.");
                profLogin.setAttackOption(gradePortalLink);
                defenderChoiceRepository.save(profLogin);

                DefenderChoice profCallIT = new DefenderChoice();
                profCallIT.setLabel("Call IT department first");
                profCallIT.setDescription("Verify with tech support before acting");
                profCallIT.setOutcome("IT confirms it's phishing. Professor avoids credential theft.");
                profCallIT.setDefenderScoreDelta(40);
                profCallIT.setAttackerScoreDelta(-30);
                profCallIT.setChoiceType("CORRECT");
                profCallIT.setEducationalNote(
                                "SMART MOVE: When uncertain about tech, asking experts prevents disasters. IT departments expect such calls.");
                profCallIT.setAttackOption(gradePortalLink);
                defenderChoiceRepository.save(profCallIT);

                DefenderChoice profCheckURL = new DefenderChoice();
                profCheckURL.setLabel("Check if URL matches official site");
                profCheckURL.setDescription("Compare domain with bookmarked portal");
                profCheckURL.setOutcome("Notices domain is wrong (should be .edu). Reports to IT security.");
                profCheckURL.setDefenderScoreDelta(50);
                profCheckURL.setAttackerScoreDelta(-35);
                profCheckURL.setChoiceType("CORRECT");
                profCheckURL.setEducationalNote(
                                "EXCELLENT: Even with low tech skills, basic URL checking saves the day. Bookmark legitimate sites.");
                profCheckURL.setAttackOption(gradePortalLink);
                defenderChoiceRepository.save(profCheckURL);

                // Nadia's choices (HIGH tech, CALM - should detect easily)
                DefenderChoice nadiaInspect = new DefenderChoice();
                nadiaInspect.setLabel("Inspect email source and headers");
                nadiaInspect.setDescription("Perform technical analysis");
                nadiaInspect.setOutcome(
                                "Identifies spoofed sender, suspicious links. Alerts entire campus about phishing campaign.");
                nadiaInspect.setDefenderScoreDelta(55);
                nadiaInspect.setAttackerScoreDelta(-40);
                nadiaInspect.setChoiceType("CORRECT");
                nadiaInspect.setEducationalNote(
                                "EXPERT RESPONSE: High tech savviness enables deep analysis. Proactive warning protects community.");
                nadiaInspect.setAttackOption(gradePortalLink);
                defenderChoiceRepository.save(nadiaInspect);

                // === ATTACK SCENARIO 2: USB Drop Attack ===
                AttackScenario usbDrop = new AttackScenario();
                usbDrop.setAttackType("USB_DROP");
                usbDrop.setName("Infected USB Drive");
                usbDrop.setDescription("USB drives labeled 'Exam Question Bank' left in campus library");
                usbDrop.setAttackerNarrative(
                                "Drop infected USBs labeled with tempting content. When plugged in, deploy keylogger and ransomware.");
                usbDrop.setLevel(level);
                usbDrop = attackScenarioRepository.save(usbDrop);

                // Attack Option: Exam Question USB
                AttackOption examUSB = new AttackOption();
                examUSB.setLabel("USB labeled 'Midterm Questions 2024'");
                examUSB.setDescription("Tempting USB left in library during exam week");
                examUSB.setAttackerMessage("USB Drive Label: MIDTERM EXAM QUESTIONS - ALL DEPARTMENTS - 2024");
                examUSB.setImpersonatedEntity("Unknown Student/Faculty");
                examUSB.setBaseAttackerPoints(35);
                examUSB.setRiskLevel(5);
                examUSB.setCriticalRisk(true);
                examUSB.setAttackScenario(usbDrop);
                examUSB = attackOptionRepository.save(examUSB);

                // Mixed student profile choices
                DefenderChoice usbPlugin = new DefenderChoice();
                usbPlugin.setLabel("Plug into computer immediately");
                usbPlugin.setDescription("Can't resist exam questions");
                usbPlugin.setOutcome("Malware installed. Computer locked with ransomware. All files encrypted.");
                usbPlugin.setDefenderScoreDelta(-50);
                usbPlugin.setAttackerScoreDelta(35);
                usbPlugin.setChoiceType("WRONG");
                usbPlugin.setEducationalNote(
                                "NEVER plug unknown USB devices. Exam stress makes students vulnerable to this classic attack.");
                usbPlugin.setAttackOption(examUSB);
                defenderChoiceRepository.save(usbPlugin);

                DefenderChoice usbIgnore = new DefenderChoice();
                usbIgnore.setLabel("Turn it into lost & found");
                usbIgnore.setDescription("Don't touch suspicious devices");
                usbIgnore.setOutcome(
                                "Security office safely examines USB in isolated environment. Malware discovered and warning issued.");
                usbIgnore.setDefenderScoreDelta(40);
                usbIgnore.setAttackerScoreDelta(-30);
                usbIgnore.setChoiceType("CORRECT");
                usbIgnore.setEducationalNote(
                                "RIGHT CHOICE: Report found USBs to security. Curiosity isn't worth the risk.");
                usbIgnore.setAttackOption(examUSB);
                defenderChoiceRepository.save(usbIgnore);

                DefenderChoice usbScan = new DefenderChoice();
                usbScan.setLabel("Scan with isolated virtual machine");
                usbScan.setDescription("Use advanced tech to safely inspect");
                usbScan.setOutcome(
                                "Nadia scans in VM, discovers malware. Creates campus-wide security awareness session.");
                usbScan.setDefenderScoreDelta(60);
                usbScan.setAttackerScoreDelta(-45);
                usbScan.setChoiceType("CORRECT");
                usbScan.setEducationalNote(
                                "EXPERT LEVEL: Using VMs for unknown devices is best practice for security professionals.");
                usbScan.setAttackOption(examUSB);
                defenderChoiceRepository.save(usbScan);

                // === ATTACK SCENARIO 3: Social Engineering Call ===
                AttackScenario socialEngCall = new AttackScenario();
                socialEngCall.setAttackType("SOCIAL_ENGINEERING");
                socialEngCall.setName("IT Support Impersonation");
                socialEngCall.setDescription("Fake IT support call requesting remote access");
                socialEngCall.setAttackerNarrative(
                                "Call faculty claiming virus detected. Request remote access tool installation to 'fix' the issue.");
                socialEngCall.setLevel(level);
                socialEngCall = attackScenarioRepository.save(socialEngCall);

                // Attack Option: System Virus Alert
                AttackOption itSupportCall = new AttackOption();
                itSupportCall.setLabel("Call: Urgent Virus on Your Computer");
                itSupportCall.setDescription("Impersonate campus IT support");
                itSupportCall.setAttackerMessage(
                                "Hello Professor, IT Security here. We detected virus on your system. Please install TeamViewer so we can remove it.");
                itSupportCall.setImpersonatedEntity("Campus IT Support");
                itSupportCall.setBaseAttackerPoints(30);
                itSupportCall.setRiskLevel(4);
                itSupportCall.setCriticalRisk(true);
                itSupportCall.setAttackScenario(socialEngCall);
                itSupportCall = attackOptionRepository.save(itSupportCall);

                // Dr. Rahman choices
                DefenderChoice profInstallRemote = new DefenderChoice();
                profInstallRemote.setLabel("Install remote access tool");
                profInstallRemote.setDescription("Trust the caller and comply");
                profInstallRemote.setOutcome(
                                "Grants full system access. Research data stolen, ransomware deployed campus-wide.");
                profInstallRemote.setDefenderScoreDelta(-55);
                profInstallRemote.setAttackerScoreDelta(30);
                profInstallRemote.setChoiceType("WRONG");
                profInstallRemote.setEducationalNote(
                                "MAJOR BREACH: Legitimate IT never asks for remote access via unsolicited calls. Always verify through official channels.");
                profInstallRemote.setAttackOption(itSupportCall);
                defenderChoiceRepository.save(profInstallRemote);

                DefenderChoice profHangupVerify = new DefenderChoice();
                profHangupVerify.setLabel("Hang up and call IT directly");
                profHangupVerify.setDescription("Verify through known IT number");
                profHangupVerify.setOutcome("Real IT confirms no virus, no call made. Scam prevented.");
                profHangupVerify.setDefenderScoreDelta(45);
                profHangupVerify.setAttackerScoreDelta(-25);
                profHangupVerify.setChoiceType("CORRECT");
                profHangupVerify.setEducationalNote(
                                "GOLD STANDARD: Never trust unsolicited tech support calls. Hang up and call official numbers.");
                profHangupVerify.setAttackOption(itSupportCall);
                defenderChoiceRepository.save(profHangupVerify);

                // Nadia choices
                DefenderChoice nadiaChallenge = new DefenderChoice();
                nadiaChallenge.setLabel("Ask for IT ticket number");
                nadiaChallenge.setDescription("Request official work order ID");
                nadiaChallenge.setOutcome(
                                "Caller can't provide ticket number. Nadia traces call, reports to authorities.");
                nadiaChallenge.setDefenderScoreDelta(50);
                nadiaChallenge.setAttackerScoreDelta(-35);
                nadiaChallenge.setChoiceType("CORRECT");
                nadiaChallenge.setEducationalNote(
                                "PRO TIP: Legitimate IT always has ticket numbers. This simple question exposes most scams.");
                nadiaChallenge.setAttackOption(itSupportCall);
                defenderChoiceRepository.save(nadiaChallenge);

                System.out.println("✓ Level 2 created: Campus Wide Attacks");
        }

        // ========== LEVEL 3: Exam Stress Period ==========
        private void createLevel3_ExamStressPeriod() {
                Level level = new Level();
                level.setName("Exam Season Exploitation");
                level.setDescription("Attackers exploit stressed students during finals week with targeted scams.");
                level.setDifficulty("MEDIUM");
                level.setEnabled(true);
                level.setOrderIndex(3);
                level.setMaxAttacks(5);
                level = levelRepository.save(level);

                // === DEFENDER PROFILE: Stressed Student Tina ===
                DefenderProfile tina = new DefenderProfile();
                tina.setName("Tina");
                tina.setDescription(
                                "Engineering student during finals week. Extremely stressed, sleep-deprived, making poor decisions.");
                tina.setAge(22);
                tina.setAgeGroup("YOUNG");
                tina.setOccupation("Engineering Student");
                tina.setTechSavviness("MEDIUM");
                tina.setMentalState("ANXIOUS");
                tina.setFinancialStatus("STRUGGLING");
                tina.setAvatarIcon("👩‍💻");
                tina.setLevel(level);
                tina = defenderProfileRepository.save(tina);

                // === ATTACK SCENARIO: Fake Exam Postponement ===
                AttackScenario examScam = new AttackScenario();
                examScam.setAttackType("PHISHING_EMAIL");
                examScam.setName("Exam Schedule Manipulation");
                examScam.setDescription("Fake emails about exam changes requiring credential verification");
                examScam.setAttackerNarrative(
                                "Students are anxious about exams. Send fake schedule change emails requiring login to see updated dates.");
                examScam.setLevel(level);
                examScam = attackScenarioRepository.save(examScam);

                // Attack Option: Exam Postponed Email
                AttackOption postponeEmail = new AttackOption();
                postponeEmail.setLabel("URGENT: Your Exam Postponed");
                postponeEmail.setDescription("Fake emergency email about exam rescheduling");
                postponeEmail.setAttackerMessage(
                                "Due to technical issues, your CS301 exam postponed. Login to portal to see new date: exam-schedule-update.net");
                postponeEmail.setImpersonatedEntity("Exam Controller Office");
                postponeEmail.setBaseAttackerPoints(35);
                postponeEmail.setRiskLevel(4);
                postponeEmail.setCriticalRisk(true);
                postponeEmail.setAttackScenario(examScam);
                postponeEmail = attackOptionRepository.save(postponeEmail);

                // Tina's choices (ANXIOUS + stressed = vulnerable)
                DefenderChoice tinaLoginQuick = new DefenderChoice();
                tinaLoginQuick.setLabel("Login immediately to check");
                tinaLoginQuick.setDescription("Panic and click link without thinking");
                tinaLoginQuick.setOutcome(
                                "Credentials stolen. Attacker enrolls Tina in fake courses, steals scholarship money from portal.");
                tinaLoginQuick.setDefenderScoreDelta(-45);
                tinaLoginQuick.setAttackerScoreDelta(35);
                tinaLoginQuick.setChoiceType("WRONG");
                tinaLoginQuick.setEducationalNote(
                                "VULNERABILITY: Anxiety clouds judgment. Always take breath and verify before acting on urgent emails.");
                tinaLoginQuick.setAttackOption(postponeEmail);
                defenderChoiceRepository.save(tinaLoginQuick);

                DefenderChoice tinaCheckOfficial = new DefenderChoice();
                tinaCheckOfficial.setLabel("Check official noticeboard");
                tinaCheckOfficial.setDescription("Verify through traditional official channels");
                tinaCheckOfficial.setOutcome("No notice on official board. Tina reports phishing email.");
                tinaCheckOfficial.setDefenderScoreDelta(40);
                tinaCheckOfficial.setAttackerScoreDelta(-30);
                tinaCheckOfficial.setChoiceType("CORRECT");
                tinaCheckOfficial.setEducationalNote(
                                "GOOD PRACTICE: Official channels like physical noticeboards and official websites are reliable sources.");
                tinaCheckOfficial.setAttackOption(postponeEmail);
                defenderChoiceRepository.save(tinaCheckOfficial);

                DefenderChoice tinaAskClassmate = new DefenderChoice();
                tinaAskClassmate.setLabel("Ask classmates in group chat");
                tinaAskClassmate.setDescription("Crowdsource verification");
                tinaAskClassmate.setOutcome(
                                "No one else received email. Group identifies it as scam. Collective security awareness increased.");
                tinaAskClassmate.setDefenderScoreDelta(35);
                tinaAskClassmate.setAttackerScoreDelta(-25);
                tinaAskClassmate.setChoiceType("CORRECT");
                tinaAskClassmate.setEducationalNote(
                                "PEER VERIFICATION: Community defense works. Scammers usually can't target everyone simultaneously.");
                tinaAskClassmate.setAttackOption(postponeEmail);
                defenderChoiceRepository.save(tinaAskClassmate);

                // === ATTACK SCENARIO: Fake Study Material ===
                AttackScenario fakeNotes = new AttackScenario();
                fakeNotes.setAttackType("MALWARE");
                fakeNotes.setName("Malicious Study Notes");
                fakeNotes.setDescription("Infected study materials shared in student groups");
                fakeNotes.setAttackerNarrative(
                                "Share infected PDF claiming to be premium study notes. When opened, ransomware encrypts all files right before exams.");
                fakeNotes.setLevel(level);
                fakeNotes = attackScenarioRepository.save(fakeNotes);

                // Attack Option: Premium Notes PDF
                AttackOption notesFile = new AttackOption();
                notesFile.setLabel("Share 'Complete Solved Questions.pdf'");
                notesFile.setDescription("Malware disguised as study material");
                notesFile.setAttackerMessage(
                                "Hey everyone! I got the premium solved question bank from a senior. Download: [malicious-link.pdf]");
                notesFile.setImpersonatedEntity("Helpful Senior Student");
                notesFile.setBaseAttackerPoints(40);
                notesFile.setRiskLevel(5);
                notesFile.setCriticalRisk(true);
                notesFile.setAttackScenario(fakeNotes);
                notesFile = attackOptionRepository.save(notesFile);

                // Tina's choices
                DefenderChoice tinaDownload = new DefenderChoice();
                tinaDownload.setLabel("Download and open immediately");
                tinaDownload.setDescription("Desperate for exam help");
                tinaDownload.setOutcome(
                                "Ransomware encrypts all study notes and projects. Demands payment right before exams. Academic disaster.");
                tinaDownload.setDefenderScoreDelta(-60);
                tinaDownload.setAttackerScoreDelta(40);
                tinaDownload.setChoiceType("WRONG");
                tinaDownload.setEducationalNote(
                                "CRITICAL: Desperation makes students vulnerable. Never download files from unknown sources, especially before exams.");
                tinaDownload.setAttackOption(notesFile);
                defenderChoiceRepository.save(tinaDownload);

                DefenderChoice tinaScanFirst = new DefenderChoice();
                tinaScanFirst.setLabel("Scan with antivirus before opening");
                tinaScanFirst.setDescription("Basic security precaution");
                tinaScanFirst.setOutcome("Antivirus detects trojan. File quarantined. Crisis averted.");
                tinaScanFirst.setDefenderScoreDelta(45);
                tinaScanFirst.setAttackerScoreDelta(-35);
                tinaScanFirst.setChoiceType("CORRECT");
                tinaScanFirst.setEducationalNote(
                                "ESSENTIAL HABIT: Always scan downloads with updated antivirus. This simple step prevents many attacks.");
                tinaScanFirst.setAttackOption(notesFile);
                defenderChoiceRepository.save(tinaScanFirst);

                DefenderChoice tinaVerifySource = new DefenderChoice();
                tinaVerifySource.setLabel("Ask for source verification");
                tinaVerifySource.setDescription("Question the file's origin");
                tinaVerifySource.setOutcome(
                                "Supposed 'senior' can't provide details. Other students confirm it's spam. Group admin removes post.");
                tinaVerifySource.setDefenderScoreDelta(40);
                tinaVerifySource.setAttackerScoreDelta(-30);
                tinaVerifySource.setChoiceType("CORRECT");
                tinaVerifySource.setEducationalNote(
                                "CRITICAL THINKING: Question sources of 'premium' or 'leaked' materials. Usually too good to be true.");
                tinaVerifySource.setAttackOption(notesFile);
                defenderChoiceRepository.save(tinaVerifySource);

                System.out.println("✓ Level 3 created: Exam Season Exploitation");
        }

        // ========== LEVEL 4: Summer Internship ==========
        private void createLevel4_SummerInternship() {
                Level level = new Level();
                level.setName("Remote Internship Dangers");
                level.setDescription("Cyber threats targeting students during remote internships and job hunting.");
                level.setDifficulty("HARD");
                level.setEnabled(true);
                level.setOrderIndex(4);
                level.setMaxAttacks(6);
                level = levelRepository.save(level);

                // === DEFENDER PROFILE: Job Seeker Arif ===
                DefenderProfile arif = new DefenderProfile();
                arif.setName("Arif");
                arif.setDescription(
                                "Final year student desperately job hunting. Financially struggling, willing to take risks for opportunities.");
                arif.setAge(23);
                arif.setAgeGroup("YOUNG");
                arif.setOccupation("Final Year Student");
                arif.setTechSavviness("MEDIUM");
                arif.setMentalState("ANXIOUS");
                arif.setFinancialStatus("STRUGGLING");
                arif.setAvatarIcon("👨‍💼");
                arif.setLevel(level);
                arif = defenderProfileRepository.save(arif);

                // === ATTACK SCENARIO: Fake Job Offer ===
                AttackScenario fakeJob = new AttackScenario();
                fakeJob.setAttackType("PHISHING_EMAIL");
                fakeJob.setName("Fraudulent Job Offer");
                fakeJob.setDescription("Too-good-to-be-true job offers requiring upfront fees or personal information");
                fakeJob.setAttackerNarrative(
                                "Target desperate graduates with fake high-paying jobs. Request fee for 'training materials' or steal identity for fraud.");
                fakeJob.setLevel(level);
                fakeJob = attackScenarioRepository.save(fakeJob);

                // Attack Option: Google Internship Scam
                AttackOption googleScam = new AttackOption();
                googleScam.setLabel("Google Internship - Pre-Selected!");
                googleScam.setDescription("Fake offer from prestigious company");
                googleScam.setAttackerMessage(
                                "Congratulations! Based on your LinkedIn, you're selected for Google Summer Internship 2024. Salary: $8000/month. Pay $500 registration fee to secure position.");
                googleScam.setImpersonatedEntity("Google HR Department");
                googleScam.setBaseAttackerPoints(45);
                googleScam.setRiskLevel(5);
                googleScam.setCriticalRisk(true);
                googleScam.setAttackScenario(fakeJob);
                googleScam = attackOptionRepository.save(googleScam);

                // Arif's choices (STRUGGLING + ANXIOUS = very vulnerable)
                DefenderChoice arifPayFee = new DefenderChoice();
                arifPayFee.setLabel("Pay registration fee immediately");
                arifPayFee.setDescription("Jump at opportunity without verification");
                arifPayFee.setOutcome("Arif borrows money and pays $500. Scammer disappears. No job, deeper in debt.");
                arifPayFee.setDefenderScoreDelta(-70);
                arifPayFee.setAttackerScoreDelta(45);
                arifPayFee.setChoiceType("WRONG");
                arifPayFee.setEducationalNote(
                                "CRITICAL RED FLAG: Legitimate companies NEVER charge fees for jobs. Financial desperation exploited ruthlessly.");
                arifPayFee.setAttackOption(googleScam);
                defenderChoiceRepository.save(arifPayFee);

                DefenderChoice arifResearch = new DefenderChoice();
                arifResearch.setLabel("Research Google's hiring process");
                arifResearch.setDescription("Verify company procedures");
                arifResearch.setOutcome(
                                "Discovers Google never charges fees and doesn't pre-select via email. Reports scam.");
                arifResearch.setDefenderScoreDelta(50);
                arifResearch.setAttackerScoreDelta(-40);
                arifResearch.setChoiceType("CORRECT");
                arifResearch.setEducationalNote(
                                "ESSENTIAL: Always research company hiring processes. Official career pages describe legitimate procedures.");
                arifResearch.setAttackOption(googleScam);
                defenderChoiceRepository.save(arifResearch);

                DefenderChoice arifCheckEmail = new DefenderChoice();
                arifCheckEmail.setLabel("Verify sender email domain");
                arifCheckEmail.setDescription("Check if email is from @google.com");
                arifCheckEmail.setOutcome(
                                "Email from 'google-hiring@gmail.com'. Obviously fake. Arif reports to anti-fraud authorities.");
                arifCheckEmail.setDefenderScoreDelta(55);
                arifCheckEmail.setAttackerScoreDelta(-45);
                arifCheckEmail.setChoiceType("CORRECT");
                arifCheckEmail.setEducationalNote(
                                "TECHNICAL WIN: Company emails always use official domains. Gmail/Yahoo = instant red flag for corporate emails.");
                arifCheckEmail.setAttackOption(googleScam);
                defenderChoiceRepository.save(arifCheckEmail);

                // === ATTACK SCENARIO: Fake Remote Work Setup ===
                AttackScenario remoteScam = new AttackScenario();
                remoteScam.setAttackType("SOCIAL_ENGINEERING");
                remoteScam.setName("Remote Work Software Scam");
                remoteScam.setDescription("Fake internship requires downloading malicious 'company software'");
                remoteScam.setAttackerNarrative(
                                "After fake offer acceptance, request download of 'proprietary remote work tool' that's actually spyware.");
                remoteScam.setLevel(level);
                remoteScam = attackScenarioRepository.save(remoteScam);

                // Attack Option: Install Company Software
                AttackOption fakeSoftware = new AttackOption();
                fakeSoftware.setLabel("Install 'TechCorp RemoteAccess Pro'");
                fakeSoftware.setDescription("Malware disguised as company tool");
                fakeSoftware.setAttackerMessage(
                                "Welcome to TechCorp! Install our secure remote work platform from: techcorp-tools.exe. Mandatory for all interns.");
                fakeSoftware.setImpersonatedEntity("TechCorp HR");
                fakeSoftware.setBaseAttackerPoints(50);
                fakeSoftware.setRiskLevel(5);
                fakeSoftware.setCriticalRisk(true);
                fakeSoftware.setAttackScenario(remoteScam);
                fakeSoftware = attackOptionRepository.save(fakeSoftware);

                // Arif's choices
                DefenderChoice arifInstall = new DefenderChoice();
                arifInstall.setLabel("Install software immediately");
                arifInstall.setDescription("Eager to start internship");
                arifInstall.setOutcome(
                                "Spyware installed. Banking credentials, crypto wallets, passwords all stolen. Identity theft initiated.");
                arifInstall.setDefenderScoreDelta(-65);
                arifInstall.setAttackerScoreDelta(50);
                arifInstall.setChoiceType("WRONG");
                arifInstall.setEducationalNote(
                                "MAJOR BREACH: Never download .exe files from emails. Legitimate companies use official app stores or verified portals.");
                arifInstall.setAttackOption(fakeSoftware);
                defenderChoiceRepository.save(arifInstall);

                DefenderChoice arifAskIT = new DefenderChoice();
                arifAskIT.setLabel("Ask for official app store link");
                arifAskIT.setDescription("Request legitimate download source");
                arifAskIT.setOutcome(
                                "Scammer can't provide official source. Arif realizes entire job was fake. Bullet dodged.");
                arifAskIT.setDefenderScoreDelta(45);
                arifAskIT.setAttackerScoreDelta(-40);
                arifAskIT.setChoiceType("CORRECT");
                arifAskIT.setEducationalNote(
                                "SMART QUESTION: Professional software is distributed through official channels, never direct .exe files.");
                arifAskIT.setAttackOption(fakeSoftware);
                defenderChoiceRepository.save(arifAskIT);

                DefenderChoice arifVerifyCompany = new DefenderChoice();
                arifVerifyCompany.setLabel("Search company on official registries");
                arifVerifyCompany.setDescription("Verify company legitimacy");
                arifVerifyCompany.setOutcome(
                                "TechCorp doesn't exist in business registries. Entire operation is elaborate scam. Reports to cybercrime unit.");
                arifVerifyCompany.setDefenderScoreDelta(60);
                arifVerifyCompany.setAttackerScoreDelta(-50);
                arifVerifyCompany.setChoiceType("CORRECT");
                arifVerifyCompany.setEducationalNote(
                                "DUE DILIGENCE: Check company registration, reviews, LinkedIn presence before accepting offers. Scammers create elaborate facades.");
                arifVerifyCompany.setAttackOption(fakeSoftware);
                defenderChoiceRepository.save(arifVerifyCompany);

                System.out.println("✓ Level 4 created: Remote Internship Dangers");
        }

        // ========== LEVEL 5: Online Shopping Spree ==========
        private void createLevel5_OnlineShoppingSpree() {
                Level level = new Level();
                level.setName("E-Commerce Fraud Season");
                level.setDescription("Shopping season brings surge in online scams targeting deal-hungry students.");
                level.setDifficulty("HARD");
                level.setEnabled(true);
                level.setOrderIndex(5);
                level.setMaxAttacks(7);
                level = levelRepository.save(level);

                // === DEFENDER PROFILE: Shopaholic Maya ===
                DefenderProfile maya = new DefenderProfile();
                maya.setName("Maya");
                maya.setDescription(
                                "Loves online shopping, often distracted by deals. Medium tech knowledge, impulsive buyer.");
                maya.setAge(21);
                maya.setAgeGroup("YOUNG");
                maya.setOccupation("Marketing Student");
                maya.setTechSavviness("MEDIUM");
                maya.setMentalState("DISTRACTED");
                maya.setFinancialStatus("STABLE");
                maya.setAvatarIcon("👩‍🎓");
                maya.setLevel(level);
                maya = defenderProfileRepository.save(maya);

                // === ATTACK SCENARIO: Fake Shopping Website ===
                AttackScenario fakeStore = new AttackScenario();
                fakeStore.setAttackType("FAKE_WEBSITE");
                fakeStore.setName("Cloned E-Commerce Site");
                fakeStore.setDescription("Fake website mimicking popular online store");
                fakeStore.setAttackerNarrative(
                                "Create lookalike site with incredible deals. Steal credit card information and never deliver products.");
                fakeStore.setLevel(level);
                fakeStore = attackScenarioRepository.save(fakeStore);

                // Attack Option: iPhone 80% Off
                AttackOption iPhoneDeal = new AttackOption();
                iPhoneDeal.setLabel("iPhone 15 Pro - 80% OFF!");
                iPhoneDeal.setDescription("Too-good-to-be-true deal on social media");
                iPhoneDeal.setAttackerMessage(
                                "FLASH SALE! iPhone 15 Pro only 15,000 Taka! Limited stock. Shop now: amaz0n-deals.com");
                iPhoneDeal.setImpersonatedEntity("Amazon");
                iPhoneDeal.setBaseAttackerPoints(40);
                iPhoneDeal.setRiskLevel(4);
                iPhoneDeal.setCriticalRisk(true);
                iPhoneDeal.setAttackScenario(fakeStore);
                iPhoneDeal = attackOptionRepository.save(iPhoneDeal);

                // Maya's choices (DISTRACTED = vulnerable to impulse)
                DefenderChoice mayaBuyNow = new DefenderChoice();
                mayaBuyNow.setLabel("Buy immediately before stock runs out");
                mayaBuyNow.setDescription("Impulsive purchase without checking");
                mayaBuyNow.setOutcome(
                                "Enters credit card info. Money stolen, no iPhone received. Fake site disappears.");
                mayaBuyNow.setDefenderScoreDelta(-50);
                mayaBuyNow.setAttackerScoreDelta(40);
                mayaBuyNow.setChoiceType("WRONG");
                mayaBuyNow.setEducationalNote(
                                "CLASSIC TRAP: Urgency + huge discount = scam. Legitimate retailers don't offer 80% off new products.");
                mayaBuyNow.setAttackOption(iPhoneDeal);
                defenderChoiceRepository.save(mayaBuyNow);

                DefenderChoice mayaCheckURL = new DefenderChoice();
                mayaCheckURL.setLabel("Notice suspicious URL spelling");
                mayaCheckURL.setDescription("Spot the '0' instead of 'o' in amazon");
                mayaCheckURL.setOutcome(
                                "Realizes amaz0n is not amazon. Checks real Amazon - normal price. Scam avoided.");
                mayaCheckURL.setDefenderScoreDelta(45);
                mayaCheckURL.setAttackerScoreDelta(-35);
                mayaCheckURL.setChoiceType("CORRECT");
                mayaCheckURL.setEducationalNote(
                                "ATTENTION TO DETAIL: Typosquatting is common. Always verify exact URL spelling.");
                mayaCheckURL.setAttackOption(iPhoneDeal);
                defenderChoiceRepository.save(mayaCheckURL);

                DefenderChoice mayaReadReviews = new DefenderChoice();
                mayaReadReviews.setLabel("Search for site reviews first");
                mayaReadReviews.setDescription("Do due diligence before purchase");
                mayaReadReviews.setOutcome(
                                "Finds hundreds of scam complaints about amaz0n-deals. Reports to fraud authorities.");
                mayaReadReviews.setDefenderScoreDelta(50);
                mayaReadReviews.setAttackerScoreDelta(-40);
                mayaReadReviews.setChoiceType("CORRECT");
                mayaReadReviews.setEducationalNote(
                                "BEST PRACTICE: Always check reviews on independent sites before shopping from new stores.");
                mayaReadReviews.setAttackOption(iPhoneDeal);
                defenderChoiceRepository.save(mayaReadReviews);

                // === ATTACK SCENARIO: Fake Payment Gateway ===
                AttackScenario paymentScam = new AttackScenario();
                paymentScam.setAttackType("PHISHING_EMAIL");
                paymentScam.setName("Payment Verification Scam");
                paymentScam.setDescription("Fake payment issue email stealing banking credentials");
                paymentScam.setAttackerNarrative(
                                "Send email claiming payment failed. Link to fake banking page to harvest credentials.");
                paymentScam.setLevel(level);
                paymentScam = attackScenarioRepository.save(paymentScam);

                // Attack Option: Payment Failed Alert
                AttackOption paymentFailed = new AttackOption();
                paymentFailed.setLabel("URGENT: Payment Verification Required");
                paymentFailed.setDescription("Fake bank security alert");
                paymentFailed.setAttackerMessage(
                                "Your bank blocked a suspicious transaction. Verify your identity within 24 hours: [fake-bank-link] or account will be frozen.");
                paymentFailed.setImpersonatedEntity("Bank Security");
                paymentFailed.setBaseAttackerPoints(45);
                paymentFailed.setRiskLevel(5);
                paymentFailed.setCriticalRisk(true);
                paymentFailed.setAttackScenario(paymentScam);
                paymentFailed = attackOptionRepository.save(paymentFailed);

                // Maya's choices
                DefenderChoice mayaClickVerify = new DefenderChoice();
                mayaClickVerify.setLabel("Click link and enter credentials");
                mayaClickVerify.setDescription("Panic about frozen account");
                mayaClickVerify.setOutcome(
                                "Banking credentials stolen. Account drained within hours. Multiple fraudulent transactions made.");
                mayaClickVerify.setDefenderScoreDelta(-60);
                mayaClickVerify.setAttackerScoreDelta(45);
                mayaClickVerify.setChoiceType("WRONG");
                mayaClickVerify.setEducationalNote(
                                "CRITICAL ERROR: Banks NEVER ask for credentials via email. Always contact bank directly through official numbers.");
                mayaClickVerify.setAttackOption(paymentFailed);
                defenderChoiceRepository.save(mayaClickVerify);

                DefenderChoice mayaCallBank = new DefenderChoice();
                mayaCallBank.setLabel("Call bank's official hotline");
                mayaCallBank.setDescription("Verify through trusted channel");
                mayaCallBank.setOutcome(
                                "Bank confirms no issue with account, no email sent. Maya reports phishing attempt.");
                mayaCallBank.setDefenderScoreDelta(50);
                mayaCallBank.setAttackerScoreDelta(-40);
                mayaCallBank.setChoiceType("CORRECT");
                mayaCallBank.setEducationalNote(
                                "CORRECT RESPONSE: For financial matters, always contact institutions directly using official contact info.");
                mayaCallBank.setAttackOption(paymentFailed);
                defenderChoiceRepository.save(mayaCallBank);

                DefenderChoice mayaCheckApp = new DefenderChoice();
                mayaCheckApp.setLabel("Check official banking app");
                mayaCheckApp.setDescription("Verify through official app");
                mayaCheckApp.setOutcome("App shows no alerts or issues. Email identified as scam and reported.");
                mayaCheckApp.setDefenderScoreDelta(55);
                mayaCheckApp.setAttackerScoreDelta(-45);
                mayaCheckApp.setChoiceType("CORRECT");
                mayaCheckApp.setEducationalNote(
                                "SMART MOVE: Official apps show real account status. Use them instead of clicking email links.");
                mayaCheckApp.setAttackOption(paymentFailed);
                defenderChoiceRepository.save(mayaCheckApp);

                System.out.println("✓ Level 5 created: E-Commerce Fraud Season");
        }
}
