# CyberWalk Game Levels Documentation

## Overview
This document describes all 5 comprehensive levels created for the CyberWalk cyber attack simulation game. Each level features unique defender profiles with specific vulnerabilities and strengths, realistic attack scenarios, and multiple defender choices with educational outcomes.

---

## 🎮 Game Mechanics

### Defender Profiles
Each level includes unique defender profiles with:
- **Demographics**: Name, age, occupation, avatar
- **Psychological State**: Mental state (calm, stressed, anxious, distracted)
- **Technical Skills**: Tech savviness (low, medium, high)
- **Financial Situation**: Financial status (stable, struggling, wealthy)

### Attack System
Attackers choose from multiple attack scenarios, each containing:
- **Attack Options**: Specific impersonation tactics
- **Risk/Reward**: Points and risk levels
- **Targeted Vulnerabilities**: Exploit defender weaknesses

### Defender Choices
For each attack, defenders choose responses that:
- **Award/Deduct Points**: Based on correctness
- **Provide Education**: Explain why choices are right/wrong
- **Reflect Realism**: Match defender's profile and vulnerabilities

---

## 📚 Level 1: University Hostel Life
**Difficulty**: EASY  
**Max Attacks**: 5  
**Setting**: Student living in hostel away from home

### Defender Profiles

#### 1. **Sifat** 👨‍🎓
- **Age**: 21 | **Occupation**: Computer Science Student
- **Tech Savviness**: HIGH | **Mental State**: STRESSED
- **Financial Status**: STRUGGLING
- **Key Characteristics**: 
  - Lives in hostel, family in Dhaka (far away)
  - Tech-savvy but emotionally vulnerable when family mentioned
  - Strong technical analysis skills
  - Financial desperation creates vulnerability

**Sifat's Vulnerabilities**:
- ❌ **Family Emergency Calls**: Can't verify in person, panic-prone
- ❌ **Scholarship Scams**: Financial need makes him susceptible
- ✅ **Friend Impersonation**: Knows friends well, can detect fakes
- ✅ **Technical Attacks**: High tech skills help identify fake WiFi, analyze emails

#### 2. **Riya** 👩‍🎓
- **Age**: 20 | **Occupation**: Business Student
- **Tech Savviness**: MEDIUM | **Mental State**: CALM
- **Financial Status**: STABLE
- **Key Characteristics**:
  - Lives close to campus, family-oriented
  - Moderate tech skills, relies on peer verification
  - Calm decision-making process

### Attack Scenarios

#### Attack 1: Family Emergency Call (FAKE_CALL)
**Attacker Strategy**: Emotional manipulation targeting students away from home

**Attack Option 1.1: Call as Mother** ⚠️ CRITICAL RISK
- **Message**: "Beta, I'm in hospital! Need 50,000 Taka urgently!"
- **Impersonates**: Mother
- **Attacker Points**: 30 | **Risk Level**: 4/5

**Sifat's Choices**:
1. ❌ **Send money immediately** (-40 defender, +30 attacker)
   - *Vulnerability: Lives far, can't verify. Emotional panic.*
   - *Lesson: Always verify emergency calls through alternative contacts*

2. ✅ **Call father to verify** (+25 defender, -15 attacker)
   - *Strength: Cross-verification prevents scam*
   - *Lesson: Scammers rely on panic and isolation*

3. ✅ **Demand video call first** (+30 defender, -20 attacker)
   - *Strength: Video calls hard to fake*
   - *Lesson: Best practice for identity verification*

**Attack Option 1.2: Call as Best Friend Rahim**
- **Message**: "Bro, got arrested! Need 30,000 bail money!"
- **Impersonates**: Best Friend
- **Attacker Points**: 15 | **Risk Level**: 2/5

**Sifat's Choices**:
1. ❌ **Send money to help** (-25 defender, +15 attacker)
   - *Lesson: Even friends' requests need verification*

