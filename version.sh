#!/bin/bash
set -euo pipefail

# HELPER
# prendo il primo argument che passo
CMD="${1:-}"
if [[ -z "$CMD" || ( "$CMD" != "snapshot" && "$CMD" != "bump-patch" && "$CMD" != "bump-minor" && "$CMD" != "bump-major" ) ]]; then
  echo "Argomenti ammessi: $0 {snapshot|bump-patch|bump-minor|bump-major}" >&2
  exit 2
fi

# Uso maven wrapper se presnte
MVN="./mvnw"
if [[ ! -x "$MVN" ]]; then
  MVN="mvn"
fi

#funzione che legge la versione del progetto
get_version() {
  "$MVN" -q -DforceStdout help:evaluate -Dexpression=project.version
}

#aumenta la versione
set_version() {
  local new_version="$1"
  echo "Setting version -> $new_version"
  "$MVN" -q -DprocessAllModules=true -DgenerateBackupPoms=false versions:set -DnewVersion="$new_version"
}

#STEP
# 1. recupero la attuale
current="$(get_version)"
echo "Current version: $current"

# 2. controlla che la versione abbia un formato supportato
if [[ ! "$current" =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)(-SNAPSHOT)?$ ]]; then
  echo "Errore: formato non supportato: '$current' (atteso x.y.z or x.y.z-SNAPSHOT)" >&2
  exit 1
fi

# 3. estrae i valori della versione
major="${BASH_REMATCH[1]}"
minor="${BASH_REMATCH[2]}"
patch="${BASH_REMATCH[3]}"
has_snapshot="${BASH_REMATCH[4]:-}"

base="${major}.${minor}.${patch}"

# 4. scrive la nuova versione (in caso di snapshot aggiunge -SNAPSHOT)
if [[ "$CMD" == "snapshot" ]]; then
  if [[ -n "$has_snapshot" ]]; then
    echo "Already a SNAPSHOT: $current (no changes)"
    exit 0
  fi
  set_version "${base}-SNAPSHOT"
  exit 0
fi

# 5. Capisce quale versione e da incrementare
case "$CMD" in
  bump-patch)
    new_major="$major"
    new_minor="$minor"
    new_patch=$((patch + 1))
    ;;
  bump-minor)
    new_major="$major"
    new_minor=$((minor + 1))
    new_patch=0
    ;;
  bump-major)
    new_major=$((major + 1))
    new_minor=0
    new_patch=0
    ;;
esac

# 6. bump
new_version="${new_major}.${new_minor}.${new_patch}"
set_version "$new_version"