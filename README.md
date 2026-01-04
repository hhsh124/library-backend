# **📚 智慧图书馆管理系统 \- 后端运行说明**

本文档旨在指导开发者如何在本地环境中快速搭建并运行图书馆管理系统的后端服务。

## **🛠️ 1\. 环境准备 (Prerequisites)**

在开始之前，请确保您的开发环境满足以下要求：

* **操作系统**: Windows / macOS / Linux  
* **Java Development Kit (JDK)**:  JDK 21 (推荐)  
* **构建工具**: Maven 3.6+  
* **数据库**: MySQL 8.0+  
* **开发工具 (IDE)**: IntelliJ IDEA 

## **🗄️ 2\. 数据库配置 (Database Setup)**

本项目使用 MySQL 作为持久层数据库，启动前需完成初始化。

### **2.1 创建数据库**

请使用您的数据库管理工具（如 Navicat, DBeaver, 或 MySQL Workbench）连接到本地 MySQL，并执行以下 SQL 语句：

\-- 创建数据库（如果不存在）  
CREATE DATABASE IF NOT EXISTS lms\_db CHARACTER SET utf8mb4 COLLATE utf8mb4\_unicode\_ci;

### **2.2 修改连接配置**

使用 IDE 打开项目，找到配置文件：  
src/main/resources/application.yml  
请根据您本地的实际情况修改以下配置项：

server:  
  port: 9090 \# 后端服务端口，请确保未被占用

spring:  
  datasource:  
    \# 数据库连接地址 (确保端口 3306 和数据库名 lms\_db 正确)  
    \# allowPublicKeyRetrieval=true 用于解决 MySQL 8.0+ 连接时的公钥检索错误  
    url: jdbc:mysql://localhost:3306/lms\_db?useSSL=false\&serverTimezone=Asia/Shanghai\&allowPublicKeyRetrieval=true  
      
    \# 👇 请修改为您本地的 MySQL 账号  
    username: root  
      
    \# 👇 请修改为您本地的 MySQL 密码  
    password: 123456  
      
    driver-class-name: com.mysql.cj.jdbc.Driver  
  jpa:  
    \# 自动更新表结构 (首次启动会自动建表)  
    hibernate:  
      ddl-auto: update  
    \# 在控制台打印 SQL 语句，方便调试  
    show-sql: true

## **🚀 3\. 启动项目 (Startup)**

### **方式一：使用 IntelliJ IDEA (推荐)**

1. **导入项目**：打开 IDEA，选择 Open，选中后端项目的根目录（包含 pom.xml 的文件夹）。  
2. **加载依赖**：等待 IDEA 自动加载 Maven 依赖。  
   * *提示*：如果下载速度过慢，请检查 Maven 的 settings.xml 是否配置了阿里云镜像。  
3. **运行**：  
   * 找到启动类：src/main/java/com/example/library/LibraryApplication.java  
   * 点击类名旁边的绿色 **▶ Run** 按钮。

### **方式二：使用命令行 (Terminal)**

在项目根目录下打开终端，执行以下命令：

\# 1\. 编译并打包 (跳过测试以节省时间)  
mvn clean package \-DskipTests

\# 2\. 运行 Jar 包 (target 目录下)  
java \-jar target/library-backend-0.0.1-SNAPSHOT.jar  
