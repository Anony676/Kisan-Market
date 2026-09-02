package com.example.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.viewmodel.SortOption

@Composable
fun FilterDialog(
    selectedSort: SortOption,
    organicOnly: Boolean,
    verifiedOnly: Boolean,
    onSortChange: (SortOption) -> Unit,
    onToggleOrganic: () -> Unit,
    onToggleVerified: () -> Unit,
    onDismiss: () -> Unit
) {
    val strings = LocalAppStrings.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = AgroGreenPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.filterDialogTitle,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_filter_dialog_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = strings.close)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Sort Section
                Text(
                    text = "${strings.filterSortBy}:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(6.dp))

                SortOption.values().forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSortChange(option) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedSort == option,
                            onClick = { onSortChange(option) },
                            colors = RadioButtonDefaults.colors(selectedColor = AgroGreenPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = option.title,
                            fontSize = 13.sp,
                            color = Color(0xFF1E293B)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE2E8DC))

                // Filters Section
                Text(
                    text = "${strings.filters}:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleOrganic() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = organicOnly,
                        onCheckedChange = { onToggleOrganic() },
                        colors = CheckboxDefaults.colors(checkedColor = AgroGreenPrimary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${strings.filterOrganicOnly} (🌿)",
                        fontSize = 13.sp,
                        color = Color(0xFF1E293B)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleVerified() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = verifiedOnly,
                        onCheckedChange = { onToggleVerified() },
                        colors = CheckboxDefaults.colors(checkedColor = AgroGreenPrimary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${strings.filterVerifiedOnly} (✓)",
                        fontSize = 13.sp,
                        color = Color(0xFF1E293B)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgroGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("apply_filter_button")
                ) {
                    Text(text = strings.btnApplyFilters, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
