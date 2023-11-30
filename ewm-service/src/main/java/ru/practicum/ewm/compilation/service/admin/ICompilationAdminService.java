package ru.practicum.ewm.compilation.service.admin;

import ru.practicum.ewm.compilation.dto.CompilationDtoOut;
import ru.practicum.ewm.compilation.dto.NewCompilationDtoIn;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDtoIn;

public interface ICompilationAdminService {
    /**
     * Создание компиляции
     *
     * @param newCompilationDtoIn - dto для создания компиляции
     * @return dto созданной компиляции
     */
    CompilationDtoOut create(NewCompilationDtoIn newCompilationDtoIn);

    /**
     * Удаление компиляции
     *
     * @param compId - id компиляции
     */
    void delete(Long compId);

    CompilationDtoOut update(UpdateCompilationDtoIn updateCompilationDtoIn,
                             Long compId);
}
