package com.rayan.easy_ecommerce.user;

import com.rayan.easy_ecommerce.user.dto.CreateUserRequestPayload;
import com.rayan.easy_ecommerce.user.dto.UpdateUserRequestPayload;
import com.rayan.easy_ecommerce.user.dto.UserIdResponsePayload;
import com.rayan.easy_ecommerce.user.dto.UserReponsePayload;
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
@RequestMapping(value = "/users", produces = {"application/json"})
@Tag(name = "Users")
public class UserController {


    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "Create a new user", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserIdResponsePayload.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "User already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserIdResponsePayload> createUser(@Valid @RequestBody CreateUserRequestPayload payload) {
        Long userId = userService.createUser(payload);
        URI location = buildResourceLocation(userId);
        return ResponseEntity.created(location).body(new UserIdResponsePayload(userId));
    }


    @Operation(summary = "Search for a specific user", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserReponsePayload.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReponsePayload> getUser(@PathVariable(value = "userId") Long userId) {
        User user = this.userService.getUser(userId);
        return ResponseEntity.ok().body(UserReponsePayload.toResponse(user));
    }


    @Operation(summary = "Search all users", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserReponsePayload.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserReponsePayload>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<User> users = this.userService.getAllUsers(pageable);
        return ResponseEntity.ok().body(users.map(UserReponsePayload::toResponse));
    }


    @Operation(summary = "Update data for a specific user", method = "PUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserIdResponsePayload.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserIdResponsePayload> updateUser(@PathVariable(value = "userId") Long userId, @Valid @RequestBody UpdateUserRequestPayload payload) {
        Long id = this.userService.updateUser(userId, payload);
        return ResponseEntity.ok().body(new UserIdResponsePayload(id));
    }


    @Operation(summary = "Delete a specific user", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping(value = "{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "userId") Long userId) {
        this.userService.deleteUser(userId);
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
