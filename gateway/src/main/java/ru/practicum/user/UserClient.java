package ru.practicum.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.booking.dto.BookItemRequestDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.client.BaseClient;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserUpdateDto;

import java.util.List;
import java.util.Map;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getUserById(Long userId) {
        return get("/" + userId);
    }


//    @PostMapping
//    ResponseEntity<Object> createUser(UserDto userDto);
//
//    @PatchMapping("/{id}")
//    ResponseEntity<Object> updateUser(Long userId, UserUpdateDto userDto);
//
//    @DeleteMapping("/{id}")
//    ResponseEntity<Void> deleteUser(Long userId);
//
//    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
//        Map<String, Object> parameters = Map.of(
//                "state", state.name(),
//                "from", from,
//                "size", size
//        );
//        return get("?state={state}&from={from}&size={size}", userId, parameters);
//    }
//
//
//    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
//        return post("", userId, requestDto);
//    }
//
//    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
//        return get("/" + bookingId, userId);
//    }
}