2. ✅ **Ask verification question** (+35 defender, -25 attacker)
   - *Strength: Knows friend well, personal questions expose imposters*
   - *Lesson: Shared knowledge provides authentication*

#### Attack 2: Evil Twin WiFi (FAKE_WIFI)
**Attacker Strategy**: Set up rogue WiFi mimicking hostel network

**Attack Option 2.1: "Hostel_WiFi_Free" Network**
- **Message**: Free WiFi with no password
- **Impersonates**: University IT Department
- **Attacker Points**: 20 | **Risk Level**: 3/5

**Sifat's Choices** (HIGH tech savviness advantage):
1. ❌ **Connect immediately** (-30 defender, +20 attacker)
   - *Lesson: Free WiFi is tempting but dangerous*

2. ✅ **Check with hostel admin** (+30 defender, -20 attacker)
   - *Strength: Tech savviness prompts questioning*
   - *Lesson: Verify network authenticity with authorities*

3. ✅ **Use mobile data instead** (+25 defender, -15 attacker)
   - *Lesson: When in doubt, use trusted connections*

**Riya's Choices** (MEDIUM tech savviness):
1. ❌ **Connect - seems legitimate** (-25 defender, +20 attacker)
   - *Vulnerability: Moderate tech skills, trusts appearances*

2. ✅ **Ask roommate verification** (+20 defender, -10 attacker)
   - *Strength: Seeks peer knowledge when uncertain*

#### Attack 3: Scholarship Scam (PHISHING_EMAIL)
**Attacker Strategy**: Target struggling students with fake opportunities

**Attack Option 3.1: Gov Scholarship Email** ⚠️ CRITICAL RISK
- **Message**: "Pre-selected for 100,000 Taka! Submit bank details in 24hrs"
- **Impersonates**: Government Education Ministry
- **Attacker Points**: 25 | **Risk Level**: 4/5

**Sifat's Choices** (Financial struggle vulnerability):
1. ❌ **Submit details immediately** (-45 defender, +25 attacker)
   - *Vulnerability: Financial desperation overrides judgment*
   - *Lesson: Legitimate scholarships never ask for bank details upfront*

2. ✅ **Check official ministry website** (+35 defender, -20 attacker)
   - *Strength: Tech knowledge prompts verification despite need*
   - *Lesson: Always verify through official sources*

3. ✅ **Analyze email headers** (+40 defender, -25 attacker)
   - *Strength: HIGH tech savviness enables technical analysis*
   - *Lesson: Email headers often expose fraud*

---

## 📚 Level 2: Campus Wide Cyber Attack
**Difficulty**: MEDIUM  
**Max Attacks**: 6  
**Setting**: Coordinated attacks during exam week targeting different demographics

### Defender Profiles

#### 1. **Dr. Rahman** 👨‍🏫
- **Age**: 58 | **Occupation**: University Professor
- **Tech Savviness**: LOW | **Mental State**: STRESSED
- **Financial Status**: STABLE
- **Key Characteristics**:
  - Senior professor, respected academic
  - Struggles with modern technology
  - Stressed during exam periods
  - Relies on IT support and traditional methods

**Dr. Rahman's Vulnerabilities**:
- ❌ **Technical Attacks**: Low tech skills make phishing effective
- ❌ **Exam Period Stress**: Rushed decisions during busy times
- ✅ **Authority Verification**: Willing to ask IT for help
- ✅ **Traditional Verification**: Uses bookmarks, phone calls

#### 2. **Nadia** 👩‍💻
- **Age**: 24 | **Occupation**: Computer Lab Assistant
- **Tech Savviness**: HIGH | **Mental State**: CALM
- **Financial Status**: STABLE
- **Key Characteristics**:
  - Handles campus IT issues daily
  - Expert-level technical analysis
  - Proactive security awareness
  - Calm, methodical decision-making

### Attack Scenarios

#### Attack 1: Cloned University Portal (FAKE_WEBSITE)
**Attacker Strategy**: Fake grade submission portal during exam week

