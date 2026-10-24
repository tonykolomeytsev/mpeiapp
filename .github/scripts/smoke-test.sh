#!/bin/bash
# Installs the release APK on the running emulator, launches it and fails on a crash.
# Collects a screenshot and the logcat into ./smoke-output.
set -u
PKG="kekmech.ru.mpeiapp"
OUT="smoke-output"
mkdir -p "$OUT"

APK=$(ls dist/*.apk | head -n 1)
echo "Installing $APK"
adb install -r "$APK" || { echo "::error::APK does not install"; exit 1; }

adb shell pm grant "$PKG" android.permission.POST_NOTIFICATIONS 2>/dev/null || true
adb logcat -c

launch_and_check() {
  local name="$1"
  adb shell am force-stop "$PKG"
  adb logcat -c
  adb shell monkey -p "$PKG" -c android.intent.category.LAUNCHER 1 >/dev/null 2>&1
  sleep 25
  adb exec-out screencap -p > "$OUT/$name.png" || true
  adb logcat -d > "$OUT/$name-logcat.txt" || true
  local pid
  pid=$(adb shell pidof "$PKG" | tr -d '\r')
  if grep -q "FATAL EXCEPTION" "$OUT/$name-logcat.txt"; then
    echo "::error::The app crashed ($name)"
    grep -A40 "FATAL EXCEPTION" "$OUT/$name-logcat.txt" | head -120
    return 1
  fi
  if [ -z "$pid" ]; then
    echo "::error::The app process is not running after launch ($name)"
    tail -80 "$OUT/$name-logcat.txt"
    return 1
  fi
  echo "[$name] OK, pid=$pid"
}

STATUS=0
echo "== light theme =="
adb shell cmd uimode night no
launch_and_check light || STATUS=1

echo "== dark theme =="
adb shell cmd uimode night yes
launch_and_check dark || STATUS=1

# The home screen must have been reached: the main activity is on top
adb shell dumpsys activity activities | grep -m1 "topResumedActivity" || true
exit $STATUS
