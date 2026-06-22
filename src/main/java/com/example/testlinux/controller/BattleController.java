package com.example.testlinux.controller;

import com.example.testlinux.aspect.strikerstat.CheckEventOwnership;
import com.example.testlinux.domain.Battle;
import com.example.testlinux.dto.AuthorizeEventDto;
import com.example.testlinux.repository.BattlesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для создания Battle через DDD-фабрику.
 *
 * Контроллер НЕ знает про new Battle() и не собирает объект вручную —
 * он лишь передаёт входные данные в фабричный метод Battle.create().
 * Вся логика создания и проверка инвариантов инкапсулированы в сущности.
 *
 * @CheckEventOwnership на КЛАССЕ — EventOwnershipAspect проверит владение соревнованием
 * во всех методах. Методы без eventId (createBattle, nextSection) аспект молча пропустит,
 * методы с eventId (из параметра или из DTO) — проверит.
 */
@Slf4j
@RestController
@RequestMapping("/battles")
@CheckEventOwnership
public class BattleController {

    private final BattlesRepository battlesRepository;

    public BattleController(BattlesRepository battlesRepository) {
        this.battlesRepository = battlesRepository;
    }

    /**
     * Создаёт новый Battle.
     * POST /battles?sectionNumber=3
     */
    @PostMapping
    public ResponseEntity<Long> createBattle(@RequestParam Integer sectionNumber) {
        // создание через фабричный метод — инварианты проверяются внутри.
        // при невалидном sectionNumber вылетит IllegalArgumentException
        Battle battle = Battle.create(sectionNumber);

        Battle saved = battlesRepository.save(battle);
        return ResponseEntity.ok(saved.getIdBattle());
    }

    /**
     * Доменное поведение: перевести бой в следующую секцию.
     * POST /battles/{id}/next-section
     */
    @PostMapping("/{id}/next-section")
    public ResponseEntity<Integer> nextSection(@PathVariable Long id) {
        Battle battle = battlesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Battle not found: " + id));

        battle.moveToNextSection();          // мутация через доменный метод, не через сеттер
        battlesRepository.save(battle);

        return ResponseEntity.ok(battle.getSectionNumber());
    }

    /**
     * eventId как ПРЯМОЙ аргумент.
     * Аспект найдёт его по имени параметра "eventId" (механизм №1).
     * GET /battles/by-event?eventId=175
     */
    @GetMapping("/by-event")
    public ResponseEntity<Void> listByEvent(@RequestParam Integer eventId) {
        // Сюда дойдёт только если соревнование eventId принадлежит текущему организатору.
        log.info("listByEvent разрешён: eventId={}", eventId);
        return ResponseEntity.ok().build();
    }

    /**
     * eventId ВНУТРИ DTO.
     * Аспект найдёт его рефлексией по полю "eventId" в объекте-аргументе (механизм №2).
     * PUT /battles/from-dto   body: {"test":0,"eventId":175}
     */
    @PutMapping("/from-dto")
    public ResponseEntity<Void> updateFromDto(@RequestBody AuthorizeEventDto dto) {
        // Тот же результат, что и listByEvent, но eventId извлечён из поля DTO —
        // одна аннотация на классе покрывает обе сигнатуры.
        log.info("updateFromDto разрешён: dto.getEventId()={}", dto.getEventId());
        return ResponseEntity.ok().build();
    }

    /**
     * Превращает IllegalArgumentException из фабрики в HTTP 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalid(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}