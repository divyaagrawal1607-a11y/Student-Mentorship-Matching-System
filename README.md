# MentorBridge — Student Mentorship Matching System

**Project Type:** NSP Java Mini Project  
**Student Name:** Divya Agarwal  
**Platform:** Windows 10 / 11  
**Language:** Java 17  
**GUI:** Java Swing (Modern FlatLaf Navy & Blue Theme)  
**Database:** MySQL / Resilient Hybrid Storage with JDBC  
**Build Tool:** Apache Maven  

---

## 1. Project Overview

**MentorBridge** is a comprehensive, production-grade desktop application engineered to streamline student-mentor connections in university and college environments. It pairs students seeking academic and career guidance with suitable faculty and industry mentors through an **intelligent multi-factor keyword matching algorithm**.

### Core Modules:
- **Role-Based Authentication & Security:** Secure BCrypt password hashing for Administrators, Students, and Mentors with self-service registration.
- **Admin Dashboard:** Real-time metrics tracking total students, active mentors, pending/accepted requests, and completed sessions with an audit trail.
- **Student Directory:** Full CRUD operations with comprehensive data validation (email regex, phone formatting, academic study year 1-6).
- **Mentor Directory:** Full profile management including expertise keywords, years of experience, availability schedules, and biographies.
- **Intelligent Mentor Matching Engine:** Case-insensitive NLP keyword tokenization comparing student interests and career aspirations against mentor expertise, ranking candidates by percentage compatibility score with transparent rationale.
- **Mentorship Request Workflow:** Multi-state lifecycle management (`PENDING` -> `ACCEPTED` / `REJECTED` / `CANCELLED`) with transition rules.
- **Session Scheduling:** Meeting calendar with date-time validation, meeting mode (Online, In-Person, Hybrid), location/video links, and meeting notes.
- **Reviews & Feedback:** 1-5 star ratings and reviews for completed mentorship meetings, preventing duplicate reviews and computing aggregate scores.
- **Reporting & Data Export:** One-click CSV export engine for students, mentors, sessions, and feedback records.

---

## 2. Default Demo Accounts

For demonstration and viva evaluation, the following pre-configured accounts are provided (all passwords hashed with BCrypt):

| Role | Name | Email | Default Password | Notes |
| :--- | :--- | :--- | :--- | :--- |
| **ADMIN** | System Administrator | `admin@mentorbridge.edu` | `admin123` | Full dashboard, user control & reports |
| **STUDENT** | Divya Agarwal | `divya.agarwal@student.edu` | `student123` | Java, Spring Boot, Cloud Computing |
| **STUDENT** | Aarav Sharma | `aarav.sharma@student.edu` | `student123` | Python, Machine Learning, Data Analytics |
| **MENTOR** | Dr. Rajesh Khanna | `rajesh.khanna@faculty.edu` | `mentor123` | Java Architecture, Cloud (15 yrs exp) |
| **MENTOR** | Priya Sunder | `priya.sunder@techcorp.com` | `mentor123` | Full-Stack Development, React (8 yrs exp) |
| **MENTOR** | Vikram Mehta | `vikram.mehta@datasolutions.org` | `mentor123` | Data Science, AI & MLOps (10 yrs exp) |

*Quick login buttons are available directly on the login screen for one-click testing.*

---

## 3. Architecture & Code Structure

```
StudentMentorshipMatchingSystem/
├── database/
│   └── schema.sql                # Idempotent MySQL DDL and seed data script
├── src/
│   ├── main/
│   │   ├── java/com/mentorbridge/
│   │   │   ├── App.java          # Application entry point & theme initialization
│   │   │   ├── config/           # DatabaseConfig (.env loader)
│   │   │   ├── database/         # ConnectionManager & DatabaseInitializer
│   │   │   ├── models/           # User, Student, Mentor, Request, Session, Feedback, MatchResult
│   │   │   ├── dao/              # Full JDBC DAO layer with prepared statements
│   │   │   ├── services/         # Business logic, MatchingService, AuthService, Dashboard
│   │   │   ├── ui/               # MainFrame, LoginFrame, RegisterDialog, Management Panels
│   │   │   └── utils/            # PasswordUtil (BCrypt), ValidationUtil, DateTimeUtil, CsvExporter
│   │   └── resources/            # Icons and configuration templates
│   └── test/java/com/mentorbridge/
│       ├── ValidationUtilTest.java
│       ├── PasswordUtilTest.java
│       └── MatchingServiceTest.java
├── tools/                        # Bundled portable OpenJDK 17 and Maven 3.9
├── .env.example                  # Environment configuration template
├── pom.xml                       # Maven build file with shaded executable JAR
├── setup.bat                     # Automated prerequisite check & compilation
├── run.bat                       # One-click desktop application launcher
└── README.md
```

---

## 4. Intelligent Matching Algorithm

The matching algorithm computes compatibility using a weighted score:
$$\text{Score} = (\text{Skill Overlap Ratio} \times 60\%) + (\text{Career Goal Synergy} \times 25\%) + (\text{Experience Factor} \times 15\%)$$

1. **Tokenization & Stop-word Filtering:** Normalizes input text and eliminates common conversational conjunctions and fillers.
2. **Skill Overlap:** Checks intersection between student interests and mentor expertise tags.
3. **Career Goal Synergy:** Cross-references career objectives with mentor biography and domain specialties.
4. **Availability Filter:** Automatically filters out unavailable or inactive mentors.
5. **Transparency Rationale:** Every recommendation produces an explanation summary describing exactly why the mentor was selected.

---

## 5. One-Click Setup & Launch Instructions

### Option A: One-Click Launch (Recommended)
1. Double-click `run.bat` in the project root.
2. If this is the first run, it will automatically build the executable JAR and launch the GUI.

### Option B: Command Line Build & Run
From Command Prompt or PowerShell:
```cmd
cd StudentMentorshipMatchingSystem
setup.bat
run.bat
```

Or using Maven directly:
```cmd
mvn clean package
java -jar target/student-mentorship-system-1.0.0.jar
```

---

## 6. Database Configuration

MentorBridge reads its database configuration automatically from the `.env` file:
```ini
DB_TYPE=mysql
DB_HOST=localhost
DB_PORT=3306
DB_NAME=mentorship_db
DB_USER=mentor_app
DB_PASSWORD=MentorAppSecurePass2026!
DB_FALLBACK_EMBEDDED=true
```

- When MySQL Server is active on port 3306, MentorBridge connects seamlessly via MySQL Connector/J.
- If MySQL Server is not running or port 3306 is unreachable, MentorBridge automatically activates its persistent embedded engine, ensuring 100% uninterrupted availability without manual reconfiguration.

---

## 7. Author & Academic Information
- **Student:** Divya Agarwal
- **Course/Project:** NSP Java Mini Project
- **Academic Year:** 2026
