package com.nnh.ra1neestore.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPasswordResetEmail(String toEmail, String resetLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Reset mật khẩu - RA1NEE STORE");

        String htmlContent = buildResetEmailContent(resetLink);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private String buildResetEmailContent(String resetLink) {
        String template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; padding: 20px; }
                        .container { max-width: 600px; margin: 0 auto; background: #f9f9f9; padding: 30px; border-radius: 8px; }
                        h2 { color: #667eea; }
                        .button { display: inline-block; padding: 12px 30px; background: #667eea; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                        .footer { margin-top: 30px; font-size: 12px; color: #666; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h2>🔐 Reset Mật Khẩu</h2>
                        <p>Xin chào,</p>
                        <p>Bạn đã yêu cầu reset mật khẩu cho tài khoản RA1NEE STORE của mình.</p>
                        <p>Click vào nút bên dưới để tạo mật khẩu mới:</p>
                        <center>
                            <a href="%s" class="button">Reset Mật Khẩu</a>
                        </center>
                        <p><strong>Lưu ý:</strong></p>
                        <ul>
                            <li>Link này chỉ có hiệu lực trong <strong>1 giờ</strong></li>
                            <li>Nếu bạn không yêu cầu reset mật khẩu, vui lòng bỏ qua email này</li>
                        </ul>
                        <div class="footer">
                            <p>Trân trọng,<br><strong>RA1NEE STORE Team</strong></p>
                            <p>© 2024 RA1NEE STORE. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """;

        return String.format(template, resetLink);
    }
}
