package com.rayan.easy_ecommerce.category;

import com.rayan.easy_ecommerce.category.dto.CategoryIdResponsePayload;
import com.rayan.easy_ecommerce.category.dto.CreateCategoryRequestPayload;
import com.rayan.easy_ecommerce.product.dto.ProductIdResponsePayload;
import com.rayan.easy_ecommerce.product.dto.ProductReponsePayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/categories", produces = {"application/json"})
@Tag(name = "Categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create a new category", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryIdResponsePayload.class))),
        @ApiResponse(responseCode = "409", description = "Category already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryIdResponsePayload> createCategory(@Valid @RequestBody CreateCategoryRequestPayload payload) {
        Long categoryId = this.categoryService.createCategory(payload);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(categoryId)
            .toUri();
        return ResponseEntity.created(location).body(new CategoryIdResponsePayload(categoryId));
    }

    @Operation(summary = "Search all categories", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Category>> getAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Category> categories = this.categoryService.getAllCategories(pageable);
        return ResponseEntity.ok().body(categories);
    }

    @Operation(summary = "Update data for a specific category", method = "PUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryIdResponsePayload.class))),
        @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryIdResponsePayload> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CreateCategoryRequestPayload payload) {
        Long id = categoryService.updateCategory(categoryId, payload);
        return ResponseEntity.ok(new CategoryIdResponsePayload(id));
    }

    @Operation(summary = "Deletes a specific category that has not yet been added to an product.", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{categoryName}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryName) {
        categoryService.deleteCategory(categoryName);
        return ResponseEntity.noContent().build();
    }
}
