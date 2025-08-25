package com.hcmute.clothingstore.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagninationResponse {

    private Meta meta;
    private Object data;


    @Data
    @Builder
    private static class Meta{
        private Long pageNumber;

        private Long pageSize;

        private Long totalElement;

        private Long totalPage;

        private boolean isLastPage;

    }
}
