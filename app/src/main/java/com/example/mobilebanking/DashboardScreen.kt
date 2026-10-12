package com.example.mobilebanking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    uiState: DashboardUIState,
    onGoToTransfer: () -> Unit,
    onGoToHistory: () -> Unit,
    onSignOut: () -> Unit,
) {
    val primary = colorResource(R.color.primary)
    val primaryDark = colorResource(R.color.primary_dark)
    val textSecondary = colorResource(R.color.text_secondary)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(24.dp),
    ) {
        // Header
        Text(
            text = uiState.greeting,
            style = MaterialTheme.typography.bodyMedium,
            color = textSecondary,
        )
        Text(
            text = stringResource(R.string.welcome_back),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = primaryDark,
        )
        Text(
            text = uiState.userEmail,
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_primary),
        )
        Text(
            text = stringResource(R.string.account_balance),
            style = MaterialTheme.typography.bodyMedium,
            color = textSecondary,
            modifier = Modifier.padding(top = 8.dp),
        )

        // Balance Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.card_background),
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_balance),
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.size(48.dp),
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = stringResource(R.string.available_balance),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textSecondary,
                    )
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .size(28.dp),
                            color = primary,
                        )
                    } else {
                        Text(
                            text = uiState.balance,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = primaryDark,
                        )
                    }
                }
            }
        }

        // Quick Actions
        Text(
            text = stringResource(R.string.quick_actions),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = primaryDark,
            modifier = Modifier.padding(top = 24.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(52.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = onGoToTransfer,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = primary),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_transfer),
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.action_transfer))
            }
            Button(
                onClick = onGoToHistory,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.secondary),
                ),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_history),
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.action_history))
            }
        }

        // Recent Transactions
        Text(
            text = stringResource(R.string.recent_transactions),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = primaryDark,
            modifier = Modifier.padding(top = 24.dp),
        )
        Text(
            text = stringResource(R.string.no_recent_transactions),
            style = MaterialTheme.typography.bodyMedium,
            color = textSecondary,
            modifier = Modifier.padding(top = 8.dp),
        )

        OutlinedButton(
            onClick = onSignOut,
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text(stringResource(R.string.action_sign_out))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    MaterialTheme {
        DashboardScreen(
            uiState = DashboardUIState(greeting = "Good morning", userEmail = "student@example.com"),
            onGoToTransfer = {},
            onGoToHistory = {},
            onSignOut = {},
        )
    }
}