**Attack Option 1.1: Urgent Grade Submission Email** ⚠️ CRITICAL RISK
- **Message**: "Grade submission deadline extended. Login: univ-portal-grades.com"
- **Impersonates**: University Registrar
- **Attacker Points**: 40 | **Risk Level**: 5/5

**Dr. Rahman's Choices** (LOW tech + STRESSED = vulnerable):
1. ❌ **Login quickly to submit** (-60 defender, +40 attacker)
   - *Vulnerability: Stress + low tech skills = perfect target*
   - *Lesson: Always verify urgent emails, especially during busy periods*

2. ✅ **Call IT department first** (+40 defender, -30 attacker)
   - *Strength: Knows to ask experts when uncertain*
   - *Lesson: IT departments expect verification calls*

3. ✅ **Check URL vs bookmarked site** (+50 defender, -35 attacker)
   - *Strength: Basic URL checking saves day*
   - *Lesson: Bookmark legitimate sites for comparison*

**Nadia's Choices** (HIGH tech + CALM = strong):
1. ✅ **Inspect email source/headers** (+55 defender, -40 attacker)
   - *Strength: Expert-level analysis, protects community*
   - *Lesson: Technical analysis prevents widespread attacks*

#### Attack 2: Infected USB Drive (USB_DROP)
**Attacker Strategy**: Drop USBs labeled "Exam Question Bank" in library

**Attack Option 2.1: Midterm Questions USB** ⚠️ CRITICAL RISK
- **Label**: "MIDTERM EXAM QUESTIONS - ALL DEPARTMENTS - 2024"
- **Payload**: Keylogger + Ransomware
- **Attacker Points**: 35 | **Risk Level**: 5/5

**Mixed Student Choices**:
1. ❌ **Plug in immediately** (-50 defender, +35 attacker)
   - *Vulnerability: Exam stress makes students desperate*
   - *Lesson: NEVER plug unknown USB devices*

2. ✅ **Turn to lost & found** (+40 defender, -30 attacker)
   - *Lesson: Report found USBs to security*

3. ✅ **Scan in virtual machine** (+60 defender, -45 attacker)
   - *Strength: Expert-level security practice*
   - *Lesson: VMs safely inspect unknown devices*

#### Attack 3: Fake IT Support Call (SOCIAL_ENGINEERING)
**Attacker Strategy**: Request remote access to "remove virus"

**Attack Option 3.1: Urgent Virus Alert Call** ⚠️ CRITICAL RISK
- **Message**: "IT Security detected virus. Install TeamViewer for removal"
- **Impersonates**: Campus IT Support
- **Attacker Points**: 30 | **Risk Level**: 4/5

**Dr. Rahman's Choices**:
1. ❌ **Install remote access tool** (-55 defender, +30 attacker)
   - *Vulnerability: Trusts authority, low tech awareness*
   - *Lesson: Legitimate IT never requests unsolicited remote access*

2. ✅ **Hang up, call IT directly** (+45 defender, -25 attacker)
   - *Lesson: GOLD STANDARD - verify through official numbers*

**Nadia's Choices**:
1. ✅ **Ask for IT ticket number** (+50 defender, -35 attacker)
   - *Strength: Knows legitimate IT uses ticket systems*
   - *Lesson: Simple question exposes most scams*

---

## 📚 Level 3: Exam Season Exploitation
**Difficulty**: MEDIUM  
**Max Attacks**: 5  
**Setting**: Finals week, attackers exploit stressed students

### Defender Profile

#### **Tina** 👩‍💻
- **Age**: 22 | **Occupation**: Engineering Student
- **Tech Savviness**: MEDIUM | **Mental State**: ANXIOUS
- **Financial Status**: STRUGGLING
- **Key Characteristics**:
  - Finals week pressure
  - Sleep-deprived, making poor decisions
  - Desperate for exam help
  - Usually competent but stress impairs judgment

