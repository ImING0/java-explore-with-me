package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDtoOut;
import ru.practicum.ewm.compilation.service.guest.ICompilationGuestService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class GuestCompilationController {
    private final ICompilationGuestService compilationGuestService;

    @GetMapping
    public ResponseEntity<List<CompilationDtoOut>> getAllCompByParams(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all compilations by params: pinned = {}, from = {}, size = {}", pinned, from,
                size);

        List<CompilationDtoOut> compilations = compilationGuestService.getAllByParams(pinned, from,
                size);

        return ResponseEntity.ok(compilations);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDtoOut> getCompById(
            @PathVariable(name = "compId", required = true) Long compId) {
        log.info("Get compilation by id: {}", compId);

        CompilationDtoOut compilation = compilationGuestService.getById(compId);

        return ResponseEntity.ok(compilation);
    }
}
