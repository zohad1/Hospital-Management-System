# 🏥 Hospital Management System

[![License: MIT](https://img.shields.io/badge/License-MIT-teal.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://adoptium.net/)
[![JavaFX](https://img.shields.io/badge/JavaFX-17.0.6-blue.svg)](https://openjfx.io/)

A **JavaFX desktop application** for managing hospital operations — from patient appointments and doctor schedules to ward management and admin controls.

---

## ✨ Features

| Portal | Capabilities |
|---|---|
| 👤 **Patient** | Book appointments, view appointment history, make payments |
| 🩺 **Doctor** | View assigned appointments, manage patient records |
| 💉 **Nurse** | Manage ward assignments, view patients, check doctor schedules |
| 🔧 **Admin** | Manage doctors, nurses, patients, rooms, and appointment times |

- **Multi-role login** — dedicated portals for Admin, Doctor, Nurse, and Patient
- **Professional UI** — clean teal/white design with a shared CSS stylesheet

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| UI Framework | JavaFX 17.0.6 + FXML |
| Build Tool | Maven (`mvnw` wrapper) |
| Database | MySQL (via XAMPP) |
| JDBC Driver | MySQL Connector/J 8.3.0 |

---

## 📋 Prerequisites

- [Java 17](https://adoptium.net/) (Eclipse Temurin recommended)
- [XAMPP](https://www.apachefriends.org/) with MySQL running
- A MySQL database named `hospitalmanagementsystem`

---

## 🚀 Setup

### 1. Clone the repository

```bash
git clone https://github.com/zohad1/Hospital-Management-System.git
cd Hospital-Management-System
```

### 2. Start MySQL

Open the **XAMPP Control Panel** and start the **MySQL** module.

### 3. Import the database

1. Open [phpMyAdmin](http://localhost/phpmyadmin)
2. Create a new schema named `hospitalmanagementsystem`
3. Import your `.sql` dump file into that schema

### 4. Configure credentials *(optional — defaults work for standard XAMPP)*

```bash
# Windows
set DB_URL=jdbc:mysql://127.0.0.1:3306/hospitalmanagementsystem
set DB_USER=root
set DB_PASSWORD=yourpassword

# Mac/Linux
export DB_URL=jdbc:mysql://127.0.0.1:3306/hospitalmanagementsystem
export DB_USER=root
export DB_PASSWORD=yourpassword
```

### 5. Run the application

```bash
# Windows
.\mvnw.cmd javafx:run

# Mac / Linux
./mvnw javafx:run
```

---

## 📁 Project Structure

```
src/main/
├── java/com/example/oop_lab_project/
│   ├── Main.java                  # App entry point
│   ├── Database.java              # Centralized DB connection
│   └── *Controller.java           # 15 FXML controllers (one per screen)
│
└── resources/com/example/oop_lab_project/
    ├── styles.css                 # Shared stylesheet
    └── *.fxml                     # All UI screen layouts
```

---

## 🔐 Roles & Login

| Role | Login Screen |
|---|---|
| **Admin** | Faculty Login → select *Admin* |
| **Doctor** | Faculty Login → select *Doctor* |
| **Nurse** | Faculty Login → select *Nurse* |
| **Patient** | Patient Login |

> Credentials are stored in the MySQL database. New patients can register via the **Sign Up** screen.

---

## 📸 Screenshots

> *Coming soon*

---

## 📝 Notes

- Built as an **OOP course project**, demonstrating real-world application of object-oriented principles in Java.
- The `mvnw` wrapper means you **don't need Maven installed globally** — it downloads the correct version automatically on first run.
- If you run into JavaFX module errors, make sure you're using **Java 17** and not a higher version, as JavaFX compatibility can vary.

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for full details.

You're free to use, copy, modify, and distribute this project, as long as you include the original copyright notice. Built as an OOP course project — feel free to fork and learn from it.
