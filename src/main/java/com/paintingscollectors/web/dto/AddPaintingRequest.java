package com.paintingscollectors.web.dto;

import com.paintingscollectors.painting.model.Style;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPaintingRequest {
    @Size(min = 5, max = 40, message = "Name length must be between 5 and 40 characters!")
    private String name;

    @Size(min = 5, max = 30, message = "Author name must be between 5 and 30 characters!")
    private String author;

    @URL(message = "Please enter valid image URL!")
    private String imageUrl;

    @NotNull(message = "You must select a style!")
    private Style style;
}
