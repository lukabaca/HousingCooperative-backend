package pl.dmcs.blaszczyk.service;

public interface MailingService {
    void sendMail(String to, String title, String messageContent);
}
