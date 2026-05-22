# UML Models for the CHMS Project

This folder contains the UML class diagrams for Project Part 2. The diagrams model four important subsystems of the Cardiovascular Health Monitoring System:

1. Alert Generation System
2. Data Storage System
3. Patient Identification System
4. Data Access Layer

The diagrams are written in PlantUML format so they can be edited easily and exported to PNG or PDF using a PlantUML extension, online renderer, or diagramming tool.

## 1. Alert Generation System

The alert generation model separates the evaluation of patient data from the delivery of alerts. `AlertGenerator` reads patient information from `DataStorage` and applies a set of `AlertRule` objects. Each rule is responsible for one medical condition, such as blood pressure limits, low oxygen saturation, or abnormal ECG values. This keeps the design flexible because a new alert type can be added by creating another class that implements `AlertRule`, without rewriting the main generator.

When a condition is detected, an `Alert` object stores the patient ID, condition, timestamp, and priority. The `AlertManager` then dispatches the alert to one or more `MedicalStaff` objects. The design keeps alert creation, alert checking, and alert routing as separate responsibilities. This is useful in a safety-critical monitoring system because the medical logic can be tested independently from the notification logic.

The model also connects the alert system to `Patient` and `DataStorage`, because alerts are based on stored patient records. `AlertPriority` is modeled as an enum to keep alert severity consistent across the system. Overall, this diagram supports real-time evaluation while still allowing future extension, such as adding personalized thresholds or new alert rules.

## 2. Data Storage System

The data storage model focuses on storing incoming patient measurements securely and making them available for later analysis. `DataStorage` is the central class and keeps a map of patient IDs to `Patient` objects. Each `Patient` owns multiple `PatientRecord` objects, which represent timestamped measurements such as ECG, saturation, or blood pressure. This makes retrieval by patient and time range straightforward.

The design includes `DataRetriever` as a separate query class so that external users do not directly manipulate the storage internals. Before records are returned, `DataStorage` uses `AccessController` to check whether the requesting `User` has permission to view the data. This is important because medical data should not be exposed to parts of the system or users that do not need it.

`DataRetentionPolicy` handles deletion rules for old data, while `AuditLog` records access and deletion events. These classes keep privacy, traceability, and maintenance concerns separate from the basic storage logic. The `PatientRecord` class also includes a `version` field to represent the idea of data versioning. The design is modular because data storage, access control, auditing, and retention policy can evolve independently.

## 3. Patient Identification System

The patient identification model shows how incoming simulator data can be linked to hospital patient records. The `PatientIdentifier` receives `IncomingPatientData` and uses the simulator patient ID to search for a matching `HospitalPatient` in the `HospitalPatientRepository`. This keeps the simulator-facing ID separate from the hospital’s internal patient information.

The `IdentityManager` is responsible for checking whether a match is trustworthy. This class is useful because patient identification is a sensitive part of the system: if the wrong patient is matched, alerts and records could be assigned incorrectly. When a patient cannot be found or when the data looks inconsistent, the problem is passed to `MismatchHandler`.

`MismatchHandler` records identification problems in `AnomalyLog`, which gives the system a clear place to track unusual cases. This is better than ignoring bad data or letting it silently enter the storage layer. The `DataSourceAdapter` is included to show where identification fits into the larger data flow. Before incoming records are stored or analyzed, they should be connected to a valid patient identity.

This design keeps matching, verification, repository access, and anomaly handling separated, which makes the system easier to test and maintain.

## 4. Data Access Layer

The data access layer model describes how external simulator data enters the CHMS. `DataListener` is modeled as an interface so that different input methods can be used without changing the rest of the system. `TCPDataListener`, `WebSocketDataListener`, and `FileDataListener` all implement the same interface, which means the system can switch between network streams and file logs more easily.

Raw incoming messages are passed to `DataSourceAdapter`. The adapter uses `DataParser` to validate and convert raw strings into `PatientRecord` objects. Once parsing succeeds, the adapter stores the record in `DataStorage`. This separates communication logic from parsing and storage logic, which keeps each class focused on a single responsibility.

The model also includes `ErrorHandler`, which handles invalid formats, broken connections, and file reading problems. This is useful because real-time data streams can fail or produce corrupted messages. By keeping errors in a dedicated class, the listeners and adapter stay cleaner.

Overall, this layer protects the rest of the system from knowing whether data came from TCP, WebSocket, or files. That makes the system more flexible and easier to extend later.
