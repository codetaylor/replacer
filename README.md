### Usage

`replacer <data_model> <template_folder> <output_folder> <templates>...`

* `data_model`: the .json data model to populate the template
* `template_folder`: the location of the template files
* `output_folder`: where to place the output folders / files
* `templates...`: a list of template filenames to process, relative to the `template_folder`

All output files will be created in the `output_folder` in the same folder location.

### Example

Folder structure:

```
/
    templates/
        test/
            template.txt
    model.json
    replacer.jar
```

Data model `/model.json`:

```json
{
  "name": "Malcolm Reynolds",
  "nicknames": [
    "Mal",
    "Captain",
    "Captain Tight Pants"
  ]
}
```

Template `/templates/test/template.txt`:

```
Name: ${name}
Nicknames:
<#list nicknames as nickname>
  - ${nickname}
</#list>
```

Command:

```
java -jar replacer.jar model.json ./templates ./output test/template.txt
```

Resulting folder structure:

```
/
    output/
        test/
            template.txt
    templates/
        test/
            template.txt
    model.json
    replacer.jar
```

Resulting `/output/test/template.txt`:

```
Name: Malcolm Reynolds
Nicknames:
  - Mal
  - Captain
  - Captain Tight Pants
```

### Templating Engine

Reference: https://freemarker.apache.org/docs/index.html