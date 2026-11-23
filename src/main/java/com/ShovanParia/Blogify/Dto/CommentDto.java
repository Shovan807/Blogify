package com.ShovanParia.Blogify.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long commentId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserDto userDto;

    private PostDto postDto;
}