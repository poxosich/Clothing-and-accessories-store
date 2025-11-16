package com.example.clothingandaccessoriesstore.dto.liked;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikedRequestDto {
    private int productId ;
    private String userEmail;
}
