package com.abdownloadmanager.shared.util.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import ir.amirab.util.logger.appLogger

val LocalTitleBarDirection = staticCompositionLocalOf<LayoutDirection> {
    appLogger.e { "DIAGNOSTIC: TitleBarDirection not provided! This means a Composable is reading LocalTitleBarDirection outside of ProvideLanguageManager scope." }
    appLogger.e { "DIAGNOSTIC: Thread=${Thread.currentThread().name}, StackTrace=${Throwable().stackTraceToString().take(500)}" }
    error("TitleBarDirection not provided")
}

@Composable
fun WithTitleBarDirection(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides LocalTitleBarDirection.current
    ) {
        content()
    }
}
