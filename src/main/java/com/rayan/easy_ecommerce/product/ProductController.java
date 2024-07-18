package com.rayan.easy_ecommerce.product;

import com.rayan.easy_ecommerce.product.dto.CreateProductRequestPayload;
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
@RequestMapping(value = "/products", produces = {"application/json"})
@Tag(name = "Products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(summary = "Create a new product", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductIdResponsePayload.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "Product already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductIdResponsePayload> createProduct(@Valid @RequestBody CreateProductRequestPayload payload) {
        Long productId = productService.createProduct(payload);
        URI location = buildResourceLocation(productId);
        return ResponseEntity.created(location).body(new ProductIdResponsePayload(productId));
    }


    @Operation(summary = "Search for a specific product", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductReponsePayload.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductReponsePayload> getProduct(@PathVariable Long productId) {
        Product product = productService.getProduct(productId);
        return ResponseEntity.ok(ProductReponsePayload.toProductResponse(product));
    }


    @Operation(summary = "Search all products", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductReponsePayload.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ProductReponsePayload>> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Product> products = productService.getAllProducts(pageRequest);
        return ResponseEntity.ok(products.map(ProductReponsePayload::toProductResponse));
    }


    @Operation(summary = "Update data for a specific product", method = "PUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductIdResponsePayload.class))),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping(value = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductIdResponsePayload> updateProduct(@PathVariable Long productId, @Valid @RequestBody CreateProductRequestPayload payload) {
        Long updatedProductId = productService.updateProduct(productId, payload);
        return ResponseEntity.ok(new ProductIdResponsePayload(updatedProductId));
    }

    @Operation(summary = "Deletes a specific product that has not yet been added to an order.", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }


    private URI buildResourceLocation(Long resourceId) {
        return ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(resourceId)
            .toUri();
    }
}
