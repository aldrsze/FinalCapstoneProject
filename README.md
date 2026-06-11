# SmartStock Inventory Management System

## Intro
**SmartStock** is a professional, comprehensive Java-based inventory management system designed specifically for small to medium-sized retail businesses (SMEs). It features QR code integration, real-time analytics, secure multi-user support, and Excel-optimized reporting, providing a robust solution for modern inventory needs.

## Video or Image Presentation if there is

### 1. Application Startup & Authentication
![](screenshots/1.png)
*Figure 1.1: Startup Screen*

![](screenshots/2.png)
*Figure 1.2: Login Panel*

![](screenshots/3.png)
*Figure 1.3: User Signup*

### 2. Dashboard & Analytics
![](screenshots/4.png)
*Figure 2.1: Main Dashboard Overview*

![](screenshots/5.png)
*Figure 2.2: Sales Analytics*

![](screenshots/6.png)
*Figure 2.3: Stock Alerts*

### 3. Product Management
![](screenshots/7.png)
*Figure 3.1: Product List View*

![](screenshots/8.png)
*Figure 3.2: Adding New Products*

![](screenshots/9.png)
*Figure 3.3: Product Search and Filter*

![](screenshots/10.png)
*Figure 3.4: Bulk Operations*

![](screenshots/11.png)
*Figure 3.5: Unit Management*

![](screenshots/12.png)
*Figure 3.6: Category Management*

![](screenshots/13.png)
*Figure 3.7: Pricing Configuration*

![](screenshots/14.png)
*Figure 3.8: Markup Settings*

![](screenshots/15.png)
*Figure 3.9: Return Processing*

![](screenshots/16.png)
*Figure 3.10: Transaction Logs*

### 4. QR Code Integration
![](screenshots/17.png)
*Figure 4.1: QR Code Generation*

![](screenshots/18.png)
*Figure 4.2: Scanning via Webcam*

![](screenshots/19.png)
*Figure 4.3: QR Code Sale Processing*

![](screenshots/20.png)
*Figure 4.4: File-based Scanning*

![](screenshots/21.png)
*Figure 4.5: Batch QR Generation*

### 5. Stock & Transactions
![](screenshots/22.png)
*Figure 5.1: Stock Summary Panel*

![](screenshots/23.png)
*Figure 6.1: Detailed Transaction Records*

### 6. Administration & Settings
![](screenshots/25.png)
*Figure 8.1: Store Settings*

![](screenshots/26.png)
*Figure 8.2: User Management*

![](screenshots/27.png)
*Figure 8.3: Role Configuration*

![](screenshots/28.png)
*Figure 8.4: Security Settings*

### 7. Employee Management
![](screenshots/29.png)
*Figure 9.1: Employee List*

![](screenshots/30.png)
*Figure 9.2: Adding Employee Accounts*

![](screenshots/31.png)
*Figure 9.3: Permission Management*

![](screenshots/32.png)
*Figure 9.4: Activity Monitoring*

![](screenshots/33.png)
*Figure 9.5: Account Recovery*

### 8. Help & User Guide
![](screenshots/34.png)
*Figure 10.1: Built-in Help System*

### 9. System Exit
![](screenshots/35.png)
*Figure 11.1: Logout Confirmation*

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
