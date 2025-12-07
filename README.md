# SmartStock Inventory Management System - Documentation

**A Professional Inventory Solution for Filipino Sari-Sari Stores**

---

## 📋 Project Overview

**SmartStock** is a comprehensive Java-based inventory management system designed specifically for Filipino retail businesses, featuring QR code integration, real-time analytics, and multi-user support.

### Academic Information
- **Course:** Bachelor of Science in Information Technology
- **Subject:** Introduction to Programming (Capstone Project 1)
- **Institution:** Quezon City University
- **Section:** BSIT-1B | Group #3
- **Development Period:** October 3 - December 2, 2025 (30 days)

### Development Team
| Name | Role |
|------|------|
| George Harold A. Alcantara | Project Manager / Documentation Writer |
| Aldrin Miguel A. Jariel | System Analyst / Developer / QA / Documentation Writer |
| John Christoper A. Perez | UI/UX Designer / Documentation Writer |
| Ron Paulo G. Angeles | Documentation Writer |
| Matthew Dane D. Calangian | Documentation Writer |

---

## 🎯 Key Features

### Product Management
- CRUD operations with validation
- Smart pricing with automatic retail price calculation
- 8 built-in categories (Beverages, Snacks, Canned Goods, etc.)
- Bulk operations (Set markup, bulk delete, mass returns)
- Advanced search and dynamic sorting
- Stock alerts with visual indicators

### QR Code Integration
- Smart generation with embedded JSON product data
- Multiple modes: Add/Update, Stock-Out, Delete
- Webcam support for real-time scanning
- Image file scanning capability
- Auto-save QR codes as PNG files

### Sales & Transaction Management
- Stock-Out operations (sales, removals, rejects)
- Historical cost tracking for accurate profit calculation
- Return processing (4 types: Customer Return, Damaged, Refund, Dispose)
- Complete transaction log with date/time stamps
- Date range filtering

### Dashboard & Analytics
- Real-time statistics (products, stock, sales, revenue)
- Best sellers report
- Stock alerts overview
- Daily sales summary with profit margins

### CSV Export (Excel-Optimized)
- One-click export with UTF-8 BOM encoding
- Professional formatting (currency, percentages, dates)
- Excel-ready reports with metadata headers

### User Management
- Role-based access (Admin and Employee)
- Secure authentication
- Employee management interface
- Account settings and security

---

## 💻 System Requirements

### Minimum Requirements
- **OS:** Windows 7/8/10/11, macOS 10.14+, Linux
- **Java:** JDK 21 or higher
- **Database:** XAMPP (MySQL 8.0)
- **RAM:** 4GB minimum, 8GB recommended
- **Storage:** 500MB
- **Display:** 1024x768 minimum (optimized for 1366x768)

### Optional
- Webcam for QR code scanning
- Microsoft Excel 2007+ for CSV viewing

---

## 🗄️ Database Architecture

### Tables
1. **users** - User accounts with roles
2. **stores** - Store profiles and settings
3. **categories** - Product categories
4. **products** - Product information
5. **units** - Units of measurement
6. **sales** - Sales transactions
7. **sale_items** - Individual sale line items
8. **stock_log** - Inventory change history

---

## 🔧 Technology Stack

### Core Technologies
- **Java 21** - Main programming language
- **Swing** - GUI framework
- **JDBC** - Database connectivity
- **MySQL 8.0** - Database management

### Libraries
- **ZXing 3.5.0** - QR code generation/decoding
- **Webcam Capture 0.3.12** - Camera integration
- **JSON** - Data serialization
- **JCalendar 1.4** - Date picker components

### Design Patterns
- Repository Pattern (data access)
- MVC Architecture (separation of concerns)
- Record Pattern (immutable data models)
- Singleton (database connections)

---

## 📸 System Screenshots

### 1. Application Startup & Authentication

![Application Splash Screen](screenshots/1.png)

**Figure 1.1:** Application splash screen displaying during startup with SmartStock branding and loading progress.

---

![Login Panel](screenshots/2.png)

**Figure 1.2:** Main login interface where users authenticate with username and password. Features include password visibility toggle and sign-up option for new users.

---

![Sign-Up Panel](screenshots/3.png)

**Figure 1.3:** User registration screen for creating new admin accounts. Only administrators can be registered through this interface; employee accounts are created by admins within the system.

---

### 2. Dashboard & Analytics

![Main Dashboard](screenshots/4.png)

**Figure 2.1:** Primary dashboard displaying key business metrics including total products, inventory value, sales data, and net profit. Features date range filtering for customized reporting periods.

---

![Out-of-Stock Alert](screenshots/5.png)

**Figure 2.2:** Dashboard view highlighting products with critical stock levels, showing color-coded status indicators (Out of Stock, Critical, Low Stock, Good, Overstocked).

---

