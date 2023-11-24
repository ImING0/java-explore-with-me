package ru.practicum.ewm.service.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.category.dto.CategoryDtoOut;
import ru.practicum.ewm.service.category.service.ICategoryService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDtoOut>> getAllCategories(
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @PositiveOrZero Integer size) {
        log.info("Getting categories from {} to {}", from, from + size);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(categoryService.getAll(pageable));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDtoOut> getCategoryById(@PathVariable Long catId) {
        log.info("Getting category with id {}", catId);
        return ResponseEntity.ok(categoryService.getById(catId));
    }
}
