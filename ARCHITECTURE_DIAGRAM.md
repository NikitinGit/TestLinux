# Архитектурная диаграмма BlindSealWordsGUI

## Обзор архитектуры

Приложение построено на основе многослойной архитектуры с четким разделением ответственности.

## Структура компонентов

```
┌───────────────────────────────────────────────────────────────────────┐
│                         PRESENTATION LAYER                            │
│  ┌──────────────────────────────────────────────────────────────┐    │
│  │              BlindSealWordsGUI (Coordinator)                 │    │
│  │         extends JFrame implements GameEventListener          │    │
│  │                                                               │    │
│  │  Responsibilities:                                            │    │
│  │  - Инициализация всех компонентов                            │    │
│  │  - Координация взаимодействия                                │    │
│  │  - Обработка пользовательского ввода (KeyListener)           │    │
│  │  - Управление жизненным циклом игры                          │    │
│  └──────────────────────────────────────────────────────────────┘    │
│                              │                                        │
│                              │ uses                                   │
│                              ▼                                        │
│  ┌──────────────────────────────────────────────────────────────┐    │
│  │                    UIComponents                              │    │
│  │                                                               │    │
│  │  Responsibilities:                                            │    │
│  │  - Создание UI элементов (labels, buttons)                   │    │
│  │  - Обновление отображения                                    │    │
│  │  - Визуальные эффекты (мигание при ошибке)                   │    │
│  └──────────────────────────────────────────────────────────────┘    │
└───────────────────────────────────────────────────────────────────────┘
                               │
                               │ uses
                               ▼
┌───────────────────────────────────────────────────────────────────────┐
│                        APPLICATION LAYER                              │
│  ┌──────────────────────────────────────────────────────────────┐    │
│  │                   InputProcessor                             │    │
│  │                                                               │    │
│  │  Responsibilities:                                            │    │
│  │  - Обработка символов ввода                                  │    │
│  │  - Валидация правильности ввода                              │    │
│  │  - Генерация событий (Observer pattern)                      │    │
│  │  - Координация генерации новых слов                          │    │
│  │                                                               │    │
│  │  Dependencies:                                                │    │
│  │  - GameState (проверка правильности)                         │    │
│  │  - TypingStatistics (обновление метрик)                      │    │
│  │  - WordProvider (генерация слов)                             │    │
│  │  - GameEventListener (интерфейс для событий)                 │    │
│  └──────────────────────────────────────────────────────────────┘    │
│                              │                                        │
│                              │ implements                             │
│                              ▼                                        │
│  ┌──────────────────────────────────────────────────────────────┐    │
│  │              GameEventListener (Interface)                   │    │
│  │                                                               │    │
│  │  Methods:                                                     │    │
│  │  - onCorrectChar()                                            │    │
│  │  - onIncorrectChar()                                          │    │
│  │  - onWordCompleted()                                          │    │
│  │  - onSecondElapsed()                                          │    │
│  │  - onGameEnd()                                                │    │
│  │  - onNewWord(String word)                                     │    │
│  └──────────────────────────────────────────────────────────────┘    │
└───────────────────────────────────────────────────────────────────────┘
                               │
                               │ uses
                               ▼
┌───────────────────────────────────────────────────────────────────────┐
│                          DOMAIN LAYER                                 │
│                                                                       │
│  ┌────────────────┐  ┌──────────────────┐  ┌────────────────────┐   │
│  │   GameState    │  │ TypingStatistics │  │   WordProvider     │   │
│  ├────────────────┤  ├──────────────────┤  ├────────────────────┤   │
│  │- currentWord   │  │- totalSymbols    │  │- WORDS[]           │   │
│  │- typedWord     │  │- wrongSymbols    │  │- random            │   │
│  │- active        │  │- completedWords  │  │                    │   │
│  │                │  │- elapsedSeconds  │  │+ getRandomWord()   │   │
│  │+ isCharCorrect()│ │                  │  │+ getWordCount()    │   │
│  │+ appendTyped() │  │+ getAccuracy()   │  │                    │   │
│  │+ isCompleted() │  │+ getFormatted*() │  │                    │   │
│  │+ reset()       │  │+ increment*()    │  │                    │   │
│  └────────────────┘  └──────────────────┘  └────────────────────┘   │
│                                                                       │
└───────────────────────────────────────────────────────────────────────┘
                               │
                               │ uses
                               ▼
┌───────────────────────────────────────────────────────────────────────┐
│                      INFRASTRUCTURE LAYER                             │
│  ┌──────────────────────────────────────────────────────────────┐    │
│  │                      GameTimer                               │    │
│  │                                                               │    │
│  │  Responsibilities:                                            │    │
│  │  - Управление таймером длительности игры (60 сек)            │    │
│  │  - Таймер подсчета секунд                                    │    │
│  │  - Таймер эффекта мигания (300 мс)                           │    │
│  │                                                               │    │
│  │  Methods:                                                     │    │
│  │  - startGameTimer(Runnable onExpire)                         │    │
│  │  - startSecondsTimer(Runnable onTick)                        │    │
│  │  - startBlinkTimer(Runnable onBlink)                         │    │
│  │  - stopAll()                                                  │    │
│  └──────────────────────────────────────────────────────────────┘    │
└───────────────────────────────────────────────────────────────────────┘
```

