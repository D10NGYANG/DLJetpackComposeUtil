#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "$0")/.." && pwd)"
baseline_dir="${1:-$repo_root/output/playwright}"
expected_width=390
expected_height=844

baseline_files=(
  "home-mobile.png"
  "m013-home-390.png"
  "m014-home-390.png"
  "m017-home-390.png"
  "m005-index-default.png"
  "bd-signal-standard-390.png"
  "time-picker-hms-390.png"
  "divider-top-390.png"
)

if ! command -v identify >/dev/null 2>&1; then
  echo "ImageMagick identify is required to verify PNG dimensions" >&2
  exit 1
fi

for filename in "${baseline_files[@]}"; do
  path="$baseline_dir/$filename"
  if [[ ! -s "$path" ]]; then
    echo "Missing or empty baseline: $path" >&2
    exit 1
  fi

  dimensions="$(identify -format '%wx%h' "$path")"
  if [[ "$dimensions" != "${expected_width}x${expected_height}" ]]; then
    echo "Unexpected dimensions for $filename: $dimensions" >&2
    exit 1
  fi
done

echo "Verified ${#baseline_files[@]} Playwright baselines at ${expected_width}x${expected_height}"