**Tina's Vulnerabilities**:
- ❌ **Exam-Related Urgency**: Panic about schedule changes
- ❌ **Study Material Desperation**: Downloads suspicious files
- ✅ **Peer Verification**: Uses classmate network
- ✅ **Traditional Checks**: Noticeboard verification

### Attack Scenarios

#### Attack 1: Fake Exam Postponement (PHISHING_EMAIL)
**Attacker Strategy**: Fake schedule changes requiring portal login

**Attack Option 1.1: Exam Postponed Email** ⚠️ CRITICAL RISK
- **Message**: "CS301 exam postponed. Login to see new date: exam-schedule-update.net"
- **Impersonates**: Exam Controller Office
- **Attacker Points**: 35 | **Risk Level**: 4/5

**Tina's Choices** (ANXIOUS = impaired judgment):
1. ❌ **Login immediately** (-45 defender, +35 attacker)
   - *Vulnerability: Anxiety clouds judgment*
   - *Lesson: Breathe and verify before acting on urgent emails*

2. ✅ **Check official noticeboard** (+40 defender, -30 attacker)
   - *Strength: Traditional verification methods work*
   - *Lesson: Physical noticeboards are reliable*

3. ✅ **Ask classmates in group** (+35 defender, -25 attacker)
   - *Strength: Community defense, scammers can't target everyone*
   - *Lesson: Peer verification is powerful*

#### Attack 2: Malicious Study Notes (MALWARE)
**Attacker Strategy**: Infected PDF as premium study material

**Attack Option 2.1: Premium Notes PDF** ⚠️ CRITICAL RISK
- **Message**: "Premium solved question bank from senior: [malicious-link.pdf]"
- **Payload**: Ransomware
- **Attacker Points**: 40 | **Risk Level**: 5/5

**Tina's Choices**:
1. ❌ **Download and open** (-60 defender, +40 attacker)
   - *Vulnerability: Desperation + exam pressure = disaster*
   - *Lesson: Never download from unknown sources before exams*

2. ✅ **Scan with antivirus first** (+45 defender, -35 attacker)
   - *Strength: Basic security habit prevents attack*
   - *Lesson: Always scan downloads with updated antivirus*

3. ✅ **Verify source/question sender** (+40 defender, -30 attacker)
   - *Strength: Critical thinking despite stress*
   - *Lesson: Premium/leaked materials usually scams*

---

## 📚 Level 4: Remote Internship Dangers
**Difficulty**: HARD  
**Max Attacks**: 6  
**Setting**: Job hunting season, targeting desperate graduates

### Defender Profile

#### **Arif** 👨‍💼
- **Age**: 23 | **Occupation**: Final Year Student
- **Tech Savviness**: MEDIUM | **Mental State**: ANXIOUS
- **Financial Status**: STRUGGLING
- **Key Characteristics**:
  - Desperately job hunting
  - Financially struggling, takes risks
  - Eager to prove himself
  - Willing to overlook red flags for opportunities

**Arif's Vulnerabilities**:
- ❌ **Job Desperation**: Overlooks warning signs
- ❌ **Financial Pressure**: Pays upfront fees
- ✅ **Research Skills**: Can verify companies
- ✅ **Technical Awareness**: Checks email domains

### Attack Scenarios

#### Attack 1: Fake Job Offer (PHISHING_EMAIL)
**Attacker Strategy**: Too-good-to-be-true jobs requiring fees

**Attack Option 1.1: Google Internship Scam** ⚠️ CRITICAL RISK
- **Message**: "Pre-selected for Google internship! $8000/month. Pay $500 registration"
- **Impersonates**: Google HR
- **Attacker Points**: 45 | **Risk Level**: 5/5

**Arif's Choices** (STRUGGLING + ANXIOUS = very vulnerable):
1. ❌ **Pay fee immediately** (-70 defender, +45 attacker)
   - *Vulnerability: Financial desperation ruthlessly exploited*
   - *Lesson: CRITICAL - Legitimate companies NEVER charge job fees*

