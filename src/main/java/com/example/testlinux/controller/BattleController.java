package com.example.testlinux.controller;

import com.example.testlinux.domain.Battle;
import com.example.testlinux.repository.BattlesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для создания Battle через DDD-фабрику.
 *
 * Контроллер НЕ знает про new Battle() и не собирает объект вручную —
 * он лишь передаёт входные данные в фабричный метод Battle.create().
 * Вся логика создания и проверка инвариантов инкапсулированы в сущности.
 */
@RestController
@RequestMapping("/battles")
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
     * Превращает IllegalArgumentException из фабрики в HTTP 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalid(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}