package io.github.egormkn;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import javafx.beans.InvalidationListener;
import javafx.beans.NamedArg;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.WritableValue;
import javafx.css.*;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jfree.fx.FXGraphics2D;
import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JavaFX component that renders LaTeX formulas
 *
 * @author Egor Makarenko
 */
// @DefaultProperty("formula") // Broken due to the bug in JavaFX ProxyBuilder
public class LatexView extends Canvas {

    /**
     * Font size to use by default
     */
    private static final float DEFAULT_SIZE = (float) Font.getDefault().getSize();

    /**
     * LaTeX formula to use by default
     */
    private static final String DEFAULT_FORMULA = "\\LaTeX";

    /**
     * Alignment to use by default
     */
    private static final Pos DEFAULT_ALIGNMENT = Pos.CENTER;

    private TeXIcon texIcon;

    public LatexView() {
        initialize();
        update();
        draw();
    }

    public LatexView(@NamedArg("formula") String formula) {
        this();
        formulaProperty().set(formula);
    }

    private void initialize() {
        widthProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable event) {
                LatexView.this.draw();
            }
        });
        heightProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable event) {
                LatexView.this.draw();
            }
        });

        getStyleClass().setAll("latex");
        setAccessibleRole(AccessibleRole.TEXT);
        ((StyleableProperty<Boolean>)(WritableValue<Boolean>)focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
    }

    private void draw() {
        double width = texIcon.getIconWidth();
        double height = texIcon.getIconHeight();

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        gc.setStroke(Color.RED);
        gc.strokeLine(0, 0, 0, height);
        gc.strokeLine(0, 0, width, 0);
        gc.strokeLine(0, height, width, height);
        gc.strokeLine(width, 0, width, height);

        gc.setFill(Color.BLACK);
        FXGraphics2D graphics = new FXGraphics2D(gc);
        texIcon.paintIcon(null, graphics, 0, 0);
    }

    protected void update() {
        TeXFormula teXFormula;
        try {
            teXFormula = new TeXFormula(getFormula());
        } catch (ParseException p) {
            teXFormula = new TeXFormula("\\text{Invalid Formula}");
        }

        texIcon = teXFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, getSize());
        setWidth(texIcon.getIconWidth());
        setHeight(texIcon.getIconHeight());
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return texIcon.getIconWidth();
    }

    @Override
    public double prefHeight(double width) {
        return texIcon.getIconHeight();
    }

    /**
     * Formula text
     */
    public final StringProperty formulaProperty() {
        if (formula == null) {
            formula = new StringPropertyBase(DEFAULT_FORMULA) {
                @Override
                public void invalidated() {
                    LatexView.this.update();
                }

                @Override
                public Object getBean() {
                    return LatexView.this;
                }

                @Override
                public String getName() {
                    return "formula";
                }
            };
        }
        return formula;
    }

    private StringProperty formula;

    public final void setFormula(String value) {
        formulaProperty().set(value == null ? DEFAULT_FORMULA : value);
    }

    public final String getFormula() {
        return formulaProperty().get();
    }

    /**
     * Formula text size
     */
    public final FloatProperty sizeProperty() {
        if (size == null) {
            size = new StyleableFloatProperty(DEFAULT_SIZE) {
                @Override
                public void invalidated() {
                    LatexView.this.update();
                }

                @Override
                public Object getBean() {
                    return LatexView.this;
                }

                @Override
                public String getName() {
                    return "size";
                }

                @Override
                public CssMetaData<LatexView, Number> getCssMetaData() {
                    return LatexView.StyleableProperties.SIZE;
                }
            };
        }
        return size;
    }

    private FloatProperty size;

    public final void setSize(float value) {
        sizeProperty().set(value);
    }

    public final float getSize() {
        return sizeProperty().get();
    }

    /**
     * Formula alignment within canvas
     */
    public final ObjectProperty<Pos> alignmentProperty() {
        if (alignment == null) {
            alignment = new StyleableObjectProperty<Pos>(DEFAULT_ALIGNMENT) {
                @Override
                public void invalidated() {
                    LatexView.this.update();
                }

                @Override
                public Object getBean() {
                    return LatexView.this;
                }

                @Override
                public String getName() {
                    return "alignment";
                }

                @Override
                public CssMetaData<LatexView, Pos> getCssMetaData() {
                    return LatexView.StyleableProperties.ALIGNMENT;
                }
            };
        }
        return alignment;
    }

    private ObjectProperty<Pos> alignment;

    public final void setAlignment(Pos value) { alignmentProperty().set(value); }

    public final Pos getAlignment() { return alignment == null ? DEFAULT_ALIGNMENT : alignment.get(); }

    private static class StyleableProperties {
        private static final CssMetaData<LatexView, Pos> ALIGNMENT =
                new CssMetaData<LatexView,Pos>("-fx-alignment",
                        new EnumConverter<Pos>(Pos.class), DEFAULT_ALIGNMENT){

                    @Override
                    public boolean isSettable(LatexView node) {
                        return node.alignment == null || !node.alignment.isBound();
                    }

                    @Override
                    public StyleableProperty<Pos> getStyleableProperty(LatexView node) {
                        return (StyleableProperty<Pos>)(WritableValue<Pos>)node.alignmentProperty();
                    }
                };

        private static final CssMetaData<LatexView,Number> SIZE =
                new CssMetaData<LatexView,Number>("-fx-font-size", SizeConverter.getInstance(), DEFAULT_SIZE) {

                    @Override
                    public boolean isSettable(LatexView node) {
                        return node.size == null || !node.size.isBound();
                    }

                    @Override
                    public StyleableProperty<Number> getStyleableProperty(LatexView node) {
                        return (StyleableProperty<Number>)(WritableValue<Number>)node.sizeProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<CssMetaData<? extends Styleable, ?>>();
            styleables.add(ALIGNMENT);
            styleables.add(SIZE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return LatexView.StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }
}