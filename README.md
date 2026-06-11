# SmartStock Inventory Management System
**SmartStock** is a professional, comprehensive Java-based inventory management system designed specifically for small to medium-sized retail businesses (SMEs). It features QR code integration, real-time analytics, secure multi-user support, and Excel-optimized reporting, providing a robust solution for modern inventory needs.

### Authentication & Dashboard
![](screenshots/2.png)
*Signup Interface*

![](screenshots/4.png)
*Main Dashboard and Real-time Analytics*

### Core Features
![](screenshots/7.png)
*Product Management and Inventory List*

![](screenshots/18.png)
*Real-time QR Code Scanning via Webcam*

![](screenshots/23.png)
*Detailed Transaction and Sales Records*

## Technologies
- **Java 21**: Core programming language and runtime.
- **Swing**: Graphical User Interface (GUI) framework.
- **SQLite**: Embedded relational database for zero-configuration setup.
- **JDBC**: Java Database Connectivity API for database operations.
- **ZXing (3.5.0)**: Library for QR code generation and decoding.
- **Webcam Capture (0.3.12)**: Integration for real-time camera scanning.
- **JSON**: Used for data serialization within QR codes.
- **JCalendar (1.4)**: Date picker components for transaction filtering.

## Features
- **Smart Product Management**: CRUD operations with automated retail price calculation and stock alerts.
- **QR Code Integration**: Real-time scanning via webcam or image files for sales and stock updates.
- **Advanced Analytics**: Real-time dashboard with best-sellers, revenue tracking, and profit margin analysis.
- **Sales & Returns**: Comprehensive transaction logging with support for various return types (Damaged, Refund, etc.).
- **Excel-Optimized Export**: Professional CSV reporting with UTF-8 BOM encoding for seamless Excel integration.
- **Role-Based Security**: Secure authentication with Admin and Employee roles and granular permissions.
- **Automated DB Initialization**: Self-repairing and self-initializing database schema on first run.

## How I built it
- **Architecture**: Developed using an MVC-inspired architectural pattern to separate GUI logic from data management.
- **Database Design**: Implemented a normalized SQLite schema with 8 tables, ensuring data integrity through foreign key constraints and prepared statements.
- **Hardware Integration**: Bridged the gap between software and hardware by implementing the Webcam Capture API for physical QR scanning.
- **Business Logic**: Designed custom algorithms for markup calculation and historical cost tracking to ensure accurate profit reporting.
- **UI Customization**: Leveraged Java Swing's extensibility to create a custom-themed, professional desktop experience tailored for retail environments.

## What I learned
- **Full-Stack Desktop Development**: Mastered the complete lifecycle of a desktop application, from UI design to local database optimization.
- **Relational Data Management**: Deepened understanding of SQL and JDBC, particularly in handling concurrent database connections and complex relational queries.
- **Integration Challenges**: Overcame the complexities of integrating third-party libraries (ZXing, Webcam Capture) and handling various OS-level drivers.
- **Retail Contextual Design**: Learned to design software based on specific user requirements, such as the unique needs of Filipino retail SMEs.
- **Defensive Programming**: Developed robust error-handling and logging systems to ensure application stability in production environments.

## Future Enhancements
- **Cloud Synchronization**: Migrating from local SQLite to a cloud database (PostgreSQL/Firebase) for multi-station synchronization.
- **Mobile Companion**: Developing a lightweight mobile app for inventory tracking and remote dashboard monitoring.
- **AI-Powered Insights**: Integrating basic machine learning for demand forecasting and inventory optimization.
- **Supplier Portal**: A dedicated module for managing vendor relations, automated purchase orders, and lead-time tracking.
- **Receipt Printing**: Native integration with thermal POS printers for physical receipt generation.

## How to run the project

### 1. Prerequisites
- **Java Development Kit (JDK) 21** or higher.
- A functional webcam (required for QR scanning features).

### 2. Setup & Installation
- Clone the repository to your local machine.
- The application uses an embedded SQLite database (`smartstock.db`), which will be automatically generated upon the first execution.

### 3. Build the Application
- Run the provided `build.bat` file to compile the source code and generate the executable JAR.

### 4. Launching the App
- Execute the `SmartStock.jar` file or use the `SmartStock.exe` wrapper if available.
- **Default Accounts**:
  - You can create an initial Admin account via the signup screen.
  - Test Admin: `admin` / `admin123`
  - Test Employee: `employee1` / `employee123`

### 5. Troubleshooting
- **Database Connection**: Ensure the application has write permissions to its own directory to create/update the `.db` file.
- **Webcam Issues**: Ensure no other application is using the webcam and that drivers are up to date.
- **CSV Export**: If CSV files don't display correctly, ensure they are opened with Microsoft Excel 2007+ which supports UTF-8 BOM.
