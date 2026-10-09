
# PS1: Software Construction – Problem Set 1 (Twitter Micro-System)

![Java](https://img.shields.io/badge/Language-Java-orange)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 📌 Overview
This repository contains the implementation and unit test suite for **Problem Set 1 (PS1)** of the **Software Construction** course at **NUST SEECS**. 

The goal of this assignment is to apply key software construction principles:
* **Abstract Data Types (ADTs)** and specification design.
* **Test-Driven Development (TDD)** using JUnit.
* **Object-oriented design patterns** and clean code practices.
* Handling immutability, defensive copying, and rep invariants.

The core implementation focuses on building a simple social network analysis tool capable of parsing, filtering, and calculating metrics over a collection of tweets.

---

## 📁 Repository Structure

```text
ps1/
├── src/
│   └── twitter/
│       ├── Filter.java         # Filtering algorithms (by author, keywords, etc.)
│       ├── Main.java           # Entry point for execution/demonstration
│       ├── SocialNetwork.java  # Graph generation (influencers, mentions)
│       ├── Timespan.java       # Utility class for time intervals
│       ├── Tweet.java          # Tweet object representation
│       └── TweetReader.java    # Parser/loader for tweet dataset
└── test/
    └── twitter/
        └── SocialNetworkTest.java  # Unit tests for social network metrics

```

---

## 🛠️ Key Features & Modules

### 1. Filtering Module (`Filter.java`)

* `writtenBy`: Filters tweets written by a specific user.
* `containing`: Filters tweets containing specific search keywords (case-insensitive).

### 2. Social Network Metrics (`SocialNetwork.java`)

* `guessFollowsGraph`: Infers a social graph (who follows whom) based on `@mentions` within tweets.
* `influencers`: Ranks users based on their popularity/follower count derived from the inferred graph.

### 3. Immutability & Helper Types

* `Tweet.java` & `Timespan.java`: Immutable data models enforcing representation independence and preventing representation exposure through defensive copying.

---

## 🚀 Getting Started

### Prerequisites

* **Java Development Kit (JDK 8 or higher)**
* **JUnit 4/5** (for running tests)
* **IDE**: Eclipse, IntelliJ IDEA, or VS Code with Java Extension Pack

### Installation & Setup

1. **Clone the repository:**
```bash
git clone [https://github.com/MZunurainTahir/ps1.git](https://github.com/MZunurainTahir/ps1.git)
cd ps1/ps1

```


2. **Compile the source files:**
```bash
javac -d bin src/twitter/*.java

```


3. **Run the application:**
```bash
java -cp bin twitter.Main

```



---

## 🧪 Running Tests

To run the test suite and verify implementation correctness:

* **Using an IDE:** Right-click the `test/twitter/` folder or `SocialNetworkTest.java` and select **Run as JUnit Test**.
* **Using Command Line:**
```bash
javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar -d bin src/twitter/*.java test/twitter/*.java
java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar:bin org.junit.runner.JUnitCore twitter.SocialNetworkTest

```



---

## 📝 License

This project is licensed under the MIT License.

```

```
