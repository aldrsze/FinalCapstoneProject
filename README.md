# SmartStock Inventory Management System - Documentation

**A Professional Inventory Solution for Small to Medium Business Enterprises**

---

## 📋 Project Overview

**SmartStock** is a comprehensive Java-based inventory management system designed specifically for Filipino retail businesses, featuring QR code integration, real-time analytics, and multi-user support.


## 🎯 Key Features

### Product Management
- CRUD operations with validation
- Smart pricing with automatic retail price calculation
- 8 built-in Units for Products
- Bulk operations (Set markup, bulk delete, mass returns)
- Advanced search and dynamic sorting
- Stock alerts with visual indicators

### QR Code Integration
- Smart generation with embedded JSON product data
- Multiple modes: Add/Update, Sell, Delete
- Webcam support for real-time scanning
- Image file scanning capability
- Auto-save QR codes as PNG files

### Sales & Transaction Management
- Stock-Out operations (sales, removals, rejects)
- Historical cost tracking for accurate profit calculation
- Return processing (4 types: Customer Return, Damaged, Refund,)
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

- **Java 21** or higher
- Webcam for QR code scanning
- Microsoft Excel 2007+ for CSV viewing

---


## 🔧 Technology Stack

### Core Technologies
- **Java 21** - Main programming language
- **Swing** - GUI framework
- **JDBC** - Database connectivity
- **SQLite** - Embedded database (no server required)

### Libraries
- **ZXing 3.5.0** - QR code generation/decoding
- **Webcam Capture 0.3.12** - Camera integration
- **JSON** - Data serialization
- **JCalendar 1.4** - Date picker components

---

## 📸 System Screenshots

### 1. Application Startup & Authentication

![](screenshots/1.png)

**Figure 1.1:**

---

![](screenshots/2.png)

**Figure 1.2:** 

---

![](screenshots/3.png)

**Figure 1.3:**

---

### 2. Dashboard & Analytics

![](screenshots/4.png)

**Figure 2.1:** 

---

![](screenshots/5.png)

**Figure 2.2:** 

---

![](screenshots/6.png)

**Figure 2.3:** 

---


### 3. Product Management

![](screenshots/7.png)

**Figure 3.1:** 

---

![](screenshots/8.png)

**Figure 3.2:** 

---

![](screenshots/9.png)

**Figure 3.3:** 

---

![](screenshots/10.png)

**Figure 3.4:** 

---

![](screenshots/11.png)

**Figure 3.5:** 

---

![](screenshots/12.png)

**Figure 3.6:** 

![](screenshots/13.png)

**Figure 3.7:** 

---

![](screenshots/14.png)

**Figure 3.8:** 

---

![](screenshots/15.png)

**Figure 3.9:** 

---

![](screenshots/16.png)

**Figure 3.10:** 

---

### 4. QR CODE

![](screenshots/17.png)

**Figure 4.1:** 

---

![](screenshots/18.png)

**Figure 4.2:** 

---

![](screenshots/19.png)

**Figure 4.3:** 

---

![](screenshots/20.png)

**Figure 4.4:** 

---

![](screenshots/21.png)

**Figure 4.5:** 

---

### 5. Stock Summary Panel

![](screenshots/22.png)

**Figure 5.1:**

---

### 6. Transaction Records Panel

![](screenshots/23.png)

**Figure 6.1:** 

---

### 7. ABOUT US

![](screenshots/24.png)

**Figure 7.1:**

---

### 8. Settings & Administration

![](screenshots/25.png)

**Figure 8.1:**

---

![](screenshots/26.png)

**Figure 8.2:**

---

![](screenshots/27.png)

**Figure 8.3:**

---

![](screenshots/28.png)

**Figure 8.4:**

---

### 9. Employee Customization

![](screenshots/29.png)

**Figure 9.1:**

---

![](screenshots/30.png)

**Figure 9.2:**

---

![](screenshots/31.png)

**Figure 9.3:**

---

![](screenshots/32.png)

**Figure 9.4:**

---

![](screenshots/33.png)

**Figure 9.5:**

### 10. Help and User Guide Dialog

---

![](screenshots/34.png)

**Figure 10.1:**

### 11. Logging out

![](screenshots/35.png)


---

### **Step by step guide on running the Program**

### 1. Install Java Development Kit (JDK 21)
```bash
# Verify installation
java -version
javac -version
```

### 2. Database Setup
The application uses SQLite embedded database - no separate installation required!

**Option A: With Sample Data**
- Use the included `smartstock_sqlite.sql` file
- Database will be created automatically on first run
- **Test Accounts:**
  - Admin: `admin` / `admin123`
  - Employee: `employee1` / `employee123`

**Option B: Clean Installation**
- Application creates empty database on first run
- Create first admin account via signup screen

### 3. Configure Database Connection (Optional)
Edit `src/config.properties` if needed:
```properties
db.url=jdbc:sqlite:smartstock.db
```

### 4. Build & Run
Open `build.bat` file to compile

### 5. Launch Application
Run the `SmartStock.jar` file

---

## 📊 Project Statistics

- **Total Lines of Code:** 8,000+
- **Java Classes:** 30+
- **Database Tables:** 8
- **Features Implemented:** 50+
- **Documentation:** 2,000+ lines
- **Development Time:** Almost 3 Months


---

## 🎓 Academic Highlights

### Why SmartStock?
✅ **Business-Focused** - Built for small Businesses with relevant products  
✅ **Student-Friendly** - Clean code with educational documentation  
✅ **Production-Ready** - Robust error handling and data validation  
✅ **Excel Integration** - One-click CSV exports for business reporting  
✅ **Modern Technology** - QR code integration for automatic operations  
✅ **Accurate Accounting** - Historical cost tracking for true profit margins


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

    - CREATE ACCOUNT FIRST.

---

## 🐛 Troubleshooting

### Common Issues

**Database Connection Error**
- Ensure smartstock.db file exists in application directory
- Check file permissions (read/write access required)
- Verify SQLite JDBC driver is in lib folder

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
- **Project Professor** - For mentorship throughout development
- **Classmates** - For feedback and testing

*To Nicole, thank you so much for all the love and support that you give. I am so thankful to have you in my 1st Programming project as a first year student. I love you so much.*

---

**© 2025 SmartStock Development Team. All Rights Reserved.**

*SmartStock - Empowering Filipino Retailers with Smart Inventory Management*

---









