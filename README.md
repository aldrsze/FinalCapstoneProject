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
- **Development Period:** October 3 - December 13, 2025 (30 days)

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

### 1. Application Startup & Authentication 🔒

![](screenshots/1.png)

**Figure 1.1:** Application Splash Screen

---

![](screenshots/2.png)

**Figure 1.2:** Login Panel

---

![](screenshots/3.png)

**Figure 1.3:** Sign-Up Panel

---

### 2. Dashboard & Analytics 📊

![](screenshots/4.png)

**Figure 2.1:** Main Dashboard View

---

![](screenshots/5.png)

**Figure 2.2:** Out-of-Stock Alert

---

![](screenshots/6.png)

**Figure 2.3:** Detailed Metrics

---

![](screenshots/7.png)

**Figure 2.4:** Profit Analysis Chart

---

![](screenshots/8.png)

**Figure 2.5:** Income Analysis Chart

---

![](screenshots/9.png)

**Figure 2.6:** Date Range Filter

---

### 3. Product Management 📦

![](screenshots/10.png)

**Figure 3.1:** Products Panel Main View

---

![](screenshots/11.png)

**Figure 3.2:** Add New Product Form

---

![](screenshots/12.png)

**Figure 3.3:** Edit Product Interface

---

![](screenshots/13.png)

**Figure 3.4:** Delete Product Confirmation

---

![](screenshots/14.png)

**Figure 3.5:** Search and Filter Functionality

---

### 4. Stock Control 🛒

![](screenshots/15.png)

**Figure 4.1:** Stock Panel Main View

---

![](screenshots/16.png)

**Figure 4.2:** Adding Stock (Stock-In)

---

![](screenshots/17.png)

**Figure 4.3:** Removing Stock (Stock-Out)

---

![](screenshots/18.png)

**Figure 4.4:** Stock Records Search

---

### 5. Transaction Records 🧾

![](screenshots/19.png)

**Figure 5.1:** Records Panel Main View

---

![](screenshots/20.png)

**Figure 5.2:** Date Range Filtering

---

![](screenshots/21.png)

**Figure 5.3:** CSV Export Confirmation

---

![](screenshots/22.png)

**Figure 5.4:** Transaction Details

---

### 6. QR Code Features 🤳

![](screenshots/23.png)

**Figure 6.1:** QR Code Generation Interface

---

![](screenshots/24.png)

**Figure 6.2:** Generated QR Code Sample

---

### 7. Settings & Administration ⚙️

![](screenshots/25.png)

**Figure 7.1:** Store Settings

---

![](screenshots/26.png)

**Figure 7.2:** Employee Manager

---

![](screenshots/27.png)

**Figure 7.3:** Add Employee Form

---

![](screenshots/28.png)

**Figure 7.4:** Edit Employee Interface

---

![](screenshots/29.png)

**Figure 7.5:** Delete Employee Confirmation

---

### 8. Help and Information

![](screenshots/30.png)

**Figure 7.6:** Help/User Guide Panel

---

![](screenshots/31.png)

**Figure 7.7:** About/Credits Panel

---

### 9. System Operations & Dialogs ✅

![](screenshots/32.png)

**Figure 8.1:** Success Confirmation Dialog

---

![](screenshots/33.png)

**Figure 8.2:** Invalid Input Error Dialog

---

![](screenshots/34.png)

**Figure 8.3:** Database Connection Failure Dialog

---

![](screenshots/35.png)

**Figure 8.4:** Logging Out Confirmation

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
✅ **Filipino-Focused** - Built for small sari-sari stores with relevant products  
✅ **Student-Friendly** - Clean code with educational documentation  
✅ **Production-Ready** - Robust error handling and data validation  
✅ **Excel Integration** - One-click CSV exports for business reporting  
✅ **Modern Technology** - QR code integration for automatic operations  
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

## 📄 License

This project is developed as an academic capstone project for educational purposes.

**Project Type:** Academic Capstone Project  
**Development Period:** September - December 2025 
**Purpose:** Educational Purposes












---

## 🙏 Acknowledgments

- **Quezon City University** - For educational support and guidance
- **Project Professor* - For mentorship throughout development
- **Classmates** - For feedback and testing
- **Nicole G. Diaz** - For supporting me since day one of development

---

**© 2025 SmartStock Development Team. All Rights Reserved.**

*SmartStock - Empowering Filipino Retailers with Smart Inventory Management*

---