## Поток данных при обработке ввода

```
┌──────────────┐
│  Пользователь│
│  нажимает    │
│  клавишу     │
└──────┬───────┘
       │
       ▼
┌──────────────────────────────┐
│  BlindSealWordsGUI           │
│  handleKeyPress(KeyEvent e)  │
└──────┬───────────────────────┘
       │
       │ inputProcessor.processCharacter(char)
       ▼
┌──────────────────────────────┐
│  InputProcessor              │
│  processCharacter(char)      │
└──────┬───────────────────────┘
       │
       ├─────► GameState.isCharCorrect(char) ──────► Boolean
       │                                                │
       │ Если TRUE:                                     │
       │   ├─► GameState.appendTypedChar(char)         │
       │   ├─► Statistics.incrementTotalSymbols()      │
       │   └─► eventListener.onCorrectChar() ──────────┼──┐
       │                                                │  │
       │ Если FALSE:                                    │  │
       │   ├─► Statistics.incrementWrongSymbols()      │  │
       │   └─► eventListener.onIncorrectChar() ────────┼──┤
       │                                                │  │
       └────────────────────────────────────────────────┘  │
                                                           │
       ┌───────────────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│  BlindSealWordsGUI           │
│  onCorrectChar() /           │
│  onIncorrectChar()           │
└──────┬───────────────────────┘
       │
       ├─► UIComponents.updateWordDisplay()
       ├─► UIComponents.updateStats()
       └─► UIComponents.highlightError() (если ошибка)
               │
               └─► GameTimer.startBlinkTimer()
                       │
                       └─► UIComponents.resetWordColor() (через 300ms)
```

## Поток данных при старте игры

```
┌──────────────┐
│  Пользователь│
│  нажимает    │
│  СТАРТ       │
└──────┬───────┘
       │
       ▼
┌────────────────────────────┐
│  BlindSealWordsGUI         │
│  resetGame()               │
└──────┬─────────────────────┘
       │
       ├─► GameTimer.stopAll()
       ├─► TypingStatistics.reset()
       ├─► GameState.setActive(true)
       ├─► UIComponents.showGameStartState()
       │
       └─► startGame()
               │
               ├─► InputProcessor.generateNewWord()
               │       │
               │       ├─► WordProvider.getRandomWord()
               │       ├─► GameState.setCurrentWord(word)
               │       └─► eventListener.onNewWord(word)
               │               │
               │               └─► UIComponents.showNewWord(word)
               │
               ├─► GameTimer.startGameTimer(this::endGame)
               │       │
               │       └─► Timer (60 секунд) → endGame()
               │
               └─► GameTimer.startSecondsTimer(this::onSecondElapsed)
                       │
                       └─► Timer (каждую секунду) → onSecondElapsed()
                               │
                               ├─► Statistics.incrementElapsedSeconds()
                               └─► UIComponents.updateTimer(seconds)
```

