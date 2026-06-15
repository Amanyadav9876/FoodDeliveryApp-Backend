@Async
public void sendOtpEmail(String toEmail, String otp) {
    try {
        RestTemplate restTemplate = new RestTemplate();
        String body = """
            {
                "from": "KHAO App <onboarding@resend.dev>",
                "to": ["%s"],
                "subject": "KHAO - Email Verification OTP",
                "text": "Tumhara OTP hai: %s\\n\\n5 minutes mein expire ho jaayega.\\n\\nKHAO Team"
            }
            """.formatted(toEmail, otp);  // ← toEmail pehle, otp baad mein

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
            "https://api.resend.com/emails",
            request,
            String.class
        );
        System.out.println("Email sent to: " + toEmail + " | Status: " + response.getStatusCode());
    } catch (Exception e) {
        System.out.println("Email send failed: " + e.getMessage());
    }
}

@Async
public void mailConfirmation(String email, String orderId) {
    try {
        RestTemplate restTemplate = new RestTemplate();
        String body = """
            {
                "from": "KHAO App <onboarding@resend.dev>",
                "to": ["%s"],
                "subject": "KHAO - Order Confirmed!",
                "text": "Tumhara order place ho gaya hai!\\nOrder ID: %s\\nApp mein track karo."
            }
            """.formatted(email, orderId);  // ← email pehle, orderId baad mein

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(
            "https://api.resend.com/emails",
            request,
            String.class
        );
        System.out.println("Order confirmation sent to: " + email);
    } catch (Exception e) {
        System.out.println("Order email failed: " + e.getMessage());
    }
}