2. ✅ **Research Google's process** (+50 defender, -40 attacker)
   - *Strength: Verification through official career pages*
   - *Lesson: Company websites describe legitimate procedures*

3. ✅ **Verify sender email domain** (+55 defender, -45 attacker)
   - *Strength: Technical check reveals gmail.com vs google.com*
   - *Lesson: Corporate emails use official domains*

#### Attack 2: Fake Remote Software (SOCIAL_ENGINEERING)
**Attacker Strategy**: Malicious "company software" installation

**Attack Option 2.1: Install Company Tool** ⚠️ CRITICAL RISK
- **Message**: "Install TechCorp RemoteAccess Pro: techcorp-tools.exe (Mandatory)"
- **Payload**: Spyware (banking, crypto, passwords)
- **Attacker Points**: 50 | **Risk Level**: 5/5

**Arif's Choices**:
1. ❌ **Install immediately** (-65 defender, +50 attacker)
   - *Vulnerability: Eagerness leads to major breach*
   - *Lesson: Never download .exe from emails*

2. ✅ **Ask for app store link** (+45 defender, -40 attacker)
   - *Strength: Questions distribution method*
   - *Lesson: Professional software uses official channels*

3. ✅ **Verify company in registries** (+60 defender, -50 attacker)
   - *Strength: Due diligence exposes elaborate scam*
   - *Lesson: Check company registration, reviews, LinkedIn*

---

## 📚 Level 5: E-Commerce Fraud Season
**Difficulty**: HARD  
**Max Attacks**: 7  
**Setting**: Shopping season with targeted scams

### Defender Profile

#### **Maya** 👩‍🎓
- **Age**: 21 | **Occupation**: Marketing Student
- **Tech Savviness**: MEDIUM | **Mental State**: DISTRACTED
- **Financial Status**: STABLE
- **Key Characteristics**:
  - Loves online shopping
  - Impulsive buyer
  - Often distracted by deals
  - Moderate tech awareness

**Maya's Vulnerabilities**:
- ❌ **Impulse Buying**: Acts without checking
- ❌ **Deal Distraction**: Too-good-to-be-true offers
- ✅ **Research Habit**: Can check reviews
- ✅ **Official Apps**: Uses banking apps for verification

### Attack Scenarios

#### Attack 1: Cloned E-Commerce Site (FAKE_WEBSITE)
**Attacker Strategy**: Fake store with incredible deals

**Attack Option 1.1: iPhone 80% Off** ⚠️ CRITICAL RISK
- **Message**: "iPhone 15 Pro only 15,000 Taka! Shop: amaz0n-deals.com"
- **Impersonates**: Amazon
- **Attacker Points**: 40 | **Risk Level**: 4/5

**Maya's Choices** (DISTRACTED = impulsive):
1. ❌ **Buy immediately** (-50 defender, +40 attacker)
   - *Vulnerability: Urgency + huge discount = trap*
   - *Lesson: Legitimate retailers don't offer 80% off new products*

2. ✅ **Notice suspicious URL** (+45 defender, -35 attacker)
   - *Strength: Attention to detail spots typosquatting*
   - *Lesson: Always verify exact URL spelling*

3. ✅ **Search site reviews** (+50 defender, -40 attacker)
   - *Strength: Due diligence before purchase*
   - *Lesson: Check independent review sites*

#### Attack 2: Payment Verification Scam (PHISHING_EMAIL)
**Attacker Strategy**: Fake bank security alert

**Attack Option 2.1: Payment Failed Alert** ⚠️ CRITICAL RISK
- **Message**: "Bank blocked transaction. Verify identity or account frozen: [fake-bank-link]"
- **Impersonates**: Bank Security
- **Attacker Points**: 45 | **Risk Level**: 5/5