## Диаграмма зависимостей

```
                    ┌───────────────────────┐
                    │   GameEventListener   │ ◄────── Interface
                    │     (interface)       │         (DIP)
                    └───────────▲───────────┘
                                │
                                │ implements
                                │
        ┌───────────────────────┴───────────────────────┐
        │                                               │
┌───────┴──────────┐                          ┌─────────▼────────┐
│BlindSealWordsGUI │◄─────────────────────────┤ InputProcessor   │
└───────┬──────────┘        notifies          └─────────┬────────┘
        │                                               │
        │ uses                                          │ uses
        │                                               │
        ├────────────────┬──────────────┬───────────────┼───────────┐
        │                │              │               │           │
        ▼                ▼              ▼               ▼           ▼
┌─────────────┐  ┌─────────────┐  ┌──────────┐  ┌─────────┐  ┌──────────┐
│UIComponents │  │  GameTimer  │  │GameState │  │Statistics│ │WordProvider│
└─────────────┘  └─────────────┘  └──────────┘  └─────────┘  └──────────┘

Legend:
   ──►  : Uses/Depends on
   ◄──  : Implements
```

## Применение паттернов проектирования

### 1. **Observer Pattern**
```
GameEventListener (Subject)
        │
        ├── BlindSealWordsGUI (Observer 1)
        └── [Возможны другие Observer]
```

### 2. **Facade Pattern**
```
BlindSealWordsGUI (Facade)
        │
        └── Упрощает взаимодействие с подсистемами:
            - GameState
            - TypingStatistics
            - InputProcessor
            - GameTimer
            - UIComponents
            - WordProvider
```

### 3. **Dependency Injection**
```
InputProcessor Constructor:
    ├── GameState state         ──► Injected
    ├── TypingStatistics stats  ──► Injected
    ├── WordProvider provider   ──► Injected
    └── GameEventListener list  ──► Injected
```

### 4. **Strategy Pattern** (потенциальное применение)
```
WordProvider (Strategy Interface)
    ├── ArrayWordProvider    (текущая реализация)
    ├── FileWordProvider     (будущее расширение)
    └── APIWordProvider      (будущее расширение)
```

## Сравнение до и после

### ДО рефакторинга:
```
BlindSealWordsGUI (240 строк)
    ├── UI компоненты (JLabel, JButton)
    ├── Игровая логика
    ├── Обработка ввода
    ├── Управление таймерами
    ├── Подсчет статистики
    ├── Генерация слов
    └── Состояние игры
```
**Проблемы:**
- Нарушение SRP (множество ответственностей)
- Сложность тестирования
- Трудность расширения
- Плохая читаемость

### ПОСЛЕ рефакторинга:
```
BlindSealWordsGUI (215 строк) - Координатор
    │
    ├── UIComponents (160 строк) - UI логика
    ├── InputProcessor (75 строк) - Обработка ввода
    ├── GameTimer (85 строк) - Таймеры
    ├── TypingStatistics (90 строк) - Статистика
    ├── WordProvider (45 строк) - Генерация слов
    ├── GameState (60 строк) - Состояние
    └── GameEventListener (35 строк) - События
```
**Преимущества:**
- Соблюдение всех принципов SOLID
- Легкость тестирования каждого компонента
- Простота расширения
- Отличная читаемость
- Переиспользуемость компонентов

## Метрики кода

| Метрика                    | До        | После      | Изменение |
|----------------------------|-----------|------------|-----------|
| Количество классов         | 1         | 7          | +600%     |
| Строк на класс (среднее)   | 240       | ~95        | -60%      |
| Ответственностей на класс  | 7         | 1          | -86%      |
| Зависимостей на класс      | N/A       | 2-4        | Controlled|
| Тестируемость              | Низкая    | Высокая    | +++       |
| Читаемость                 | Средняя   | Отличная   | +++       |

## Заключение

Рефакторинг превратил монолитный класс в хорошо спроектированную систему с четкими границами ответственности, следующую всем принципам SOLID и использующую проверенные паттерны проектирования.
