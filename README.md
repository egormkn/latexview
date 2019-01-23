# LatexView

LatexView is a custom JavaFX component that renders LaTeX text and formulas using [JLatexMath](https://github.com/opencollab/jlatexmath) library.

## Installation

// TODO

## Usage

### With FXML

See [ExampleFXML.java](src/test/java/ExampleFXML.java)

```fxml
<LatexView formula="\\LaTeX"/>

<LatexView formula="\\LaTeX" size="20"/>
```

### Without FXML

See [ExampleJFX.java](src/test/java/ExampleJFX.java)

```java
LatexView latex = new LatexView("\\LaTeX");
latex.setSize(30);

VBox vbox = new VBox(latex);
Scene scene = new Scene(vbox, 640, 480);
```