**Maya's Choices**:
1. ❌ **Click and enter credentials** (-60 defender, +45 attacker)
   - *Vulnerability: Panic about frozen account*
   - *Lesson: Banks NEVER ask credentials via email*

2. ✅ **Call bank's official hotline** (+50 defender, -40 attacker)
   - *Strength: Verifies through trusted channel*
   - *Lesson: Contact institutions directly for financial matters*

3. ✅ **Check official banking app** (+55 defender, -45 attacker)
   - *Strength: App shows real account status*
   - *Lesson: Use official apps instead of email links*

---

## 🎯 Game Design Philosophy

### Dynamic Vulnerability System
Each defender profile has:
- **Contextual Weaknesses**: Based on their situation (e.g., Sifat vulnerable to family calls because he lives far)
- **Contextual Strengths**: Based on their skills (e.g., Sifat strong against tech attacks due to CS background)
- **Psychological Realism**: Mental state affects decision quality
- **Financial Motivation**: Money situation influences risk-taking

### Educational Focus
Every choice includes:
- **Immediate Outcome**: What happens in the scenario
- **Point Changes**: Scoring for both attacker and defender
- **Educational Note**: Explains the cybersecurity principle
- **Vulnerability/Strength Tags**: Highlights profile characteristics at play

### Scoring System
- **Defender Score Delta**: -70 to +60 points
  - Negative = Poor security decision
  - Positive = Good security practice
- **Attacker Score Delta**: -50 to +50 points
  - Positive = Successful exploitation
  - Negative = Failed attack

### Difficulty Progression
1. **Level 1 (EASY)**: Basic attacks, clear warning signs
2. **Level 2 (MEDIUM)**: Multiple demographics, coordinated attacks
3. **Level 3 (MEDIUM)**: Psychological pressure, time constraints
4. **Level 4 (HARD)**: Elaborate scams, professional impersonation
5. **Level 5 (HARD)**: Financial attacks, sophisticated phishing

---

## 🔑 Key Learning Objectives

### Technical Skills
- URL verification and typosquatting detection
- Email header analysis
- Domain authentication
- Safe file handling practices
- Network security awareness

### Behavioral Skills
- Verification before action
- Calm decision-making under pressure
- Seeking expert help when uncertain
- Peer consultation and community defense
- Traditional backup verification methods

### Vulnerability Awareness
- How stress impairs judgment
- Financial desperation exploitation
- Emotional manipulation tactics
- Authority impersonation dangers
- Urgency as manipulation tool

### Defense Strategies
- Always verify through official channels
- Use multiple verification methods
- Don't trust unsolicited communications
- Research before financial commitments
- Maintain updated security tools
- Build security-aware communities

---

## 📊 Profile Vulnerability Matrix

| Profile | Tech Skills | Mental State | Financial | Key Vulnerability | Key Strength |
|---------|-------------|--------------|-----------|-------------------|--------------|
| Sifat | HIGH | STRESSED | STRUGGLING | Family emergencies (distance) | Technical analysis |
| Riya | MEDIUM | CALM | STABLE | Trust in appearances | Peer verification |
| Dr. Rahman | LOW | STRESSED | STABLE | Technical complexity | Asks experts |
| Nadia | HIGH | CALM | STABLE | None (expert) | All technical |
| Tina | MEDIUM | ANXIOUS | STRUGGLING | Exam pressure | Community checks |
| Arif | MEDIUM | ANXIOUS | STRUGGLING | Job desperation | Research skills |
| Maya | MEDIUM | DISTRACTED | STABLE | Impulse buying | Review checking |

---

## 🎓 Conclusion

This comprehensive level system provides:
- **20+ unique attack scenarios**
- **7 diverse defender profiles**
- **100+ decision points**
- **Realistic vulnerability modeling**
- **Educational cybersecurity principles**
- **Dynamic, context-aware gameplay**

Each scenario is designed to teach specific cybersecurity concepts while maintaining engaging gameplay through realistic character development and situational awareness.
