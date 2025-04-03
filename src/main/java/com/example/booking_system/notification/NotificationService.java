package com.example.booking_system.notification;

import com.example.booking_system.booking.Booking;
import org.aspectj.apache.bcel.generic.RET;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;//final = variabila nu mai poate fi modificata dupa ce este initializata


    @Autowired//autowired injecteaza automat depedentele = o depedenta este o clasa care are nevoie de alta clasa ca sa functionez
    //injectare = furnizare automata a unei instante
    //In loc sa creezi tu manual instantele claselor de care nevoie, Spring le creeaza automat si mi le ofera acolu unde am nevoie
    //Cauta un bean de tip NotificationRepository si daca exista, il injecteaza in variabila
    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository=notificationRepository;
    }

    public Notification createNotification(Long userId, Booking booking, String message){
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setBooking(booking);
        notification.setMessage(message);

        return notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(Long userId){
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId){
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    public void markAsRead(Notification notification){
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
    }





}
