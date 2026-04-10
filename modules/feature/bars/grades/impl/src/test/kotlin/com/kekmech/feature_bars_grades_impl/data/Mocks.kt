package com.kekmech.feature_bars_grades_impl.data

import arrow.core.Either
import com.kekmech.feature_bars_grades_api.ControlActivity
import com.kekmech.feature_bars_grades_api.ControlWeek
import com.kekmech.feature_bars_grades_api.Discipline
import com.kekmech.feature_bars_grades_api.Grade
import com.kekmech.feature_bars_grades_api.Grades
import com.kekmech.feature_bars_grades_api.Semester
import com.kekmech.feature_bars_grades_impl.data.dto.ControlActivityDto
import com.kekmech.feature_bars_grades_impl.data.dto.ControlWeekDto
import com.kekmech.feature_bars_grades_impl.data.dto.DisciplineDto
import com.kekmech.feature_bars_grades_impl.data.dto.GradesDto

internal object Mocks {

    val GRADES = Grades(
        semester = Semester("2026h1"),
        disciplines = listOf(
            Discipline(
                title = "Иностранный язык",
                teacher = "Чеботарева О.А.",
                assessmentType = "зачёт с оценкой",
                totalCredits = 2.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivity(title = "1. КМ-1. Лексико-грамматический тест 1 (Infinitive. Infinitive Constructions: Complex Object, Complex Subject)", weight = 25, weekNumber = 3, dateRange = "06.03.26", grade = Grade(4.0f), dateReceived = "06.03.26")),
                    Either.Right(ControlActivity(title = "2. КМ-2. Лексико-грамматический тест 2 (Subject Clause. Predicative Clause. Object Clauses)", weight = 25, weekNumber = 7, dateRange = "03.04.26", grade = Grade(4.0f), dateReceived = "03.04.26")),
                    Either.Right(ControlActivity(title = "3. КМ-3. Лексико-грамматический тест 3 (Adverbial Clause Conditional Clause (0, 1st, 2nd, 3rd Conditionals)", weight = 25, weekNumber = 12, dateRange = "08.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "4. КМ-4. Лексико-грамматический тест 4 (Attribute. Attributive Clauses)", weight = 25, weekNumber = 15, dateRange = "29.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeek(number = 1, weekInSemester = 4, grade = Grade(4.0f))),
                    Either.Right(ControlWeek(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeek(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            Discipline(
                title = "Организационное поведение",
                teacher = "Кирилина Т.Ю.",
                assessmentType = "зачёт (без оценки) (по совокупности)",
                totalCredits = 2.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivity(title = "1. Организационное поведение как отрасль научного знания. Личность в организации", weight = 25, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = Grade(5.0f), dateReceived = "27.03.26")),
                    Either.Right(ControlActivity(title = "2. Группы, команды и организационная культура", weight = 25, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = Grade(5.0f), dateReceived = "10.04.26")),
                    Either.Right(ControlActivity(title = "3. Лидерство и конфликты", weight = 25, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "4. Выполнение домашних заданий", weight = 25, weekNumber = 16, dateRange = "01.06.26-07.06.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeek(number = 1, weekInSemester = 4, grade = Grade(0.0f))),
                    Either.Right(ControlWeek(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeek(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            Discipline(
                title = "Проектный менеджмент",
                teacher = "Сотниченко Е.",
                assessmentType = "зачёт (без оценки) (по совокупности)",
                totalCredits = 2.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivity(title = "1. «Жизненный цикл проекта: основные понятия»", weight = 20, weekNumber = 3, dateRange = "04.03.26", grade = Grade(3.0f), dateReceived = "20.03.26")),
                    Either.Right(ControlActivity(title = "2. «Планирование проекта: построение сетевого графика и диаграммы Ганта»", weight = 20, weekNumber = 7, dateRange = "01.04.26", grade = Grade(3.0f), dateReceived = "08.04.26")),
                    Either.Right(ControlActivity(title = "3. «Управление реализацией проекта»", weight = 30, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "4. «Контроль реализации проекта»", weight = 30, weekNumber = 15, dateRange = "25.05.26-31.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeek(number = 1, weekInSemester = 4, grade = Grade(3.0f))),
                    Either.Right(ControlWeek(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeek(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            Discipline(
                title = "Промышленная робототехника",
                teacher = "Орлов И.В.",
                assessmentType = "зачёт с оценкой",
                totalCredits = 4.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivity(title = "1. КМ-1 Тест «Промышленные робототехнические системы»", weight = 30, weekNumber = 7, dateRange = "30.03.26-05.04.26", grade = Grade(5.0f), dateReceived = "06.04.26")),
                    Either.Right(ControlActivity(title = "2. КМ-2 Тест «Организация рабочей среды роботизированного производства»", weight = 30, weekNumber = 11, dateRange = "27.04.26-03.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "3. КМ-3 Тест «Системы управления промышленными роботами»", weight = 40, weekNumber = 14, dateRange = "18.05.26-24.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeek(number = 1, weekInSemester = 4, grade = null)),
                    Either.Right(ControlWeek(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeek(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            Discipline(
                title = "Статистическая динамика автоматических систем",
                teacher = "Меркурьев И.В.",
                assessmentType = "экзамен",
                totalCredits = 6.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivity(title = "1. КМ-1: «Определение математического ожидания, дисперсии и корреляционной функции на входе и выходе системы автоматического управления».", weight = 25, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = Grade(5.0f), dateReceived = "16.03.26")),
                    Either.Right(ControlActivity(title = "2. КМ-2. «Статистический анализ системы автоматического управления в частотной области».", weight = 25, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = Grade(5.0f), dateReceived = "01.04.26")),
                    Either.Right(ControlActivity(title = "3. КМ-3. «Методы оптимальной фильтрации».", weight = 25, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "4. КМ-4. \"Оценка точности автоматической системы управления при случайных воздействиях\"", weight = 25, weekNumber = 15, dateRange = "25.05.26-31.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeek(number = 1, weekInSemester = 4, grade = Grade(5.0f))),
                    Either.Right(ControlWeek(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeek(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            Discipline(
                title = "Теория принятия решений",
                teacher = "Еремеев А.П.",
                assessmentType = "зачёт (без оценки) (по совокупности)",
                totalCredits = 2.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivity(title = "1. Методы анализа проблемной ситуации и поиск решения в конфликтных ситуациях на основе теоретико-игровых моделей", weight = 20, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = Grade(5.0f), dateReceived = "14.03.26")),
                    Either.Right(ControlActivity(title = "2. Методы анализа проблемной ситуации и поиск решения в конфликтных ситуациях на основе теоретико-игровых моделей", weight = 20, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "3. Многокритериальные задачи принятия решений и методы рационального и иррационального поведения лиц, принимающих решения", weight = 30, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "4. Методы коллективного принятия решений и системы поддержки принятия решений", weight = 30, weekNumber = 15, dateRange = "25.05.26-31.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeek(number = 1, weekInSemester = 4, grade = Grade(5.0f))),
                    Either.Right(ControlWeek(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeek(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            Discipline(
                title = "Управление движением мобильных колесных роботов",
                teacher = "Адамов Б.И.",
                assessmentType = "экзамен",
                totalCredits = 4.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivity(title = "1. Уравнения Лагранжа с неопределёнными множителями", weight = 18, weekNumber = 4, dateRange = "11.03.26", grade = Grade(5.0f), dateReceived = "14.03.26")),
                    Either.Right(ControlActivity(title = "2. Уравнения Маджи", weight = 18, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "3. Уравнения Аппеля", weight = 22, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "4. Моделирование управляемого движения мобильного колесного робота", weight = 24, weekNumber = 15, dateRange = "25.05.26-31.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "5. Динамика, управление и навигация мобильных роботов", weight = 18, weekNumber = 15, dateRange = "25.05.26-31.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeek(number = 1, weekInSemester = 4, grade = Grade(5.0f))),
                    Either.Right(ControlWeek(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeek(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            Discipline(
                title = "Учебная практика: научно-исследовательская работа",
                teacher = "Комерзан Е.В. (руководитель - Комерзан Е.В.)",
                assessmentType = "зачёт с оценкой",
                totalCredits = 2.0f,
                deadline = "06.06.2026",
                activities = listOf(
                    Either.Right(ControlActivity(title = "1. КМ-1 Постановка задачи разработки новой мехатронной или робототехнической системы различного назначения в рамках выпускной квалификационной работы, инициативного научного проекта, задания предприятия или олимпиадного задания. Принять участие в одном из конкурсов и проектов платформы «Россия — страна возможностей». Платформа объединяет более 26 конкурсов, проектов и олимпиад, см. https://rsv.ru/competitions/", weight = 25, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = Grade(5.0f), dateReceived = "03.04.26")),
                    Either.Right(ControlActivity(title = "2. КМ-2 Создание математической модели объекта исследования в виде трехмерного графического образа и/или системы дифференциальных уравнений, описывающих функционирование разрабатываемой системы. Подготовка презентации, отчетных материалов по заданию, тезисов докладов конференции, научной статьи по теме проекта.", weight = 25, weekNumber = 6, dateRange = "23.03.26-29.03.26", grade = Grade(5.0f), dateReceived = "09.04.26")),
                    Either.Right(ControlActivity(title = "3. КМ-3 Исследование свойств разрабатываемой мехатронной или робототехнической системы. Анализ полученных аналитических и численных результатов. Участие в отборочных и финальных мероприятиях платформы «Россия — страна возможностей».", weight = 25, weekNumber = 10, dateRange = "20.04.26-26.04.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "4. КМ-4 Подготовка отчета о практике.", weight = 25, weekNumber = 13, dateRange = "11.05.26-17.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeek(number = 1, weekInSemester = 4, grade = Grade(0.0f))),
                    Either.Right(ControlWeek(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeek(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            Discipline(
                title = "Электропневмогидравлические модули робототехнических систем",
                teacher = "Гнездилов С.Г.",
                assessmentType = "экзамен",
                totalCredits = 7.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivity(title = "1. КМ-1 Тест №1. «Морфологические «портреты» электропневмогидравлических модулей (ЭпгМ) для силовых систем мехатронных и робототехнических систем (МРтС)».", weight = 20, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = Grade(5.0f), dateReceived = "24.03.26")),
                    Either.Right(ControlActivity(title = "2. КМ-2 Тест №2. «Классификация, схемотехнические исполнения, статические и динамиче-ские характеристики гидромеханических следящих приводов с дроссельным управ-лением для силовых систем МРтС».", weight = 30, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "3. КМ-3 Контрольная работа №1. «Расчёт энергетических, регулировочных характеристик и зоны нечувствительности следящего привода с дроссельным управлением для заданных законов движения и структуры нагрузки регулируемых органов (РО) МРтС».", weight = 30, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "4. КМ-4 Контрольная работа №2. «Схемотехнические исполнения, энергетические, регулировочные и динамические характеристики шаговых приводов, приводов с насосным, моторным и частотным управлением и автономных гидроприводов. Пер-спективы применения в приводах новых решений».", weight = 20, weekNumber = 16, dateRange = "01.06.26-07.06.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeek(number = 1, weekInSemester = 4, grade = null)),
                    Either.Right(ControlWeek(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeek(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            Discipline(
                title = "Электропневмогидравлические модули робототехнических систем",
                teacher = "Гнездилов С.Г. (руководитель - Гнездилов С.Г.)",
                assessmentType = "защита КП/КР",
                totalCredits = 1.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivity(title = "1. КМ-1 Оценка выполнения первого раздела КР, включающего решение лабораторных задач: Изучение устройства стенда и его компонентов, Сборка типовых схем гидропривода, Испытание регулируемого дросселя.", weight = 10, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = Grade(5.0f), dateReceived = "24.03.26")),
                    Either.Right(ControlActivity(title = "2. КМ-2 Оценка выполнения первого раздела КР, включающего решение лабораторных задач: Испытание регулятора расхода, Испытание напорного клапана с пропорциональным управлением, Испытание гидравлического распределителя с пропорциональным управлением.", weight = 25, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "3. КМ-3 Оценка выполнения первого раздела КР, включающего решение лабораторных задач: Испытание гидросистемы с автоматическим регулированием давления источника питания, Испытание гидросистемы с автоматическим регулированием торможения гидродвигателя.", weight = 35, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivity(title = "4. КМ-4 Оценка выполнения первого раздела КР, включающего решение лабораторных задач: Испытание гидросистемы с заданным позиционированием выходного звена гидродвигателя. Испытание гидросистемы с автоматическим регулированием плавности движения выходного звена многопозиционного гидропривода.", weight = 30, weekNumber = 16, dateRange = "01.06.26-07.06.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeek(number = 1, weekInSemester = 4, grade = Grade(0.0f))),
                    Either.Right(ControlWeek(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeek(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            )
        )
    )

    val GRADES_DTO = GradesDto(
        semesterCode = "2026h1",
        disciplines = listOf(
            DisciplineDto(
                title = "Иностранный язык",
                teacher = "Чеботарева О.А.",
                assessmentType = "зачёт с оценкой",
                totalCredits = 2.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivityDto(title = "1. КМ-1. Лексико-грамматический тест 1 (Infinitive. Infinitive Constructions: Complex Object, Complex Subject)", weight = 25, weekNumber = 3, dateRange = "06.03.26", grade = 4.0f, dateReceived = "06.03.26")),
                    Either.Right(ControlActivityDto(title = "2. КМ-2. Лексико-грамматический тест 2 (Subject Clause. Predicative Clause. Object Clauses)", weight = 25, weekNumber = 7, dateRange = "03.04.26", grade = 4.0f, dateReceived = "03.04.26")),
                    Either.Right(ControlActivityDto(title = "3. КМ-3. Лексико-грамматический тест 3 (Adverbial Clause Conditional Clause (0, 1st, 2nd, 3rd Conditionals)", weight = 25, weekNumber = 12, dateRange = "08.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "4. КМ-4. Лексико-грамматический тест 4 (Attribute. Attributive Clauses)", weight = 25, weekNumber = 15, dateRange = "29.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeekDto(number = 1, weekInSemester = 4, grade = 4.0f)),
                    Either.Right(ControlWeekDto(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeekDto(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            DisciplineDto(
                title = "Организационное поведение",
                teacher = "Кирилина Т.Ю.",
                assessmentType = "зачёт (без оценки) (по совокупности)",
                totalCredits = 2.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivityDto(title = "1. Организационное поведение как отрасль научного знания. Личность в организации", weight = 25, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = 5.0f, dateReceived = "27.03.26")),
                    Either.Right(ControlActivityDto(title = "2. Группы, команды и организационная культура", weight = 25, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = 5.0f, dateReceived = "10.04.26")),
                    Either.Right(ControlActivityDto(title = "3. Лидерство и конфликты", weight = 25, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "4. Выполнение домашних заданий", weight = 25, weekNumber = 16, dateRange = "01.06.26-07.06.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeekDto(number = 1, weekInSemester = 4, grade = 0.0f)),
                    Either.Right(ControlWeekDto(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeekDto(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            DisciplineDto(
                title = "Проектный менеджмент",
                teacher = "Сотниченко Е.",
                assessmentType = "зачёт (без оценки) (по совокупности)",
                totalCredits = 2.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivityDto(title = "1. «Жизненный цикл проекта: основные понятия»", weight = 20, weekNumber = 3, dateRange = "04.03.26", grade = 3.0f, dateReceived = "20.03.26")),
                    Either.Right(ControlActivityDto(title = "2. «Планирование проекта: построение сетевого графика и диаграммы Ганта»", weight = 20, weekNumber = 7, dateRange = "01.04.26", grade = 3.0f, dateReceived = "08.04.26")),
                    Either.Right(ControlActivityDto(title = "3. «Управление реализацией проекта»", weight = 30, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "4. «Контроль реализации проекта»", weight = 30, weekNumber = 15, dateRange = "25.05.26-31.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeekDto(number = 1, weekInSemester = 4, grade = 3.0f)),
                    Either.Right(ControlWeekDto(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeekDto(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            DisciplineDto(
                title = "Промышленная робототехника",
                teacher = "Орлов И.В.",
                assessmentType = "зачёт с оценкой",
                totalCredits = 4.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivityDto(title = "1. КМ-1 Тест «Промышленные робототехнические системы»", weight = 30, weekNumber = 7, dateRange = "30.03.26-05.04.26", grade = 5.0f, dateReceived = "06.04.26")),
                    Either.Right(ControlActivityDto(title = "2. КМ-2 Тест «Организация рабочей среды роботизированного производства»", weight = 30, weekNumber = 11, dateRange = "27.04.26-03.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "3. КМ-3 Тест «Системы управления промышленными роботами»", weight = 40, weekNumber = 14, dateRange = "18.05.26-24.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeekDto(number = 1, weekInSemester = 4, grade = null)),
                    Either.Right(ControlWeekDto(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeekDto(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            DisciplineDto(
                title = "Статистическая динамика автоматических систем",
                teacher = "Меркурьев И.В.",
                assessmentType = "экзамен",
                totalCredits = 6.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivityDto(title = "1. КМ-1: «Определение математического ожидания, дисперсии и корреляционной функции на входе и выходе системы автоматического управления».", weight = 25, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = 5.0f, dateReceived = "16.03.26")),
                    Either.Right(ControlActivityDto(title = "2. КМ-2. «Статистический анализ системы автоматического управления в частотной области».", weight = 25, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = 5.0f, dateReceived = "01.04.26")),
                    Either.Right(ControlActivityDto(title = "3. КМ-3. «Методы оптимальной фильтрации».", weight = 25, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "4. КМ-4. \"Оценка точности автоматической системы управления при случайных воздействиях\"", weight = 25, weekNumber = 15, dateRange = "25.05.26-31.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeekDto(number = 1, weekInSemester = 4, grade = 5.0f)),
                    Either.Right(ControlWeekDto(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeekDto(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            DisciplineDto(
                title = "Теория принятия решений",
                teacher = "Еремеев А.П.",
                assessmentType = "зачёт (без оценки) (по совокупности)",
                totalCredits = 2.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivityDto(title = "1. Методы анализа проблемной ситуации и поиск решения в конфликтных ситуациях на основе теоретико-игровых моделей", weight = 20, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = 5.0f, dateReceived = "14.03.26")),
                    Either.Right(ControlActivityDto(title = "2. Методы анализа проблемной ситуации и поиск решения в конфликтных ситуациях на основе теоретико-игровых моделей", weight = 20, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "3. Многокритериальные задачи принятия решений и методы рационального и иррационального поведения лиц, принимающих решения", weight = 30, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "4. Методы коллективного принятия решений и системы поддержки принятия решений", weight = 30, weekNumber = 15, dateRange = "25.05.26-31.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeekDto(number = 1, weekInSemester = 4, grade = 5.0f)),
                    Either.Right(ControlWeekDto(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeekDto(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            DisciplineDto(
                title = "Управление движением мобильных колесных роботов",
                teacher = "Адамов Б.И.",
                assessmentType = "экзамен",
                totalCredits = 4.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivityDto(title = "1. Уравнения Лагранжа с неопределёнными множителями", weight = 18, weekNumber = 4, dateRange = "11.03.26", grade = 5.0f, dateReceived = "14.03.26")),
                    Either.Right(ControlActivityDto(title = "2. Уравнения Маджи", weight = 18, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "3. Уравнения Аппеля", weight = 22, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "4. Моделирование управляемого движения мобильного колесного робота", weight = 24, weekNumber = 15, dateRange = "25.05.26-31.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "5. Динамика, управление и навигация мобильных роботов", weight = 18, weekNumber = 15, dateRange = "25.05.26-31.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeekDto(number = 1, weekInSemester = 4, grade = 5.0f)),
                    Either.Right(ControlWeekDto(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeekDto(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            DisciplineDto(
                title = "Учебная практика: научно-исследовательская работа",
                teacher = "Комерзан Е.В. (руководитель - Комерзан Е.В.)",
                assessmentType = "зачёт с оценкой",
                totalCredits = 2.0f,
                deadline = "06.06.2026",
                activities = listOf(
                    Either.Right(ControlActivityDto(title = "1. КМ-1 Постановка задачи разработки новой мехатронной или робототехнической системы различного назначения в рамках выпускной квалификационной работы, инициативного научного проекта, задания предприятия или олимпиадного задания. Принять участие в одном из конкурсов и проектов платформы «Россия — страна возможностей». Платформа объединяет более 26 конкурсов, проектов и олимпиад, см. https://rsv.ru/competitions/", weight = 25, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = 5.0f, dateReceived = "03.04.26")),
                    Either.Right(ControlActivityDto(title = "2. КМ-2 Создание математической модели объекта исследования в виде трехмерного графического образа и/или системы дифференциальных уравнений, описывающих функционирование разрабатываемой системы. Подготовка презентации, отчетных материалов по заданию, тезисов докладов конференции, научной статьи по теме проекта.", weight = 25, weekNumber = 6, dateRange = "23.03.26-29.03.26", grade = 5.0f, dateReceived = "09.04.26")),
                    Either.Right(ControlActivityDto(title = "3. КМ-3 Исследование свойств разрабатываемой мехатронной или робототехнической системы. Анализ полученных аналитических и численных результатов. Участие в отборочных и финальных мероприятиях платформы «Россия — страна возможностей».", weight = 25, weekNumber = 10, dateRange = "20.04.26-26.04.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "4. КМ-4 Подготовка отчета о практике.", weight = 25, weekNumber = 13, dateRange = "11.05.26-17.05.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeekDto(number = 1, weekInSemester = 4, grade = 0.0f)),
                    Either.Right(ControlWeekDto(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeekDto(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            DisciplineDto(
                title = "Электропневмогидравлические модули робототехнических систем",
                teacher = "Гнездилов С.Г.",
                assessmentType = "экзамен",
                totalCredits = 7.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivityDto(title = "1. КМ-1 Тест №1. «Морфологические «портреты» электропневмогидравлических модулей (ЭпгМ) для силовых систем мехатронных и робототехнических систем (МРтС)».", weight = 20, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = 5.0f, dateReceived = "24.03.26")),
                    Either.Right(ControlActivityDto(title = "2. КМ-2 Тест №2. «Классификация, схемотехнические исполнения, статические и динамиче-ские характеристики гидромеханических следящих приводов с дроссельным управ-лением для силовых систем МРтС».", weight = 30, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "3. КМ-3 Контрольная работа №1. «Расчёт энергетических, регулировочных характеристик и зоны нечувствительности следящего привода с дроссельным управлением для заданных законов движения и структуры нагрузки регулируемых органов (РО) МРтС».", weight = 30, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "4. КМ-4 Контрольная работа №2. «Схемотехнические исполнения, энергетические, регулировочные и динамические характеристики шаговых приводов, приводов с насосным, моторным и частотным управлением и автономных гидроприводов. Пер-спективы применения в приводах новых решений».", weight = 20, weekNumber = 16, dateRange = "01.06.26-07.06.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeekDto(number = 1, weekInSemester = 4, grade = null)),
                    Either.Right(ControlWeekDto(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeekDto(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            ),
            DisciplineDto(
                title = "Электропневмогидравлические модули робототехнических систем",
                teacher = "Гнездилов С.Г. (руководитель - Гнездилов С.Г.)",
                assessmentType = "защита КП/КР",
                totalCredits = 1.0f,
                deadline = "07.06.2026",
                activities = listOf(
                    Either.Right(ControlActivityDto(title = "1. КМ-1 Оценка выполнения первого раздела КР, включающего решение лабораторных задач: Изучение устройства стенда и его компонентов, Сборка типовых схем гидропривода, Испытание регулируемого дросселя.", weight = 10, weekNumber = 4, dateRange = "09.03.26-15.03.26", grade = 5.0f, dateReceived = "24.03.26")),
                    Either.Right(ControlActivityDto(title = "2. КМ-2 Оценка выполнения первого раздела КР, включающего решение лабораторных задач: Испытание регулятора расхода, Испытание напорного клапана с пропорциональным управлением, Испытание гидравлического распределителя с пропорциональным управлением.", weight = 25, weekNumber = 8, dateRange = "06.04.26-12.04.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "3. КМ-3 Оценка выполнения первого раздела КР, включающего решение лабораторных задач: Испытание гидросистемы с автоматическим регулированием давления источника питания, Испытание гидросистемы с автоматическим регулированием торможения гидродвигателя.", weight = 35, weekNumber = 12, dateRange = "04.05.26-10.05.26", grade = null, dateReceived = null)),
                    Either.Right(ControlActivityDto(title = "4. КМ-4 Оценка выполнения первого раздела КР, включающего решение лабораторных задач: Испытание гидросистемы с заданным позиционированием выходного звена гидродвигателя. Испытание гидросистемы с автоматическим регулированием плавности движения выходного звена многопозиционного гидропривода.", weight = 30, weekNumber = 16, dateRange = "01.06.26-07.06.26", grade = null, dateReceived = null))
                ),
                controlWeeks = listOf(
                    Either.Right(ControlWeekDto(number = 1, weekInSemester = 4, grade = 0.0f)),
                    Either.Right(ControlWeekDto(number = 2, weekInSemester = 8, grade = null)),
                    Either.Right(ControlWeekDto(number = 3, weekInSemester = 12, grade = null))
                ),
                intermediateAttestation = Either.Right(null),
                finalGrade = null
            )
        )
    )
}
