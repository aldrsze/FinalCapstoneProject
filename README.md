# 🏪 SmartStock Inventory Management System

**A Professional Inventory Solution for Filipino Sari-Sari Stores**

SmartStock is a comprehensive Java-based inventory management system featuring QR code integration, real-time analytics, Excel-optimized CSV exports, and multi-user support. Designed specifically for retail businesses like sari-sari stores.

---

## 📋 Table of Contents

- [Quick Start](#-quick-start)
- [Features](#-features)
- [System Requirements](#-system-requirements)
- [Installation Guide](#-installation-guide)
- [Database Setup](#-database-setup)
- [Usage](#-usage)
- [Project Structure](#-project-structure)
- [Technologies Used](#-technologies-used)
- [Screenshots](#-screenshots)
- [Troubleshooting](#-troubleshooting)
- [License](#-license)

---

## 🚀 Quick Start

### For Presentation (With Sample Data)

1. **Install MySQL** - Ensure MySQL 8.0 or higher is running
2. **Import Database** - Import `Database/smartstock_db.sql` from project folder
3. **Build Project** - Run `build.bat` (creates SmartStock.jar)
4. **Run Application** - Execute `SmartStock.jar` or double-click SmartStock.exe
5. **Login** - Username: `admin`, Password: `admin123`

### For Fresh Installation (Empty Database)

1. **Import Clean Schema** - Import `Database/smartstock_clean.sql` (creates `smartstock_clean` database)
2. **Update Config** - Edit `src/config.properties`:
   ```properties
   db.url=jdbc:mysql://localhost:3306/smartstock_clean
   db.user=root
   db.password=
   ```
3. **Build & Run** - Execute `build.bat` then run the JAR file
4. **Create Account** - Use the signup feature to create your first admin account

---

## ✨ Features

### 📦 Product Management

- **CRUD Operations**: Add, edit, delete products with validation
- **Smart Pricing**: Cost price, markup percentage, automatic retail price calculation
- **Category System**: 8 built-in categories (Beverages, Snacks, Canned Goods, etc.)
- **Bulk Operations**: Set markup for multiple products, bulk delete, mass returns
- **Advanced Search**: Real-time search across product names and categories
- **Dynamic Sorting**: Click column headers to sort, automatic row renumbering
- **Stock Alerts**: Visual indicators for out-of-stock and low-stock items

### 🏷️ QR Code Integration

- **Smart Generation**: Create QR codes with embedded JSON product data
- **Multiple Modes**: Add/Update, Stock-Out , or Delete products via QR scanning
- **Webcam Support**: Real-time scanning using device camera
- **Image Scanning**: Upload and scan QR images from files
- **Auto-Save**: QR codes saved as PNG with sanitized filenames
- **Data Embedding**: QR contains ID, name, category, unit, cost, retail price, stock

### 💰 Sales & Transaction Management

- **Stock-Out Operations**: Remove Products from inventory (includes sales, removals, rejects)
- **Historical Cost Tracking**: Saves cost at time of Stock-Out for accurate profit calculation
- **Profit Analysis**: Calculate margins using actual COGS, not current cost
- **Return Processing**: 4 return types (Customer Return, Damaged, Refund, Dispose)
- **Transaction Log**: Complete audit trail with date/time stamps
- **Date Filtering**: View transactions by custom date ranges
- **Transaction Safety**: Database locks prevent concurrent modification issues

### 📊 Dashboard & Analytics

- **Real-Time Stats**: Total products, low stock alerts, today's sales, revenue
- **Quick Actions**: Direct access to Add Product, Process Sale, Generate QR
- **Stock Alerts**: Immediate visibility of inventory issues
- **Sales Overview**: Daily sales summary with profit margins
- **Store Information**: Display store name, location, contact details

### 📁 CSV Export (Excel-Optimized)

- **One-Click Export**: Export products, stock, or transactions to CSV
- **Excel-Ready**: UTF-8 BOM encoding for perfect Excel compatibility
- **Smart Formatting**:
  - Currency: `PHP 45.00` format
  - Percentages: `33.33%` format
  - Dates: `02-Dec-2024 08:15 AM` format
  - Numbers: Whole numbers for quantities
- **Metadata Headers**: Report type, generation date, record count
- **Clean Data**: Row numbers excluded, empty values show as "-"
- **Professional Reports**: Ready for printing or further analysis

### 👥 User Management

- **Role-Based Access**: Admin and Employee roles with different permissions
- **Employee Management**: Admins can add/remove employees, manage credentials
- **Secure Authentication**: Password validation and account security
- **Access Control**: Employees view-only access to analytics and records
- **Account Management**: Change username, password, or delete account with authentication

### ⚙️ Store Settings

- **Profile Management**: Store name, location, contact information
- **Default Markup**: Set default markup percentage for new products
- **Account Settings**: Change username and password with verification
- **Account Deletion**: Secure account deletion with password authentication and cascade cleanup
- **Multi-Tab Interface**: Organized settings with 4 tabs (Profile, Username, Password, Delete Account)

---

## 💻 System Requirements

### Minimum Requirements

- **Operating System**: Windows 7/8/10/11, macOS 10.14+, or Linux
- **Java**: JDK 21 or higher
- **Database**: XAMPP (includes MySQL 8.0 and phpMyAdmin)
- **RAM**: 4GB minimum, 8GB recommended
- **Disk Space**: 500MB for application and database
- **Display**: 1024x768 minimum resolution

### Optional Requirements

- **Webcam**: For QR code scanning functionality
- **Excel**: Microsoft Excel 2007+ for CSV file viewing (optional)

---

## 📥 Installation Guide

### Step 1: Install Java Development Kit (JDK 21)

1. Download JDK 21 from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
2. Install and set up `JAVA_HOME` environment variable
3. Verify installation:
   ```bash
   java -version
   javac -version
   ```

### Step 2: Install XAMPP

1. Download XAMPP from [Apache Friends](https://www.apachefriends.org/)
2. Install XAMPP with MySQL component
3. Start XAMPP Control Panel
4. Start Apache and MySQL services

### Step 3: Download SmartStock

1. Clone or download this repository
2. Extract to your preferred location (e.g., `E:\CapstoneProject101`)

### Step 4: Configure Database Connection

Edit `src/config.properties` and `bin/config.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/smartstock
db.user=root
db.password=your_mysql_password
```

---

## 🗄️ Database Setup

### Option 1: Presentation Database (With Sample Data)

Perfect for demonstrations and testing.

1. Open phpMyAdmin (http://localhost/phpmyadmin)
2. Click "Import" tab
3. Choose file: `smartstock_presentation.sql`
4. Click "Go" to import

**Included Sample Data:**

- 2 users (1 admin, 1 employee)
- 1 store profile (Tindahan ni Maria)
- 8 product categories
- 42 products (Filipino sari-sari store items)
- 42 sales transactions
- 85 sale items
- 25 stock log entries

**Test Accounts:**

- Admin: `admin` / `admin123`
- Employee: `employee1` / `emp123`

### Option 2: Empty Database (Clean Installation)

For production use or custom setup.

### Option 2: Empty Database (Clean Installation)

For production use or custom setup.

1. Open phpMyAdmin (http://localhost/phpmyadmin)
2. Click "Import" tab
3. Choose file: `Database/smartstock_clean.sql`
4. Click "Go" to import
5. Update `config.properties` to use `smartstock_clean` database:
   ```properties
   db.url=jdbc:mysql://localhost:3306/smartstock_clean
   ```
6. Run application and create your first admin account via signup

**Database Tables:**

- `users` - User accounts with roles
- `stores` - Store profiles and settings
- `categories` - Product categories
- `products` - Product information
- `sales` - Sales transactions
- `sale_items` - Individual sale line items
- `stock_log` - Inventory change history

---

## 🎯 Usage

### Building the Application

Run the build script:

```bash
build.bat
```

- Compiles all Java source files
- Creates class files in `bin/` directory
- Generates `SmartStock.jar` executable
- Pauses on errors for debugging

### Running the Application

Execute the JAR file:

```bash
java -jar SmartStock.jar
```

Or double-click `SmartStock.exe` on Windows.

- Launches the SmartStock application
- Opens login screen
- Requires database connection

### First-Time Setup

1. **Create Account**: Click "Sign Up" on login screen
2. **Setup Store**: After first login, configure store details
3. **Add Categories**: Pre-configured categories are ready
4. **Add Products**: Start adding your inventory
5. **Set Markup**: Configure default markup percentage

### Daily Operations

#### Product Management

1. Navigate to **Products** tab
2. Click **+ Add Product** to add new items
3. Fill in product details (name, category, cost, markup)
4. Click **Save** to add to inventory
5. Use **Sell**, **Edit**, or **Delete** buttons for individual products
6. Use **Actions** button for bulk operations

#### Processing Sales

1. Select product from table
2. Click **Sell** button
3. Enter quantity to sell
4. Confirm sale - stock updates automatically
5. Profit recorded using historical cost

#### QR Code Operations

1. Click **Generate QR** on Products panel
2. Switch to **Generation** tab in QR panel
3. Select product and generate QR code
4. Save QR code as PNG for printing
5. Use **Scanning** tab to scan QR codes via webcam or image upload

#### Viewing Reports

1. Go to **Stock** tab for inventory levels
2. Go to **Records** tab for transaction history
3. Use date range filter for specific periods
4. Click **↓ Export CSV** to download Excel-ready reports

#### Managing Users (Admin Only)

1. Click **⚙ Settings** in top-right
2. Select **Manage Employees**
3. Add new employees with username/password
4. Remove employees as needed

---

## 📁 Project Structure

```
CapstoneProject101/
├── src/                          # Source code
│   ├── App.java                  # Application entry point
│   ├── config.properties         # Database configuration
│   └── com/inventorysystem/
│       ├── data/                 # Repository layer (DAO)
│       │   ├── DatabaseConnection.java
│       │   ├── ProductRepository.java
│       │   ├── UserRepository.java
│       │   ├── StoreRepository.java
│       │   ├── RecordsRepository.java
│       │   ├── StockRepository.java
│       │   └── DashboardRepository.java
│       ├── model/                # Data models (Records)
│       │   ├── Product.java
│       │   ├── User.java
│       │   ├── Store.java
│       │   ├── Category.java
│       │   ├── TransactionRecord.java
│       │   └── StockRecord.java
│       ├── gui/                  # User interface
│       │   ├── userFrame.java
│       │   ├── LoginPanel.java
│       │   ├── SignupPanel.java
│       │   ├── MainApplicationPanel.java
│       │   ├── dashboardPanel.java
│       │   ├── productsPanel.java
│       │   ├── stockPanel.java
│       │   ├── recordsPanel.java
│       │   ├── QRCodePanel.java
│       │   ├── SettingsPanel.java
│       │   ├── StoreSettingsDialog.java
│       │   ├── EmployeeManagerDialog.java
│       │   ├── DateRangePanel.java
│       │   └── UIConstants.java
│       └── util/                 # Utility classes
│           ├── DebugLogger.java
│           └── CSVExporter.java
├── bin/                          # Compiled classes
│   ├── config.properties
│   └── com/inventorysystem/...
├── lib/                          # External libraries (JAR files)
│   ├── core-3.5.0.jar           # ZXing core
├── lib/                          # External libraries (JAR files)
│   ├── core-3.5.0.jar           # ZXing core
│   ├── javase-3.5.0.jar         # ZXing JavaSE
│   ├── webcam-capture-*.jar     # Webcam support
│   ├── mysql-connector-*.jar    # MySQL JDBC driver
│   └── jcalendar-*.jar          # Date picker
├── smartstock_presentation.sql   # Database with sample data
├── run.bat                       # Run script
├── MANIFEST.MF                   # JAR manifest file
└── README.md                     # This file
```

---

## 🔧 Technologies Used

### Programming Languages

- **Java 21**: Core application language

### Frameworks & Libraries

- **Swing**: GUI framework for desktop interface
- **JDBC**: Database connectivity
- **ZXing 3.5.0**: QR code generation and decoding
- **Webcam Capture 0.3.12**: Camera access for QR scanning
- **JSON**: Data serialization for QR codes
- **JCalendar 1.4**: Date picker components

### Database

- **MySQL 8.0**: Relational database management
- **InnoDB**: Storage engine with transaction support

### Design Patterns

- **Repository Pattern**: Data access abstraction
- **MVC Architecture**: Separation of concerns
- **Record Pattern**: Immutable data models (Java 14+)
- **Singleton**: Database connection management

### Database

- **MySQL 8.0** (via XAMPP): Relational database management
- **InnoDB**: Storage engine with transaction support
- **phpMyAdmin**: Web-based database administration
- **Batch Scripts**: Build automation
- **Windows PowerShell**: Terminal environment

---

## 📸 Screenshots

### Login Screen

Clean and modern authentication interface with signup option.

### Dashboard

Real-time analytics with sales overview, stock alerts, and quick actions.

### Products Panel

Comprehensive product management with search, sort, and bulk operations.

### QR Code Generation

Generate QR codes with embedded product data, save as PNG.

### QR Code Scanning

Scan QR codes via webcam or upload images for instant product lookup.

### Transaction Records

Complete sales history with date filtering and CSV export.

### Stock Management

Monitor inventory levels with visual alerts and export capabilities.

### Settings Panel

Configure store profile, default markup, and manage employees.

---

## 🐛 Troubleshooting

### Database Connection Issues

**Problem**: "Could not connect to database"

### Database Connection Issues

**Problem**: "Could not connect to database"
**Solutions**:

1. Verify XAMPP MySQL service is running (green in XAMPP Control Panel)
2. Check `config.properties` credentials (default XAMPP: user=root, password=empty)
3. Open phpMyAdmin to verify database exists
4. Ensure port 3306 is not blocked by firewall
   **Problem**: "javac is not recognized"
   **Solution**: Add Java to PATH environment variable

```bash
set PATH=%PATH%;C:\Program Files\Java\jdk-21\bin
```

### Webcam Not Working

**Problem**: QR scanner shows black screen
**Solutions**:

1. Grant camera permissions to Java
2. Close other apps using webcam
3. Update webcam drivers
4. Try different USB port

### CSV Export Not Opening in Excel

**Problem**: Garbled characters in Excel
**Solution**: File already has UTF-8 BOM encoding. If issues persist:

1. Right-click CSV → Open With → Excel
2. Or Import as Text File in Excel with UTF-8 encoding

### Application Won't Start

**Problem**: ClassNotFoundException or NoClassDefFoundError
**Solutions**:

1. Rebuild using `build.bat`
2. Verify all JARs are in `lib/` folder
3. Check MANIFEST.MF has correct classpath

### Low Stock Alerts Not Showing

**Problem**: Products with low stock not highlighted
**Solution**: Check `products` table `quantity_in_stock` values. Low stock = 10 or less, Out of stock = 0

---

## 📚 Additional Documentation

Comprehensive guides available in `docs/` folder:

### Low Stock Alerts Not Showing

**Problem**: Products with low stock not highlighted
**Solution**: Check `products` table `quantity_in_stock` values. Low stock = 10 or less, Out of stock = 0

### XAMPP MySQL Won't Start

**Problem**: MySQL service won't start in XAMPP
**Solutions**:

1. Check if port 3306 is already in use (another MySQL instance)
2. Click "Config" → "my.ini" and change port to 3307 if needed
3. Update `config.properties` with new port: `jdbc:mysql://localhost:3307/smartstock`
4. Restart XAMPP as Administrator

# Run the application

run.bat

````

### Code Style Guidelines
- **Naming**: camelCase for variables, PascalCase for classes
- **Comments**: Use // identifier for student-friendly code
- **Formatting**: Consistent indentation, clear spacing
- **Error Handling**: Try-catch blocks with user-friendly messages

### Adding New Features
1. Create new classes in appropriate packages (`data/`, `model/`, `gui/`, or `util/`)
2. Update repository classes for database operations
3. Add UI components in `gui/` package
4. Update this README with new features
5. Test thoroughly before committing

### Database Migrations
To modify database structure:
```sql
-- Add new column
ALTER TABLE products ADD COLUMN new_field VARCHAR(255);

-- Modify existing column
ALTER TABLE products MODIFY COLUMN product_name VARCHAR(200);

-- Add index for performance
CREATE INDEX idx_product_name ON products(product_name);
````

---

## 🔐 Security Features

- **Password Protection**: Secure user authentication
- **Role-Based Access**: Admin and Employee permissions
- **SQL Injection Prevention**: Prepared statements used throughout
- **Transaction Safety**: Database locks prevent race conditions
- **Input Validation**: Client-side and server-side validation
- **Error Handling**: Graceful error messages without exposing system details

---

## 🚀 Performance Optimizations

- **Database Indexing**: Primary keys and foreign keys indexed
- **Connection Pooling**: Efficient database connection management
- **Lazy Loading**: Load data only when needed
- **Prepared Statements**: Reusable SQL statements for better performance
- **Transaction Batching**: Bulk operations wrapped in transactions
- **CSV Streaming**: Large exports handled efficiently

---

## 🎯 Key Highlights

### Why SmartStock?

✅ **Filipino-Focused**: Built for sari-sari stores with relevant products  
✅ **Student-Friendly**: Clean code with educational documentation  
✅ **Production-Ready**: Robust error handling and data validation  
✅ **Excel Integration**: One-click CSV exports for business reporting  
✅ **Modern QR Technology**: Contactless product management  
✅ **Accurate Accounting**: Historical cost tracking for true profit margins

### Business Benefits

- 📊 **Real-Time Insights**: Instant sales and inventory analytics
- 💰 **Profit Tracking**: Accurate profit calculation using COGS
- ⏱️ **Time Savings**: Automated stock updates and calculations
- 📱 **QR Integration**: Modern, efficient product scanning
- 📈 **Growth Ready**: Multi-user support for expanding businesses
- 📑 **Professional Reports**: Excel-ready exports for accounting

---

## 📄 License

This project is developed as a capstone project for educational purposes.

**Project Type**: Academic Capstone Project  
**Development Period**: 2024-2025  
**Purpose**: Educational demonstration of software engineering principles

### Usage Rights

- ✅ Educational use and learning
- ✅ Portfolio demonstration
- ✅ Academic presentations
- ❌ Commercial distribution without permission
- ❌ Claiming as original work without attribution

---

## 🤝 Contributing

This is a student capstone project. For academic integrity, external contributions are not accepted during the academic period.

After project submission, contributions may be welcome for:

- Bug fixes
- Feature enhancements
- Documentation improvements
- Translation to other languages

---

## 📞 Support & Contact

### For Questions or Issues:

1. 📖 Check the `docs/` folder for detailed guides
2. 🔍 Review the troubleshooting section above
3. 📧 Contact project maintainers (academic purposes only)

### Reporting Bugs

If you encounter issues:

1. Check if the issue is already documented
2. Verify your setup follows installation guide
3. Check MySQL service and database connection
4. Review error logs in terminal/console

---

## 🎓 Acknowledgments

### Technology Providers

### Reporting Bugs

If you encounter issues:

1. Check if the issue is already documented
2. Verify XAMPP MySQL service is running
3. Check database connection via phpMyAdmin
4. Review error logs in terminal/console

- Java Documentation and Tutorials
- MySQL Official Documentation
- Stack Overflow Community
- GitHub Open Source Projects

### Special Thanks

- **Project Advisors**: For guidance throughout development
- **Classmates**: For feedback and testing
- **Filipino Retailers**: For real-world requirements and insights
- **Open Source Community**: For excellent libraries and tools

---

## 📊 Project Statistics

- **Total Lines of Code**: ~8,000+ lines
- **Java Classes**: 30+ classes
- **Database Tables**: 7 tables
- **Features Implemented**: 50+ features
- **Documentation Pages**: 2,000+ lines
- **Development Time**: 3-4 months
- **Testing Hours**: 100+ hours

---

## 🗺️ Roadmap (Future Enhancements)

### Planned Features (Post-Academic)

- [ ] **Mobile App**: Android/iOS companion app
- [ ] **Cloud Sync**: Multi-device synchronization
- [ ] **Barcode Support**: Standard barcode scanning
- [ ] **Email Reports**: Automated daily/weekly reports
- [ ] **Multi-Store**: Support multiple store locations
- [ ] **Advanced Analytics**: Charts, graphs, trend analysis
- [ ] **Supplier Management**: Track suppliers and purchase orders
- [ ] **Customer Database**: Loyalty program and customer tracking
- [ ] **Print Receipts**: Receipt printer integration
- [ ] **Backup/Restore**: Automated database backups

---

## 💡 Tips & Best Practices

### For Developers

- Read `CODE_EXPLANATION.txt` for detailed code walkthrough
- Use `7_DAY_RECREATION_GUIDE.txt` to rebuild from scratch
- Check `DebugLogger.java` for troubleshooting utilities
- Follow repository pattern for new database operations

### For Users

- **Backup Regularly**: Export database weekly
- **Monitor Stock**: Check alerts daily
- **Review Reports**: Analyze sales trends monthly
- **Update Prices**: Keep markup percentages current
- **Train Staff**: Ensure employees understand the system

### For Presentations

### For Developers

- Study the source code structure in `src/com/inventorysystem/`
- Use phpMyAdmin to inspect database structure and relationships
- Check `DebugLogger.java` for troubleshooting utilities
- Follow repository pattern for new database operations

---

<div align="center">

## 🏆 SmartStock

**Empowering Filipino Retailers with Smart Inventory Management**

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat&logo=java)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat&logo=mysql)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-Educational-green?style=flat)](LICENSE)

---

### Quick Links

[📥 Download](#-installation-guide) •
[📖 Documentation](docs/) •
[🐛 Report Bug](#-support--contact) •
[💡 Request Feature](#-roadmap-future-enhancements)

---

**Made with ❤️ by Capstone Development Team**

_Supporting local businesses through technology_

---

_Last Updated: December 2, 2025_  
_Version: 1.0.0_  
_Status: ✅ Production Ready_

</div>

- **Pattern:** MVC (Model-View-Controller) with Repository pattern
- **Database Access:** Try-with-resources for automatic connection management
- **Transaction Management:** BEGIN/COMMIT/ROLLBACK with FOR UPDATE locks
- **UI Design:** Custom UIConstants for consistent styling, dynamic row renderers
- **Security:** PreparedStatement with parameterized queries (SQL injection prevention)
- **Code Style:** Student-friendly simple comments (identifier-based)

---

## 📁 Project Structure

```
CapstoneProject101/
├── src/                       - Java source code
│   ├── App.java              - Application entry point
│   ├── config.properties     - Database configuration
│   │
│   └── com/inventorysystem/
│       ├── data/             - Repository classes (database access)
│       │   ├── DatabaseConnection.java
│       │   ├── ProductRepository.java
│       │   ├── UserRepository.java
│       │   ├── DashboardRepository.java
│       │   ├── StockRepository.java
│       │   ├── RecordsRepository.java
│       │   └── StoreRepository.java
│       │
│       ├── gui/              - User interface components
│       │   ├── userFrame.java           - Main application frame
│       │   ├── LoginPanel.java          - Authentication
│       │   ├── SignupPanel.java         - User registration
│       │   ├── dashboardPanel.java      - Analytics dashboard
│       │   ├── productsPanel.java       - Product management (3000+ lines)
│       │   ├── stockPanel.java          - Stock movements
│       │   ├── recordsPanel.java        - Transaction history
│       │   ├── QRCodePanel.java         - QR code generation
│       │   ├── AboutPanel.java          - Team information
│       │   ├── UIConstants.java         - UI styling constants
│       │   ├── DateRangePanel.java      - Date filtering widget
│       │   ├── MainApplicationPanel.java - Main layout
│       │   ├── EmployeeManagerDialog.java - Employee management
│       │   ├── StoreSettingsDialog.java - Store settings
│       │   └── CustomTableRenderer.java - Dynamic row numbering
│       │
│       ├── model/            - Data models (POJOs)
│       │   ├── User.java
│       │   ├── Product.java
│       │   ├── Category.java
│       │   ├── Store.java
│       │   ├── TransactionRecord.java
│       │   └── StockRecord.java
│       │
│       └── util/             - Utility classes
│           ├── DebugLogger.java - Logging utility
│           └── SoundUtil.java   - Sound effects
│
├── bin/                      - Compiled .class files
│   ├── config.properties
│   ├── com/inventorysystem/  - Compiled package structure
│   └── resources/
│
├── lib/                      - External JAR libraries
│   ├── core-3.4.1.jar       - ZXing core
│   ├── javase-3.4.1.jar     - ZXing Java SE
│   ├── webcam-capture-*.jar - Webcam library
│   ├── json-*.jar           - JSON processing
│   └── mysql-connector-*.jar - MySQL driver
│
├── resources/                - Application resources
│   ├── student_inventory_db.sql - Database schema and sample data
│   └── avatars/              - Team member avatars (for About panel)
│
├── .vscode/                  - VS Code workspace settings
├── SmartStock.jar            - Executable application (runnable JAR)
├── MANIFEST.MF               - JAR manifest file
├── build.bat                 - Windows compilation script
├── run.bat                   - Windows launch script
└── README.md                 - This file (project overview)
```

---

## 📋 System Requirements

### Minimum Requirements

- **Java Runtime:** JRE 21 or higher (OpenJDK or Oracle JDK)
- **Database:** MySQL 5.7+ or MariaDB 10.2+
- **Memory:** 512 MB RAM
- **Display:** 1280x720 resolution
- **Storage:** 100 MB free space
- **OS:** Windows 7+, Linux, macOS

### Recommended Configuration

- **Java Runtime:** JRE 21 (latest update)
- **Database:** MySQL 8.0+ or MariaDB 10.6+
- **Memory:** 1 GB RAM or more
- **Display:** 1366x768 or higher
- **Webcam:** For QR code scanning feature

---

## 🎯 Default Accounts

| Username  | Password | Role     | Access Level | Permissions                                      |
| --------- | -------- | -------- | ------------ | ------------------------------------------------ |
| Admin     | 123      | Admin    | Full Access  | All features, settings, employee management      |
| Employee1 | 123      | Employee | Limited      | View/manage products (admin's data), no settings |

**Note:** Employees see their admin's inventory data. Create new employees via Settings > Employee Manager.

## 📞 Support & Troubleshooting

Having issues? Common solutions:

1. **Common Issues:**

   - Database connection error → Check XAMPP MySQL is running
   - Login failed → Verify database imported correctly (use `resources/student_inventory_db.sql`)
   - QR Scanner not working → Allow webcam permissions
   - Compilation errors → Ensure all JARs in lib/ folder

2. **Database Reset:**
   - Re-import `resources/student_inventory_db.sql` in phpMyAdmin
   - Verify database name is `capstone_inventory_db`

---

## 🎓 Academic Information

**Course:** Bachelor of Science in Information Technology  
**Subject:** Capstone Project 1 (1st Semester, AY 2025-2026)  
**Institution:** Manuel S. Enverga University Foundation - Candelaria, Inc.  
**Section:** BSIT-1B  
**Group:** #3

### Development Team

| Name                       | Role                                                   |
| -------------------------- | ------------------------------------------------------ |
| George Harold A. Alcantara | Project Manager / Documentation Writer                 |
| Aldrin Miguel A. Jariel    | System Analyst / Developer / QA / Documentation Writer |
| John Christoper A. Perez   | UI/UX Designer / Documentation Writer                  |
| Ron Paulo G. Angeles       | Documentation Writer                                   |
| Matthew Dane D. Calangian  | Documentation Writer                                   |

---

## 📝 License & Usage

This is an educational capstone project developed for academic purposes.

**License:** Free to use for learning and educational purposes  
**Development Period:** October 3 - November 2, 2025 (30 days)  
**Version:** 1.0.0 (Build 20251202)  
**Last Updated:** December 2, 2025

---

## 🌟 Highlights & Achievements

- ✅ Complete inventory management system with 7 main modules
- ✅ QR code integration with JSON data structure (Add/Update/Sell/Delete modes)
- ✅ **Historical cost tracking** for accurate profit calculation (critical business feature)
- ✅ Real-time analytics dashboard with date range filtering
- ✅ Multi-user system with role-based access control (Admin/Employee)
- ✅ Professional UI/UX with dynamic row numbering
- ✅ Comprehensive documentation (7+ detailed guides)
- ✅ Bulk operations: Set Markup, Remove Stock, Delete, Return (4 types)
- ✅ **Transaction integrity** with BEGIN/COMMIT/ROLLBACK and FOR UPDATE locks
- ✅ **Stock validation** to prevent negative inventory
- ✅ Secure database access with try-with-resources and PreparedStatement
- ✅ Webcam integration for real-time QR scanning
- ✅ Student-friendly code style (simple comments, clear structure)

---

## 🔮 Future Enhancements

Potential features for future versions:

- Password hashing (currently plain text for demo)
- PDF/Excel export for reports
- Email notifications for low stock alerts
- Barcode support (in addition to QR codes)
- Mobile application companion
- Advanced analytics with sales forecasting
- Multi-store support with centralized management
- Supplier management module
- Purchase order system
- Backup/restore functionality
- Audit trail for all transactions

---

## 🙏 Acknowledgments

Special thanks to:

- Our instructors and advisors
- Manuel S. Enverga University Foundation - Candelaria, Inc.
- Open-source library contributors (ZXing, Webcam Capture)
- The Java and MySQL communities

---

**📖 For complete documentation, visit the `/docs/` folder and start with `INDEX.txt`**

**💼 SmartStock - Streamline Your Business, Maximize Your Profits**

© 2025 SmartStock Development Team. All Rights Reserved.

## 💡 Usage Guide

### Dashboard

- View key metrics: total products, stock, out-of-stock count
- Monitor financial data: cost, income, profit
- Analyze best sellers and stock alerts
- Filter data by date range

### Products Panel

- **Add Product:** Click Add/Update → Fill form → Save
- **Edit Product:** Select row → Click Add/Update → Modify → Save
- **Sell Product:** Select row → Click Sell → Enter quantity → Confirm
- **Return Product:** Select row → Return → Choose type (Customer Return/Reject/Refund/Dispose) → Process
- **Remove Stock:** Select row(s) → Remove Stock → Enter quantity and reason → Confirm
- **Generate QR:** Select product → Generate QR → Save as PNG
- **Scan QR:** Enable Scanner → Choose mode (Add/Sell/Delete) → Point webcam at QR code
- **Bulk Operations:** Select multiple rows → Bulk Operations → Choose action (Set Markup/Remove Stock/Delete/Return)
- **Search:** Use search bar to filter by any column
- **Sort:** Click column headers to sort

### Stock Panel

- View stock movements (In, Out, Available)
- Filter by date range
- Track inventory changes over time

### Records Panel

- View complete transaction history
- Filter by date range
- See sales, purchases, returns

### Settings

- **Store Settings:** Customize store name, address, contact
- **Employee Manager:** Add/view employees (Admin only)
- **User Profile:** View account information

---

## 📞 Support

**Common Troubleshooting:**

1. **Database Connection Issues:**

   - Ensure XAMPP MySQL is running (port 3306)
   - Verify `config.properties` has correct credentials
   - Database name: `capstone_inventory_db`

2. **Import Database:**

   - Open phpMyAdmin (http://localhost/phpmyadmin)
   - Create database: `capstone_inventory_db`
   - Import: `resources/student_inventory_db.sql`

3. **Build Issues:**
   - Ensure all JARs are in `lib/` folder
   - Run `build.bat` from project root
   - Check Java version: `java -version` (need JDK 21+)

---

**💼 SmartStock - Empowering Small Businesses with Smart Inventory Solutions**
