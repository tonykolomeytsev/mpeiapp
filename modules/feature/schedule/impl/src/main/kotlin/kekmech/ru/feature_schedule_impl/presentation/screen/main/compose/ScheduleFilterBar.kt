package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.res_icons.R.drawable as ResIcons
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun ScheduleFilterBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedClassesType: ClassesType?,
    onClassesTypeSelect: (ClassesType?) -> Unit,
    selectedDiscipline: String?,
    onDisciplineSelect: (String?) -> Unit,
    availableDisciplines: List<String>,
    matchingClassesCount: Int?,
    isExpanded: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFilterActive = searchQuery.isNotBlank() || selectedClassesType != null || selectedDiscipline != null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
    ) {
        // Collapsed state with active filter: compact single-line pill
        if (!isExpanded && isFilterActive) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MpeixTheme.palette.primary.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = buildString {
                            append("Фильтр: ")
                            if (selectedDiscipline != null) append("«$selectedDiscipline»")
                            else if (searchQuery.isNotBlank()) append("«$searchQuery»")
                            selectedClassesType?.let {
                                if (selectedDiscipline != null || searchQuery.isNotBlank()) append(" • ")
                                append(
                                    when (it) {
                                        ClassesType.LECTURE -> "Лекции"
                                        ClassesType.PRACTICE -> "Практики"
                                        ClassesType.LAB -> "Лабораторные"
                                        ClassesType.COURSE -> "Курсовые"
                                        else -> "Занятия"
                                    }
                                )
                            }
                            matchingClassesCount?.let { append(" ($it)") }
                        },
                        style = MpeixTheme.typography.paragraphNormal.copy(
                            color = MpeixTheme.palette.primary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Clear filter button
                Text(
                    text = "Сбросить ✕",
                    style = MpeixTheme.typography.paragraphNormal.copy(
                        color = MpeixTheme.palette.contentVariant,
                        fontSize = 12.sp,
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable {
                            onSearchQueryChange("")
                            onClassesTypeSelect(null)
                            onDisciplineSelect(null)
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                )
            }
        }

        // Expanded state: full search and filter controls
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
            ) {
                // Search Input Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MpeixTheme.palette.surfacePlus1)
                        .border(
                            width = 1.dp,
                            color = MpeixTheme.palette.outline.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                        )
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(ResIcons.ic_search_24),
                        contentDescription = null,
                        tint = MpeixTheme.palette.contentVariant,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Поиск по предмету...",
                                style = MpeixTheme.typography.paragraphNormal.copy(
                                    color = MpeixTheme.palette.contentDisabled,
                                    fontSize = 13.sp,
                                ),
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = {
                                onSearchQueryChange(it)
                                if (it.isNotBlank() && selectedDiscipline != null) {
                                    onDisciplineSelect(null)
                                }
                            },
                            singleLine = true,
                            textStyle = MpeixTheme.typography.paragraphNormal.copy(
                                color = MpeixTheme.palette.content,
                                fontSize = 13.sp,
                            ),
                            cursorBrush = SolidColor(MpeixTheme.palette.primary),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    if (searchQuery.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(MpeixTheme.palette.surfacePlus3)
                                .clickable { onSearchQueryChange("") },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(ResIcons.ic_close_24),
                                contentDescription = "Очистить",
                                tint = MpeixTheme.palette.content,
                                modifier = Modifier.size(12.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Готово",
                        style = MpeixTheme.typography.paragraphNormal.copy(
                            color = MpeixTheme.palette.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(onClick = onClose)
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Type filter chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CompactTypeChip(
                        label = "Все",
                        isSelected = selectedClassesType == null,
                        onClick = { onClassesTypeSelect(null) },
                    )
                    CompactTypeChip(
                        label = "Лекции",
                        isSelected = selectedClassesType == ClassesType.LECTURE,
                        color = MpeixTheme.palette.classesTypeLecture,
                        onClick = {
                            onClassesTypeSelect(
                                if (selectedClassesType == ClassesType.LECTURE) null else ClassesType.LECTURE
                            )
                        },
                    )
                    CompactTypeChip(
                        label = "Практики",
                        isSelected = selectedClassesType == ClassesType.PRACTICE,
                        color = MpeixTheme.palette.classesTypePractice,
                        onClick = {
                            onClassesTypeSelect(
                                if (selectedClassesType == ClassesType.PRACTICE) null else ClassesType.PRACTICE
                            )
                        },
                    )
                    CompactTypeChip(
                        label = "Лабораторные",
                        isSelected = selectedClassesType == ClassesType.LAB,
                        color = MpeixTheme.palette.classesTypeLab,
                        onClick = {
                            onClassesTypeSelect(
                                if (selectedClassesType == ClassesType.LAB) null else ClassesType.LAB
                            )
                        },
                    )
                    CompactTypeChip(
                        label = "Курсовые",
                        isSelected = selectedClassesType == ClassesType.COURSE,
                        color = MpeixTheme.palette.classesTypePractice,
                        onClick = {
                            onClassesTypeSelect(
                                if (selectedClassesType == ClassesType.COURSE) null else ClassesType.COURSE
                            )
                        },
                    )
                }

                // Quick disciplines list from current schedule
                if (availableDisciplines.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        availableDisciplines.forEach { discipline ->
                            val isSelected = selectedDiscipline.equals(discipline, ignoreCase = true)
                            CompactDisciplineChip(
                                discipline = discipline,
                                isSelected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        onDisciplineSelect(null)
                                    } else {
                                        onDisciplineSelect(discipline)
                                        onSearchQueryChange("")
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactTypeChip(
    label: String,
    isSelected: Boolean,
    color: androidx.compose.ui.graphics.Color = MpeixTheme.palette.primary,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) color.copy(alpha = 0.18f) else MpeixTheme.palette.surfacePlus1
    val textColor = if (isSelected) color else MpeixTheme.palette.contentVariant

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = if (isSelected) color.copy(alpha = 0.6f) else MpeixTheme.palette.outline.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MpeixTheme.typography.paragraphNormal.copy(
                color = textColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp,
            ),
        )
    }
}

@Composable
private fun CompactDisciplineChip(
    discipline: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) MpeixTheme.palette.primary else MpeixTheme.palette.surfacePlus2
    val textColor = if (isSelected) MpeixTheme.palette.contentAccent else MpeixTheme.palette.content

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = discipline,
            style = MpeixTheme.typography.paragraphNormal.copy(
                color = textColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 11.sp,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (isSelected) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "✕",
                style = MpeixTheme.typography.paragraphNormal.copy(
                    color = textColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}
