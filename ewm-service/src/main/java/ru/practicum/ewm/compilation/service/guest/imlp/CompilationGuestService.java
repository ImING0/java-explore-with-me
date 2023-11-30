package ru.practicum.ewm.compilation.service.guest.imlp;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDtoOut;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.QCompilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.service.guest.ICompilationGuestService;
import ru.practicum.ewm.error.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationGuestService implements ICompilationGuestService {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDtoOut> getAllByParams(Boolean pinned,
                                                  Integer from,
                                                  Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        QCompilation compilation = QCompilation.compilation;
        BooleanExpression predicate = compilation.isNotNull();
        if (pinned != null) {
            predicate = predicate.and(compilation.pinned.eq(pinned));
        }
        return compilationRepository.findAll(predicate, pageable)
                .stream()
                .map(CompilationMapper::toCompilationDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDtoOut getById(Long compId) {
        Compilation compilation = getCompOrThrow(compId);
        return CompilationMapper.toCompilationDtoOut(compilation);
    }

    private Compilation getCompOrThrow(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Compilation with id %d not found", compId)));
    }
}