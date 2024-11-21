package com.example.topcvrecruiter.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtils {

    public static File saveImageToInternalStorage(Context context, Uri imageUri, String imageName) {
        ContentResolver contentResolver = context.getContentResolver();

        // Lấy đường dẫn của thư mục để lưu ảnh
        File directory = new File(context.getFilesDir(), "app_images");
        if (!directory.exists()) {
            directory.mkdir(); // Tạo thư mục nếu nó chưa tồn tại
        }

        // Tạo một file mới trong thư mục đã tạo
        File imageFile = new File(directory, imageName);

        try (InputStream inputStream = contentResolver.openInputStream(imageUri);
             OutputStream outputStream = new FileOutputStream(imageFile)) {

            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
            }

            // Đảm bảo file được lưu thành công
            outputStream.flush();
            Log.d("ImageUtils", "Image saved to " + imageFile.getAbsolutePath());
            return imageFile;
        } catch (IOException e) {
            Log.e("ImageUtils", "Failed to save image: " + e.getMessage());
            return null;
        }
    }
}
