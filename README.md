# Expense Tracker API

A Spring Boot based REST API for managing personal expenses with categories.  
Supports CRUD operations, pagination, category filters, and will include JWT authentication for multi-user support.

---

## 🚀 Features
- Manage **categories** (Food, Transport, Shopping, etc.)
- Manage **expenses** with description, amount, date, and category
- **Pagination** and **category-based filters** for expenses
- **One-to-Many** relationship between Category and Expense
- **Custom exception handling**
- **Flyway migration** for database schema
- Planned: **JWT authentication & per-user data isolation**

---

## 🛠️ Tech Stack
- Java 17
- Spring Boot 3
- Spring Data JPA
- MySQL + Flyway (DB migrations)
- Lombok
- Postman (for API testing)

---

## 📂 Project Structure