![Detailed Metrics](screenshots/6.png)

**Figure 2.3:** Detailed inventory metrics panel showing comprehensive breakdown of current stock values and quantities across all product categories.

---

![Profit Analysis Chart](screenshots/7.png)

**Figure 2.4:** Visual representation of profit trends over selected date range, displaying the net profit calculation based on historical cost tracking.

---

![Income Analysis Chart](screenshots/8.png)

**Figure 2.5:** Revenue analysis chart showing total sales income over time, with date range customization options for specific period analysis.

---

![Date Range Filter](screenshots/9.png)

**Figure 2.6:** Date range selection panel with quick preset options (Today, Yesterday, Last 7 Days, Last 30 Days, This Month, Last Month, All Time) for filtering dashboard data.

---

### 3. Product Management

![Products Panel Main View](screenshots/10.png)

**Figure 3.1:** Main product management interface displaying complete inventory list with columns for ID, Name, Category, Unit, Cost Price, Total Cost, Markup %, Retail Price, Total Retail, and Stock. Features include search functionality, column sorting, and bulk operations.

---

![Add New Product](screenshots/11.png)

**Figure 3.2:** Product entry form for adding new items to inventory. Fields include Product Name, Category, Unit of Measurement, Cost Price, and Stock Quantity. The system auto-generates product IDs.

---

![Edit Product](screenshots/12.png)

**Figure 3.3:** Product editing interface allowing modification of existing product details. Product ID is read-only during edit operations to maintain data integrity.

---

![Delete Product Confirmation](screenshots/13.png)

**Figure 3.4:** Safety confirmation dialog before permanently deleting a product from the system. Warning message emphasizes that this action cannot be undone.

---

![Search and Filter](screenshots/14.png)

**Figure 3.5:** Advanced search and filtering capabilities allowing real-time filtering of products by any column data (name, category, price, stock level, etc.).

---

### 4. Stock Control

![Stock Panel Main View](screenshots/15.png)

**Figure 4.1:** Stock summary interface displaying product inventory movements with columns for Stock In, Stock Out, and Available quantities. Color-coded status indicators show inventory health.

---

![Adding Stock (Stock-In)](screenshots/16.png)

**Figure 4.2:** Stock-In operation interface for recording new inventory received from suppliers or returns from customers.

---

![Removing Stock (Stock-Out)](screenshots/17.png)

**Figure 4.3:** Stock-Out operation for processing sales, removals, rejects, or disposal. Includes quantity validation against current stock levels.

---

![Stock Records Search](screenshots/18.png)

**Figure 4.4:** Stock movement history with search and filter capabilities, showing all inventory transactions with date range filtering options.

---

### 5. Transaction Records

![Records Panel Main View](screenshots/19.png)

**Figure 5.1:** Complete transaction history displaying all stock movements including Stock-In, Stock-Out, Customer Returns, Refunds, and Deletions. Each transaction shows date, product details, quantity, cost, and retail values.

---

![Date Range Filtering](screenshots/20.png)

**Figure 5.2:** Transaction records filtered by custom date range, showing period-specific inventory movements for focused analysis.

---

![CSV Export Confirmation](screenshots/21.png)

**Figure 5.3:** Success notification after exporting transaction records to CSV format. Files are Excel-ready with UTF-8 BOM encoding and professional formatting.

---

![Transaction Details](screenshots/22.png)

**Figure 5.4:** Detailed view of individual transaction showing complete information including product details, transaction type, quantities, prices, and any associated notes.

---

### 6. QR Code Features

![QR Code Generation](screenshots/23.png)

**Figure 6.1:** QR Code generation interface where users can create scannable QR codes for products. Generated codes contain embedded JSON data including product ID, name, category, unit, cost price, and stock information.

---

![Generated QR Code](screenshots/24.png)

**Figure 6.2:** Sample generated QR code output displaying product information below the code. QR codes can be saved as PNG files for printing on product labels or packaging.

---

### 7. Settings & Administration

![Store Settings](screenshots/25.png)

**Figure 7.1:** Store configuration interface with multiple tabs for Profile (store name, location, contact), Username change, Password change, and Account Deletion options. Includes default markup percentage setting for pricing automation.

---

![Employee Manager](screenshots/26.png)

**Figure 7.2:** Employee management interface (Admin only) displaying list of employee accounts with options to add, edit, or remove employee users.

---

![Add Employee](screenshots/27.png)

**Figure 7.3:** Employee account creation form where admins can add new employee users with username and password credentials.

---

![Edit Employee](screenshots/28.png)

**Figure 7.4:** Employee account editing interface allowing admins to modify existing employee credentials after password authentication.

---

![Delete Employee](screenshots/29.png)

**Figure 7.5:** Employee account deletion confirmation dialog ensuring secure removal of employee access.

---

