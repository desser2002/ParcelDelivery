package org.dzianisbova.parceldelivery.sortingcenter.infrastructure.web;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CreateSortingCenterRequest {
    @NotBlank
    private String name;
}
