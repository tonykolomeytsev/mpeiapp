package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
internal fun ClassesItemCard(
    classes: Classes,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onGroupClick: ((String) -> Unit)? = null,
) {
    val isTeacher = classes.scheduleType == ScheduleType.PERSON

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MpeixTheme.palette.surfacePlus1,
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MpeixTheme.palette.outline.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            // Top row: lesson number + time + lesson type badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (classes.number > 0) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(MpeixTheme.palette.primary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = classes.number.toString(),
                                style = MpeixTheme.typography.paragraphNormal.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MpeixTheme.palette.primary,
                                    fontSize = 11.sp,
                                ),
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = "${classes.time.start.format(timeFormatter)} - ${classes.time.end.format(timeFormatter)}",
                        style = MpeixTheme.typography.paragraphNormal.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MpeixTheme.palette.content,
                            fontSize = 13.sp,
                        ),
                        maxLines = 1,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                ClassesTypeBadge(classesType = classes.type, rawType = classes.rawType)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Discipline name
            Text(
                text = classes.name,
                style = MpeixTheme.typography.header4.copy(
                    fontWeight = FontWeight.Bold,
                    color = MpeixTheme.palette.content,
                    fontSize = 15.sp,
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )

            if (isTeacher) {
                // Teacher mode: show room and clickable student groups
                if (classes.place.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "📍 ${classes.place}",
                            style = MpeixTheme.typography.paragraphNormal.copy(
                                color = MpeixTheme.palette.primary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                if (classes.groups.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = "Группы:",
                            style = MpeixTheme.typography.paragraphNormal.copy(
                                color = MpeixTheme.palette.contentVariant,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                            ),
                        )
                        val groupTokens = classes.groups.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        Row(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            groupTokens.forEach { group ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MpeixTheme.palette.primary.copy(alpha = 0.12f))
                                        .clickable {
                                            val cleanGroup = group.substringBefore('\\').trim()
                                            onGroupClick?.invoke(cleanGroup)
                                        }
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                ) {
                                    Text(
                                        text = "$group ↗",
                                        style = MpeixTheme.typography.labelNormal.copy(
                                            color = MpeixTheme.palette.primary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                        ),
                                        maxLines = 1,
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Student mode: show place, teacher person, and subgroup
                if (classes.place.isNotBlank() || classes.person.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (classes.place.isNotBlank()) {
                            Text(
                                text = "📍 ${classes.place}",
                                style = MpeixTheme.typography.paragraphNormal.copy(
                                    color = MpeixTheme.palette.primary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false),
                            )
                        }
                        if (classes.place.isNotBlank() && classes.person.isNotBlank()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "•",
                                style = MpeixTheme.typography.paragraphNormal.copy(
                                    color = MpeixTheme.palette.contentDisabled,
                                    fontSize = 10.sp,
                                ),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        if (classes.person.isNotBlank()) {
                            Text(
                                text = "👤 ${classes.person}",
                                style = MpeixTheme.typography.paragraphNormal.copy(
                                    color = MpeixTheme.palette.contentVariant,
                                    fontSize = 12.sp,
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1.5f, fill = false),
                            )
                        }
                    }
                }

                if (classes.groups.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = classes.groups,
                        style = MpeixTheme.typography.paragraphNormal.copy(
                            color = MpeixTheme.palette.contentDisabled,
                            fontSize = 11.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            // Attached note preview
            classes.attachedNotePreview?.takeIf { it.isNotBlank() }?.let { note ->
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MpeixTheme.palette.surfacePlus3)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = "📝 $note",
                        style = MpeixTheme.typography.paragraphNormal.copy(
                            color = MpeixTheme.palette.content,
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun ClassesTypeBadge(
    classesType: ClassesType,
    rawType: String?,
    modifier: Modifier = Modifier,
) {
    val (badgeColor, label) = when (classesType) {
        ClassesType.LECTURE -> MpeixTheme.palette.classesTypeLecture to "Лекция"
        ClassesType.PRACTICE -> MpeixTheme.palette.classesTypePractice to "Практика"
        ClassesType.LAB -> MpeixTheme.palette.classesTypeLab to "Лабораторная"
        ClassesType.COURSE -> MpeixTheme.palette.classesTypePractice to "Курсовая"
        ClassesType.EXAM -> MpeixTheme.palette.redMarkColor to "Экзамен"
        ClassesType.CONSULTATION -> MpeixTheme.palette.primary to "Консультация"
        ClassesType.UNDEFINED -> MpeixTheme.palette.contentDisabled to (rawType ?: "Занятие")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(badgeColor.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = label,
            style = MpeixTheme.typography.paragraphNormal.copy(
                fontWeight = FontWeight.SemiBold,
                color = badgeColor,
                fontSize = 11.sp,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
