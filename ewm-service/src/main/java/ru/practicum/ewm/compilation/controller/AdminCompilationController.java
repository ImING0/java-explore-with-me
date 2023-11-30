package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDtoOut;
import ru.practicum.ewm.compilation.dto.NewCompilationDtoIn;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDtoIn;
import ru.practicum.ewm.compilation.service.admin.ICompilationAdminService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationController {
    private final ICompilationAdminService compilationAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CompilationDtoOut> createCompilation(
            @RequestBody @Valid NewCompilationDtoIn compilationDtoIn) {
        return new ResponseEntity<>(compilationAdminService.create(compilationDtoIn),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable("compId") Long compId) {
        log.info("Deleting compilation with id {}", compId);
        compilationAdminService.delete(compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDtoOut> updateCompById(@PathVariable("compId") Long compId,
                                                            @RequestBody
                                                            @Valid UpdateCompilationDtoIn updateCompilationDtoIn) {
        return new ResponseEntity<>(compilationAdminService.update(updateCompilationDtoIn, compId),
                HttpStatus.OK);
    }
}