![Help/User Guide](screenshots/30.png)

**Figure 7.6:** Comprehensive help and user guide panel providing instructions on system features including Getting Started, Product Management, Sales & Transactions, QR Code Features, Dashboard & Analytics, Reports & Export, Settings, and Troubleshooting.

---

![About/Credits](screenshots/31.png)

**Figure 7.7:** About panel displaying development team information, academic details (BSIT-1B Group #3), project overview, and copyright information.

---

### 8. System Operations

![Success Confirmation](screenshots/32.png)

**Figure 8.1:** Success message dialog confirming successful completion of operations such as product addition, updates, sales processing, or data export.

---

![Invalid Input Error](screenshots/33.png)

**Figure 8.2:** Input validation error message guiding users to correct invalid or missing data entries.

---

![Database Connection Failure](screenshots/34.png)

**Figure 8.3:** Database connection error notification alerting users when the system cannot connect to MySQL database (typically when XAMPP is not running).

---

![Successful Logout](screenshots/35.png)

**Figure 8.4:** Logout confirmation message ensuring users have successfully and securely exited the system.

---

## 🚀 Installation & Setup

### 1. Install Java Development Kit (JDK 21)
```bash
# Verify installation
java -version
javac -version
```

### 2. Install XAMPP
- Download from [Apache Friends](https://www.apachefriends.org/)
- Start Apache and MySQL services

### 3. Database Setup
**Option A: With Sample Data**
```sql
-- Import via phpMyAdmin: smartstock_presentation.sql
-- Test Accounts:
-- Admin: admin / admin123
-- Employee: employee1 / emp123
```

**Option B: Clean Installation**
```sql
-- Import: smartstock_clean.sql
-- Create first admin account via signup
```

### 4. Configure Database Connection
Edit `src/config.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/smartstock
db.user=root
db.password=
```

### 5. Build & Run
```bash
# Build
build.bat

# Run
java -jar SmartStock.jar
```

---

## 📊 Project Statistics

- **Total Lines of Code:** 8,000+
- **Java Classes:** 30+
- **Database Tables:** 7
- **Features Implemented:** 50+
- **Documentation:** 2,000+ lines
- **Development Time:** 30 days
- **Testing Hours:** 100+

---

## 🎓 Academic Highlights

### Why SmartStock?
✅ **Filipino-Focused** - Built for sari-sari stores with relevant products  
✅ **Student-Friendly** - Clean code with educational documentation  
✅ **Production-Ready** - Robust error handling and data validation  
✅ **Excel Integration** - One-click CSV exports for business reporting  
✅ **Modern Technology** - QR code integration for contactless operations  
✅ **Accurate Accounting** - Historical cost tracking for true profit margins

### Learning Outcomes
- Full-stack Java desktop application development
- Database design and SQL optimization
- MVC architecture implementation
- User interface/experience design
- Software testing and quality assurance
- Team collaboration and project management
- Technical documentation writing

---

## 🔐 Security Features

- Password-protected authentication
- Role-based access control (Admin/Employee)
- SQL injection prevention (prepared statements)
- Transaction safety (database locks)
- Input validation (client and server-side)
- Secure error handling

---

## 📝 Default Accounts

| Username | Password | Role | Access Level |
|----------|----------|------|--------------|
| admin | admin123 | Admin | Full Access |
| employee1 | emp123 | Employee | View Only |

---

## 🐛 Troubleshooting

### Common Issues

**Database Connection Error**
- Verify XAMPP MySQL is running
- Check config.properties credentials
- Ensure database exists in phpMyAdmin

**Webcam Not Working**
- Grant camera permissions
- Close other apps using webcam
- Update webcam drivers

**CSV Files Won't Open**
- Right-click → Open with Excel
- File has UTF-8 BOM encoding

---

## 📞 Support

For questions or issues:
1. Check troubleshooting section
2. Review installation guide
3. Verify MySQL service status
4. Contact project advisors

---

## 📄 License

This project is developed as an academic capstone project for educational purposes.

**Project Type:** Academic Capstone Project  
**Development Period:** 2024-2025  
**Purpose:** Educational demonstration of software engineering principles

### Usage Rights
✅ Educational use and learning  
✅ Portfolio demonstration  
✅ Academic presentations  
❌ Commercial distribution without permission

---

## 🙏 Acknowledgments

- **Quezon City University** - For educational support and guidance
- **Project Advisors** - For mentorship throughout development
- **Classmates** - For feedback and testing
- **Filipino Retailers** - For real-world requirements
- **Open Source Community** - For excellent libraries and tools

---

**© 2025 SmartStock Development Team. All Rights Reserved.**

*SmartStock - Empowering Filipino Retailers with Smart Inventory Management*

---

**Version:** 1.0.0  
**Last Updated:** December 2, 2025  
**Status:** ✅ Production Ready
