# FinancialSimulator

<!-- LOGO DEL PROYECTO -->
<br />
<div align="center">
  <a href="#">
    <img src="https://kotlinlang.org/docs/images/kotlin-logo.png" alt="Logo" width="700">
  </a>
</div>

## About The Project

`Financial Simulator` is a backend application built with **Kotlin** and **Ktor** to showcase advanced backend development skills. This application designed to calculate and simulate loan amortization schedules, with support for exporting results in JSON, CSV, and PDF formats.:

- Calculate and simulate loan amortization schedules fixed-rate.

This project serves as a practical demonstration of my expertise in server-side programming and test.

---

## Features

- **API Integration**:
    - Available endpoints:
        - `/api/loan/amortization/fixed`: Calculate and simulate loan amortization schedules.

- **JSON Serialization**:
    - Efficiently handles data using `kotlinx.serialization`.

- **Test and Mockk**:
    - Development and testing with mockk.

---

## Technologies Used

- **Language**: [Kotlin](https://kotlinlang.org/)
- **Framework**: [Ktor](https://ktor.io/)
- **Serialization**: `kotlinx.serialization`
- **Testing**: [MockK](https://mockk.io/), [Kotlin Test](https://kotlinlang.org/docs/kotlin-test.html)
- **Configuration**: `application.conf`

---

## Installation

### Prerequisites

1. [JDK 17+](https://adoptium.net/)
2. [Gradle](https://gradle.org/install/)
3. [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/Thecode42/FinancialSimulator.git
   cd 
2. Start the application:
   ```bash
   ./gradlew run

### Usage
### Available Endpoints

1. POST /api/loan/amortization/fixed:
   - **Description:** Calculate and simulate loan amortization schedules.
     - **Request Body:**
        ```bash
              {
                  "principal": 35000.00,
                  "annual_rate": 14,
                  "term_in_months": 36
              }
     - **Response:**
        ```bash
              {
                "month": 1,
                "payment": "1196.22",
                "interest": "408.33",
                "principal": "787.89",
                "remaining_balance": "34212.11"
            },
            {
                "month": 2,
                "payment": "1196.22",
                "interest": "399.14",
                "principal": "797.08",
                "remaining_balance": "33415.03"
             }

### Project Structure
    ```bash
    FinancialSimulator/
    ├── src/
    │   ├── main/
    │   │   ├── kotlin/
    │   │   │   └── com/
    │   │   │       └── financialsimulator/
    │   │   │           ├── models/
    │   │   │           │   ├── LoanRequestModel.kt
    │   │   │           │   ├── PaymentScheduleResponseMopdel.kt
    │   │   │           ├── routes/
    │   │   │           │   ├── LoanRoutes.kt
    │   │   │           │   │── Routing.kt
    │   │   │           ├── serializations/
    │   │   │           │   ├── ServerSerializer.kt
    │   │   │           │   ├── BigDecimalSerializer.kt
    │   │   │           ├── services/
    │   │   │           │   └── FixedAmortizationService.kt
    │   │   │           ├── utils/
    │   │   │           │   └── LoanUtil.kt
    │   │   │           └── Application.kt
    │   └── resources/
    │       └── logback.xml
    │       └── application.conf
    └── test/
        ├── kotlin/
        │   └── com/
        │       └── financialsimulator/
        │           └── ApplicationTest.kt
        │           └── LoanRoutesTest.kt


## License

This project is licensed under the MIT License. Please consult the `LICENSE` file for more details.
