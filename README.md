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
*Initial loading screen with progress indicator*

![Login Panel](screenshots/2.png)
*User authentication interface*

![Sign-Up Panel](screenshots/3.png)
*New user registration form*

---

### 2. Dashboard & Analytics
![Main Dashboard](screenshots/4.png)
*Overview of key metrics and statistics*

![Out-of-Stock Alert](screenshots/5.png)
*Products requiring immediate attention*

![Detailed Metrics](screenshots/6.png)
*Current inventory breakdown*

![Profit Analysis Chart](screenshots/7.png)
*Visual profit trends*

![Income Analysis Chart](screenshots/8.png)
*Revenue generation overview*

![Date Range Filter](screenshots/9.png)
*Custom date range selection*

---

### 3. Product Management
![Products Panel Main View](screenshots/10.png)
*Complete product listing with search*

![Add New Product](screenshots/11.png)
*Product entry form*

![Edit Product](screenshots/12.png)
*Product modification interface*

![Delete Product Confirmation](screenshots/13.png)
*Deletion safety confirmation*

![Search and Filter](screenshots/14.png)
*Advanced filtering options*

---

### 4. Stock Control
![Stock Panel Main View](screenshots/15.png)
*Inventory level management*

![Adding Stock (Stock-In)](screenshots/16.png)
*Recording new inventory*

![Removing Stock (Stock-Out)](screenshots/17.png)
*Processing inventory removal*

![Stock Records Search](screenshots/18.png)
*Filtering stock movement history*

---

### 5. Transaction Records
![Records Panel Main View](screenshots/19.png)
*Complete transaction history*

![Date Range Filtering](screenshots/20.png)
*Period-specific records*

![CSV Export Confirmation](screenshots/21.png)
*Successful export notification*

![Transaction Details](screenshots/22.png)
*Individual transaction information*

---

### 6. QR Code Features
![QR Code Generation](screenshots/23.png)
*Creating product QR codes*

![Generated QR Code](screenshots/24.png)
*Scannable QR code output*

---

### 7. Settings & Administration
![Store Settings](screenshots/25.png)
*Store configuration interface*

![Employee Manager](screenshots/26.png)
*User account management*

![Add Employee](screenshots/27.png)
*Creating new employee accounts*

![Edit Employee](screenshots/28.png)
*Modifying employee details*

![Delete Employee](screenshots/29.png)
*Employee removal confirmation*

![Help/User Guide](screenshots/30.png)
*System usage instructions*

![About/Credits](screenshots/31.png)
*Development team information*

---

### 8. System Operations
![Success Confirmation](screenshots/32.png)
*Operation completed successfully*

![Invalid Input Error](screenshots/33.png)
*Input validation message*

![Database Connection Failure](screenshots/34.png)
*Connection error handling*

![Successful Logout](screenshots/35.png)
*Secure session termination*

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
