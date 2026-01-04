#!/bin/bash

# Compose Compiler Metrics Generator

set -e

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}   Compose Compiler Metrics Generator${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

BUILD_TYPE="Debug"

echo -e "${GREEN}✓${NC} Debug build kullanılıyor"
echo ""

echo -e "${BLUE}→${NC} Metrics oluşturuluyor..."
echo ""

# Try compileDebugKotlin first, fallback to assembleDebug if needed
if ./gradlew compileDebugKotlin -PcomposeCompilerReports=true --quiet 2>/dev/null; then
    echo -e "${GREEN}✓${NC} Compile başarılı"
elif ./gradlew assembleDebug -PcomposeCompilerReports=true --quiet 2>/dev/null; then
    echo -e "${GREEN}✓${NC} Assemble başarılı"
else
    echo -e "${RED}✗ Build başarısız oldu${NC}"
    echo -e "${YELLOW}İpucu:${NC} Gradle'ın çalıştığından ve Java'nın kurulu olduğundan emin olun"
    exit 1
fi

echo ""
echo -e "${GREEN}✓ Metrics başarıyla oluşturuldu!${NC}"
echo ""

REPORT_DIR="app/build/compose_compiler"

# Kotlin Compose Plugin raporları doğrudan compose_compiler klasörüne yazar
# debug/release alt klasörüne değil
if [ ! -d "$REPORT_DIR" ]; then
    echo -e "${RED}✗ Rapor klasörü bulunamadı: $REPORT_DIR${NC}"
    echo -e "${YELLOW}İpucu:${NC} Build'i -PcomposeCompilerReports=true ile çalıştırdığınızdan emin olun"
    exit 1
fi

# Rapor dosyalarını kontrol et
REPORT_FILES=$(find "$REPORT_DIR" -name "*-composables.txt" 2>/dev/null | head -1)
if [ -z "$REPORT_FILES" ]; then
    echo -e "${YELLOW}⚠️${NC}  Rapor dosyaları henüz oluşmamış"
    echo -e "${BLUE}→${NC}  Build'i tekrar çalıştırıyorum..."
    echo ""
    
    if ./gradlew compileDebugKotlin -PcomposeCompilerReports=true --quiet 2>/dev/null; then
        echo -e "${GREEN}✓${NC} Build tamamlandı"
    elif ./gradlew assembleDebug -PcomposeCompilerReports=true --quiet 2>/dev/null; then
        echo -e "${GREEN}✓${NC} Build tamamlandı"
    else
        echo -e "${RED}✗ Build başarısız${NC}"
        exit 1
    fi
    
    # Tekrar kontrol et
    REPORT_FILES=$(find "$REPORT_DIR" -name "*-composables.txt" 2>/dev/null | head -1)
    if [ -z "$REPORT_FILES" ]; then
        echo -e "${RED}✗ Rapor dosyaları hala oluşmadı${NC}"
        echo -e "${YELLOW}İpucu:${NC} Compose Compiler yapılandırmasını kontrol edin"
        exit 1
    fi
fi

echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}   İstatistikler${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# Rapor dosyalarını bul
COMPOSABLES_FILE=$(find "$REPORT_DIR" -name "*-composables.txt" 2>/dev/null | head -1)
CLASSES_FILE=$(find "$REPORT_DIR" -name "*-classes.txt" 2>/dev/null | head -1)

if [ -z "$COMPOSABLES_FILE" ]; then
    echo -e "${RED}✗ Composables raporu bulunamadı${NC}"
    exit 1
fi

TOTAL_COMPOSABLES=$(grep -c "^restartable" "$COMPOSABLES_FILE" 2>/dev/null || echo "0")
echo -e "Toplam Composable:        ${GREEN}${TOTAL_COMPOSABLES}${NC}"

SKIPPABLE=$(grep -c "restartable skippable" "$COMPOSABLES_FILE" 2>/dev/null || echo "0")
echo -e "Skippable Composable:     ${GREEN}${SKIPPABLE}${NC}"

NOT_SKIPPABLE=$((TOTAL_COMPOSABLES - SKIPPABLE))
echo -e "Not Skippable:            ${YELLOW}${NOT_SKIPPABLE}${NC}"

# Skippable rate
if [ "$TOTAL_COMPOSABLES" -gt 0 ]; then
    SKIPPABLE_RATE=$(echo "scale=1; $SKIPPABLE * 100 / $TOTAL_COMPOSABLES" | bc 2>/dev/null || echo "0")
    if (( $(echo "$SKIPPABLE_RATE >= 80" | bc -l 2>/dev/null || echo "0") )); then
        COLOR=$GREEN
    elif (( $(echo "$SKIPPABLE_RATE >= 60" | bc -l 2>/dev/null || echo "0") )); then
        COLOR=$YELLOW
    else
        COLOR=$RED
    fi
    echo -e "Skippable Rate:           ${COLOR}${SKIPPABLE_RATE}%${NC}"
else
    echo -e "Skippable Rate:           ${RED}N/A${NC}"
fi

echo ""

UNSTABLE_PARAMS=$(grep -c "unstable " "$COMPOSABLES_FILE" 2>/dev/null || echo "0")
echo -e "Unstable Parametreler:    ${YELLOW}${UNSTABLE_PARAMS}${NC}"

if [ -n "$CLASSES_FILE" ]; then
    STABLE_CLASSES=$(grep -c "^stable class" "$CLASSES_FILE" 2>/dev/null || echo "0")
    echo -e "Stable Sınıflar:          ${GREEN}${STABLE_CLASSES}${NC}"

    UNSTABLE_CLASSES=$(grep -c "^unstable class" "$CLASSES_FILE" 2>/dev/null || echo "0")
    echo -e "Unstable Sınıflar:        ${RED}${UNSTABLE_CLASSES}${NC}"
else
    echo -e "Stable Sınıflar:          ${YELLOW}N/A${NC}"
    echo -e "Unstable Sınıflar:        ${YELLOW}N/A${NC}"
fi

echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# En çok unstable parametresi olan Composable'lar
if [ -n "$COMPOSABLES_FILE" ]; then
    echo -e "${YELLOW}⚠️  En Çok Unstable Parametresi Olan Composable'lar (Top 5):${NC}"
    echo ""
    UNSTABLE_COMPOSABLES=$(grep "fun " "$COMPOSABLES_FILE" | grep "unstable" | head -5)
    if [ -n "$UNSTABLE_COMPOSABLES" ]; then
        echo "$UNSTABLE_COMPOSABLES" | while read -r line; do
            echo -e "  ${RED}•${NC} ${line}"
        done
    else
        echo -e "  ${GREEN}✓${NC} Unstable parametreli Composable bulunamadı!"
    fi
fi

echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# Dosya konumları
echo -e "${GREEN}📂 Rapor Dosyaları:${NC}"
echo ""
if [ -d "$REPORT_DIR" ] && [ "$(ls -A "$REPORT_DIR" 2>/dev/null)" ]; then
    ls -lh "${REPORT_DIR}" | tail -n +2 | while read -r line; do
        filename=$(echo "$line" | awk '{print $NF}')
        size=$(echo "$line" | awk '{print $5}')
        echo -e "  ${BLUE}•${NC} ${filename} (${size})"
    done
else
    echo -e "  ${YELLOW}⚠️${NC}  Henüz rapor dosyası oluşmamış"
fi

echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# Öneriler
echo -e "${YELLOW}💡 Öneriler:${NC}"
echo ""

if [ "$NOT_SKIPPABLE" -gt "$((TOTAL_COMPOSABLES / 5))" ]; then
    echo -e "  ${YELLOW}⚠️${NC}  Skippable olmayan Composable'lar fazla (${NOT_SKIPPABLE})"
    echo -e "      → Parametreleri @Immutable sınıflar yap"
    echo ""
fi

if [ "$UNSTABLE_PARAMS" -gt "$((TOTAL_COMPOSABLES / 2))" ]; then
    echo -e "  ${YELLOW}⚠️${NC}  Unstable parametreler fazla (${UNSTABLE_PARAMS})"
    echo -e "      → Data class'lara @Immutable annotation ekle"
    echo ""
fi

if [ "$UNSTABLE_CLASSES" -gt 5 ]; then
    echo -e "  ${YELLOW}⚠️${NC}  Unstable sınıflar var (${UNSTABLE_CLASSES})"
    echo -e "      → Mutable property'leri immutable yap"
    echo ""
fi

echo -e "${GREEN}✓${NC} Detaylı raporlar için: ${BLUE}${REPORT_DIR}/${NC}"
echo -e "${GREEN}✓${NC} Dokümantasyon için: ${BLUE}COMPOSE_METRICS.md${NC}"
echo ""

# Raporları aç (macOS)
if [[ "$OSTYPE" == "darwin"* ]]; then
    read -p "$(echo -e ${YELLOW}Raporları Finder\'da açmak ister misiniz? [Y/n]: ${NC})" OPEN_FINDER
    OPEN_FINDER=${OPEN_FINDER:-Y}

    if [[ "$OPEN_FINDER" =~ ^[Yy]$ ]]; then
        open "$REPORT_DIR"
        echo -e "${GREEN}✓${NC} Finder'da açıldı"
    fi
fi

echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""
