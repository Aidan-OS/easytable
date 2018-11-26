package org.vandeseer.easytable.structure;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.Settings;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.cell.CellBaseData;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;

@Builder
@Setter(AccessLevel.PRIVATE)
public class Row {

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private Table table;

    @Getter
    private List<CellBaseData> cells;

    private Color borderColor;

    @Getter
    @Setter(AccessLevel.NONE)
    private Settings settings;

    private float height;

    private Row(final List<CellBaseData> cells) {
        super();
        this.cells = cells;
    }

    public Color getBorderColor() {
        final Optional<Color> optBorderColor = Optional.ofNullable(borderColor);
        return optBorderColor.orElse(getTable().getBorderColor());
    }

    public float getHeight() {
        final float maxCellHeight = getCells().stream()
                .map(CellBaseData::getHeight)
                .max(naturalOrder())
                .orElseThrow(RuntimeException::new);

        return Math.max(height, maxCellHeight);
    }


    public static class RowBuilder {

        private List<CellBaseData> cells = new LinkedList<>();
        private Optional<Color> borderColor = Optional.empty();
        private Settings settings = Settings.builder().build();

        private RowBuilder() {

        }

        public RowBuilder add(final CellBaseData cell) {
            cells.add(cell);
            return this;
        }

        public Row.RowBuilder font(final PDFont font) {
            settings.setFont(font);
            return this;
        }

        public Row.RowBuilder fontSize(final Integer fontSize) {
            settings.setFontSize(fontSize);
            return this;
        }

        public Row.RowBuilder textColor(final Color textColor) {
            settings.setTextColor(textColor);
            return this;
        }

        public Row.RowBuilder backgroundColor(final Color backgroundColor) {
            settings.setBackgroundColor(backgroundColor);
            return this;
        }

        public Row.RowBuilder horizontalAlignment(HorizontalAlignment alignment) {
            settings.setHorizontalAlignment(alignment);
            return this;
        }

        public Row.RowBuilder verticalAlignment(VerticalAlignment alignment) {
            settings.setVerticalAlignment(alignment);
            return this;
        }

        public Row build() {
            final Row row = new Row(cells);
            row.settings = settings;

            borderColor.ifPresent(row::setBorderColor);

            row.height = height;

            return row;
        }

    }

}
