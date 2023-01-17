[//]: # (title: HTML)

HTML is Dokka's default and recommended output format. It is currently in Beta and approaching the Stable release.

You can see an example of the output by browsing documentation
for [kotlinx.coroutines](https://kotlinlang.org/api/kotlinx.coroutines/).

## Generate HTML documentation

HTML as an output format is supported by all runners. To generate HTML documentation, follow these steps depending on 
your build tool or runner:

* For [Gradle](dokka-gradle.md#generate-documentation), run `dokkaHtml` or `dokkaHtmlMultiModule` tasks.
* For [Maven](dokka-maven.md#generate-documentation), run the `dokka:dokka` goal.
* For [CLI runner](dokka-cli.md#generate-documentation), run with HTML dependencies set.

> HTML pages generated by this format need to be hosted on a web server in order to render everything correctly.
>
> You can use any free static site hosting service, such as
> [GitHub Pages](https://docs.github.com/en/pages/getting-started-with-github-pages/about-github-pages).
>
> Locally, you can use the [built-in IntelliJ web server](https://www.jetbrains.com/help/idea/php-built-in-web-server.html).
>
{type="note"}

## Configuration

HTML format is Dokka's base format, so it is configurable through `DokkaBase` and `DokkaBaseConfiguration`
classes:

<tabs group="build-script">
<tab title="Kotlin" group-key="kotlin">

Via type-safe Kotlin DSL:

```kotlin
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.base.DokkaBaseConfiguration

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:%dokkaVersion%")
    }
}

tasks.withType<DokkaTask>().configureEach {
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        customAssets = listOf(file("my-image.png"))
        customStyleSheets = listOf(file("my-styles.css"))
        footerMessage = "(c) 2022 MyOrg"
        separateInheritedMembers = false
        templatesDir = file("dokka/templates")
        mergeImplicitExpectActualDeclarations = false
    }
}
```

Via JSON:

```kotlin
import org.jetbrains.dokka.gradle.DokkaTask

tasks.withType<DokkaTask>().configureEach {
    val dokkaBaseConfiguration = """
    {
      "customAssets": ["${file("assets/my-image.png")}"],
      "customStyleSheets": ["${file("assets/my-styles.css")}"],
      "footerMessage": "(c) 2022 MyOrg",
      "separateInheritedMembers": false,
      "templatesDir": "${file("dokka/templates")}",
      "mergeImplicitExpectActualDeclarations": false
    }
    """
    pluginsMapConfiguration.set(
        mapOf(
            // fully qualified plugin name to json configuration
            "org.jetbrains.dokka.base.DokkaBase" to dokkaBaseConfiguration
        )
    )
}
```

</tab>
<tab title="Groovy" group-key="groovy">

```groovy
import org.jetbrains.dokka.gradle.DokkaTask

tasks.withType(DokkaTask.class) {
    String dokkaBaseConfiguration = """
    {
      "customAssets": ["${file("assets/my-image.png")}"],
      "customStyleSheets": ["${file("assets/my-styles.css")}"],
      "footerMessage": "(c) 2022 MyOrg"
      "separateInheritedMembers": false,
      "templatesDir": "${file("dokka/templates")}",
      "mergeImplicitExpectActualDeclarations": false
    }
    """
    pluginsMapConfiguration.set(
            // fully qualified plugin name to json configuration
            ["org.jetbrains.dokka.base.DokkaBase": dokkaBaseConfiguration]
    )
}
```

</tab>
<tab title="Maven" group-key="mvn">

```xml
<plugin>
    <groupId>org.jetbrains.dokka</groupId>
    <artifactId>dokka-maven-plugin</artifactId>
    ...
    <configuration>
        <pluginsConfiguration>
            <!-- Fully qualified plugin name -->
            <org.jetbrains.dokka.base.DokkaBase>
                <!-- Options by name -->
                <customAssets>
                    <asset>${project.basedir}/my-image.png</asset>
                </customAssets>
                <customStyleSheets>
                    <stylesheet>${project.basedir}/my-styles.css</stylesheet>
                </customStyleSheets>
                <footerMessage>(c) MyOrg 2022 Maven</footerMessage>
                <separateInheritedMembers>false</separateInheritedMembers>
                <templatesDir>${project.basedir}/dokka/templates</templatesDir>
                <mergeImplicitExpectActualDeclarations>false</mergeImplicitExpectActualDeclarations>
            </org.jetbrains.dokka.base.DokkaBase>
        </pluginsConfiguration>
    </configuration>
</plugin>
```

</tab>
<tab title="CLI" group-key="cli">

Via [command line options](dokka-cli.md#run-with-command-line-options):

```Bash
java -jar dokka-cli-%dokkaVersion%.jar \
     ...
     -pluginsConfiguration "org.jetbrains.dokka.base.DokkaBase={\"customAssets\": [\"my-image.png\"], \"customStyleSheets\": [\"my-styles.css\"], \"footerMessage\": \"(c) 2022 MyOrg\", \"separateInheritedMembers\": false, \"templatesDir\": \"dokka/templates\", \"mergeImplicitExpectActualDeclarations\": false}
"
```

Via [JSON configuration](dokka-cli.md#run-with-json-configuration):

```json
{
  "moduleName": "Dokka Example",
  "pluginsConfiguration": [
    {
      "fqPluginName": "org.jetbrains.dokka.base.DokkaBase",
      "serializationFormat": "JSON",
      "values": "{\"customAssets\": [\"my-image.png\"], \"customStyleSheets\": [\"my-styles.css\"], \"footerMessage\": \"(c) 2022 MyOrg\", \"separateInheritedMembers\": false, \"templatesDir\": \"dokka/templates\", \"mergeImplicitExpectActualDeclarations\": false}"
    }
  ]
}
```

</tab>
</tabs>

### Configuration options

The table below contains all of the possible configuration options and their purpose.

| **Option**                              | **Description**                                                                                                                                                                                                                                                                               |
|-----------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `customAssets`                          | List of paths for image assets to be bundled with documentation. The image assets can have any file extension. For more information, see [Customizing assets](#customize-assets).                                                                                                             |
| `customStyleSheets`                     | List of paths for `.css` stylesheets to be bundled with documentation and used for rendering. For more information, see [Customizing styles](#customize-styles).                                                                                                                              |
| `templatesDir`                          | Path to the directory containing custom HTML templates. For more information, see [Templates](#templates).                                                                                                                                                                                    |
| `footerMessage`                         | The text displayed in the footer.                                                                                                                                                                                                                                                             |
| `separateInheritedMembers`              | This is a boolean option. If set to `true`, Dokka renders properties/functions and inherited properties/inherited functions separately. This is disabled by default.                                                                                                                          |
| `mergeImplicitExpectActualDeclarations` | This is a boolean option. If set to `true`, Dokka merges declarations that are not declared as [expect/actual](https://kotlinlang.org/docs/multiplatform-connect-to-apis.html), but have the same fully qualified name. This can be useful for legacy codebases. This is disabled by default. |

For more information about configuring Dokka plugins, see [Configuring Dokka plugins](dokka-plugins.md#configure-dokka-plugins).

## Customization

To help you add your own look and feel to your documentation, the HTML format supports a number of customization options.

### Customize styles

You can use your own stylesheets by using the `customStyleSheets`
[configuration option](#configuration). These are applied to every page.

It's also possible to override Dokka's default stylesheets by providing files with the same name:

| **Stylesheet name**  | **Description**                                                    |
|----------------------|--------------------------------------------------------------------|
| `style.css`          | Main stylesheet, contains most of the styles used across all pages |
| `logo-styles.css`    | Header logo styling                                                |
| `prism.css`          | Styles for [PrismJS](https://prismjs.com/) syntax highlighter      |
| `jetbrains-mono.css` | Font styling                                                       |

The source code for all of Dokka's stylesheets is
[available on GitHub](https://github.com/Kotlin/dokka/tree/%dokkaVersion%/plugins/base/src/main/resources/dokka/styles).

### Customize assets

You can provide your own images to be bundled with documentation by using the `customAssets`
[configuration option](#configuration). 

These files are copied to the `<output>/images` directory.

It's possible to override Dokka's images and icons by providing files with the same name. The most
useful and relevant one being `logo-icon.svg`, which is the image that's used in the header. The rest is mostly icons.

You can find all images used by Dokka on 
[GitHub](https://github.com/Kotlin/dokka/tree/%dokkaVersion%/plugins/base/src/main/resources/dokka/images).

### Change the logo

To customize the logo, you can begin by [providing your own asset](#customize-assets) for `logo-icon.svg`.

If you don't like how it looks, or you want to use a `.png` file instead of the default `.svg` file,
you can [override the `logo-styles.css` stylesheet](#customize-styles) to customize it.

For an example of how to do this, see our
[custom format example project](https://github.com/Kotlin/dokka/tree/%dokkaVersion%/examples/gradle/dokka-customFormat-example).

### Modify the footer

You can modify text in the footer by using the `footerMessage` [configuration option](#configuration).

### Templates

Dokka provides the ability to modify [FreeMarker](https://freemarker.apache.org/) templates used for generating 
documentation pages.

You can change the header completely, add your own banners/menus/search, load analytics, change body styling and so on.

Dokka uses the following templates:

| **Template**                      | **Description**                                                                                                       |
|-----------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| `base.ftl`                        | Defines the general design of all pages to be rendered.                                                               |
| `includes/header.ft`              | The page header that by default contains the logo, version, source set selector, light/dark theme switch, and search. |
| `includes/footer.ft`              | The page footer that contains the `footerMessage` [configuration option](#configuration) and copyright.               |
| `includes/page_metadata.ft`       | Metadata used within `<head>` container.                                                                              |
| `includes/source_set_selector.ft` | [The source set](https://kotlinlang.org/docs/multiplatform-discover-project.html#source-sets) selector in the header. |

The base template is `base.ftl` and it includes all of the remaining listed templates. You can find the source code for all of Dokka's templates
[on GitHub](https://github.com/Kotlin/dokka/tree/%dokkaVersion%/plugins/base/src/main/resources/dokka/templates).

You can override any template by using the `templatesDir` [configuration option](#configuration). Dokka searches
for the exact template names within the given directory. If it fails to find user-defined templates, it uses the
default templates.

#### Variables

The following variables are available inside all templates:

| **Variable**       | **Description**                                                                                                                                                                                    |
|--------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `${pageName}`      | The page name                                                                                                                                                                                      |
| `${footerMessage}` | The text which is set by the `footerMessage` [configuration option](#configuration)                                                                                                                |
| `${sourceSets}`    | A nullable list of [source sets](https://kotlinlang.org/docs/multiplatform-discover-project.html#source-sets) for multi-platform pages. Each item has `name`, `platform`, and `filter` properties. |
| `${projectName}`   | The project name. It's available only within the `template_cmd` directive.                                                                                                                         |
| `${pathToRoot}`    | The path to root from the current page. It's useful for locating assets and is available only within the `template_cmd` directive.                                                                 |

Variables `projectName` and `pathToRoot` are available only within the `template_cmd` directive as they require more
context and thus they need to be resolved at later stages by the [MultiModule](dokka-gradle.md#multi-project-builds) task:

```html
<@template_cmd name="projectName">
   <span>${projectName}</span>
</@template_cmd>
```

#### Directives

You can also use the following Dokka-defined [directives](https://freemarker.apache.org/docs/ref_directive_userDefined.html):

| **Variable**    | **Description**                                                                                                                                                                              |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `<@content/>`   | The main page content.                                                                                                                                                                       |
| `<@resources/>` | Resources such as scripts and stylesheets.                                                                                                                                                   |
| `<@version/>`   | The module version taken from configuration. If the [versioning plugin](https://github.com/Kotlin/dokka/tree/master/plugins/versioning) is applied, it is replaced with a version navigator. |
