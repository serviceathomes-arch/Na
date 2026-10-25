package com.example.cineflow.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cineflow.data.model.ComplexityLevel
import com.example.cineflow.data.model.GenreCategory
import com.example.cineflow.data.model.PromptExample
import com.example.cineflow.data.model.PromptTemplate
import com.example.cineflow.data.model.StylePreset
import com.example.cineflow.data.repository.PromptLibraryRepository
import com.example.cineflow.ui.AppScreen
import com.example.cineflow.ui.CineFlowViewModel
import com.example.cineflow.ui.LibraryTab
import com.example.ui.theme.CineAmber
import com.example.ui.theme.CineAmberBright
import com.example.ui.theme.CineBorder
import com.example.ui.theme.CineCardSurface
import com.example.ui.theme.CineCyan
import com.example.ui.theme.CineDarkSurface
import com.example.ui.theme.CineObsidian
import com.example.ui.theme.CineSurfaceVariant
import com.example.ui.theme.CineTextMuted
import com.example.ui.theme.CineTextPrimary
import com.example.ui.theme.CineTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PromptLibraryScreen(
    viewModel: CineFlowViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.libraryState.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val allExamples = PromptLibraryRepository.promptExamples
    val allTemplates = PromptLibraryRepository.promptTemplates

    // Filtered examples based on tab and active sub-filters
    val filteredExamples = remember(state.selectedTab, state.searchQuery, state.selectedGenreFilter, state.selectedStyleFilter, state.selectedComplexityFilter) {
        allExamples.filter { example ->
            val matchesSearch = state.searchQuery.isBlank() ||
                example.title.contains(state.searchQuery, ignoreCase = true) ||
                example.promptText.contains(state.searchQuery, ignoreCase = true) ||
                example.tags.any { it.contains(state.searchQuery, ignoreCase = true) }

            val matchesTabFilter = when (state.selectedTab) {
                LibraryTab.GENRES -> state.selectedGenreFilter == GenreCategory.ALL || example.genre == state.selectedGenreFilter
                LibraryTab.STYLES -> state.selectedStyleFilter == null || example.style == state.selectedStyleFilter
                LibraryTab.COMPLEXITY -> state.selectedComplexityFilter == null || example.complexity == state.selectedComplexityFilter
                LibraryTab.TEMPLATES -> true
            }

            matchesSearch && matchesTabFilter
        }
    }

    Box(modifier = modifier.fillMaxSize().background(CineObsidian)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header with Randomizer action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Prompt Library",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = CineTextPrimary
                    )
                    Text(
                        text = "Curated genres, styles, and fillable templates",
                        style = MaterialTheme.typography.bodySmall,
                        color = CineTextSecondary
                    )
                }

                // Random Generator shortcut button
                Button(
                    onClick = { viewModel.navigateTo(AppScreen.RANDOMIZER) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CineAmber,
                        contentColor = CineObsidian
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("library_random_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = "Roll",
                        tint = CineObsidian,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Roll Dice",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = CineObsidian
                    )
                }
            }

            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("library_search_field"),
                placeholder = {
                    Text("Search prompts, keywords, atmospheres...", color = CineTextMuted, fontSize = 13.sp)
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = CineTextMuted)
                },
                trailingIcon = {
                    if (state.searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = CineTextMuted)
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CineAmber,
                    unfocusedBorderColor = CineBorder,
                    focusedTextColor = CineTextPrimary,
                    unfocusedTextColor = CineTextPrimary,
                    cursorColor = CineAmber,
                    focusedContainerColor = CineDarkSurface,
                    unfocusedContainerColor = CineDarkSurface
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // CATEGORY TABS (Genre, Style, Complexity, Templates)
            TabRow(
                selectedTabIndex = state.selectedTab.ordinal,
                containerColor = CineDarkSurface,
                contentColor = CineAmber,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[state.selectedTab.ordinal]),
                        color = CineAmber,
                        height = 3.dp
                    )
                }
            ) {
                LibraryTab.entries.forEach { tab ->
                    Tab(
                        selected = state.selectedTab == tab,
                        onClick = { viewModel.setLibraryTab(tab) },
                        text = {
                            Text(
                                text = when (tab) {
                                    LibraryTab.GENRES -> "By Genre"
                                    LibraryTab.STYLES -> "By Style"
                                    LibraryTab.COMPLEXITY -> "Complexity"
                                    LibraryTab.TEMPLATES -> "Templates"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (state.selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                                color = if (state.selectedTab == tab) CineAmber else CineTextSecondary
                            )
                        }
                    )
                }
            }

            // SUB-FILTER CHIPS ACCORDING TO ACTIVE TAB
            when (state.selectedTab) {
                LibraryTab.GENRES -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GenreCategory.entries.forEach { genre ->
                            val isSelected = state.selectedGenreFilter == genre
                            FilterChipPill(
                                label = genre.displayName,
                                isSelected = isSelected,
                                onClick = { viewModel.filterByGenre(genre) }
                            )
                        }
                    }
                }
                LibraryTab.STYLES -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChipPill(
                            label = "All Styles",
                            isSelected = state.selectedStyleFilter == null,
                            onClick = { viewModel.filterByStyle(null) }
                        )
                        StylePreset.entries.forEach { style ->
                            val isSelected = state.selectedStyleFilter == style
                            FilterChipPill(
                                label = style.displayName,
                                isSelected = isSelected,
                                onClick = { viewModel.filterByStyle(style) }
                            )
                        }
                    }
                }
                LibraryTab.COMPLEXITY -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChipPill(
                            label = "All Complexities",
                            isSelected = state.selectedComplexityFilter == null,
                            onClick = { viewModel.filterByComplexity(null) }
                        )
                        ComplexityLevel.entries.forEach { complexity ->
                            val isSelected = state.selectedComplexityFilter == complexity
                            FilterChipPill(
                                label = complexity.displayName,
                                isSelected = isSelected,
                                onClick = { viewModel.filterByComplexity(complexity) }
                            )
                        }
                    }
                }
                LibraryTab.TEMPLATES -> {
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        Text(
                            text = "Interactive fill-in-the-blank prompt blueprints:",
                            style = MaterialTheme.typography.bodySmall,
                            color = CineTextSecondary
                        )
                    }
                }
            }

            // CONTENT LIST (Prompts or Templates)
            if (state.selectedTab == LibraryTab.TEMPLATES) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(allTemplates, key = { it.id }) { template ->
                        TemplateCard(
                            template = template,
                            onCustomize = { viewModel.selectTemplate(template) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredExamples, key = { it.id }) { example ->
                        PromptExampleCard(
                            example = example,
                            onUsePrompt = { viewModel.usePromptExample(example) },
                            onCopy = {
                                clipboardManager.setText(AnnotatedString(example.promptText))
                                Toast.makeText(context, "Prompt copied to clipboard!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }

        // TEMPLATE CUSTOMIZER DIALOG
        state.activeTemplate?.let { template ->
            TemplateCustomizerDialog(
                template = template,
                selections = state.templateSelections,
                onSlotChanged = { key, value -> viewModel.updateTemplateSlotValue(key, value) },
                onDismiss = { viewModel.selectTemplate(template) }, // toggle off
                onApply = { viewModel.applyTemplateToStudio() }
            )
        }
    }
}

@Composable
fun FilterChipPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) CineAmber else CineDarkSurface)
            .border(1.dp, if (isSelected) CineAmber else CineBorder, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) CineObsidian else CineTextPrimary
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PromptExampleCard(
    example: PromptExample,
    onUsePrompt: () -> Unit,
    onCopy: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CineDarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Optional preview thumbnail
            example.previewDrawableRes?.let { drawableRes ->
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = example.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Title & Language badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = example.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CineTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CineSurfaceVariant)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        tint = CineCyan,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = example.languageName,
                        style = MaterialTheme.typography.labelSmall,
                        color = CineCyan,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Prompt text
            Text(
                text = example.promptText,
                style = MaterialTheme.typography.bodyMedium,
                color = CineTextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Metadata pills (Genre, Style, Camera, Complexity)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PillTag(example.genre.displayName, CineAmber)
                PillTag(example.style.displayName, CineCyan)
                PillTag(example.cameraMotion.displayName, CineTextSecondary)
                PillTag("${example.complexity.displayName} (Lvl ${example.complexity.levelNumber})", CineTextMuted)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCopy, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Prompt",
                        tint = CineTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Button(
                    onClick = onUsePrompt,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CineAmber,
                        contentColor = CineObsidian
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Use",
                        tint = CineObsidian,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Use in Studio",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = CineObsidian
                    )
                }
            }
        }
    }
}

