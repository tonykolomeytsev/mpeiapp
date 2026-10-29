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

# Opens the main screen (a selected group is faked) and taps the bottom tabs the way a user does.
# Prints what is on the screen and what the app logged, so that a tab that "does nothing" can be seen in the job log.
explore_tabs() {
  echo "== bottom tabs =="
  adb shell am force-stop "$PKG"
  # a slow emulator shows "... isn't responding" dialogs of other apps (launcher, Google services) over the app
  adb shell settings put global hide_error_dialogs 1 >/dev/null 2>&1 || true
  adb shell am broadcast -a android.intent.action.CLOSE_SYSTEM_DIALOGS >/dev/null 2>&1 || true
  adb root >/dev/null 2>&1 || true
  adb wait-for-device
  # a selected group: without it the app shows the onboarding instead of the main screen
  local uid
  uid=$(adb shell stat -c %u "/data/data/$PKG" | tr -d '\r')
  adb shell "mkdir -p /data/data/$PKG/shared_prefs"
  adb shell "printf '%s' '<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?><map><string name=\"selected_group\">А-01-21</string><string name=\"selected_group_type\">group</string></map>' > /data/data/$PKG/shared_prefs/mpeix.xml"
  adb shell "chown $uid:$uid /data/data/$PKG/shared_prefs/mpeix.xml; chmod 660 /data/data/$PKG/shared_prefs/mpeix.xml"
  adb logcat -c
  adb shell monkey -p "$PKG" -c android.intent.category.LAUNCHER 1 >/dev/null 2>&1
  sleep 20
  for tab in navigation_map navigation_profile navigation_dashboard; do
    tap_by_id "$tab"
    sleep 4
    echo "--- after tapping $tab: texts on the screen ---"
    screen_texts | tee "$OUT/texts-$tab.txt" | head -40
    adb exec-out screencap -p > "$OUT/tab-$tab.png" || true
  done
  echo "--- app log (errors) ---"
  adb logcat -d > "$OUT/tabs-logcat.txt" || true
  grep -E "TabSwitch|FATAL EXCEPTION" -A25 "$OUT/tabs-logcat.txt" | head -120
  grep -E " E " "$OUT/tabs-logcat.txt" | grep -v "AccessibilityNodeInfoDumper" | tail -40

  # every tab has to show its own screen
  local failed=0
  grep -qiE "Scheme|Схема" "$OUT/texts-navigation_map.txt" || { echo "::error::The Map tab does not show the map"; failed=1; }
  grep -qiE "Settings|Настройки" "$OUT/texts-navigation_profile.txt" || { echo "::error::The Profile tab does not open"; failed=1; }
  grep -qiE "Upcoming|Ближайшие" "$OUT/texts-navigation_dashboard.txt" || { echo "::error::The Home tab does not open"; failed=1; }
  return $failed
}

screen_texts() {
  adb shell uiautomator dump /sdcard/ui.xml >/dev/null 2>&1
  adb pull /sdcard/ui.xml "$OUT/ui.xml" >/dev/null 2>&1
  python3 - "$OUT/ui.xml" <<'PY'
import re, sys
xml = open(sys.argv[1], encoding="utf-8").read()
seen = []
for t in re.findall(r'text="([^"]+)"', xml):
    if t not in seen:
        seen.append(t)
print(" | ".join(seen) if seen else "(no texts)")
PY
}

tap_by_id() {
  local xy="" attempt
  for attempt in 1 2 3 4; do
    xy=$(find_tab "$1")
    [ -n "$xy" ] && break
    echo "tab $1 not found (attempt $attempt), closing dialogs and trying again"
    adb shell am broadcast -a android.intent.action.CLOSE_SYSTEM_DIALOGS >/dev/null 2>&1 || true
    adb shell input keyevent KEYCODE_BACK
    adb shell monkey -p "$PKG" -c android.intent.category.LAUNCHER 1 >/dev/null 2>&1
    sleep 8
  done
  if [ -z "$xy" ]; then echo "tab $1 not found on the screen"; return 1; fi
  echo "tap $1 at $xy"
  adb shell input tap $xy
}

find_tab() {
  adb shell uiautomator dump /sdcard/ui.xml >/dev/null 2>&1
  adb pull /sdcard/ui.xml "$OUT/ui.xml" >/dev/null 2>&1
  python3 - "$OUT/ui.xml" "$1" <<'PY'
import re, sys
xml = open(sys.argv[1], encoding="utf-8").read()
m = re.search(r'resource-id="[^"]*:id/' + sys.argv[2] + r'"[^>]*bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"', xml)
if m:
    x1, y1, x2, y2 = map(int, m.groups())
    print((x1 + x2) // 2, (y1 + y2) // 2)
PY
}

STATUS=0
echo "== light theme =="
adb shell cmd uimode night no
launch_and_check light || STATUS=1

echo "== dark theme =="
adb shell cmd uimode night yes
launch_and_check dark || STATUS=1

explore_tabs || STATUS=1

# The home screen must have been reached: the main activity is on top
adb shell dumpsys activity activities | grep -m1 "topResumedActivity" || true
exit $STATUS
