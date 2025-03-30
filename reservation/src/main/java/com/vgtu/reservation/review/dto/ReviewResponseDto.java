package com.vgtu.reservation.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {

    private String content;
    private LocalDateTime createdAt;

}