@Composable
fun PillTag(text: String, color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.12f))
            .border(0.5.dp, color.copy(alpha = 0.35f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelSmall, color = color, fontSize = 10.sp)
    }
}

@Composable
fun TemplateCard(
    template: PromptTemplate,
    onCustomize: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CineDarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = template.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CineTextPrimary
                )
                PillTag(template.genre.displayName, CineAmber)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = template.description,
                style = MaterialTheme.typography.bodySmall,
                color = CineTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CineObsidian)
                    .border(1.dp, CineBorder, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = template.templatePattern,
                    style = MaterialTheme.typography.bodySmall,
                    color = CineAmberBright,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onCustomize,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CineSurfaceVariant, contentColor = CineAmber),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Tune, contentDescription = "Customize", tint = CineAmber, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Customize & Fill Blueprint", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TemplateCustomizerDialog(
    template: PromptTemplate,
    selections: Map<String, String>,
    onSlotChanged: (String, String) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CineDarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CineAmber)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = template.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CineTextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = CineTextMuted)
                    }
                }

                Text(
                    text = "Tap options below to customize each slot in the prompt blueprint:",
                    style = MaterialTheme.typography.bodySmall,
                    color = CineTextSecondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                template.slots.forEach { slot ->
                    val currentVal = selections[slot.key] ?: slot.defaultVal

                    Text(
                        text = slot.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = CineAmber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        slot.suggestedOptions.forEach { option ->
                            val isSelected = currentVal == option
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) CineAmber else CineObsidian)
                                    .border(1.dp, if (isSelected) CineAmber else CineBorder, RoundedCornerShape(6.dp))
                                    .clickable { onSlotChanged(slot.key, option) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) CineObsidian else CineTextPrimary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Assembled preview
                Text(
                    text = "ASSEMBLED PROMPT PREVIEW",
                    style = MaterialTheme.typography.labelSmall,
                    color = CineTextMuted,
                    fontSize = 10.sp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CineObsidian)
                        .border(1.dp, CineBorder, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = template.buildPrompt(selections),
                        style = MaterialTheme.typography.bodySmall,
                        color = CineTextPrimary,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onApply,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CineAmber, contentColor = CineObsidian),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Apply", tint = CineObsidian)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Apply & Open in Video Studio", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
