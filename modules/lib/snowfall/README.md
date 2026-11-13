# Snowfall

Compose snow effect: `Snowfall(state = rememberSnowfallState(), color = ...)`.

Keep `SnowfallState` above the screens which show the snow, so the flakes keep falling when one
screen with the snow is replaced by another (e.g. when bottom tabs switch).

The flake look (gaussian sizes, size-dependent speed, small angle, fading towards the bottom) comes
from the [JetRadar Mobile Snowfall](https://github.com/JetradarMobile/android-snowfall) view, which
this module used to be a fork of.
