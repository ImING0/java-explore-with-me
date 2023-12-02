package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDtoIn;
import ru.practicum.ewm.category.dto.CategoryDtoOut;
import ru.practicum.ewm.category.service.admin.ICategoryAdminService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final ICategoryAdminService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDtoOut> createCategory(
            @RequestBody @Valid CategoryDtoIn categoryDtoIn) {
        log.info("Creating category {}", categoryDtoIn);

        CategoryDtoOut newCategory = categoryService.create(categoryDtoIn);

        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDtoOut> updateCategory(
            @RequestBody @Valid CategoryDtoIn categoryDtoIn,
            @PathVariable Long catId) {
        log.info("Updating category with id {}", catId);

        CategoryDtoOut updatedCategory = categoryService.update(categoryDtoIn, catId);

        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Deleting category with id {}", catId);

        categoryService.delete(catId);
    }
}
