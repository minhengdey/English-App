# English App

## 1. Giới thiệu dự án

Dự án Ứng dụng hỗ trợ tự học tiếng Anh hướng đến việc giúp người dùng cải thiện kỹ năng học từ vựng một cách hiệu quả, thú vị và cá nhân hóa. Xuất phát từ thực tế rằng nhiều người gặp khó khăn trong việc ghi nhớ từ vựng và thiếu các công cụ hỗ trợ học tập tiện lợi.

## 2. Các chức năng chính

- **Flashcards thông minh** – Ứng dụng phương pháp lặp lại ngắt quãng (Spaced Repetition) để tối ưu hóa việc ghi nhớ từ vựng.

- **Tích hợp từ điển & dịch văn bản** – Giúp người dùng tra cứu nghĩa từ, phát âm và dịch đoạn văn bản một cách nhanh chóng, chính xác.

- **Game học tập** – Kết hợp học tập với giải trí thông qua các trò chơi giúp củng cố kiến thức từ vựng.

- **Cá nhân hóa nội dung học** – Cho phép người dùng tạo danh sách từ vựng theo chủ đề yêu thích, phù hợp với nhu cầu cá nhân.

## 3. Công nghệ

### 3.1 Công nghệ sử dụng

- Java

- Spring Boot

- MySQL

- JavaFX

- OAuth 2.0

- JWT

- Postman

### 3.2. Cấu trúc dự án

#### 3.2.1. Backend

```java
APIEnglishApp (Backend - Spring Boot)
│── src/main/java/com/example/apienglishapp
│   ├── configuration  # Cấu hình Spring Boot (Security, CORS, etc.)
│   ├── controller     # Xử lý request từ client
│   ├── dto            # Định nghĩa request/response DTOs
│   ├── entity         # Các class ánh xạ database
│   ├── repository     # Tầng truy vấn dữ liệu (JPA Repository)
│   ├── service        # Xử lý logic nghiệp vụ
│── src/main/resources/db  # File cấu hình database
│── src/test/java/com/example/apienglishapp  # Unit tests
```

**APIEnglishApp:** Backend sử dụng Java Spring Boot, cung cấp API phục vụ ứng dụng.

#### 3.2.2. Frontend

```java
FEEnglishApp (Frontend - JavaFX)
│── src/main/java/com/noface/englishapp
│   ├── controller  # Điều khiển giao diện
│   ├── model       # Định nghĩa dữ liệu
│   ├── utils       # Các hàm hỗ trợ
│   ├── view        # Giao diện người dùng
│── src/main/resources/com/noface/englishapp  # Các file tài nguyên
```

**FEEnglishApp:** Frontend JavaFX để tạo giao diện người dùng.

## 4. Sơ đồ thực thể - quan hệ

![alt text](/images/image-19.png)

## 5. Kết quả cài đặt

### 5.1. Giao diện đăng nhập

#### Màn hình đăng nhập

![alt text](/images/image.png)

### 5.2. Giao diện đăng ký

#### Màn hình đăng ký

![alt text](/images/image-1.png)

### 5.3. Giao diện hồ sơ người dùng
 
#### Màn hình thông tin cá nhân

![alt text](/images/image-2.png)

### 5.4. Giao diện các chủ đề
 
#### Màn hình hiện các topic của mình

![alt text](/images/image-3.png)
 
#### Màn hình thêm từ

![alt text](/images/image-4.png)
 	 
#### Màn hình edit topic (có thể ấn vào 1 từ để edit từ đó)

![alt text](/images/image-16.png)
 		 
#### Màn hình learn của topic (nhấn show sẽ hiện ra màn hình bên phải)

![alt text](/images/image-17.png)

![alt text](/images/image-18.png)

### 5.5. Giao diện dịch văn bản
 
#### Màn hình dịch văn bản (nhập văn bản và nhấn Translate sẽ trả ra kết quả bên dưới)

![alt text](/images/image-9.png)

### 5.6. Giao diện tra từ
 
#### Màn hình tra từ (nhập từ vào và nhấn nút tra từ sẽ trả ra kết quả tương tự như trên)

![alt text](/images/image-10.png)

### 5.7. Giao diện trò chơi

#### Màn hình game (nhấp vào 1 trong 2 game để chơi)

![alt text](/images/image-11.png)

### 5.8. Giao diện trò chơi ghép chữ
 
#### Màn hình game ghép chữ (ấn vào từng chữ cái để ghép thành 1 từ đúng theo topic)

![alt text](/images/image-12.png)
 
#### Màn hình sau khi ghép xong từ đúng và ấn show (sẽ trả về nghĩa,, phiên âm của từ đó)

![alt text](/images/image-13.png)

### 5.9. Giao diện trò chơi nghe từ
 
#### Màn hình game nghe từ (ấn vào nút hình loa để nghe từ theo chủ đề đã chọn)

![alt text](/images/image-14.png)
 
#### Màn hình sau khi nghe và điền từ rồi ấn nút kiểm tra

![alt text](/images/image-15.png)