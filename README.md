# Kanji Kun

## Overview

An accessibility service for android that helps learners of Japanese by providing furigana (reading aid) for kanji inside other applications.

Core elements:

- Accessibility service with ability to read text on the screen
- Language processing to determine correct furigana locally
- Renders sentence at bottom of the screen with reading aid

## Case studies

- [Universal Copy](https://play.google.com/store/apps/details?id=com.camel.corp.universalcopy)


## Dependencies

- [Kuromoji](https://www.atilika.org/): Japanese morphological analyzer
- [FuriganaTextView](https://github.com/lofe90/FuriganaTextView): Custom TextView for Android for rendering Japanese text with furigana.
- [KanaConverter](http://mariten.github.io/kanatools-java/en/kana-converter/)

## Development

This project has been developed using Android Studio.

## Contributions

Contributions are welcome! Please fork and open a pull request

### Enabling/Disabling

Provided running Android 8.0 and higher, users can enable and disable the accessibility service from any screen by long-pressing both volume keys at the same time.
TODO: implement [using this](https://developer.android.com/guide/topics/ui/accessibility/service#shortcut)