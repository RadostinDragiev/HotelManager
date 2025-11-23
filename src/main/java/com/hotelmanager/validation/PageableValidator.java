package com.hotelmanager.validation;

import com.hotelmanager.exception.exceptions.PageOutOfBoundsException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableValidator {

    public static void validatePageRequest(Page<?> page, Pageable pageable) {
        if (pageable.getPageNumber() >= page.getTotalPages() && page.getTotalPages() > 0) {
            throw new PageOutOfBoundsException("Requested page exceeds total pages. " +
                    "Max page index: " + (page.getTotalPages() - 1));
        }
    }
}
