package ru.practicum.ewm.service.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.category.dto.CategoryDtoIn;
import ru.practicum.ewm.service.category.dto.CategoryDtoOut;
import ru.practicum.ewm.service.category.service.ICategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final ICategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDtoOut> createCategory(
            @RequestBody @Valid CategoryDtoIn categoryDtoIn) {
        log.info("Creating category {}", categoryDtoIn);
        return new ResponseEntity<>(categoryService.create(categoryDtoIn), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDtoOut> updateCategory(
            @RequestBody @Valid CategoryDtoIn categoryDtoIn,
            @PathVariable Long catId) {
        log.info("Updating category with id {}", catId);
        return new ResponseEntity<>(categoryService.update(categoryDtoIn, catId), HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long catId) {
        log.info("Deleting category with id {}", catId);
        categoryService.delete(catId);
        return ResponseEntity.noContent()
                .build();
    }
}
