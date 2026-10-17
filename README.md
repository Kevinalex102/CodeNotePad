# CodeNotePad

## A lightweight text editor which supports code.

CodeNotePad is a lightweight desktop text editor built with Java Swing. It allows users to write and organize text, insert code snippets and images, and save their work in a project file.

The application is designed for general text editing with additional features for working with code. **It is not a full code editor or IDE**: it does not provide code execution, compilation, debugging, or testing tools.

## Features 

* **Text editing**: Write and format text using basic text-editing tools.

* **Code snippets**: Insert code blocks into your document and export them as separate files.

* **Image support**: Insert images directly into your document.

* **Project files**: Save and reopen documents while preserving their contents and inserted components.

* **Light and dark themes**: Switch between visual themes.

* **Localization**: Interface available in English and Brazilian Portuguese.

* **Cross-platform support**: Available for Windows and Linux.

![Application Screenshot](/docs/screenshots/Screenshot1.png)

## Installation

### Windows
1. Download CodeNotePad-1.0.exe.

2. Run the installer.

3. Follow the installation instructions.

4. Launch CodeNotePad.

### Linux
1. Download CodeNotePad-1.0-x86_64.AppImage.

2. Open a terminal in the directory containing the downloaded file.

3. Make the file executable:
    >chmod +x CodeNotePad-1.0-x86_64.AppImage

4. Run the application:
    >./CodeNotePad-1.0-x86_64.AppImage

### Java (JAR)
You can also run the application using Java 17 or later.

Download CodeNotePad-1.0.jar and the accompanying lib directory, keeping the original directory structure.

Run the application from the directory containing the JAR and lib folder:

    java -cp "CodeNotePad-1.0.jar:lib/*" forms.NotepadForm

On Windows, use a semicolon instead of a colon in the classpath:

    java -cp "CodeNotePad-1.0.jar;lib/*" forms.NotepadForm

## Known limitations
* Limited export formats: Code snippets can only be exported in the file types supported by the application (txt, java, cs, js, html, css).

* Basic text formatting: The main text area uses Java Swing's JTextArea, which does not support rich text formatting. The entire main text area shares a single text color and font style, so individual words or lines cannot be formatted independently.

* No code execution: Code snippets cannot be compiled, executed, or debugged directly within the application.

* No advanced code intelligence: Features such as syntax highlighting, code completion, and integrated development tools are not provided.

### Technologies
* Java 17
* Java Swing
* FlatLaf
* Jackson
* Thumbnailator

### License

CodeNotePad is licensed under the MIT License. See the [LICENSE](/LICENSE) file for details.

CodeNotePad logo is provided by flaticon.com.
<a href="https://www.flaticon.com/free-icons/notepad" title="notepad icons">Notepad icons created by Magnific - Flaticon</a>