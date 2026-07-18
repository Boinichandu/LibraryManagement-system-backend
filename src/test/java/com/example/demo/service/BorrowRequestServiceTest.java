package com.example.demo.service;

import com.example.demo.dto.NotificationResponse;
import com.example.demo.model.Book;
import com.example.demo.model.BorrowRequest;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.BorrowRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowRequestServiceTest {

    @Mock
    private BorrowRequestRepository borrowRequestRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BorrowRequestService borrowRequestService;

    @Test
    void approveReturn_shouldRejectRequestThatWasNeverApproved() {
        BorrowRequest request = new BorrowRequest();
        request.setId(100L);
        request.setBookId(1L);
        request.setStatus("PENDING");

        when(borrowRequestRepository.findById(100L)).thenReturn(Optional.of(request));

        String result = borrowRequestService.approveReturn(100L);

        assertThat(result).isEqualTo("Request is not currently borrowed");
    }

    @Test
    void getNotifications_shouldShowReminderWhenDueDateIsTomorrow() {
        BorrowRequest request = new BorrowRequest();
        request.setUserId(1L);
        request.setStatus("APPROVED");
        request.setDueDate(LocalDate.now().plusDays(1));

        when(borrowRequestRepository.findByUserId(1L)).thenReturn(List.of(request));

        List<NotificationResponse> notifications = borrowRequestService.getNotifications(1L);

        assertThat(notifications).hasSize(1);
        assertThat(notifications.get(0).getMessage()).contains("tomorrow");
    }
}
