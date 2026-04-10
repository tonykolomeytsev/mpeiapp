package com.kekmech.feature_bars_grades_api

public sealed interface ObserveGradesError {
    public data object Network : ObserveGradesError
    public data object SessionExpired : ObserveGradesError
    public data object Internal : ObserveGradesError
}