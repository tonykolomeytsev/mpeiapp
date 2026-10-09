package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun DayScheduleContent(
    classesList: List<Classes>?,
    isLoading: Boolean,
    errorMessage: String?,
    onClassesClick: (Classes) -> Unit,
    onReloadClick: () -> Unit,
    isFilterActive: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        when {
            isLoading && classesList == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = MpeixTheme.palette.primary,
                        modifier = Modifier.size(40.dp),
                    )
                }
            }

            errorMessage != null && classesList.isNullOrEmpty() -> {
                ErrorStateContent(
                    message = errorMessage,
                    onReloadClick = onReloadClick,
                )
            }

            classesList.isNullOrEmpty() -> {
                if (isFilterActive) {
                    FilterEmptyContent()
                } else {
                    SelfStudyContent()
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(
                        items = classesList,
                        key = { "${it.number}_${it.name}_${it.time.start}" },
                    ) { cls ->
                        ClassesItemCard(
                            classes = cls,
                            onClick = { onClassesClick(cls) },
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(60.dp)) // padding for bottom bar
                    }
                }
            }
        }
    }
}

@Composable
private fun SelfStudyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "☕",
                fontSize = 48.sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "День самостоятельной работы",
                style = MpeixTheme.typography.header3.copy(
                    fontWeight = FontWeight.Bold,
                    color = MpeixTheme.palette.content,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "В этот день занятий в расписании нет. Отличное время для отдыха или подготовки к парам!",
                style = MpeixTheme.typography.paragraphNormal.copy(
                    color = MpeixTheme.palette.contentVariant,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}

@Composable
private fun ErrorStateContent(
    message: String,
    onReloadClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "⚠️",
                fontSize = 40.sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Не удалось загрузить расписание",
                style = MpeixTheme.typography.header3.copy(
                    fontWeight = FontWeight.Bold,
                    color = MpeixTheme.palette.content,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MpeixTheme.typography.paragraphNormal.copy(
                    color = MpeixTheme.palette.contentVariant,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onReloadClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MpeixTheme.palette.primary,
                ),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = "Повторить",
                    style = MpeixTheme.typography.labelBig.copy(
                        color = MpeixTheme.palette.contentAccent,
                    ),
                )
            }
        }
    }
}

@Composable
private fun FilterEmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "🔍",
                fontSize = 44.sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Занятия не найдены",
                style = MpeixTheme.typography.header3.copy(
                    fontWeight = FontWeight.Bold,
                    color = MpeixTheme.palette.content,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "В этот день нет пар, подходящих под выбранные фильтры. Выберите другой день или сбросьте параметры поиска.",
                style = MpeixTheme.typography.paragraphNormal.copy(
                    color = MpeixTheme.palette.contentVariant,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}

