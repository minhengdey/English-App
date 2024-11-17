//package com.example.apienglishapp.configuration;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataInitializer implements CommandLineRunner {
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Chạy lệnh Python để tạo dữ liệu vào database
//        ProcessBuilder processBuilder = new ProcessBuilder("python", "resource/db/convert_db_sqlite3_into_mysql.py");
//        processBuilder.inheritIO();  // Để xem output của script nếu cần
//        Process process = processBuilder.start();
//        process.waitFor();  // Đợi cho script chạy xong
//
//        // Kiểm tra nếu có lỗi
//        if (process.exitValue() != 0) {
//            throw new RuntimeException("Python script failed to execute.");
//        }
//    }
//}
