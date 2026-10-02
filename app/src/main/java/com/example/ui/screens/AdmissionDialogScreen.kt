package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.model.Cadet
import com.example.data.model.FieldConfig
import com.example.ui.theme.ArmyGreenDark
import com.example.ui.theme.ArmyGreenPrimary
import com.example.ui.theme.PetFailRed
import com.example.ui.theme.TacticalGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdmissionDialog(
    cadetToEdit: Cadet? = null,
    generatedRollNo: String,
    fieldConfig: FieldConfig,
    onSaveCadet: (Cadet) -> Unit,
    onDeleteCadet: ((Cadet) -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val isEditMode = cadetToEdit != null

    var rollNo by remember { mutableStateOf(cadetToEdit?.rollNo ?: generatedRollNo) }
    var fullName by remember { mutableStateOf(cadetToEdit?.fullName ?: "") }
    var photoUri by remember { mutableStateOf(cadetToEdit?.photoUri) }
    var ageText by remember { mutableStateOf(cadetToEdit?.age?.toString() ?: "19") }
    var phone by remember { mutableStateOf(cadetToEdit?.phone ?: "") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri.toString()
        }
    }

    val batchOptions = fieldConfig.batchList.ifEmpty { listOf("Alpha Commando", "Bravo Agnipath", "General") }
    var selectedBatch by remember { mutableStateOf(cadetToEdit?.batchName ?: batchOptions.first()) }
    var batchExpanded by remember { mutableStateOf(false) }

    val examOptions = listOf(
        "Indian Army Soldier GD",
        "Indian Army Technical",
        "Indian Army Clerk / SKT",
        "Indian Army Tradesman",
        "SSC GD Constable",
        "Army Military Police (CMP)",
        "NDA / CDS Cadet",
        "State Police Sub-Inspector",
        "General Defense Recruit"
    )
    var selectedExam by remember { mutableStateOf(cadetToEdit?.targetExam ?: examOptions.first()) }
    var examExpanded by remember { mutableStateOf(false) }

    var heightText by remember { mutableStateOf(cadetToEdit?.heightCm?.toString() ?: "172.0") }
    var weightText by remember { mutableStateOf(cadetToEdit?.weightKg?.toString() ?: "68.0") }
    var chestNormalText by remember { mutableStateOf(cadetToEdit?.chestNormalCm?.toString() ?: "80.0") }
    var chestExpandedText by remember { mutableStateOf(cadetToEdit?.chestExpandedCm?.toString() ?: "86.0") }

    val bloodOptions = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
    var selectedBlood by remember { mutableStateOf(cadetToEdit?.bloodGroup ?: "B+") }
    var bloodExpanded by remember { mutableStateOf(false) }

    var medicalStatus by remember { mutableStateOf(cadetToEdit?.medicalStatus ?: "SHAPE-1 (Fit)") }
    var notes by remember { mutableStateOf(cadetToEdit?.notes ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm && cadetToEdit != null && onDeleteCadet != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete ${fieldConfig.personLabel} Record?") },
            text = { Text("Are you sure you want to permanently delete ${cadetToEdit.fullName} (${cadetToEdit.rollNo}) and all associated physical records?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteCadet(cadetToEdit)
                        showDeleteConfirm = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PetFailRed)
                ) {
                    Text("Delete Record")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ArmyGreenDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isEditMode) Icons.Default.Person else Icons.Default.AssignmentInd,
                        contentDescription = null,
                        tint = TacticalGold,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isEditMode) "MANAGE ${fieldConfig.personLabel.uppercase()} RECORD" else "NEW ${fieldConfig.personLabel.uppercase()} ENROLLMENT",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )
                    Text(
                        text = "${fieldConfig.idLabel.uppercase()}: $rollNo",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TacticalGold
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (errorMessage != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                // ID / Roll No Field (Editable for record management)
                OutlinedTextField(
                    value = rollNo,
                    onValueChange = { rollNo = it },
                    label = { Text("${fieldConfig.idLabel} (Unique ID) *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admission_roll_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Photo Upload Avatar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(ArmyGreenDark)
                            .border(2.dp, TacticalGold, CircleShape)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (photoUri != null) {
                            AsyncImage(
                                model = photoUri,
                                contentDescription = "Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Add Photo",
                                tint = TacticalGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = if (photoUri != null) "Photo Uploaded" else "Upload Profile Photo",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        OutlinedButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(if (photoUri != null) "Change Photo" else "Pick Photo")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Full Name
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("${fieldConfig.personLabel} Full Name *") },
                    placeholder = { Text("e.g. Vikramaditya Rathore") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admission_fullname_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Age & Phone
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = ageText,
                        onValueChange = { ageText = it },
                        label = { Text("Age (Yrs)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admission_age_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Contact Phone") },
                        placeholder = { Text("+91 98765...") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .weight(2f)
                            .testTag("admission_phone_input"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Batch Selection
                ExposedDropdownMenuBox(
                    expanded = batchExpanded,
                    onExpandedChange = { batchExpanded = !batchExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedBatch,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(fieldConfig.groupLabel) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = batchExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("admission_batch_selector")
                    )
                    ExposedDropdownMenu(
                        expanded = batchExpanded,
                        onDismissRequest = { batchExpanded = false }
                    ) {
                        batchOptions.forEach { batch ->
                            DropdownMenuItem(
                                text = { Text(batch) },
                                onClick = {
                                    selectedBatch = batch
                                    batchExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category Selection
                ExposedDropdownMenuBox(
                    expanded = examExpanded,
                    onExpandedChange = { examExpanded = !examExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedExam,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(fieldConfig.categoryLabel) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = examExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("admission_exam_selector")
                    )
                    ExposedDropdownMenu(
                        expanded = examExpanded,
                        onDismissRequest = { examExpanded = false }
                    ) {
                        examOptions.forEach { exam ->
                            DropdownMenuItem(
                                text = { Text(exam) },
                                onClick = {
                                    selectedExam = exam
                                    examExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "PHYSICAL MEASUREMENT STANDARDS (PST)",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Height & Weight
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = heightText,
                        onValueChange = { heightText = it },
                        label = { Text("Height (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admission_height_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { weightText = it },
                        label = { Text("Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admission_weight_input"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Chest Normal & Chest Expanded
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = chestNormalText,
                        onValueChange = { chestNormalText = it },
                        label = { Text("Chest Norm (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = chestExpandedText,
                        onValueChange = { chestExpandedText = it },
                        label = { Text("Chest Exp (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Blood group & Medical status
                Row(modifier = Modifier.fillMaxWidth()) {
                    ExposedDropdownMenuBox(
                        expanded = bloodExpanded,
                        onExpandedChange = { bloodExpanded = !bloodExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedBlood,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Blood") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodExpanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = bloodExpanded,
                            onDismissRequest = { bloodExpanded = false }
                        ) {
                            bloodOptions.forEach { blood ->
                                DropdownMenuItem(
                                    text = { Text(blood) },
                                    onClick = {
                                        selectedBlood = blood
                                        bloodExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = medicalStatus,
                        onValueChange = { medicalStatus = it },
                        label = { Text("Medical Status") },
                        modifier = Modifier.weight(2f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Remarks & Training Goals") },
                    placeholder = { Text("e.g. Aiming for Group 1 sprint...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Row {
                if (isEditMode && onDeleteCadet != null) {
                    OutlinedButton(
                        onClick = { showDeleteConfirm = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PetFailRed),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("delete_cadet_button")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }

                Button(
                    onClick = {
                        if (fullName.isBlank()) {
                            errorMessage = "Name is required"
                            return@Button
                        }
                        if (rollNo.isBlank()) {
                            errorMessage = "${fieldConfig.idLabel} is required"
                            return@Button
                        }
                        val height = heightText.toFloatOrNull() ?: 170.0f
                        val weight = weightText.toFloatOrNull() ?: 65.0f
                        val chestNorm = chestNormalText.toFloatOrNull() ?: 80.0f
                        val chestExp = chestExpandedText.toFloatOrNull() ?: (chestNorm + 5.0f)
                        val age = ageText.toIntOrNull() ?: 19

                        val savedCadet = cadetToEdit?.copy(
                            rollNo = rollNo.trim(),
                            fullName = fullName.trim(),
                            age = age,
                            phone = if (phone.isBlank()) "+91 99999 00000" else phone.trim(),
                            batchName = selectedBatch,
                            targetExam = selectedExam,
                            heightCm = height,
                            weightKg = weight,
                            chestNormalCm = chestNorm,
                            chestExpandedCm = chestExp,
                            bloodGroup = selectedBlood,
                            medicalStatus = medicalStatus,
                            photoUri = photoUri,
                            notes = notes.trim()
                        ) ?: Cadet(
                            rollNo = rollNo.trim(),
                            fullName = fullName.trim(),
                            age = age,
                            phone = if (phone.isBlank()) "+91 99999 00000" else phone.trim(),
                            batchName = selectedBatch,
                            targetExam = selectedExam,
                            heightCm = height,
                            weightKg = weight,
                            chestNormalCm = chestNorm,
                            chestExpandedCm = chestExp,
                            bloodGroup = selectedBlood,
                            medicalStatus = medicalStatus,
                            photoUri = photoUri,
                            notes = notes.trim()
                        )
                        onSaveCadet(savedCadet)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ArmyGreenPrimary),
                    modifier = Modifier.testTag("admission_submit_button")
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isEditMode) "Update Record" else "Save & Enroll")
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("admission_cancel_button")
            ) {
                Text("Cancel")
            }
        }
    )
}
